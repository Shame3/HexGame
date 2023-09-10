package com.hex.controller;


import com.hex.service.GameService;
import com.hex.serviceimpl.GameServiceImpl;
import com.hex.serviceimpl.StepServiceImpl;
import com.hex.serviceimpl.chessTableServiceImpl;
import com.hex.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import com.hex.entity.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/chessTable")
public class ChessTableHandler {

     @Autowired private HttpServletRequest request;
     @Autowired private chessTableServiceImpl  chessTableService;
     @Autowired private GameServiceImpl gameService;
     @Autowired private StepServiceImpl stepService;
     @GetMapping ("/findAll")//http://localhost:8080/chessTable/findAll/
    public Collection<chessTable> table() throws ParseException { deleteByTime();
    return chessTableService.findAll(); }

    @GetMapping ("/findById/{tableId}") //http://localhost:8080/chessTable/findById/1
    public ApiResult table(@PathVariable("tableId")int tableId) {
        chessTable result= chessTableService.findById(tableId);
        if (result!=null)
        return ApiResultHandler.buildApiResult(200, "请求成功", result);
        else return ApiResultHandler.buildApiResult(400, "请求失败",null);
    }

    @RequestMapping("joinTable")//http://localhost:8080/chessTable/joinTable?tableId=1&redOrblue=blue&userId=1
    public ApiResult join(@RequestParam("tableId") int tableId, @RequestParam("redOrblue") String blackOrwhite,
                          @RequestParam("userId") int userId){
        chessTable chesstable=chessTableService.findById(tableId);
        if (blackOrwhite.equals("red")){
            if (chesstable.userRed != -1) {
                if(chesstable.userRed==userId)
                    return ApiResultHandler.buildApiResult(200, "您已入座", chesstable);
                return ApiResultHandler.buildApiResult(400, "该位置有人", chesstable);
            } else {
                chessTable old=chessTableService.findByUserId(userId);
                if (old!=null)  leave(tableId,userId);
                chesstable.setUserRed(userId);chessTableService.update(chesstable);
                if (chesstable.getUserBlue() != -1) {//开始游戏
                    return GameBegin(tableId, chesstable);
                } else {
                    chessTableService.update(chesstable);
                    return ApiResultHandler.buildApiResult(200, "success", chesstable);
                }
            }
        }

        if (blackOrwhite.equals("blue")){
            if(chesstable.userBlue!=-1){
                if(chesstable.userBlue==userId)
                    return ApiResultHandler.buildApiResult(200, "您已入座", chesstable);
                return ApiResultHandler.buildApiResult(400, "该位置有人", chesstable);
            }  else {
                chessTable old=chessTableService.findByUserId(userId);
                if (old!=null) leave(tableId,userId);
                chessTableService.update(old);
                chesstable.setUserBlue(userId);chessTableService.update(chesstable);
                if (chesstable.getUserRed() != -1) {//开始游戏
                    return GameBegin(tableId, chesstable);
                } else {
                    chessTableService.update(chesstable);
                    return ApiResultHandler.buildApiResult(200, "success", chesstable);
                }
            }
        }
        return ApiResultHandler.buildApiResult(400,"error",null);

    }

    private ApiResult GameBegin(@RequestParam("tableId") int tableId, chessTable chesstable) {
        Game game=gameService.findBytableId(tableId);
        if (game!=null) {
            return ApiResultHandler.buildApiResult(200, "继续游戏", chesstable);
        }
        game=new Game(-1,tableId,0,-1);
        gameService.add(game);
        game = gameService.findBytableId(tableId);
        chesstable.setGameId(game.id);
        chessTableService.update(chesstable);
        return ApiResultHandler.buildApiResult(200, "游戏开始", game);
    }

    @RequestMapping("leaveTable")//http://localhost:8080/chessTable/leaveTable?tableId=0&userId=0
    public ApiResult leave(@RequestParam("tableId") int tableId, @RequestParam("userId") int userId){
        chessTable chesstable=chessTableService.findById(tableId);
        if (chesstable==null )  ApiResultHandler.buildApiResult(400, "棋盘不存在", null);
        if (userId==chesstable.userRed)  chesstable.userRed=-1;
        if (userId==chesstable.userBlue)  chesstable.userBlue=-1;
        if (chesstable.userRed==-1&&chesstable.userBlue==-1&&chesstable.gameId>0) {  //清空game
            gameService.deleteBytableId(tableId);
            stepService.deleteByGameId(chesstable.gameId);
            chesstable.gameId=-1;
        }
        chessTableService.update(chesstable);
        chesstable=chessTableService.findById(tableId);
        return ApiResultHandler.buildApiResult(200,"success",chesstable);
    }

    @RequestMapping("initAll")
    public ApiResult init(){
        Long adminId= Long.valueOf(-1);
        if (request.getSession().getAttribute("adminId")!=null)
            adminId = (Long) request.getSession().getAttribute("adminId");
        if (adminId==-1) return ApiResultHandler.buildApiResult(500,"请重新登录",null);

        List<chessTable>  chessTables=chessTableService.findAll();
        for (int i=0;i<chessTables.size();i++){
            chessTable now=chessTables.get(i);
            Game game=gameService.findBytableId(now.id);
            if (game!=null){
                gameService.deleteBytableId(now.id);
                stepService.deleteByGameId(game.id);
            }
            now.setGameId(-1);now.setUserRed(-1);now.setUserBlue(-1);
            chessTableService.update(now);
        }
        return ApiResultHandler.buildApiResult(200,"所有棋盘初始化成功",chessTableService.findAll());
    }

    public void deleteByTime() throws ParseException {
        List<chessTable>  chessTables=chessTableService.findAll();
        for (int i=0;i<chessTables.size();i++){  //8个小时
            chessTable now=chessTables.get(i);

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(now.up.toString());
            long old=date.getTime()/1000/60-(60*8);
            Date nowTime = new Date();
            long stamp = nowTime.getTime()/1000/60;
            if(stamp-old>=3){
                Game game=gameService.findBytableId(now.id);
                if(game==null){
                    now.setGameId(-1);now.setUserRed(-1);now.setUserBlue(-1);
                    chessTableService.update(now);
                }
            }
        }
    }
}
