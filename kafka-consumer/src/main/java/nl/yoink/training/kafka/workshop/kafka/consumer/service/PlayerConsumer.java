package nl.yoink.training.kafka.workshop.kafka.consumer.service;

import nl.yoink.training.kafka.workshop.kafka.producer.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PlayerConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(PlayerConsumer.class);

	@KafkaListener(topics = "#{'${kafka.topic.player}'}")
	public void consume(Player player) {
		LOG.info(String.format("#### -> Consumed message -> %s", player));
	}
}
