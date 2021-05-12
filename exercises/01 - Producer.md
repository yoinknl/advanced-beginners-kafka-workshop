# Developing a Kafka Producer

In the kafka producer module a Spring Boot project has been set up. The project already has the correct dependencies in the POM and the 
folder structure that you will need to do the exercises.

1. Create a configuration class in the config folder called "KafkaSetupConfig". This class will create the topics. Use section 3 of the following link
as an inspiration: https://www.baeldung.com/spring-kafka. The "KafkaSetupConfig" class needs the following:
    1. @Configuration annotation
    2. A KafkaProperties class, has already been created.
    3. An integer field called "partitions" and an integer field called "replicationFactor". Inject these with values from the application.properties file with 6 for the partition and 1 for the replicationFactor.
    4. A bean creating a KafkaAdmin instance in which the AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG in the properties is set equal to the BootStrapServers from the autowired Kafkaproperties. See the Bealdung example on how to do this.
    5. A bean creating a NewTopic using Spring Kafka TopicBuilder (TopicBuilder.name().partitions(), etc). The name of the topic is injected in the beans method name from the application.properties. The partitions and replicationFactor are taken from the fields created in step 3.
    6. This will ensure the topics are created correctly on start up of the application

2. In the model folder
3. Create a class called "Player" in this folder. The class has the following fields:
    1. String id
    2. String name
    3. int age
    4. int score

4. Create class "KafkaProducerConfig" in the configuration folder. Use section 4.1 of the following link as an inspiration: https://www.baeldung.com/spring-kafka.
5. Again add the configuration annotation and create an @Autowired field for the KafkaProperties class
6. Create a bean that returns a Map<String, Object>. This map contains the producer properties. Set the following properties:
    1. ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to the bootstrap servers from the kafkaProperties
    2. ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG so it can handle a String as key
    3. ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG so it can handle a Player object as value
7. Create a bean for a ProducerFactory that can send records with a String as the key and a Player as the value and has the properties from step 7.
8. Create a bean for a KafkaTemplate that can send records with a String as the key and a Player as the value using the ProducerFactory from step 8.

9. Create Service class (PlayerService) that has a method that accepts a Player object and uses the KafkaTemplate to send this to Kafka. Use the player id as the key.
How to do this can be seen in section 4.2 of the link.
10. Create a controller class (PlayerController) that receives a player object from an api call and uses the PlayerService to send this to Kafka
11. Use the Kafka CLI commands in the cheat sheet of the exercises folder to check if the messsage arrived correctly on the topic