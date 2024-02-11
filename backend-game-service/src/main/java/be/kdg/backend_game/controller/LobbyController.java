package be.kdg.backend_game.controller;


import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.service.JwtExtractorHelper;
import be.kdg.backend_game.service.dto.lobby.InviteDto;
import be.kdg.backend_game.service.dto.lobby.LobbyInfoDto;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import be.kdg.backend_game.service.exception.InvalidInviteException;
import be.kdg.backend_game.service.game.GameService;
import be.kdg.backend_game.service.game.LobbyService;
import be.kdg.backend_game.service.exception.TileNotFoundException;
import be.kdg.backend_game.service.exception.LobbyNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private final LobbyService lobbyService;
    private final GameService gameService;

    public LobbyController(LobbyService lobbyService, GameService gameService) {
        this.lobbyService = lobbyService;
        this.gameService = gameService;
    }

    @GetMapping(value = "/get-lobby")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getLobby(@AuthenticationPrincipal Jwt token, @RequestParam UUID lobbyId) {
        try {
            var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            var lobby = lobbyService.getLobbyInfoDto(lobbyId, subjectId);
            var serializedLobby = serializeLobby(lobby);
            return ResponseEntity.ok(serializedLobby);
        } catch (LobbyNotFoundException lobbyNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lobby not found");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-all-open-lobbies")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getAllOpenLobbies(@AuthenticationPrincipal Jwt token) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        return ResponseEntity.ok(lobbyService.getAllOpenLobbies(subjectId));
    }


    @PostMapping("/create-lobby")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> createLobby(@AuthenticationPrincipal Jwt token, @RequestBody NewLobbyDto newLobbyDto) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            LobbyInfoDto lobby = lobbyService.createLobby(newLobbyDto, subjectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(lobby);
        } catch (TileNotFoundException accountNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(accountNotFoundException.getMessage());
        }
    }

    @PostMapping("/join-lobby")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> joinLobby(@AuthenticationPrincipal Jwt token, @RequestParam UUID lobbyId) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            lobbyService.joinLobby(lobbyId, subjectId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (LobbyNotFoundException | TileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/leave-lobby")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> leaveLobby(@AuthenticationPrincipal Jwt token, @RequestParam UUID lobbyId) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            lobbyService.leaveLobby(lobbyId, subjectId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (LobbyNotFoundException | TileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/start-game")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> startGame(@AuthenticationPrincipal Jwt token, @RequestParam UUID lobbyId) {
        try {
            UUID subjectid = JwtExtractorHelper.convertjwtToSubjectId(token);
            var game = lobbyService.startGameFromLobby(lobbyId);
            return ResponseEntity.status(HttpStatus.CREATED).body(game.getGameId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-active-games")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getActiveGames(@AuthenticationPrincipal Jwt token) {
        UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        var games = lobbyService.getActiveGamesOfPlayer(subjectId);
        return ResponseEntity.status(HttpStatus.OK).body(games);
    }

    @PostMapping("/quick-join-lobby")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> quickJoinPlayer(@AuthenticationPrincipal Jwt token, @RequestParam GameTypeEnum gameTypeEnum) {
        UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        var foundLobby = lobbyService.quickJoinAvailableLobby(subjectId, gameTypeEnum);
        if (foundLobby.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(foundLobby);
        }
    }


    @PostMapping("/send-invite")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> sendInvite(@AuthenticationPrincipal Jwt token,@RequestParam UUID lobbyId, @RequestParam UUID accountId){
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            InviteDto invite = lobbyService.createInvite(lobbyId, accountId, subjectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(invite);
        } catch (LobbyNotFoundException | AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidInviteException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/accept-invite")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> acceptInvite(@AuthenticationPrincipal Jwt token,@RequestParam UUID inviteId){
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            lobbyService.acceptInvite(inviteId, subjectId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (LobbyNotFoundException | AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidInviteException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-invite")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> deleteInvite(@AuthenticationPrincipal Jwt token, @RequestParam UUID inviteId){
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            lobbyService.removeInvite(inviteId, subjectId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidInviteException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    private String serializeLobby(LobbyInfoDto lobby) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(lobby);
    }
}
