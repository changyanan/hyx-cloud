package com.globalegrow.async.serialize.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.globalegrow.async.serialize.Serialization;



/**
 * 使用原生JackJson2方式序列化，反序列化
 * @author hulei
 *
 */
@Component
@ConditionalOnClass(ObjectMapper.class)
public class Jackson2Serialization implements Serialization {
	
	private static final Logger log = LoggerFactory.getLogger(Jackson2Serialization.class);

	private final ObjectMapper objectMapper=new ObjectMapper() ;
	{
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	@Override
	public String getContentType() {
		return "x-application/Jackson2";
	}

	@Override
	public byte[] serialize(Object object) {
		try {
			if(object==null) {
				return empty;
			}
			return objectMapper.writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			log.error("Jackson2 序列化数据失败",e);
			return empty;
		}
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> valueType) {
		try {
			if(bytes==null) {
				return null;
			}
			return objectMapper.readValue(bytes, valueType);
		} catch (IOException e) {
			log.error("Jackson2反序列化数据失败",e);
			return null;
		}
	}

}
