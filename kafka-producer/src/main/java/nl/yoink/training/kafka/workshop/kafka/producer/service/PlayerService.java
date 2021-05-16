package nl.yoink.training.kafka.workshop.kafka.producer.service;

import nl.yoink.training.kafka.workshop.kafka.producer.model.Player;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

	private String playerTopic;
	private KafkaTemplate<String, Player> kafkaTemplate;

	public PlayerService(@Value("${kafka.topic.player}") String playerTopic, KafkaTemplate<String, Player> kafkaTemplate) {
		this.playerTopic = playerTopic;
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendPlayer(Player player) {
		kafkaTemplate.send(playerTopic, player.getId(), player);
	}
}
