# Set up the developer environment

1. Run the following command in your terminal in the root of this project:
    ```
    docker-compose up -d
    ```
    This will start the docker containers of the Zookeeper server and the Kafka server.
    Wait until the command has finished.
2. Test if the Kafka server is running using the following command:
    ```
    nc -z localhost 9092
    ```
   It should tell you that a connection could be established.
3. Lastly, check the logs of the Kafka container to be sure it started up:
    ```
    docker-compose logs kafka | grep -i started
    ```
    The logs should state that the server has started. If this is the case you're ready to go!