package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.user_management.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, UUID> {
    Optional<UserStatistics> findUserStatisticsByAccount_AccountId(UUID accountId);
    Optional<UserStatistics> findUserStatisticsByAccount_SubjectId(UUID subjectId);
}
