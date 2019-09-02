package com.hyx.core.converter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.NumberUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hyx.core.Context;
import com.hyx.core.utils.DateUtils;

@Configuration
public class GlobalegrowConverterConfig implements InitializingBean{
	 
	private static final Logger log = LoggerFactory.getLogger(GlobalegrowConverterConfig.class);

	public@Bean GenericConverter numberGenericConverter(){
		return new GenericConverter() {
			
			public@Override Set<ConvertiblePair> getConvertibleTypes() {
				return Collections.singleton(new ConvertiblePair(String.class, Number.class));
			}
			
			@SuppressWarnings("unchecked")
			public@Override Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
				String sourceStr = (String)source;
				if(sourceStr==null||(sourceStr=sourceStr.trim()).isEmpty()){
					log.info("接口{}，参数为空，视图转化为{}",Context.getRequestUri(),targetType.getType().getName());
					return null;
				}
				return NumberUtils.parseNumber(sourceStr, (Class<Number>) targetType.getType());
			}
		};
	}

	public@Bean GenericConverter enumGenericConverter(){
		return new GenericConverter() {
			
			@Override
			public Set<ConvertiblePair> getConvertibleTypes() {
				return Collections.singleton(new ConvertiblePair(String.class, Enum.class));
			}
			
			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Enum<?> convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
				String sourceStr = (String)source;
				if(sourceStr==null||(sourceStr=sourceStr.trim()).isEmpty()){
					log.info("接口{}，参数为空，视图转化为{}",Context.getRequestUri(),targetType.getType().getName());
					return null;
				}
				Class type=targetType.getType();
				try {
					return Enum.valueOf(type, sourceStr);
				} catch (Exception e) {
					log.info("接口{}，参数{}为空，视图转化为枚举{} ，失败",Context.getRequestUri(),sourceStr,targetType.getType().getName());
					try {
						return Enum.valueOf(type, sourceStr.toLowerCase());
					} catch (Exception exception) {
						return Enum.valueOf(type, sourceStr.toUpperCase());
					}
				}
			}
		};
	}
	
	public@Bean Converter<String, String> stringToStringConverter(){
		return new Converter<String, String>() {
			public String convert(String source) {
				if(source==null){
					log.info("接口{}，参数为空，视图转化为{}",Context.getRequestUri(),String.class.getName());
					return null;
				}
				return source;
			}
		};
	}
	
	public@Bean Converter<String, Boolean> stringToBooleanConverter(){
		return new Converter<String, Boolean>() {
			public Boolean convert(String source) {
				if(source==null||(source=source.trim()).isEmpty()) {
					return null;
				}
				return Boolean.valueOf(source);
			}
		};
	}

	public@Bean Converter<String, Character> stringToCharConverter(){
		return new Converter<String, Character>() {
			public Character convert(String source) {
				if(source==null||(source=source.trim()).isEmpty()){
					log.info("接口{}，参数为空，视图转化为{}",Context.getRequestUri(),String.class.getName());
					return null;
				}
				return source.charAt(0);
			}
		};
	}

	
	public@Bean Converter<String, Date> stringToDateConverter() {
		return new Converter<String, Date>() {
			public Date convert(String source) {
				return DateUtils.parse(source);
			}
		};
	}
	
	public SimpleModule simpleModule() {
		SimpleModule module = new SimpleModule();
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
		module.addSerializer(Long.class, ToStringSerializer.instance);
		module.addSerializer(long.class, ToStringSerializer.instance);
		module.addSerializer(Long.TYPE, ToStringSerializer.instance);
		
		return module;
	}
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("初始化转化实例完毕.....");
		objectMapper.registerModule(simpleModule());
		DateFormat dd = objectMapper.getDateFormat();
		objectMapper.setDateFormat(new SimpleDateFormat() {
			private static final long serialVersionUID = 1L;
			{
				if(dd!=null) {
					this.setTimeZone(dd.getTimeZone());
					this.setCalendar(dd.getCalendar());
					this.setNumberFormat(dd.getNumberFormat());
					try {
						this.setLenient(dd.isLenient());
					} catch (Exception e) {
						log.debug("初始化异常",e);
					}
				}
			}

			@Override
			public Date parse(String source, ParsePosition pos) {
				try {
					pos.setIndex(1);
					pos.setErrorIndex(0);
					return DateUtils.parse(source);
				} catch (Throwable e) {
					pos.setIndex(0);
					pos.setErrorIndex(1);
					return null;
				}
			}

			@Override
			public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
				if(dd==null)
					return super.format(date, toAppendTo, fieldPosition);
				return dd.format(date, toAppendTo, fieldPosition);
			}

			@Override
			public Object clone() {
				return this;
			}

		});
	}

}
