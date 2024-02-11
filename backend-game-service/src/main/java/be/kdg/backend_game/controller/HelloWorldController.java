package be.kdg.backend_game.controller;

import be.kdg.backend_game.service.AccountService;

import be.kdg.backend_game.domain.game.Tile;
import be.kdg.backend_game.domain.game.TileSeedEnum;
import be.kdg.backend_game.domain.game.TileTypeEnum;
import be.kdg.backend_game.service.HelloWorldService;
import be.kdg.backend_game.service.dto.HelloWorldResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/helloworld")
public class HelloWorldController {
    private final HelloWorldService helloWorldService;
    private final AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(HelloWorldController.class.toString());

    public HelloWorldController(HelloWorldService helloWorldService, AccountService accountService) {
        this.helloWorldService = helloWorldService;
        this.accountService = accountService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<HelloWorldResponseDto> helloWorld() {
        Optional<HelloWorldResponseDto> helloWorld = helloWorldService.getHelloWorld();
        if (helloWorld.isPresent()) {
            return ResponseEntity.ok(helloWorld.get());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
