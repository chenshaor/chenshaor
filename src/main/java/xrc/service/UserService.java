package xrc.service;

import com.alibaba.fastjson.JSONObject;
import xrc.domain.Event;
import xrc.domain.User;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/*用户模块业务层接口*/
public interface UserService {

    //接口定义方法  转至相应impl中实现方法内容
    void reg(User user) throws SQLException, ClassNotFoundException;

    //验证密码是否正确
    void examine(User user);

    //验证是否连接数据库
    void connection(User user) throws SQLException, ClassNotFoundException;

    //注册功能 用户名以及密码
    List<Object> register(JSONObject user) throws ClassNotFoundException, SQLException;

    //登录功能
    List<Object> login(JSONObject user);

    //获取用户发起的投票事项的列表
    //List<Object> getPublishedVoteList(JSONObject event);




    //获取数据库事项表内容
    List<Object> getWaitForVoteList(JSONObject user) throws ParseException, ClassNotFoundException, SQLException;

    //用户发起一项投票
    List<Object> publish(JSONObject event) throws ClassNotFoundException, SQLException;
}
