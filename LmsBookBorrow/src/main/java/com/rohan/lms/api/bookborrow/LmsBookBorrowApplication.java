package com.rohan.lms.api.bookborrow;

import java.util.HashMap;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.client.RestTemplate;

import com.rohan.lms.api.bookborrow.model.Book;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJms
public class LmsBookBorrowApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsBookBorrowApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
        typeIdMappings.put("JMS_TYPE", Book.class);

        converter.setTypeIdMappings(typeIdMappings);
		
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("JMS_TYPE");
		return converter;
	}

}
