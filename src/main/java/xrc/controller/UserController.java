package xrc.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xrc.domain.Event;
import xrc.domain.User;
import xrc.service.UserService;


import java.sql.*;
import java.text.ParseException;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /*@RequestMapping("/reg")
    public JsonResult<Void> reg(User user) throws SQLException, ClassNotFoundException {

        //创建响应结果对象
        JsonResult<Void> result = new JsonResult<>();
        userService.reg(user);
        result.setState(OK);
        result.setMessage("login succeeded");

        return result;
    }*/

    /*@RequestMapping("/register")
    public JsonResult<User> register(String name,String pwd) throws SQLException, ClassNotFoundException {
        User data = userService.register(name, pwd);
        return new JsonResult<>(OK,data);
    }*/

    //用户注册功能
    @PostMapping("/register")
    public List<Object> register(@RequestBody JSONObject user) throws SQLException, ClassNotFoundException {

        return userService.register(user);

    }

    //用户登录功能
    @PostMapping("/login")
    public List<Object> login(@RequestBody JSONObject user){

        System.out.println(user);
        return userService.login(user);

    }

    //未完成
    @GetMapping("/get_waitforvote_list")
    public List<Object> getWaitForVoteList(@RequestBody JSONObject user) throws ParseException, SQLException, ClassNotFoundException {
        return userService.getWaitForVoteList(user);
    }

    //投票发起者发布事项
    @PostMapping("/publish")
    public List<Object> publish(@RequestBody JSONObject event) throws SQLException, ClassNotFoundException {
        return userService.publish(event);
    }

}
