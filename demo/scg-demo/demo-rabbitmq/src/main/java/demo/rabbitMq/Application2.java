package demo.rabbitMq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Application2 implements CommandLineRunner{

	 
	@Autowired RabbitTemplate rabbitTemplate;
	@Override
	public void run(String... args) throws Exception {
		rabbitTemplate.convertAndSend("iss_stock_info_detail_exchange", null, "322222222222222");
	}
	
	@RabbitListener(queues = "iss_stock_info_detail_test", containerFactory = "rabbitListenerContainerFactory")
	public void testRabbitListener(@Payload String json) {

		System.err.println(json);
	}

}
