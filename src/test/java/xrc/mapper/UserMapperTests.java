package xrc.mapper;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrc.domain.ECGroup;
import xrc.domain.ECPoint;
import xrc.domain.Event;
import xrc.domain.User;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert(){

        /*JSONObject user = new User();
        user.setName("ssss");
        user.setPwd("123465");

        userMapper.insertList(user);
        System.out.println(row);*/
    }

    @Test
    public void findByAll(){
        List<User> all = userMapper.findByAll();
        System.out.println(all);
    }

    @Test
    public void fetchSql(){
        List<Event> all = userMapper.fetchSql();
        System.out.println(all);
    }

    @Test
    public void findByName(){
        User user = userMapper.findByUserName("ssss");
        System.out.println(user);
    }
}
