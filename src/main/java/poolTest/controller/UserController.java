package poolTest.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import poolTest.bean.User;
import poolTest.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	public UserService userService;
	/**
	 * 用户登陆
	 * @param username
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login",produces = "text/html; charset=utf-8",method = RequestMethod.POST)
	public String userLogin(
			@RequestParam("username") final String username,
			@RequestParam("password") final String password) {
		
		Map<String, Object> map = userService.loginUserLock(username);
		if((boolean) map.get("flag")) {//被限制
			return username+"登陆失败,被限制剩余"+map.get("lockTime")+"分钟";
		}else {
			
			User user = userService.login(username, password);
			
			if(user == null) {//登陆失败
				return	userService.loginValdate(username);
			}else {//登陆成功
				//清空key
				return "";
			}
			
		}
	}
}
