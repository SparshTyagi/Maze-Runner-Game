package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Floor{
    public static final int UNIT = 16; //Unit of space in game
    private Texture tileSheet = new Texture(Gdx.files.internal("basictiles.png"));
    private TextureRegion floorTexture;

    public Floor(int floorType) {
        switch (floorType){
            default -> floorTexture = new TextureRegion(tileSheet,3*UNIT,0,UNIT,UNIT);
            case 1-> floorTexture = new TextureRegion(tileSheet,7*UNIT,0,UNIT,UNIT);
            case 2-> floorTexture = new TextureRegion(tileSheet,7*UNIT,UNIT,UNIT,UNIT);
            case 3-> floorTexture = new TextureRegion(tileSheet,UNIT,2*UNIT,UNIT,UNIT);
            case 4-> floorTexture = new TextureRegion(tileSheet,0,8*UNIT,UNIT,UNIT);
        }
    }

    public TextureRegion getFloorTexture() {
        return floorTexture;
    }
}
