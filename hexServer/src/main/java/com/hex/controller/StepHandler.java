package com.hex.controller;

import com.hex.entity.*;
import com.hex.serviceimpl.GameServiceImpl;
import com.hex.serviceimpl.StepServiceImpl;
import com.hex.serviceimpl.chessTableServiceImpl;
import com.hex.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class StepHandler {
    @Autowired
    private StepServiceImpl stepService;
    @Autowired
    private GameServiceImpl gameService;
    @Autowired
    private chessTableServiceImpl chessTableService;

    @GetMapping("step/findAll")//http://localhost:8080/step/findAll/
    public Collection<Step> findAll(){
        return stepService.findAll();
    }

    @RequestMapping("step/findByGameId/{gameId}")//http://localhost:8080/step/findAll/
    public Collection<Step> findAllByGameId(@PathVariable("gameId")int gameId){
        return stepService.findByGameId(gameId);
    }

    @RequestMapping("/step/{userId}/{gameId}/{index}")//http://localhost:8080/step/1/1/1
    //http://localhost:8080/step/2/1/2
    public ApiResult step(@PathVariable("userId")int userId, @PathVariable("gameId")int gameId,
                          @PathVariable("index")int index){
        if (gameId==-1) return ApiResultHandler.buildApiResult(400, "游戏不存在", null);
        Game game=gameService.findById(gameId);
        if (game==null)  return ApiResultHandler.buildApiResult(400, "游戏不存在", null);
        chessTable chesstable=chessTableService.findById(game.tableId);
        chessTableService.update(chesstable);
        if (chesstable==null)  return ApiResultHandler.buildApiResult(400, "游戏不存在", null);
        if(userId!=chesstable.getUserRed()&&userId!=chesstable.getUserBlue())
            return ApiResultHandler.buildApiResult(400, "未加入该对局", null);
        if(chesstable.gameId<0)
            return ApiResultHandler.buildApiResult(400, "对局尚未开始", null);
        if ((game.getNext()==0&&userId==chesstable.getUserBlue())||
                (game.getNext()==1&&userId==chesstable.getUserRed()))
            return ApiResultHandler.buildApiResult(400, "当前不是你走子", null);
        if (game.getWinner()!=-1)
            return ApiResultHandler.buildApiResult(400,"已有人获胜",null);

        Step step=new Step(0,game.getId(),index,0);
        if (userId==chesstable.getUserBlue()) step.setColor(1);
        game.setNext(1-game.getNext());
        gameService.update(game);
        if (step==null) System.out.println("error");
        stepService.add(step);
        List<Step> totalStep=stepService.findByGameId(gameId);
        Judge judge=new Judge();
        for (int i=0;i<totalStep.size();i++){
            Step now=totalStep.get(i);
            int nowIndex=now.getIndexNum();int nowColor=now.getColor();
            if (nowColor==0)judge.used[nowIndex]=1;
            else judge.used[nowIndex]=2;
        }
        int nowWinner=judge.judgesf();
        if (nowWinner==1)  {
            game.setWinner(1);gameService.update(game);
            return ApiResultHandler.buildApiResult(200,"红方获胜",null);
        }
        else if (nowWinner==2) {
            game.setWinner(2);gameService.update(game);
            return ApiResultHandler.buildApiResult(200,"蓝方获胜",null);
        }
        else {
            return ApiResultHandler.buildApiResult(200,"success",totalStep);
        }
    }
}
