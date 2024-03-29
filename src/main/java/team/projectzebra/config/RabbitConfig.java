package team.projectzebra.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Proudly created by dmaslov on 02/01/2018.
 */
@Profile("prod")
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;
    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;
    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;
    @Value("#{new Integer('${spring.rabbitmq.port}')}")
    private Integer rabbitmqPort;
    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitmqVirtualHost;
    @Value("${spring.rabbitmq.concurrentConsumers}")
    private Integer concurrentConsumers;
    @Value("${spring.rabbitmq.maxConcurrentConsumers}")
    private Integer maxConcurrentConsumers;


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(rabbitmqHost);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        connectionFactory.setPort(rabbitmqPort);
        connectionFactory.setVirtualHost(rabbitmqVirtualHost);
        return connectionFactory;
    }



//    @Bean
//    public RetryOperationsInterceptor rabbitSourceRetryInterceptor() {
//        return RetryInterceptorBuilder.stateless()
//                .maxAttempts(1)
//                .recoverer(new RejectAndDontRequeueRecoverer())
//                .build();
//    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory());
//        factory.setConcurrentConsumers(concurrentConsumers);
//        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setAdviceChain(new Advice[]{rabbitSourceRetryInterceptor()});
//        return factory;
//    }

    @Bean
    public RabbitTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonConverter());
        return template;
    }


    @Bean
    public MessageConverter jsonConverter() {

        return new Jackson2JsonMessageConverter();
    }


}
