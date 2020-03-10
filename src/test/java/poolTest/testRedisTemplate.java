package poolTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import poolTest.bean.User;
import poolTest.redis.RedisUtil;
import poolTest.service.UserServiceImp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:reids/applicationContext-*.xml"})
@SuppressWarnings("all")
public class testRedisTemplate {

	@Autowired
	private UserServiceImp userServiceImp;
	
	@Autowired
    private RedisUtil redisUtil;
	
    @Resource(name="redisTemplate")
    private RedisTemplate redisTemplate;
    
    /**
     * redisTemplate 操作redis
     */
    @Test
    public void testSpringRedis() {
        // stringRedisTemplate的操作
        // String读写
        redisTemplate.delete("myStr");
        //设置有效期1分钟   1,TimeUnit.MINUTES
        redisTemplate.opsForValue().set("myStr", "skyLine",1,TimeUnit.MINUTES);
        System.out.println(redisTemplate.opsForValue().get("myStr"));
        System.out.println("有效期"+redisTemplate.getExpire("myStr"));
        System.out.println(redisUtil.getExpire("myStr"));
        System.out.println("-------String--------");

        // List读写
        redisTemplate.delete("myList");
        redisTemplate.opsForList().rightPush("myList", "T");
        redisTemplate.opsForList().rightPush("myList", "L");
        redisTemplate.opsForList().leftPush("myList", "A");
        List<String> listCache = redisTemplate.opsForList().range("myList", 0, -1);
        for (String s : listCache) {
            System.out.println(s);
        }
        //返回存储在键中的列表的长度。如果键不存在，则将其解释为空列表，并返回0。当key存储的值不是列表时返回错误。
        System.out.println(redisTemplate.opsForList().size("myList"));
        System.out.println("--------List-------");

        // Set读写
        redisTemplate.delete("mySet");
        redisTemplate.opsForSet().add("mySet", "A");
        redisTemplate.opsForSet().add("mySet", "B");
        redisTemplate.opsForSet().add("mySet", "C");
        Set<String> setCache = redisTemplate.opsForSet().members("mySet");
        for (String s : setCache) {
            System.out.println(s);
        }
        System.out.println("--------Set-------");

        // Hash读写
        redisTemplate.delete("myHash");
        redisTemplate.opsForHash().put("myHash", "BJ", "北京");
        redisTemplate.opsForHash().put("myHash", "SH", "上海");
        redisTemplate.opsForHash().put("myHash", "HN", "河南");
        Map<String, String> hashCache = redisTemplate.opsForHash().entries("myHash");
        for (Map.Entry entry : hashCache.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
        
        User user = new User();
        user.setUserName("test");
        user.setUserSex("未知");
        //存储key：test keyid：row  内容：user对象
        redisTemplate.opsForHash().put("test","row",user);
        System.out.println(redisTemplate.opsForHash().get("test","row"));
        System.out.println("-------Hash--------");
        
    }
    
    @Test
    public void testSpringRedis2(){
        String  str = "string";//1.字符串
        List<String> list = new ArrayList<String>();//list
        list.add("0");
        list.add("中国");
        list.add("2");
        Set<String> set = new HashSet<String>();//set
        set.add("0");
        set.add("中国");
        set.add("2");
        Map<String, Object> map = new HashMap();//map
        map.put("key1", "str1");
        map.put("key2", "中国");
        map.put("key3", "str3");
        
        redisUtil.del("myStr","str");//删除数据
        
        //1.字符串操作
        redisUtil.set("str", str);
        redisUtil.expire("str", 120);//指定失效时间为2分钟
        String str1 = (String) redisUtil.get("str");
        System.out.println(str1);
        
        //2.list操作
        redisUtil.lSet("list", list);
        redisUtil.expire("list", 120);//指定失效时间为2分钟
        List<Object> list1 = redisUtil.lGet("list", 0, -1);
        System.out.println(list1);
            
        //3.set操作
        redisUtil.sSet("set", set);
        redisUtil.expire("set", 120);//指定失效时间为2分钟
        Set<Object> set1 = redisUtil.sGet("set");
        System.out.println(set1);
        
        
        //3.map操作
        redisUtil.hmset("map", map);
        redisUtil.expire("map", 120);//指定失效时间为2分钟
         Map<Object, Object> map1 = redisUtil.hmget("map");
        System.out.println(map1);
        
        
    }
    
    @Test
    public void listQueue() {
    	//1.初始化任务  一次
//    	userServiceImp.listQueueInit("1");
    	//2.商家任务
    	List<Object> list = userServiceImp.listQueueWait("1");
    	System.out.println("商家待执行任务"+list);
    	
    	//3.小哥触发任务
    	userServiceImp.listQueueTouch("1");
    	//4.小哥任务
    	List<Object> list1 = userServiceImp.listQueueWait("1");
    	System.out.println("小哥待执行任务"+list1);
    	
    	//5.完成任务
    	List<Object> list2 = userServiceImp.listQueueSucc("1");
    	System.out.println("已执行任务"+list2);
	}
    
    
    
    
}
