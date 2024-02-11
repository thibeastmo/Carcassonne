package be.kdg.backend_game.sender;

import be.kdg.backend_game.configuration.RabbitTopology;
import be.kdg.backend_game.message.AccountListMessage;
import be.kdg.backend_game.message.AccountMessage;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountSender {
    private final RabbitTemplate rabbitTemplate;

    public AccountSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Retryable(retryFor = AmqpException.class, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void sendAccounts(List<AccountMessage> accountMessages) {
        AccountListMessage accountListMessage = new AccountListMessage(accountMessages);
        System.out.println(accountListMessage);
        rabbitTemplate.convertAndSend(
                RabbitTopology.TOPIC_EXCHANGE,
                "carcassonne.account.all",
                accountListMessage
        );
    }
}
