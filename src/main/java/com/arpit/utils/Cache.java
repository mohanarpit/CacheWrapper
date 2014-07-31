package com.arpit.utils;

import java.io.IOException;

public class Cache {
	private Config config = new Config();

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
		case "memcache": 
			try {
				return MemcacheWrapper.getInstance();
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}
}
