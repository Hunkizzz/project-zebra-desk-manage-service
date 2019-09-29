package team.projectzebra.rabbitmq;

/**
 * Created by dmaslov on 22/07/2018.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import team.projectzebra.dto.WorkspaceStatusDto;
@Profile("prod")
@Component
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.workspace-reservation-service.exchange.stomp}")
    private String workspaceReservationServiceStompExchange;
    @Value("${spring.rabbitmq.workspace-reservation-service.routingkey.stompRoutingKey}")
    public String workspaceReservationServiceRoutingKey;

    @Autowired
    public Producer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

//    private String itemMessageToJson(SearchResultDto processingQueueItem) {
//
//        try {
//            return jacksonObjectMapper.writeValueAsString(processingQueueItem);
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public void sendMessage(WorkspaceStatusDto workspaceStatusDto) {
        logger.debug("Sending message...");

        rabbitTemplate.convertAndSend(
                workspaceReservationServiceStompExchange,
                workspaceReservationServiceRoutingKey,
                workspaceStatusDto);
    }
}