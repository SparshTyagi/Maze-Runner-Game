package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Trap extends Entity {
    public static final int UNIT = 16; //Unit of space in game
    public Trap(float xCoordinate, float yCoordinate) {
        super(xCoordinate,yCoordinate);
        super.setTexture(new TextureRegion(new Texture(Gdx.files.internal("basictiles.png")), 2*UNIT,9*UNIT, UNIT, UNIT));
    }
}
