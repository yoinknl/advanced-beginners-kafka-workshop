package nl.yoink.training.kafka.workshop.kafka.streams.service;

import nl.yoink.training.kafka.workshop.kafka.producer.model.Player;
import nl.yoink.training.kafka.workshop.kafka.streams.model.Score;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class ScoreStreamBuilder {

	@Bean
	public KStream<String, Score> constructScoreBoard(StreamsBuilder streamsBuilder, @Value("${kafka.topic.player}") String playerTopic,
			@Value("${kafka.topic.score-board}") String scoreBoardTopic) {
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<Player> playerSerde = new JsonSerde<>(Player.class);
		JsonSerde<Score> scoreSerde = new JsonSerde<>(Score.class);

		KStream<String, Score> playerScoreStream = streamsBuilder.stream(playerTopic,
				Consumed.with(stringSerde, playerSerde))
				.peek(this::print)
				.filter((id, player) -> player.getScore() > 0)
				.selectKey((id, player) -> player.getName())
				.mapValues(Player::getScore)
				.groupByKey(Grouped.with(stringSerde, Serdes.Integer()))
				.aggregate(Score::new, this::addPlayerScoreToScore,
						Materialized.<String, Score, KeyValueStore<Bytes, byte[]>>as("player-score-store")
								.withKeySerde(stringSerde)
								.withValueSerde(scoreSerde)
								.withRetention(Duration.ofDays(14)))
				.toStream();

		playerScoreStream.to(scoreBoardTopic, Produced.with(stringSerde, scoreSerde));

		return playerScoreStream;
	}

	private void print(String key, Player player) {
		System.out.println("key: " + key + "\n value: " + player.toString());
	}

	private Score addPlayerScoreToScore(String playerName, int playerScore, Score score) {
		if (score.getPlayerName() == null) {
			score.setPlayerName(playerName);
		}
		score.setScore(score.getScore() + playerScore);

		return score;
	}
}
