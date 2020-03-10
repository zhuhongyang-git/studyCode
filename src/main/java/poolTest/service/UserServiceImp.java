package poolTest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import poolTest.bean.User;

@Service("userService")
public class UserServiceImp implements UserService{

	@Autowired // (自动注入redisTemplet)
	public RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 登陆
	 */
	@Override
	public User login(String name, String paw) {
		
		return null;
	}

	/**
	 * 登陆校验
	 */
	@Override
	public String loginValdate(String name) {
		//记录登陆失败次数
		String failKey =User.getLoginCountFailKey(name);
		int num = 5;//登陆失败次数
		if(redisTemplate.hasKey(failKey)) {//存在失败key
			//查询失败key结果
			long loginCountFail = Long.parseLong( redisTemplate.opsForValue().get(failKey)+"");
			if(loginCountFail<(num-1)) {//失败次数小于4，总共5次,还允许输入
				redisTemplate.opsForValue().increment(failKey, 1);//对指定key，增加指定数据
				long failTime = redisTemplate.getExpire(failKey,TimeUnit.SECONDS);
				return name+"登陆失败，在"+failTime+"秒内还允许登陆"+(num-loginCountFail-1)+"次";
			}else {//超出指定限制登陆次数
				//限制登陆时间
				redisTemplate.opsForValue().set(User.getLoginTimeLockKey(name), 1);
				//获取有效时间
				redisTemplate.expire(User.getLoginTimeLockKey(name), 1, TimeUnit.HOURS);
				return "因登陆超出限制次数："+num+"次，请1小时后重试";
			}
			
		}else {//不存在
			//第一次失败次数为1
			redisTemplate.opsForValue().set(failKey, 1);
			//设置失效时间2分钟
			redisTemplate.expire(failKey, 2,TimeUnit.MINUTES);
			return "登陆失败，还允许输入"+(num-1)+"次";
		}
		
	}

	/**
	 * 登陆用户是否被限制
	 * 判断当前登陆用户是否被限制
	 * 查询key是否存在，如果存在就被限制，返回提示
	 * 如果不存在就不被限制
	 */
	@Override
	public Map<String, Object> loginUserLock(String name) {
		String key = User.getLoginTimeLockKey(name);
		Map<String, Object> map = new HashMap<String, Object>();
		if(redisTemplate.hasKey(key)) {//如果存在
			long lockTime = redisTemplate.getExpire(key, TimeUnit.MINUTES);//获取失效时间:分钟
			map.put("flag", true);
			map.put("lockTime", lockTime);//剩余时间，返回分钟
		}else {
			map.put("flag", false);
		}
		return map;
	}

	/**
	 * list 操作redis实现
	 * 分页
	 */
	@Override
	public List<Object> listPages(int pageNum, int pageSize) {
	
		String key = "list";
		for (int i = 0; i < 10; i++) {
			redisTemplate.opsForList().leftPush(key, i);
		}
		
		int start = (pageNum-1)*pageSize;
		int end = pageSize*pageNum -1;
		
		return redisTemplate.opsForList().range(key, start, end);
	}
	
	/**
	 * 模拟淘宝下单发货
	 * list操作redis
	 */
	public void listQueueInit(String id) {
		String key = "goods:"+id;//初始化
		redisTemplate.opsForList().leftPushAll(key, "1.下单","2.发件","3.运输","4.派送","5.签收");
		
	}
	
	/**
	 * 触发事件
	 */
	public void listQueueTouch(String id) {
		String key = "goods:"+id +":succ";//完成的
		redisTemplate.opsForList().rightPopAndLeftPush("goods:"+id, key);
	}
	/**
	 * 完成的
	 * @param id
	 * @return
	 */
	public List<Object> listQueueSucc(String id) {
		String key = "goods:"+id+":succ";
		return redisTemplate.opsForList().range(key, 0, -1);
	}
	/**
	 * 剩余的
	 * @param id
	 * @return
	 */
	public List<Object> listQueueWait(String id) {
		String key = "goods:"+id;
		return redisTemplate.opsForList().range(key, 0, -1);
	}

}
