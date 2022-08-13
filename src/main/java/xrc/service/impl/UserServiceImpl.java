package xrc.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import xrc.domain.*;
import xrc.mapper.UserMapper;
import xrc.service.UserService;
import xrc.service.ex.InsertException;
import xrc.service.ex.NameDuplicatedException;
import xrc.service.ex.PwdNotMatchException;
import xrc.service.ex.UserNotFoundException;
import xrc.domain.MaskVote;


import java.sql.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {


    //调用mapper层方法，将user传递下去
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) throws SQLException, ClassNotFoundException {

        //实现想要的方法，如此例为判断用户名是否有重复

        Connection conn1 = null;
        Connection conn2 = null;
        Connection connection = null;
        int n = 10; //总人数
        int t = 7; //门限值
        ECGroup ecgroup = new ECGroup();

        ECPoint h = ecgroup.randompoint();

        //密码加密算法MD5
        String oldPwd = user.getPwd();
        //获取盐值 随机生成
        String salt = UUID.randomUUID().toString().toUpperCase();
        System.out.println(salt);
        //将密码和盐值作为一个整体加密处理
        String md5Pwd = getMD5Pwd(oldPwd, salt);
        //将加密后的密码重新补全到user对象中
        user.setPwd(md5Pwd);

        //盐值也需要记录
        user.setSalt(salt);
        //通过user参数来获取传递过来的username
        String name = user.getName();
        //调用findByName方法
        User result = userMapper.findByUserName(name);
        //判断结果集是否为null，若不为，则抛出占用异常
        if (result != null) {
            throw new NameDuplicatedException("用户名被占用");
        }

        //此处需要优化

        //执行注册业务功能实现
       /* Integer rows = userMapper.insertList(user);
        System.out.println(rows);
        if (rows != 1) {
            throw new InsertException("用户注册时未知异常");
        }*/

        String driverName = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/db01";
        String userName = "root";
        String userPwd = "123456";

        Class.forName(driverName);
        conn1 = DriverManager.getConnection(dbURL, userName, userPwd);
        conn2 = DriverManager.getConnection(dbURL, userName, userPwd);
        connection = DriverManager.getConnection(dbURL, userName, userPwd);
        System.out.println("连接数据库成功");

        //用户注册时创建密钥
        BigInteger[][] secret = user.keygen(t, ecgroup);
        for (int i = 0; i < t + 1; i++) {
            System.out.println(secret[0][i]);
            System.out.println(secret[1][i]);
            String sql1 = "INSERT INTO user1(secret) VALUES (" + secret[0][i] + ")";
            String sql2 = "INSERT INTO user1(secret) VALUES (" + secret[1][i] + ")";

            PreparedStatement statement1 = conn1.prepareStatement(sql1);
            PreparedStatement statement2 = conn2.prepareStatement(sql2);
            statement1.execute();
            statement2.execute();

        }
        conn1.close();
        conn2.close();

        System.out.println("================================");

        //测试数据库查询代码
        String sql = "select * from user1";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        ECPoint C[] = new ECPoint[t + 1];
        ECPoint A[] = new ECPoint[t + 1];
        //ArrayList<Object> secretList = new ArrayList<>();
        BigInteger[][] fx = new BigInteger[2][t + 1];
        int i = 0;

        while (resultSet.next()) {
            String string1 = resultSet.getString(2);

            fx[0][i] = new BigInteger(string1);
            System.out.println(fx[0][i]);
            resultSet.next();
            String string2 = resultSet.getString(2);
            fx[1][i] = new BigInteger(string2);
            System.out.println(fx[1][i]);
            i++;

        }

        C = user.C(7, fx, ecgroup, h);
        for (int i1 = 0; i1 < C.length; i1++) {
            System.out.println(C[i1]);
        }

        System.out.println("================");
//        System.out.println(C);
        user.A(7, fx, ecgroup);
        for (int i1 = 0; i1 < A.length; i1++) {
            System.out.println(C[i1]);
        }


    }


    //验证加密正确性
    @Override
    public void examine(User user) {
        String result = getMD5Pwd("1234", "704DE66A-9426-45A2-960E-8BBA470E5350");
        System.out.println(result);
    }


    //连接数据库
    public void connection(User user) throws SQLException, ClassNotFoundException {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/db01";
        String userName = "root";
        String userPwd = "123456";

        Class.forName(driverName);
        DriverManager.getConnection(dbURL, userName, userPwd);
        System.out.println("连接数据库成功");
    }

    //注册功能
    @Override
    public List<Object> register(JSONObject user) throws SQLException, ClassNotFoundException {

        String name = user.getString("username");   //获取前端传递的姓名以及密码
        String password = user.getString("password");
        User byName = userMapper.findByUserName(name);

        JSONObject result = new JSONObject();   //创建返回给前端状态
        List<Object> resultList = new ArrayList<>();    //生成结果状态列表

        if (byName != null) {
            //throw new NameDuplicatedException("用户名被占用");

            result.put("result", "注册失败");
            result.put("code", "用户名被占用");
            result.put("key", "");
            resultList.add(result);

        } else {
            System.out.println(name);
            System.out.println(password);

            String salt = UUID.randomUUID().toString().toUpperCase();
            System.out.println(salt);
            String md5Pwd = getMD5Pwd(password, salt);


            String driverName = "com.mysql.cj.jdbc.Driver";
            String dbURL = "jdbc:mysql://localhost:3306/db01";
            String userName = "root";
            String userPwd = "123456";

            Class.forName(driverName);
            Connection connection = DriverManager.getConnection(dbURL, userName, userPwd);

            String sql = "insert into test (name,pwd,salt) values('"+name+"','"+md5Pwd+"','"+salt+"')";
            //System.out.println(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            int n = 10; //总人数
            int t = 7; //门限值
            ECGroup ecgroup = new ECGroup();

            //ECPoint h = ecgroup.randompoint();


            //用户注册时创建密钥
            //调用maskvote中的keygen方法
            MaskVote maskVote = new MaskVote();
            BigInteger[][] secret = maskVote.keygen(t, ecgroup);

            List<String> secretList = new ArrayList<>();   //生成密钥列表

            for (int i = 0; i < t + 1; i++) {
                System.out.println(secret[0][i]);
                System.out.println(secret[1][i]);

                secretList.add(String.valueOf(secret[0][i]));
                secretList.add(String.valueOf(secret[1][i]));
            }

            result.put("result", "注册成功");
            result.put("code", "");
            result.put("key", secretList);

            resultList.add(result);
        }
        System.out.println(resultList);
        return resultList;


    }

    //登陆功能进行判断
    @Override
    public List<Object> login(JSONObject user) {

        //根据用户名称来查询用户数据是否存在
        String name = user.getString("username");
        String pwd = user.getString("password");
        System.out.println(user);

        User byName = userMapper.findByUserName(name);
        JSONObject result = new JSONObject();
        List<Object> resultList = new ArrayList<>();

        if (byName == null) {
            //throw new UserNotFoundException("用户数据不存在");
            result.put("result", "登录失败");
            result.put("code", "用户不存在");

        } else {
            String oldPwd = byName.getPwd();

            //检测密码是否匹配
            String salt = byName.getSalt();    //先获取盐值
            String newPwd = getMD5Pwd(pwd, salt);   //将密码按照md5算法进行加密

            System.out.println(newPwd);
            System.out.println(oldPwd);

            //密码进行比较
            if (!oldPwd.equals(newPwd)) {

//            throw new PwdNotMatchException("用户密码错误");
                result.put("result", "登录失败");
                result.put("code", "用户密码错误");
            } else {

                result.put("result", "登录成功");
                result.put("code", "");
            }
        }
        resultList.add(result);
        return resultList;

    }

    @Override
    public List<Object> getWaitForVoteList(JSONObject user) throws ParseException, ClassNotFoundException, SQLException {

        String name = user.getString("username");

        String driverName = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/db01";
        String userName = "root";
        String userPwd = "123456";

        Class.forName(driverName);
        Connection connection = DriverManager.getConnection(dbURL, userName, userPwd);
        String sql = "SELECT voterList FROM EVENT";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuffer stringBuffer = new StringBuffer();
        while (resultSet.next()){
            //stringBuffer.append()
        }



        return null;
    }

    @Override
    public List<Object> publish(JSONObject event) throws ClassNotFoundException, SQLException {

        ArrayList<Object> eventList = new ArrayList<>();

        String username = event.getString("username");
        String voteItem = event.getString("voteitem");
        String voterList = event.getString("voterlist");
        String timeKeyStart = event.getString("time_key_start");
        String timeVoteStart = event.getString("time_vote_start");
        String timeSumStart = event.getString("time_sum_start");
        String timeSumEnd = event.getString("time_sum_end");
        eventList.add(username);
        eventList.add(voteItem);
        eventList.add(voterList);
        eventList.add(timeKeyStart);
        eventList.add(timeVoteStart);
        System.out.println(eventList);


        String driverName = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/db01";
        String userName = "root";
        String userPwd = "123456";

        Class.forName(driverName);
        Connection connection = DriverManager.getConnection(dbURL, userName, userPwd);

        String sql = "insert into event(username,voteItem,voterList,timeKeyStart,timeVoteStart,timeSumStart,timeSumEnd)" +
                "values ('"+username+"','"+voteItem+"','"+voterList+"','"+timeKeyStart+"','"+timeVoteStart+"','"+timeSumStart+"','"+timeSumEnd+"')";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = preparedStatement.executeUpdate();

        JSONObject result = new JSONObject();   //创建返回给前端状态
        List<Object> resultList = new ArrayList<>();    //生成结果状态列表
        if (i > 0){

            result.put("result","发起投票成功");
            result.put("code","事项进数据库");
        }else {
            result.put("result","发起投票失败");
            result.put("code","事项没进数据库");
        }
        resultList.add(result);
        return resultList;

    }

    //未完成
    /*@Override
    public List<Object> getPublishedVoteList(JSONObject event) {

        String name = event.getString("username");    //调取数据库事项表中的用户名 并进行比较

        Event byName = userMapper.findBySqlName(name);
        if (byName == null) {
            throw new UserNotFoundException("用户不在列表中");
        }
        List<Event> sqlEvent = userMapper.fetchSql();
        System.out.println(sqlEvent);

        List<User> nameList = userMapper.findByAll();
        for (User user1 : nameList) {
            if (name.equals(user1)) {
                //此人为投票发起者
                Date timeKeyStart = event.getDate("time_key_start");
                Date timeVoteStart = event.getDate("time_vote_start");
                Date timeSumStart = event.getDate("time_sum_start");
                Date timeSumEnd = event.getDate("time_sum_end");

                //时间判断
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currentTime = formatter.parse(formatter.format(date));
                //System.out.println(currentTime);
                if (currentTime.before(timeKeyStart)) {
                    return "未开始";
                } else if (currentTime.after(timeKeyStart) && currentTime.before(timeVoteStart)) {
                    return "预投票";
                } else if (currentTime.after(timeVoteStart) && currentTime.before(timeSumStart)) {
                    return "投票";
                } else if (currentTime.after(timeSumStart) && currentTime.before(timeSumEnd)) {
                    return "计票";
                } else if (currentTime.after(timeSumEnd)) {
                    return "已结束";
                }


            } else {
                return "此人不是投票发起者";
            }
        }
        return null;

    }*/

    /*定义MD5算法加密*/
    private String getMD5Pwd(String pwd, String salt) {

        //进行三次加密
        for (int i = 0; i < 3; i++) {
            //MD5加密算法方法调用
            pwd = DigestUtils.md5DigestAsHex((salt + pwd + salt).getBytes()).toUpperCase();
        }

        //返回加密后的pwd
        return pwd;

    }


}
