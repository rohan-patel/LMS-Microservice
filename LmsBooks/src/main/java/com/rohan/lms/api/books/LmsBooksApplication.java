package com.rohan.lms.api.books;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.client.RestTemplate;

import com.rohan.lms.api.books.model.Book;

import jakarta.jms.ConnectionFactory;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJms
public class LmsBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsBooksApplication.class, args);
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}
	
//	@Bean
//	public MessageConverter jacksonJmsMessageConverter() {
//		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//		
////		Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
////        typeIdMappings.put("JMS_TYPE", HashMap.class);
////
////        converter.setTypeIdMappings(typeIdMappings);
//		
//		converter.setTargetType(MessageType.TEXT);
//		converter.setTypeIdPropertyName("JMS_TYPE");
//		return converter;
//	}
//	
//	@Bean
//	public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,
//			MessageConverter jacksonJmsMessageConverter) {
//		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
//		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
//		return jmsTemplate;
//	}

}
