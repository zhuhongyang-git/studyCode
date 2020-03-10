package poolTest;
 
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import poolTest.bean.User;
import poolTest.dao.UserDao;
 
 
/**
 * 数据库连接池使用测试
 * 
 * @author tuzongxun
 * @date 2017年2月15日 上午11:16:36
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:reids/applicationContext-*.xml"})
public class PoolTest {
    @Autowired
    UserDao userDao;
 
    @Test
    public void findCountTest() {
        List<User> count = userDao.findCount();
        System.out.println(count.size());
    }
    
    @Test
    public void addUserTest() {
    	User u = new User();
    		u.setUserName("Test23");
    	int i = userDao.add(u);
    	System.out.println(i);
    }
}
