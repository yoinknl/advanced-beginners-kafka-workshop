# Developing a Kafka Consumer

NOTE: Perform the following steps inside the kafka-consumer module.

Inspiration for step 1 to 3: https://www.baeldung.com/spring-kafka

1. Create a config folder
2. Copy the class called "KafkaSetupConfig" in the config folder from the producer including the application.properties.
3. In the application.properties set the consumer group id to "player-consumer-group" and the auto offset reset to "earliest".
4. Use section 5.1 from the link as inspiration and create a class called "KafkaConsumerConfig" in the config folder and add the following:
    1. The @Configuration and @EnableKafka annotation
    2. An @Autowired field for KafkaProperties
    3. A bean called consumerConfig() returning a Map<String, Object> in which the following properties are set:
        - ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to the bootstrap servers from kafkaProperties
        - ConsumerConfig.GROUP_ID_CONFIG to the groupId of the consumer in the kafkaProperties
        - ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to the auto offset reset of the consumer in the kafkaProperties
        - ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer.class
        - ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer.class
        - JsonDeserializer.TRUSTED_PACKAGES to all ("*")
    4. Create a bean for a ConsumerFactory that can consume a message with a String as key and a Player as value. Use a dependency on the Producer for the player class. The factory uses the properties from step 3.
    5. Create a bean for a ConcurrentKafkaListenerContainerFactory that can consume a message with a String as key and a Player as value.
    Set the consumer factory of step 4 as the consumer factory of the containerFactory.

5. Create a folder called "service" and in it a service class (PlayerConsumer) that has a method to consume a Player message using the @KafkaListener annotation. 
See section 5.2 from the Baeldung link as an example. You can use SpEL to set the topic from the application.properties. Log the received message.
6. Start the consumer and test if it consumes a message correctly.

Optional if enough time for the Kafka streams part:
7. Use the CLI commands to see how the partitions are assigned to the consumer.
8. Start two other instances of the consumer application and look again at the partitions.
9. Shut down one of the consumers and look again. Look also at the logs of the Kafka consumer.
