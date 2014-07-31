CacheWrapper
============

This is an extremely simple wrapper around different caching back-ends. Currently only Memcache is supported

Add this repository as an alternate repository
```xml
	<repositories>
		<repository>
			<id>CacheWrapper-mvn-repo</id>
			<url>https://raw.github.com/mohanarpit/CacheWrapper/mvn-repo/</url>
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
Sample Usage:

```java
CacheInterface instance = new Cache().getInstance("memcache");
instance.set("key", 0, "value");
String value = (String) instance.get("key");
System.out.println(value);
```
