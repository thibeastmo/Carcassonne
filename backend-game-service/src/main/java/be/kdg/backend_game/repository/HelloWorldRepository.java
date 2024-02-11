package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.HelloWorldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HelloWorldRepository extends JpaRepository<HelloWorldEntity, UUID> {
}
