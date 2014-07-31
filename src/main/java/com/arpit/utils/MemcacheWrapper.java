package com.arpit.utils;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * @author arpit
 *
 */
public class MemcacheWrapper implements CacheInterface {
	private static MemcacheWrapper instance= null ;
	private static MemcachedClient[] connections = null ;
	private Config config = new Config(); 

	private MemcacheWrapper() throws IOException {
		connections = new MemcachedClient[config.getNumberOfConnections()]; 
		for(int i=0 ; i<config.getNumberOfConnections() ; i++){
			MemcachedClient	 client = new MemcachedClient(new DefaultConnectionFactory(),AddrUtil.getAddresses(config.getNodes())) ;
			connections[i] = client; 
		}
	}

	public static MemcacheWrapper getInstance() throws IOException{
		if(instance == null){
			synchronized(MemcacheWrapper.class){
				instance = new MemcacheWrapper(); 
			}
		}
		return instance; 
	}

	public MemcachedClient getCache(String key){
		return getCache(key, MODE.RANDOM);
	}

	public MemcachedClient getCache(){
		return getCache(null, MODE.RANDOM);
	}

	/**
	 * Get the cache node to which the key will be stored.
	 * @param key
	 * @param algorithm 
	 */
	private MemcachedClient getCache(String key, MODE algorithm) {
		MemcachedClient c= null;

		switch(algorithm){
		case RANDOM: 
			//Randomly use one of the available connections in the pool
			int i = (int) (Math.random()* config.getNumberOfConnections());
			c = connections[i];
			break;
		case HASH:
			//Pick the cache node based on the key hash
			int hashCode = key.hashCode() % config.getNumberOfConnections() ;
			if(hashCode<0) hashCode *= -1; 
			break;
		}
		return c;
	}

	public void set(String key,  int ttl, Object obj ){
		getCache(key).set(config.getNamespace()+key, ttl, obj) ;
	}

	public Object get(String key){
		Object obj = getCache(key).get(config.getNamespace()+key) ;
		return obj ;
	}

	public boolean shutdown(){
		for(int i=0 ; i<config.getNumberOfConnections() ; i++){
			if(connections[i] != null)
				connections[i].shutdown(); 
		}
		return true; 
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
}