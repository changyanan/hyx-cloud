package com.hyx.core.consts;

import java.nio.charset.Charset;

/**
 * 全局缓存配置
 * 
 * @author lenovo
 *
 */
public interface Const {

	/*
	 * 默认编码
	 */
	Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	/**
	 * 默认时间编码
	 */
	String DETA_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 默认语言
	 */
	String DEFAULT_LANGUAGE = "en";

	/**
	 * 校验重复请求的次数
	 */
	int CHECK_REPEAT_REQUEST_COUNT = 4;

	/**
	 * 校验重复请求的时间范围,单位秒 ，
	 * 也就是CheckRepeatRequestTime秒内重复提交超过CheckRepeatRequestCount次后，就为校验不通过
	 */
	int CHECK_REPEAT_REQUEST_TIME = 5;

	/**
	 * redis 事务重试次数
	 */
	int REDIS_TRANSACTION_RETRY_NUMBER = 3;

	/**
	 * 默认客户端用户标识
	 */
	String USERID_COOKIE_NAME = "USERID_COOKIE_NAME";

	/**
	 * http请求流水号
	 */
	String REQUEST_SERIAL_NUMBER = "REQUEST_SERIAL_NUMBER";

	/**
	 * Spring MVC请求跳转前缀
	 */
	String REDIRECT_PREFIX = "redirect:";

	/**
	 * Spring MVC服务器转发前缀
	 */
	String FORWARD_PREFIX = "forward:";
}
