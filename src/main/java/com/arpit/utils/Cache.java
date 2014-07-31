package com.arpit.utils;

import java.io.IOException;

public class Cache {
	private Config config = new Config();
	private final String MEMCACHE = "memcache";
	private final String REDIS = "redis";
	
	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public CacheInterface getInstance(String str){
		if(str == null)
			return null;
		str = str.toLowerCase().trim();
		switch(str){
		case MEMCACHE: 
			try {
				return MemcacheWrapper.getInstance();
			} catch (IOException e) {
				return null;
			}
		case REDIS:
			try {
				return RedisWrapper.getInstance();
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}
}
