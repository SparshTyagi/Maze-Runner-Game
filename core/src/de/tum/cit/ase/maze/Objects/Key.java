package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
public class Key extends Entity {
    public static final int UNIT = 16; //Unit of space in game

    private Animation<TextureRegion> keyAnimation;
    public Key(float xCoordinate, float yCoordinate) {
        super(xCoordinate,yCoordinate);
        Texture walkSheet = new Texture(Gdx.files.internal("objects.png"));
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> keyFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animations
        for (int col = 0; col < animationFrames; col++) {
            keyFrames.add(new TextureRegion(walkSheet, col*UNIT, 4*UNIT, UNIT, UNIT));
        }
        keyAnimation = new Animation<>(0.1f, keyFrames);
        super.setAnimation(keyAnimation);
    }

}
