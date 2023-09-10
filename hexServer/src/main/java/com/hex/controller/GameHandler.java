package com.hex.controller;
import com.hex.entity.ApiResult;
import com.hex.entity.chessTable;
import com.hex.serviceimpl.GameServiceImpl;
import com.hex.serviceimpl.chessTableServiceImpl;
import com.hex.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.hex.entity.*;

import java.util.Collection;
import java.util.Vector;

@RestController
@RequestMapping("/game")
public class GameHandler {
    @Autowired
    private chessTableServiceImpl chessTableService;
    @Autowired
    private GameServiceImpl gameService;

    @GetMapping("/findAll")//http://localhost:8080/game/findAll
    public Collection<Game> findAll(){  return gameService.findAll();  }

    @GetMapping ("/findById/{gameId}") //http://localhost:8080/game/findById/1
    public ApiResult findById(@PathVariable("gameId")int gameId) {
        Game result= gameService.findById(gameId);
        if (result!=null)  return ApiResultHandler.buildApiResult(200, "请求成功", result);
        else return ApiResultHandler.buildApiResult(400, "请求失败", null);
    }

    @GetMapping ("/findBytableId/{tableId}") //http://localhost:8080/game/findBytableId/1
    public ApiResult findBytableId(@PathVariable("tableId")int tableId) {
        Game result= gameService.findBytableId(tableId);
        if (result!=null)  return ApiResultHandler.buildApiResult(200, "请求成功", result);
        else return ApiResultHandler.buildApiResult(400, "请求失败", null);
    }

    @GetMapping("/deleteById/{id}")//http://localhost:8080/game/deleteById/1
    public ApiResult deleteById(@PathVariable("id")int id){
        gameService.deleteById(id);
        Game game=gameService.findById(id);
        if (game==null) return ApiResultHandler.buildApiResult(200, "删除成功",null);
        else return ApiResultHandler.buildApiResult(400, "删除失败", game);
    }

    @GetMapping("/deleteBytableId/{tableId}")//
    public ApiResult deleteBytableId(@PathVariable("tableId")int tableId){
        gameService.deleteBytableId(tableId);
        Game game=gameService.findBytableId(tableId);
        if (game==null) return ApiResultHandler.buildApiResult(200, "删除成功",null);
        else return ApiResultHandler.buildApiResult(400, "删除失败", game);
    }

    @RequestMapping("/update")//http://localhost:8080/game/update?id=1&tableId=1&next=0
    public ApiResult update(@RequestParam("id") int id,@RequestParam("tableId") int tableId,
                            @RequestParam("next") int next){
        Game game=new Game(id,tableId,next,-1);gameService.update(game);
        game=gameService.findById(id);
        return ApiResultHandler.buildApiResult(200,"请求成功",game);
    }

    @GetMapping("/add/{tableId}")//http://localhost:8080/game/add/2
    public ApiResult add(@PathVariable("tableId") int tableId){
        Game game=gameService.findBytableId(tableId);
        if (game!=null) return ApiResultHandler.buildApiResult(400, "该table已有游戏", game);
        else {
            game = new Game(0, tableId, 0, -1);
            gameService.add(game);game=gameService.findBytableId(tableId);
            if (game==null) return ApiResultHandler.buildApiResult(400, "添加失败", null);
            else return ApiResultHandler.buildApiResult(200, "添加成功", game);
        }
    }
}
