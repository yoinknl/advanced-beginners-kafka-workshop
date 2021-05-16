# Exercise: Kafka streams

Look at the first code example in the following link. Only look at the first code snippet and don't read further than "Step 1: Download the code". https://kafka.apache.org/26/documentation/streams/quickstart

We will now build a Kafka streams application in the module called kafka-streams.

1. Create a config folder
2. Copy the class called "KafkaSetupConfig" in the config folder from the consumer including the application.properties.
3. Add a topic called "score-board" using the topic builder, put the name of the topic in the properties and use that property in the builder.
4. Add the "@EnableKafkaStreams" annotation to the KafkaStreamsApplication class.
5. Add "spring.kafka.streams.application-id=player-consumer-application" to the application properties
6. Create a folder called "model" and create a class in it called "Score" with playerName as a String, score as an int and getters and setters.
7. Create a folder called "service" and in it a class named "ScoreStreamBuilder" with @Component annotation.
8. Create a @Bean method that returns a KStream<String, Score> called constructScoreBoard() with arguments: StreamsBuilder streamsBuilder, "@Value("${kafka.topic.player}") String playerTopic and @Value("${kafka.topic.score-board}") String scoreBoardTopic".
The StreamsBuilder is a convenient topology builder provided by the autoconfiguration of Spring when you add the "@EnableKafkaStreams" annotation.
9. We will now use the StreamsBuilder to create a topology. First we need to define the Serdes that are used by KafkaStreams to serialize and deserialize the records.
The source of the topology we will build contains records with a string as the key and a Player object as the value. Therefore, we need to add the following Serdes:
    ```
   Serde<String> stringSerde = Serdes.String();
   JsonSerde<Player> playerSerde = new JsonSerde<>(Player.class);
   ```
   Create a dependency in the POM on the producer and use the Player class from the Producer.
10. Now write a KStream<String, Score> object called "playerScoreStream" and use the streamsbuilder to create a stream. 
The StreamsBuilder.stream() method does this for you. The method takes two arguments. The first is the playerTopic argument to specify the topic name.
The second one is optional but specifies how to consume the records. This is done with a "Consumed" object that takes two serdes. One for the key and one for the value.
Add the following as the second argument: "Consumed.with(stringSerde, playerSerde)". You know have a KStream<String, Player> stream!
11. Now we are going to perform some stateless stream operations on the stream. The first is a peek operation. Write ".peek()" after the ".stream()" method.
    The peek method allows you to do something with the data without changing it. One example is logging the data. That is what we are going to do.
    The method takes a function as an argument. The arguments of the function are key and value. So the lambda will look something like this:
       ```
       .streamsBuilder
       .stream(..)
       .peek((key, value) -> ..)
       ```
   
    When you have written the lambda extract it to a function. For instance:

    ```
    private void print(String key, Player player) {
        System.out.println("key: " + key + "\n value: " + player.toString());
    }
    ```
    This allows you to put a breakpoint in the function and helps you with debugging the stream and seeing if the stream is working as it is supposed to.
12. Now add ".filter((key, value) -> ..)" to filter out all player objects that do not have a score higher than 0. 
13. Now use selectKey((key, value) -> ..) to take the player name from the player object and make it the key. 
We can use this later to add all the scores of players with the same name.
14. Now use .mapValues(player -> ..) to only take the score of the player. You should then have a stream with
a String as the key (the player name) and an Integer as the value (the player score).
15. Now do a groupByKey(Grouped.with(..)) and put the stringSerde as the first argument in the with() method and Serdes.Integer() as the second argument.
This prepares the stream to do an aggregate operation that we will do in the next step.
16. Now add the following code for the aggregate operation:
    ```
        .aggregate(Score::new, this::addPlayerScoreToScore,
            Materialized.<String, Score, KeyValueStore<Bytes, byte[]>>as("player-score-store")
                    .withKeySerde(stringSerde)
                    .withValueSerde(scoreSerde)
                    .withRetention(Duration.ofDays(14)))
    
        // outside the constructScoreBoard() method in the class:
        private Score addPlayerScoreToScore(String playerName, int playerScore, Score score) {
            if (score.getPlayerName() == null) {
                score.setPlayerName(playerName);
            }
            score.setScore(score.getScore() + playerScore);
    
            return score;
        }  
    ```

    The aggregate method creates a new Score object when there is no known Score object for the key of the record (in our case the player 
    name as a String). Then it calls the aggregator method (in our case addPlayerScoreToScore()) and finally you need to declare how this 
    state will be kept. This is done using a KeyValueStore.
17. After the aggregate write .toStream(); to make a stream out of the KTable again. You now have created a KStream<String, Score> playerScoreStream object.
18. Add a serde for a Score class at the top of the method. Now write playerScoreStream.to(..) and write it to the scoreBoardTopic using 
Produced.with() with a serde for a String and for a Score class.
19. Now return the playerScoreStream. This concludes the topology. Use the following command to create a console consumer to check if the topology works:
    ```
    docker-compose exec kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic score-board --from-beginning   
    ```

