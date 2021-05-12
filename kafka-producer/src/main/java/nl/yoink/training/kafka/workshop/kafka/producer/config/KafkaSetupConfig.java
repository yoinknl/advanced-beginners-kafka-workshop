package nl.yoink.training.kafka.workshop.kafka.producer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

// Add the annotation to make the class a configuration class
public class KafkaSetupConfig {

	@Autowired
	private KafkaProperties kafkaProperties;

	// partitions

	// replication factor

	// Kafka admin bean

	// Create player topic
}
