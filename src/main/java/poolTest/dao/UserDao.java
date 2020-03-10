package poolTest.dao;

import java.util.List;

import poolTest.bean.User;



/**
   *  用户管理
 * @author zhy
 *
 */
public interface UserDao {

	public List<User> findCount();
	
	public int add(User user);
}
