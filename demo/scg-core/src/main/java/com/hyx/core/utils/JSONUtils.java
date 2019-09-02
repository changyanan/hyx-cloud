package com.hyx.core.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Component
public abstract class JSONUtils {

	private final static Logger logger = LoggerFactory.getLogger(JSONUtils.class);

	private static ObjectMapper jacksonObjectMapper = new ObjectMapper();
	
	static{
		jacksonObjectMapper.setSerializationInclusion(Include.NON_NULL);
		jacksonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		jacksonObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		SimpleModule module=new SimpleModule();
		module.addDeserializer(Date.class, new JsonDeserializer<Date>() {

			@Override
			public Date deserialize(JsonParser p, DeserializationContext ctxt)
					throws IOException, JsonProcessingException {
				if(p.getValueAsString()==null) {
					return null;
				}
				Date data = DateUtils.parse(p.getValueAsString());
				return data;
			}
		});
		jacksonObjectMapper.registerModule(module);
	}
	
	@Autowired
	void setObjectMapper(ObjectMapper jacksonObjectMapper) {
		JSONUtils.jacksonObjectMapper = jacksonObjectMapper;
	}

	public static String toJSONString(Object obj) {
		try {
			if(obj==null)
				return null;
			return jacksonObjectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("对象序列化为json字符串失败", e);
			return null;
		}
	}

	public static <T> T fromObject(String content, TypeReference<T> typeReference) {
		try {
			if(content==null)
				return null;
			return jacksonObjectMapper.readValue(content, typeReference);
		} catch (IOException e) {
			logger.error(" 反序列化json失败", e);
			return null;
		}
	}

	public static <T> T fromObject(String content, Class<T> tagcaz) {
		try {
			if(content==null)
				return null;
			return jacksonObjectMapper.readValue(content, tagcaz);
		} catch (IOException e) {
			logger.error(" 反序列化json失败  ", e);
			return null;
		}
	}

	public static <T> T fromObject(Object obj, Class<T> tagcaz) {
		try {
			if(obj==null)
				return null;
			String content = toJSONString(obj);
			return jacksonObjectMapper.readValue(content, tagcaz);
		} catch (IOException e) {
			logger.error(" 反序列化json失败", e);
			return null;
		}
	}

	public static <T1, T2> Map<T1, T2> fromObject(String content) {
		try {
			if(content==null)
				return null;
			return jacksonObjectMapper.readValue(content, new TypeReference<Map<T1, T2>>() { });
		} catch (IOException e) {
			logger.error(" 反序列化json为map失败", e);
			return null;
		}
	}

	public static <T> List<T> fromArray(String content) {
		try {
			if(content==null)
				return null;
			return jacksonObjectMapper.readValue(content, new TypeReference<List<T>>() { });
		} catch (IOException e) {
			logger.error(" 反序列化json数组失败", e);
			return null;
		}
	}

	public static <T> T fromObject(File inputfile, Class<T> t) {
		try {
			return jacksonObjectMapper.readValue(inputfile, t);
		} catch (IOException e) {
			logger.error("读文件{}反序列化为{}类型失败", inputfile.getAbsoluteFile(), t.getName(), e);
			return null;
		}
	}

	public static <T> T fromObject(byte[] bytes, Class<T> tagcaz) {
		try {
			if (bytes == null)
				return null;
			return jacksonObjectMapper.readValue(bytes, 0, bytes.length, tagcaz);
		} catch (IOException e) {
			logger.error(" 反序列化json失败", e);
			return null;
		}
	}

	public static <T> T fromObject(byte[] bytes, TypeReference<T> ref) {
		try {
			if (bytes == null)
				return null;
			return jacksonObjectMapper.readValue(bytes, 0, bytes.length, ref);
		} catch (IOException e) {
			logger.error(" 反序列化json失败", e);
			return null;
		}
	}

	public static Object fromObject(String content, Type tagType) {
		try {
			if (content == null)
				return null;
			JavaType javaType = TypeFactory.defaultInstance().constructType(tagType);
			return jacksonObjectMapper.readValue(content, javaType);
		} catch (IOException e) {
			logger.error(" 反序列化json失败", e);
			return null;
		}
	}
	
	
	/**
	 * 根据jsonPath获取json中的数据
	 * List<Object> list=JSONUtils.getJsonObjectByPath("{\"name\": [1,2,{\"id\":799,\"size\": \"XXX\"}]}","name.0","name.2.id");
	 * @param json json数据
	 * @param paths 需要访问的数据路径集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getJsonObjectByPath(String json, String ... paths){
		List<Object> ret=new ArrayList<Object>(paths.length);
		Map<String, Object> map = fromObject(json);
		for (String path : paths) {
			Object node = map;
			for (String p : path.split("\\.")) {
				if(List.class.isInstance(node)){
					Integer index = Integer.parseInt(p);
					if(((List<Object>) node).size()<=index) {
						node=null;
					}else {
						node = ((List<Object>) node).get(index);
					}
				} else {
					node = ((Map<String, Object>) node).get(p);
				}
				if(node==null) {
					break;
				}
			}
			ret.add(node);
		}
		return ret;
	}
 
}
