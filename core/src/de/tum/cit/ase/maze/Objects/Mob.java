package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Enums.*;

public class Mob extends Entity{
    //preparing player movement animations
    private Animation<TextureRegion> mobDownAnimation,mobUpAnimation,mobLeftAnimation,mobRightAnimation;
    private TextureRegion idleMob;
    private Direction currentDirection;
    private float prevXPosition;
    private float prevYPosition;

    public Mob(int mobType,float xCoordinate, float yCoordinate) {
        super(xCoordinate,yCoordinate);
        currentDirection=Direction.DOWN;
        loadmobAnimation(mobType);
    }

    /**
     * Loads the mob animation from the mob.png file.
     */
    public void loadmobAnimation(int type) {
        Texture walkSheet = new Texture(Gdx.files.internal("mobs.png"));
        int sheetx,sheety;
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;

        switch (type) {
            default -> {//Doll
                sheetx = 0;
                sheety = 0;
            }
            case 1 -> {//Skeleton
                sheetx = 9 * frameWidth;
                sheety = 0;
            }
            case 2 -> {//Slime
                sheetx = 0;
                sheety = 4 * frameHeight;
            }
            case 3 -> { //Ghost
                sheetx = 6 * frameWidth;
                sheety = 4 * frameHeight;
            }
            case 4 -> { //Spider
                sheetx = 9 * frameWidth;
                sheety = 4 * frameHeight;
            }
        }


        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> downWalkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> upWalkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> leftWalkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> rightWalkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animations
        for (int col = 0; col < animationFrames; col++) {
            downWalkFrames.add(new TextureRegion(walkSheet, sheetx + col*frameWidth, sheety, frameWidth, frameHeight));
            leftWalkFrames.add(new TextureRegion(walkSheet, sheetx + col*frameWidth, sheety+frameHeight, frameWidth, frameHeight));
            rightWalkFrames.add(new TextureRegion(walkSheet, sheetx + col*frameWidth, sheety+frameHeight*2, frameWidth, frameHeight));
            upWalkFrames.add(new TextureRegion(walkSheet, sheetx + col*frameWidth, sheety+frameHeight*3, frameWidth, frameHeight));
        }

        mobDownAnimation = new Animation<>(0.1f, downWalkFrames);
        mobRightAnimation = new Animation<>(0.1f, rightWalkFrames);
        mobUpAnimation = new Animation<>(0.1f, upWalkFrames);
        mobLeftAnimation = new Animation<>(0.1f, leftWalkFrames);
        currentDirection= Direction.DOWN;
        idleMob = new TextureRegion(walkSheet, sheetx+frameWidth, sheety, frameWidth, frameHeight);

    }

    public Animation<TextureRegion> getmobWalkAnimation(Direction direction) {
        switch (direction) {
            case DOWN -> {
                idleMob =mobDownAnimation.getKeyFrame(0.1f);
                currentDirection = Direction.DOWN;
                return mobDownAnimation;
            }
            case UP -> {
                idleMob =mobUpAnimation.getKeyFrame(0.1f);
                currentDirection = Direction.UP;
                return mobUpAnimation;
            }
            case LEFT -> {
                idleMob =mobLeftAnimation.getKeyFrame(0.1f);
                currentDirection = Direction.LEFT;
                return mobLeftAnimation;
            }
            case RIGHT -> {
                idleMob =mobRightAnimation.getKeyFrame(0.1f);
                currentDirection = Direction.RIGHT;

                return mobRightAnimation;
            }
        }
        return new Animation<>(0.1f, idleMob);
    }

    public TextureRegion getIdleMob() {
        return idleMob;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public float getPrevXPosition() {
        return prevXPosition;
    }

    public void mobRotate(){
        switch (currentDirection) {
            case DOWN -> {
                currentDirection = Direction.LEFT;
            }
            case UP -> {
                currentDirection = Direction.RIGHT;
            }
            case LEFT -> {
                currentDirection = Direction.UP;
            }
            case RIGHT -> {
                currentDirection = Direction.DOWN;
            }
        }
    }

    public void setPrevXPosition(float prevXPosition) {
        this.prevXPosition = prevXPosition;
    }

    public float getPrevYPosition() {
        return prevYPosition;
    }

    public void setPrevYPosition(float prevYPosition) {
        this.prevYPosition = prevYPosition;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }
}
