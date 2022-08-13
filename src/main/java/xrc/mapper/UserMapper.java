package xrc.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import xrc.domain.Event;
import xrc.domain.User;

import java.util.List;

@Mapper  //告诉Mybatis这是mapper接口 创建此接口的代理对象
public interface UserMapper{

    Integer insertList(JSONObject user);

    User findByUserName(String name);

    List<User> findByAll();

    List<Event> fetchSql();
    
    void insertEvent(Event event);

    Event findBySqlName(String name);
}
