package poolTest.bean;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7009531497207368074L;
	private String userId;
	private String userName;
	private String userSex;
	private String userPhone;
	private String userPw;
	private String userType;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userSex=" + userSex + ", userPhone=" + userPhone
				+ ", userPw=" + userPw + ", userType=" + userType + "]";
	}
	

	/*
	 * 锁定限制登陆key
	 */
	public static String getLoginTimeLockKey(String username) {
		return "user:loginTime:lock:" + username;
	}
	/*
	 * 登陆失败key
	 */
	public static String getLoginCountFailKey(String username) {
		return "user:loginCount:fail:" + username;
	}
}
