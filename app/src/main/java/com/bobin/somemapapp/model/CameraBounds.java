package com.bobin.somemapapp.model;

public class CameraBounds {
    private MapCoordinates leftTop;
    private MapCoordinates rightBottom;
    private MapCoordinates center;

    public CameraBounds(MapCoordinates leftTop, MapCoordinates rightBottom, MapCoordinates center) {
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
        this.center = center;
    }

    public MapCoordinates getLeftTop() {
        return leftTop;
    }

    public MapCoordinates getRightBottom() {
        return rightBottom;
    }

    public MapCoordinates getCenter() {
        return center;
    }
}
