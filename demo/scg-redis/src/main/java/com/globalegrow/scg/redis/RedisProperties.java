//package com.globalegrow.scg.redis;
//
//import java.time.Duration;
//
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//@ConfigurationProperties(prefix = "spring.redis")
//public class RedisProperties {
//
//	private int database = 0;
//
//	/**
//	 * Connection URL. Overrides host, port, and password. User is ignored. Example:
//	 * redis://user:password@example.com:6379
//	 */
//	private String url;
//
//	/**
//	 * Redis server host.
//	 */
//	private String host = "localhost";
//
//	/**
//	 * Login password of the redis server.
//	 */
//	private String password;
//
//	/**
//	 * Redis server port.
//	 */
//	private int port = 6379;
//
//	/**
//	 * Whether to enable SSL support.
//	 */
//	private boolean ssl=false;
//
//	/**
//	 * Connection timeout.
//	 */
//	private long timeout=30000;
//
//	private Cluster cluster;
//
//	private final Lettuce lettuce = new Lettuce();
//
//	public int getDatabase() {
//		return this.database;
//	}
//
//	public void setDatabase(int database) {
//		this.database = database;
//	}
//
//	public String getUrl() {
//		return this.url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//
//	public String getHost() {
//		return this.host;
//	}
//
//	public void setHost(String host) {
//		this.host = host;
//	}
//
//	public String getPassword() {
//		return this.password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public int getPort() {
//		return this.port;
//	}
//
//	public void setPort(int port) {
//		this.port = port;
//	}
//
//	public boolean isSsl() {
//		return this.ssl;
//	}
//
//	public void setSsl(boolean ssl) {
//		this.ssl = ssl;
//	}
//
//	public void setTimeout(long timeout) {
//		this.timeout = timeout;
//	}
//
//	public long getTimeout() {
//		return this.timeout;
//	}
//
//
//	public Cluster getCluster() {
//		return this.cluster;
//	}
//
//	public void setCluster(Cluster cluster) {
//		this.cluster = cluster;
//	}
//
//	public Lettuce getLettuce() {
//		return this.lettuce;
//	}
//
//	/**
//	 * Lettuce client properties.
//	 */
//	public static class Lettuce {
//
//		/**
//		 * Shutdown timeout.
//		 */
//		private Duration shutdownTimeout = Duration.ofMillis(100);
//
//		/**
//		 * Lettuce pool configuration.
//		 */
//		private Pool pool;
//
//		public Duration getShutdownTimeout() {
//			return this.shutdownTimeout;
//		}
//
//		public void setShutdownTimeout(Duration shutdownTimeout) {
//			this.shutdownTimeout = shutdownTimeout;
//		}
//
//		public Pool getPool() {
//			return this.pool;
//		}
//
//		public void setPool(Pool pool) {
//			this.pool = pool;
//		}
//
//	}
//
//}
