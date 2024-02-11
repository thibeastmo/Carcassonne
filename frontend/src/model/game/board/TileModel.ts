export interface TileModel {
    image_url: string,
    orientation: number,
    coordinates: {
        x: number,
        y: number
    },
    location: {
        top: number,
        left: number
    }
}