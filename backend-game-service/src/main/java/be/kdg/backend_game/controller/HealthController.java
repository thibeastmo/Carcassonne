package be.kdg.backend_game.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    private static final Logger logger = Logger.getLogger(HealthController.class.getPackageName());



    @GetMapping("/")
    public ResponseEntity<?> health() {
        logger.info("Health checked");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
