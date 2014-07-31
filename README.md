CacheWrapper
============

This is an extremely simple wrapper around different caching back-ends. 

Current back-ends supported:
  * Memcache
  * Redis (minimal support for Redis as an LRU cache. Clustering not enabled for Redis)

Add this repository as an alternate repository
```xml
	<repositories>
		<repository>
			<id>cache-wrapper-mvn-repo</id>
			<url>https://raw.github.com/mohanarpit/cache-wrapper/mvn-repo/</url>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
	</repositories>
```
Add the dependency to your pom.xml as below
```xml
	<dependencies>
		<dependency>
			<groupId>com.arpit</groupId>
			<artifactId>cache-wrapper</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	</dependencies>
```
### Sample Usage:

Set the configs such as host, number of connections etc.
```java
Config config = new Config();
config.setNodes("localhost");
Cache cache = new Cache();
cache.setConfig(config);
```
Get the instance of the cache that you want. 

For Memcache
```java
CacheInterface cmem = cache.getInstance("memcache");
```

For Redis
```java
CacheInterface cred = cache.getInstance("redis");
```
Simply use it as a cache now
```java
cmem.set("key", 0, "values");
System.out.println(cmem.get("key"));

cred.set("key", 0, "values");
System.out.println(cred.get("key"));
```
That's it! Dance around in glorious joy :)
