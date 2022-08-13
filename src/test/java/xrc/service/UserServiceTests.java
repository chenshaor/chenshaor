package xrc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrc.domain.ECGroup;
import xrc.domain.User;
import xrc.mapper.UserMapper;
import xrc.service.ex.ServiceException;

import java.sql.SQLException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTests {

    @Autowired
    //使用UserService层
    private UserService userService;

    @Test
    public void reg(){
        try {

            int t=7;
            ECGroup ecgroup = new ECGroup();

            User user = new User();
            user.setName("aaab");
            user.setPwd("1234");
            user.keygen(t,ecgroup);

            userService.reg(user);
            System.out.println("OK");
        } catch (ServiceException | SQLException e) {
            //获取类的对象，再获取类的名称
            System.out.println(e.getMessage());

            //获取异常的具体信息
            System.out.println(e.getClass().getSimpleName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void examine(){
        User user = new User();
        userService.examine(user);
    }

    @Test
    public void connection() throws SQLException, ClassNotFoundException {
        User user = new User();
        userService.connection(user);

    }








}
