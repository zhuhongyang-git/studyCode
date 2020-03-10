package poolTest.service;

import java.util.List;
import java.util.Map;

import poolTest.bean.User;

public interface UserService {

	public User login(String name,String paw) ;
	
	public String loginValdate(String name) ;
	
	public Map<String, Object> loginUserLock(String name);
	
	public List<Object> listPages(int pageNum,int pageSize);
}
