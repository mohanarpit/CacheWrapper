package com.arpit.utils;

public class Config {
	private Integer numberOfConnections = 10 ;
	private String nodes = "127.0.0.1:11211";
	private static String NAMESPACE="Default" ;
	
	/**
	 * Set the number of connections that will be supported from the application. Defaults to 10.
	 * @param numberOfConnections
	 */
	public void setNumberOfConnections(Integer numberOfConnections) {
		this.numberOfConnections = numberOfConnections;
	}

	public Integer getNumberOfConnections() {
		return numberOfConnections;
	}

	/**
	 * Set the namespace under which the keys will be stored.
	 * @param namespace
	 */
	public static void setNamespace(String namespace) {
		NAMESPACE = namespace;
	}

	public String getNamespace() {
		return NAMESPACE;
	}

	/**
	 * Set the node addresses to which the application will connect. Defaults to 127.0.0.1:11211
	 * Multiple nodes must be space separated. Example, "127.0.0.1:11211 192.168.x.x:11211"
	 * @param nodeString
	 */
	public void setNodes(String nodeString) {
		nodes = nodeString;
	}

	public String getNodes() {
		return nodes;
	}
}
