package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.Serf;
import lombok.Getter;

import java.util.*;

@Getter
public class PlayersSerfsDto {
    private UUID gameId;
    private List<PlayerSerfsDto> playersSerfs;

    public PlayersSerfsDto() {}
    public PlayersSerfsDto(List<Serf> serfs, UUID gameId) {
        playersSerfs = new ArrayList<>();
        Map<Integer, List<Serf>> serfMap = new HashMap<>();
        //group by playerNumber
        for (var serf : serfs) {
            serfMap.computeIfAbsent(serf.getPlayer().getPlayerNumber(), k -> new ArrayList<>()).add(serf);
        }

        for (var serfMapItem : serfMap.entrySet()) {
            playersSerfs.add(new PlayerSerfsDto(serfMapItem.getValue(), serfMapItem.getKey()));
        }
        this.gameId = gameId;
    }
}
