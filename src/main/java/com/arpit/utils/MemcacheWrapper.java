package com.arpit.utils;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * @author arpit
 *
 */
public class MemcacheWrapper extends Cache implements CacheInterface {
	private static MemcacheWrapper instance= null ;
	private static MemcachedClient[] connections = null ;
	
	private MemcacheWrapper() throws IOException {
		connections = new MemcachedClient[getConfig().getNumberOfConnections()]; 
		for(int i=0 ; i<getConfig().getNumberOfConnections() ; i++){
			MemcachedClient	 client = new MemcachedClient(new DefaultConnectionFactory(),AddrUtil.getAddresses(getConfig().getNodes())) ;
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
			int i = (int) (Math.random()* getConfig().getNumberOfConnections());
			c = connections[i];
			break;
		case HASH:
			//Pick the cache node based on the key hash
			int hashCode = key.hashCode() % getConfig().getNumberOfConnections() ;
			if(hashCode<0) hashCode *= -1; 
			break;
		}
		return c;
	}

	public void set(String key,  int ttl, Object obj ){
		getCache(key).set(getConfig().getNamespace()+key, ttl, obj) ;
	}

	public Object get(String key){
		Object obj = getCache(key).get(getConfig().getNamespace()+key) ;
		return obj ;
	}

	public boolean shutdown(){
		for(int i=0 ; i<getConfig().getNumberOfConnections() ; i++){
			if(connections[i] != null)
				connections[i].shutdown(); 
		}
		return true; 
	}
}