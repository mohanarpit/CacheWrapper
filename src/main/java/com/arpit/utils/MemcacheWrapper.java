package com.arpit.utils;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * @author arpit
 *
 */
public class MemcacheWrapper {
	private static String NAMESPACE ;
	private static MemcacheWrapper instance= null ;
	private static MemcachedClient[] connections = null ;
	private static Integer numberOfConnections = 10 ;
	private static String nodes = "127.0.0.1:11211" ;

	private MemcacheWrapper() throws IOException {
		connections = new MemcachedClient[numberOfConnections]; 
		for(int i=0 ; i<numberOfConnections ; i++){
			MemcachedClient client = new MemcachedClient(new DefaultConnectionFactory(),AddrUtil.getAddresses(nodes)) ;
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
			int i = (int) (Math.random()* numberOfConnections);
			c = connections[i];
			break;
		case HASH:
			//Pick the cache node based on the key hash
			int hashCode = key.hashCode() % numberOfConnections ;
			if(hashCode<0) hashCode *= -1; 
			break;
		}
		return c;
	}

	public void set(String key,  int ttl, Object obj ){
		getCache(key).set(NAMESPACE+key, ttl, obj) ;
	}

	public Object get(String key){
		Object obj = getCache(key).get(NAMESPACE+key) ;
		return obj ;
	}

	public boolean shutdown(){
		for(int i=0 ; i<numberOfConnections ; i++){
			if(connections[i] != null)
				connections[i].shutdown(); 
		}
		return true; 
	}

	/**
	 * Set the number of connections that will be supported from the application. Defaults to 10.
	 * @param numberOfConnections
	 */
	public static void setNumberOfConnections(Integer numberOfConnections) {
		MemcacheWrapper.numberOfConnections = numberOfConnections;
	}

	public static Integer getNumberOfConnections() {
		return numberOfConnections;
	}

	/**
	 * Set the namespace under which the keys will be stored.
	 * @param namespace
	 */
	public static void setNamespace(String namespace) {
		NAMESPACE = namespace;
	}

	public static String getNamespace() {
		return NAMESPACE;
	}

	/**
	 * Set the node addresses to which the application will connect. Defaults to 127.0.0.1:11211
	 * Multiple nodes must be space separated. Example, "127.0.0.1:11211 192.168.x.x:11211"
	 * @param nodeString
	 */
	public static void setNodes(String nodeString) {
		nodes = nodeString;
	}

	public static String getNodes() {
		return nodes;
	}
}