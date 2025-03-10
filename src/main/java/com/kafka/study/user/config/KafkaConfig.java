package com.kafka.study.user.config;

import com.kafka.study.user.dto.event.CreateOrderEvent;
import com.kafka.study.user.dto.event.ErrorOrderEvent;
import com.kafka.study.user.dto.event.FinishOrderEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // 동적으로 만들고
    @Bean
    public <T> KafkaTemplate<String, T> kafkaTemplate(ProducerFactory<String, T> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    // 구체화 시키고
    @Bean
    public KafkaTemplate<String, ErrorOrderEvent> errorEventKafkaTemplate() {
        return kafkaTemplate(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, FinishOrderEvent> endEventKafkaTemplate() {
        return kafkaTemplate(producerFactory());
    }


    // 공통으로 -> 제네릭
    // 별도의 장치가 필요하다.
    // 최대한 제네릭하게 바꿔야 한다.
    // Consumer Factory 설정
    @Bean
    public <T> ConsumerFactory<String, T> kafkaConsumer() {
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 아래 두개는 등록하지 않아도 정상적으로 구동은 되어짐
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        consumerConfig.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(consumerConfig);
    }

    // 이거 없으면 json으로 안 만들어줌
    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }


    // 팩토리로 등록
    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> consumerCommonFiled() {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumer());
        return factory;
    }


    // 구체화
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CreateOrderEvent> containerFactory() {
        return consumerCommonFiled();
    }


}
