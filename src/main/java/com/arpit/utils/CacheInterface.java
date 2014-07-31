package com.arpit.utils;

public interface CacheInterface {

	public void set(String key,  int ttl, Object obj );
	
	public Object get(String key);
	
	public boolean shutdown();
	
	public enum MODE{
		RANDOM, HASH;
	}
}
