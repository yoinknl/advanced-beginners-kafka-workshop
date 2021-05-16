package nl.yoink.training.kafka.workshop.kafka.consumer.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaSetupConfig {

	@Autowired
	private KafkaProperties kafkaProperties;

	@Value("${kafka.partitions}")
	private int partitions;

	@Value("${kafka.replicationFactor}")
	private short replicationFactor;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic playerTopic(@Value("${kafka.topic.player}") String playerTopic) {
		return TopicBuilder
				.name(playerTopic)
				.partitions(partitions)
				.replicas(replicationFactor)
				.build();
	}
}
