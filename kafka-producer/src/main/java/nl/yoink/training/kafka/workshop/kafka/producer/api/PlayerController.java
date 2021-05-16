package nl.yoink.training.kafka.workshop.kafka.producer.api;

import nl.yoink.training.kafka.workshop.kafka.producer.model.Player;
import nl.yoink.training.kafka.workshop.kafka.producer.service.PlayerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

	private PlayerService playerService;

	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@PostMapping("/player")
	public void sendPlayer(@RequestBody Player player) {

		playerService.sendPlayer(player);
	}

}
