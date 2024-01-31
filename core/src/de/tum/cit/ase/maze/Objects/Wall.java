package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wall extends Entity{
    public static final int UNIT = 16; //Unit of space in game
    private Texture tileSheet = new Texture(Gdx.files.internal("basictiles.png"));
    private TextureRegion wallTexture;

    public Wall(int wallType,float xCoordinate, float yCoordinate) {
        super(xCoordinate,yCoordinate);
        switch (wallType){
            default -> wallTexture= new TextureRegion(tileSheet,7*UNIT,4*UNIT,UNIT,UNIT);
            case 1-> wallTexture= new TextureRegion(tileSheet,4*UNIT,0,UNIT,UNIT);
            case 2-> wallTexture= new TextureRegion(tileSheet,7*UNIT,2*UNIT,UNIT,UNIT);
            case 3-> wallTexture= new TextureRegion(tileSheet,4*UNIT,7*UNIT,UNIT,UNIT);
            case 4-> wallTexture= new TextureRegion(tileSheet,4*UNIT,9*UNIT,UNIT,UNIT);
        }
        super.setTexture(wallTexture);
    }
}
