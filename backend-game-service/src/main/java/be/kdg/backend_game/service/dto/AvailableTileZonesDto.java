package be.kdg.backend_game.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AvailableTileZonesDto {
    private Set<Integer> availableZones;

    public AvailableTileZonesDto(Set<Integer> availableZones) {
        this.availableZones = availableZones;
    }
}
