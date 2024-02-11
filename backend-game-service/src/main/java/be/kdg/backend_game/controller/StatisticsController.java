package be.kdg.backend_game.controller;


import be.kdg.backend_game.service.JwtExtractorHelper;
import be.kdg.backend_game.service.dto.statistics.UserStatisticsDto;
import be.kdg.backend_game.service.statistics.UserStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private static final Logger logger = Logger.getLogger(StatisticsController.class.getPackageName());
    private final UserStatisticsService userStatisticsService;

    public StatisticsController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getUserStatisticsForPlayer(@AuthenticationPrincipal Jwt token) {
        try {
            var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            var userStatisticsOptional = userStatisticsService.retrieveUserStatisticsBySubjectId(subjectId);
            if (userStatisticsOptional.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(new UserStatisticsDto(userStatisticsOptional.get()));
        }
        catch (Exception ex){
            logger.log(Level.SEVERE, "Could not return user statistics for id: " + token.getId());
            return ResponseEntity.internalServerError().build();
        }
    }
}
