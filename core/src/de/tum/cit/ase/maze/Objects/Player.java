package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Enums.*;

public class Player extends Entity {
    //preparing player movement animations
    private Animation<TextureRegion> characterDownAnimation,characterUpAnimation,characterLeftAnimation,characterRightAnimation;
    private Animation<TextureRegion> characterDownAttackAnimation,characterUpAttackAnimation,characterLeftAttackAnimation,characterRightAttackAnimation;
    private Animation<TextureRegion> characterDownDamageAnimation,characterUpDamageAnimation,characterLeftDamageAnimation,characterRightDamageAnimation;
    private float prevXPosition;
    private float prevYPosition;
    private TextureRegion idlePlayer;
    private Direction currentDirection;
    private int lives,points;
    private boolean fighting;
    private boolean keyCollected;


    public Player(float xCoordinate, float yCoordinate) {
        super(xCoordinate, yCoordinate);
        lives=5;
        points=0;
        keyCollected=false;

        loadCharacterAnimation();
    }

    /**
     * Loads the character animation from the character.png file.
     */
    public void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> downWalkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> upWalkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> leftWalkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> rightWalkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> downAttackFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> upAttackFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> leftAttackFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> rightAttackFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> downDamageFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> upDamageFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> leftDamageFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> rightDamageFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animations
        for (int col = 0; col < animationFrames; col++) {
            downWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight)); rightWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, frameHeight, frameWidth, frameHeight)); upWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, frameHeight*2, frameWidth, frameHeight)); leftWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, frameHeight*3, frameWidth, frameHeight));
            downAttackFrames.add(new TextureRegion(walkSheet, col * frameWidth*2, frameHeight*4, frameWidth*2, frameHeight)); upAttackFrames.add(new TextureRegion(walkSheet, col * frameWidth*2, frameHeight*5, frameWidth*2, frameHeight)); rightAttackFrames.add(new TextureRegion(walkSheet, col * frameWidth*2, frameHeight*6, frameWidth*2, frameHeight)); leftAttackFrames.add(new TextureRegion(walkSheet, col * frameWidth*2, frameHeight*7, frameWidth*2, frameHeight));

        }
        for (int col = 0; col < animationFrames-1; col++) {
            downDamageFrames.add(new TextureRegion(walkSheet, (col+5) * frameWidth, 0, frameWidth, frameHeight)); rightDamageFrames.add(new TextureRegion(walkSheet, (col+5) * frameWidth, frameHeight, frameWidth, frameHeight)); upDamageFrames.add(new TextureRegion(walkSheet, (col+5) * frameWidth, frameHeight*2, frameWidth, frameHeight)); leftDamageFrames.add(new TextureRegion(walkSheet, (col+5) * frameWidth, frameHeight*3, frameWidth, frameHeight));

        }

            characterDownAnimation = new Animation<>(0.1f, downWalkFrames); characterRightAnimation = new Animation<>(0.1f, rightWalkFrames); characterUpAnimation = new Animation<>(0.1f, upWalkFrames); characterLeftAnimation = new Animation<>(0.1f, leftWalkFrames);
        characterDownAttackAnimation = new Animation<>(0.1f, downAttackFrames); characterRightAttackAnimation = new Animation<>(0.1f, rightAttackFrames); characterUpAttackAnimation = new Animation<>(0.1f, upAttackFrames); characterLeftAttackAnimation = new Animation<>(0.1f, leftAttackFrames);
        characterDownDamageAnimation = new Animation<>(0.1f, downDamageFrames); characterRightDamageAnimation = new Animation<>(0.1f, rightDamageFrames); characterUpDamageAnimation = new Animation<>(0.1f, upDamageFrames); characterLeftDamageAnimation = new Animation<>(0.1f, leftDamageFrames);

        currentDirection= Direction.DOWN;
        idlePlayer = characterDownAnimation.getKeyFrame(0);

    }

    public Animation<TextureRegion> getCharacterWalkAnimation(Direction direction) {
        switch (direction) {
            case DOWN -> {
                idlePlayer =characterDownAnimation.getKeyFrame(0);
                currentDirection = Direction.DOWN;
                return characterDownAnimation;
            }
            case UP -> {
                idlePlayer =characterUpAnimation.getKeyFrame(0);
                currentDirection = Direction.UP;
                return characterUpAnimation;
            }
            case LEFT -> {
                idlePlayer =characterLeftAnimation.getKeyFrame(0);
                currentDirection = Direction.LEFT;
                return characterLeftAnimation;
            }
            case RIGHT -> {
                idlePlayer =characterRightAnimation.getKeyFrame(0);
                currentDirection = Direction.RIGHT;

                return characterRightAnimation;
            }
        }
        return new Animation<>(0.1f, idlePlayer);
    }
    public Animation<TextureRegion> getCharacterFightAnimation() {
        Direction direction=currentDirection;
        switch (direction) {
            case DOWN -> {
                return characterDownAttackAnimation;
            }
            case UP -> {
                return characterUpAttackAnimation;
            }
            case LEFT -> {
                return characterLeftAttackAnimation;
            }
            case RIGHT -> {
                return characterRightAttackAnimation;
            }
        }
        return new Animation<>(0.1f, idlePlayer);
    }
    public Animation<TextureRegion> getCharacterDamageAnimation() {
        Direction direction=currentDirection;
        switch (direction) {
            case DOWN -> {
                return characterDownDamageAnimation;
            }
            case UP -> {
                return characterUpDamageAnimation;
            }
            case LEFT -> {
                return characterLeftDamageAnimation;
            }
            case RIGHT -> {
                return characterRightDamageAnimation;
            }
        }
        return new Animation<>(0.1f, idlePlayer);
    }

    public float getPrevXPosition() {
        return prevXPosition;
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

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }


    public boolean isKeyCollected() {
        return keyCollected;
    }

    public void setKeyCollected(boolean keyCollected) {
        this.keyCollected = keyCollected;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }



    public boolean isFighting() {
        return fighting;
    }

    public void setFighting(boolean fighting) {
        this.fighting = fighting;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }
}
