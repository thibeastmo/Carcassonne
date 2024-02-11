package be.kdg.backend_game.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity(name="HELLO_WORLD")
@Data
public class HelloWorldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID helloId;
    private String helloValue;
}
