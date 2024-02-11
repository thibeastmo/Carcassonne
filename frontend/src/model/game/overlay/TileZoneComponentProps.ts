

export interface TileZoneComponentProps {
    availableTileZones?: number[],
    tileZoneId: number,
    placeSerfOnTileZone :  (tileZoneId: number) => void;
    placedTileZoneId: number;
    orientation: number;
}