/**
* Created by weiwenjie on 2017-3-24
*/
package gri.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-24
 * RedisHelper 和 RedisContainerService的区别在于：
 * RedisHelper是多个数格引擎公用的redis空间，现主要用于实现区块链
 * RedisContainerService是单个数格引擎私有的redis空间。
 */
public class RedisHelper{
	private static JedisPool jedisPool; 
	static{  
		JedisPoolConfig config = new JedisPoolConfig();  
        config.setMaxTotal(1);  
        config.setMinIdle(50);  
        config.setMaxIdle(3000);  
        config.setMaxWaitMillis(5000);  
        jedisPool = new JedisPool(config,Constant.redis_server, 6379);
    } 
	
	private String viewName;
	public final static String RESULT_KEY="result";

	public RedisHelper(String viewName){
		this.viewName = "__"+viewName+"__";
	}
	
	public Set<String> keys() {
		Jedis jedis = jedisPool.getResource();
		Set<String> result = jedis.keys(viewName+"*");
		jedis.close();
		return result;
	}
	
	public void cleanView(){
		Jedis jedis = jedisPool.getResource();
		Set<String> keys = jedis.keys(viewName+"*");
		if(!keys.isEmpty()){
			String[] strs = new String[keys.size()];
			keys.toArray(strs);
			jedis.del(strs);
		}
		jedis.close();
	}
	
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(viewName+key);
		jedis.close();
		return result;
	}
	
	public String set(String key, String data) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(viewName+key,data);
		jedis.close();
		return result;
	}
	
	public long del(String...keys) {
		Jedis jedis = jedisPool.getResource();
		long result = jedis.del(keys);
		jedis.close();
		return result;
	}
	
	public boolean hset(String key, String entry, String data) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(viewName+key, entry, data);
		jedis.close();
		return result>0;
	}
	
	public String hget(String key, String entry) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hget(viewName+key, entry);
		jedis.close();
		return result;
	}
	
	
	public boolean hexists(String key, String entry) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.hexists(viewName+key, entry);
		jedis.close();
		return result;
	}


	
	public long hdel(String key, String...entries) {
		Jedis jedis = jedisPool.getResource();
		long result = jedis.hdel(viewName+key, entries);
		jedis.close();
		return result;
	}
	
	
	public long hlen(String key) {
		Jedis jedis = jedisPool.getResource();
		long result = jedis.hlen(viewName+key);
		jedis.close();
		return result;
	}
	
	
	public Set<String> hkeys(String key) {
		Jedis jedis = jedisPool.getResource();
		Set<String> result = jedis.hkeys(viewName+key);
		jedis.close();
		return result;
	}
	
	
	public List<String> hvalues(String key) {
		Jedis jedis = jedisPool.getResource();
		List<String> result = jedis.hvals(viewName+key);
		jedis.close();
		return result;
	}
	
	public Map<String,String> hgetAll(String key) {
		Jedis jedis = jedisPool.getResource();
		Map<String,String> result = jedis.hgetAll(viewName+key);
		jedis.close();
		return result;
	}	
	
	public List<String> lget(String key) {
		Jedis jedis = jedisPool.getResource();
		List<String> result = jedis.lrange(viewName+key, 0, -1);
		jedis.close();
		return result;
	}
	
	
	public long lpush(String key,String data) {
		Jedis jedis = jedisPool.getResource();
		long result = jedis.rpush(viewName+key, data);
		jedis.close();
		return result;
	}
	
	
	public String lpop(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.lpop(viewName+key);
		jedis.close();
		return result;
	}
	
	
	public long llen(String key) {
		Jedis jedis = jedisPool.getResource();
		long result = jedis.llen(viewName+key);
		jedis.close();
		return result;
	}
	
	public long sadd(String key,String...members) {
		Jedis jedis = jedisPool.getResource();
		long result = jedis.sadd(viewName+key, members);
		jedis.close();
		return result;
	}
	
	public long slen(String key) {
		Jedis jedis = jedisPool.getResource();
		long result = jedis.scard(viewName+key);
		jedis.close();
		return result;
	}
	
	public long sdel(String key,String...members) {
		Jedis jedis = jedisPool.getResource();
		long result = jedis.srem(viewName+key, members);
		jedis.close();
		return result;
	}
	
	public boolean sexists(String key,String member) {
		Jedis jedis = jedisPool.getResource();
		boolean result = jedis.sismember(viewName+key, member);
		jedis.close();
		return result;
	}
}
