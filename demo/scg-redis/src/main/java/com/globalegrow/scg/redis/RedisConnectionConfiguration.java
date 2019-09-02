//package com.globalegrow.scg.redis;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.RedisSentinelConfiguration;
//
//abstract class RedisConnectionConfiguration {
//
//	private final RedisProperties properties;
//
//	private final RedisClusterConfiguration clusterConfiguration;
//
//	protected RedisConnectionConfiguration(RedisProperties properties,
//			ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
//			ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider) {
//		this.properties = properties;
//		this.clusterConfiguration = clusterConfigurationProvider.getIfAvailable();
//	}
//
//	protected final RedisClusterConfiguration getClusterConfiguration() {
//		if (this.clusterConfiguration != null) {
//			return this.clusterConfiguration;
//		}
//		if (this.properties.getCluster() == null) {
//			return null;
//		}
//		org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster clusterProperties = this.properties.getCluster();
//		RedisClusterConfiguration config = new RedisClusterConfiguration( clusterProperties.getNodes());
//		if (clusterProperties.getMaxRedirects() != null) {
//			config.setMaxRedirects(clusterProperties.getMaxRedirects());
//		}
////		if (this.properties.getPassword() != null) {
////			config.setPassword(this.properties.getPassword());
////		}
//		return config;
//	}
//
//	protected ConnectionInfo parseUrl(String url) {
//		try {
//			URI uri = new URI(url);
//			boolean useSsl = (url.startsWith("rediss://"));
//			String password = null;
//			if (uri.getUserInfo() != null) {
//				password = uri.getUserInfo();
//				int index = password.indexOf(':');
//				if (index >= 0) {
//					password = password.substring(index + 1);
//				}
//			}
//			return new ConnectionInfo(uri, useSsl, password);
//		}
//		catch (URISyntaxException ex) {
//			throw new IllegalArgumentException("Malformed url '" + url + "'", ex);
//		}
//	}
//
//	protected static class ConnectionInfo {
//
//		private final URI uri;
//
//		private final boolean useSsl;
//
//		private final String password;
//
//		public ConnectionInfo(URI uri, boolean useSsl, String password) {
//			this.uri = uri;
//			this.useSsl = useSsl;
//			this.password = password;
//		}
//
//		public boolean isUseSsl() {
//			return this.useSsl;
//		}
//
//		public String getHostName() {
//			return this.uri.getHost();
//		}
//
//		public int getPort() {
//			return this.uri.getPort();
//		}
//
//		public String getPassword() {
//			return this.password;
//		}
//
//	}
//
//}
