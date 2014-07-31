package com.arpit.utils;

import java.io.IOException;

import redis.clients.jedis.Jedis;

public class RedisWrapper extends Cache implements CacheInterface {
	private static RedisWrapper instance= null ;
	private static Jedis jedis = null;
	
	private RedisWrapper() throws IOException {
		jedis = new Jedis(getConfig().getNodes());
	}

	public static RedisWrapper getInstance() throws IOException{
		if(instance == null){
			synchronized(RedisWrapper.class){
				instance = new RedisWrapper(); 
			}
		}
		return instance; 
	}

	@Override
	public void set(String key, int ttl, Object obj) {
		if(jedis != null)
			jedis.set(key, obj.toString());
	}

	@Override
	public Object get(String key) {
		return (jedis != null) ? jedis.get(key):null;
	}

	@Override
	public boolean shutdown() {
		if(jedis != null){
			jedis.shutdown();
			return true;
		}
		return false;
	}
}
