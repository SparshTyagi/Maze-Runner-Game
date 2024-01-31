package de.tum.cit.ase.maze.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.ase.maze.Enums.Direction;
import de.tum.cit.ase.maze.Objects.*;
import de.tum.cit.ase.maze.Objects.Entity;
import com.badlogic.gdx.math.Rectangle;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
//BONUS FEATURES : Mobs follow player when close, life based speed, In-game Timer, combat system, points, point animations, more types of opponents and designs in preloaded maps
public class GameScreen implements Screen {
    public static final int UNIT = 16; //Unit of space in game

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private Player player;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch HUDBatch;
    private List<Entity> entityList;
    private Floor floor;
    private Direction direction;
    private Texture objectTextures;
    private TextureRegion healthIcon,keyIcon;
    //Runtime of the game
    private float runTime = 0f;
    //Dynamic character speed, which increases every time you take damage
    private float characterSpeed=2f;
    private boolean paused;
    private int[][] gameArray;
    private int gameType;
    private float cooldownTime = 1f;
    private boolean onCooldown = false;
    private float timer;
    private int finalScore;

//    private DecimalFormat decimalFormat = new DecimalFormat("##.##");

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        try {
            game.loadMap(game.getFilePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        shapeRenderer=new ShapeRenderer();
        HUDBatch=new SpriteBatch();
        objectTextures = new Texture(Gdx.files.internal("objects.png"));
        healthIcon =new TextureRegion(objectTextures,4*UNIT,0,UNIT,UNIT);
        keyIcon =new TextureRegion(objectTextures,0,4*UNIT,UNIT,UNIT);

        gameType=game.getGameType();
        gameArray=game.getMapArray();

        //load new instances of every type of game object
        entityList=new ArrayList<>();
        floor=new Floor(gameType);

        direction=Direction.NONE;

//        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.33f;
        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        //load entities from array
        loadEntities();

        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Game-Music.mp3"));
        game.setMusic(backgroundMusic);
    }

    // Screen interface methods with necessary functionality

    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pause();
            game.goToMenu();
        }
        if (!paused) {
            timer= 99-runTime;
            if (Float.valueOf(timer)<=0){
                game.goToDeath();
            }

            ScreenUtils.clear(0, 0, 0, 255); // Clear the screen
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

            //Update the runtime
            runTime += delta;
            // Update the camera
            camera.position.set(player.getxCoordinate(), player.getyCoordinate() + UNIT * 2, 0);
            camera.update();
            game.getSpriteBatch().setProjectionMatrix(camera.combined);

            game.getSpriteBatch().begin();

            //Draw Floor Tiles everywhere
            for (int i = 0; i < gameArray.length; i++) {
                for (int j = 0; j < gameArray[0].length; j++) {
                    game.getSpriteBatch().draw(floor.getFloorTexture(), UNIT * i, UNIT * j, UNIT, UNIT);
                }
            }
            //Draw entities
            renderEntities();

            // Player movement
            playerMovement(runTime);

            //Collision
            playerCollisionCheck();
            handleAttacking();

            //Mob Movement
            mobMovement(delta);


            game.getSpriteBatch().end(); // Important to call this after drawing everything

            // Render the HUD as an overlay
            renderHUD();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
        paused=true;
    }

    @Override
    public void resume() {
        paused=false;
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    /**
     * Load all the entities from our gameArray and store them in a list
     */
    public void loadEntities(){
        for (int i = 0; i < gameArray.length; i++) {
            for (int j = 0; j < gameArray[0].length; j++) {
                switch (gameArray[i][j]) {
                    case 0 -> entityList.add(new Wall(gameType,UNIT*i,UNIT*j));
                    case 1 -> {
                        entityList.add(new Entrance(UNIT*i,UNIT*j));
                        player = new Player(UNIT * i, UNIT * j);
                    }
                    case 2 -> {
                        entityList.add(new Exit(UNIT * i, UNIT * j));
                    }
                    case 3 -> entityList.add(new Trap(UNIT*i,UNIT*j));
                    case 4 -> entityList.add(new Mob(gameType,UNIT*i,UNIT*j));
                    case 5 -> entityList.add(new Key(UNIT*i,UNIT*j));
                }
            }
        }
    }

    /**
     * Render entities from our entity list
     */
    public void renderEntities(){

        for (Entity entity:entityList) {
            switch (entity.getClass().getSimpleName()) {
                default -> game.getSpriteBatch().draw(entity.getTexture(), entity.getxCoordinate(), entity.getyCoordinate(), UNIT, UNIT);
                case "Mob" -> {
                    game.getSpriteBatch().draw(
                            ((Mob) entity).getmobWalkAnimation(((Mob) entity).getCurrentDirection()).getKeyFrame(runTime, true),
                            entity.getxCoordinate(), entity.getyCoordinate(), UNIT, UNIT);

                }
                case "Key" -> game.getSpriteBatch().draw(
                        entity.getAnimation().getKeyFrame(runTime, true),
                        entity.getxCoordinate(), entity.getyCoordinate(), UNIT, UNIT);
            }
        }
    }

    /**
     * Generates the player and entity hit boxes and checks if there is any overlap/collisions, and if so implements the effect of the collision
     * @return true if a collision occurred
     */
    public boolean playerCollisionCheck(){
        Rectangle playerHitbox = new Rectangle((int)player.getxCoordinate(),(int)player.getyCoordinate(),UNIT-4,UNIT-4);
        for(Entity entity:entityList){
            Rectangle entityHitbox = new Rectangle((int)entity.getxCoordinate(),(int)entity.getyCoordinate(),UNIT-4,UNIT-4);
            if(entityHitbox.overlaps(playerHitbox)){
                switch (entity.getClass().getSimpleName()){
                    case "Wall" ->{
                        player.setxCoordinate(player.getPrevXPosition());
                        player.setyCoordinate(player.getPrevYPosition());
                        return true;
                    }
                    case "Key"->{
                        player.setKeyCollected(true);
                        player.setPoints(player.getPoints()+100);
                        Music music= Gdx.audio.newMusic(Gdx.files.internal("Key-Sound.wav"));
                        game.getCurrentMusic().pause();
                        music.play();
                            game.getSpriteBatch().draw(
                                    new TextureRegion(objectTextures, 5 * UNIT, 14 * UNIT, UNIT, UNIT),
                                    player.getxCoordinate() + UNIT,
                                    player.getyCoordinate() + UNIT,
                                    UNIT,
                                    UNIT
                            );
                            game.getSpriteBatch().draw(
                                    new TextureRegion(objectTextures, 6 * UNIT, 16 * UNIT, UNIT, UNIT),
                                    player.getxCoordinate() + 2 * UNIT,
                                    player.getyCoordinate() + UNIT,
                                    UNIT,
                                    UNIT
                            );
                            game.getSpriteBatch().draw(
                                    new TextureRegion(objectTextures, 6 * UNIT, 16 * UNIT, UNIT, UNIT),
                                    player.getxCoordinate() + 3 * UNIT,
                                    player.getyCoordinate() + UNIT,
                                    UNIT,
                                    UNIT
                            );
                        music.setOnCompletionListener(listener -> game.getCurrentMusic().play());
                        entityList.remove(entity);
                        return true;
                    }
                    case "Exit"->{
                        if (player.isKeyCollected()){
                            game.goToVictory();
                        } else{
                            player.setxCoordinate(player.getPrevXPosition());
                            player.setyCoordinate(player.getPrevYPosition());
                            return true;
                        }
                    }
                    case "Trap"->{
                        damageTaken();
                    }
                    case "Mob"->{
                        damageTaken();
                    }
                }
            }
        }
        return false;
    }

    /**
     * Handles mob Collision
     * @param mob A given Mob Entity
     * @return true if there was a collision
     */
    public boolean mobCollisionCheck(Mob mob){
        Rectangle mobHitbox = new Rectangle((int)mob.getxCoordinate(),(int)mob.getyCoordinate(),UNIT-4,UNIT-4);
        for(Entity entity:entityList){
            if (!entity.getClass().getSimpleName().equals("Mob")){
                Rectangle entityHitbox = new Rectangle((int)entity.getxCoordinate(),(int)entity.getyCoordinate(),UNIT-4,UNIT-4);
                if(mobHitbox.overlaps(entityHitbox)){
                    return true;
                }
            }
            if ((int)entity.getxCoordinate()>(gameArray[0].length-1)*UNIT||(int)entity.getyCoordinate()>(gameArray.length-1)*UNIT||(int)entity.getxCoordinate()<0||(int)entity.getyCoordinate()<0){
                return true;
            }
        }
        return false;
    }
    /**
     * Handles mob Movement
     * @param delta the time period between rendering frames
     *
     */
    public void mobMovement(float delta){
        for(Entity mob:entityList){
            if (mob.getClass().getSimpleName().equals("Mob")){
                if(!mobCollisionCheck((Mob)mob)) {
                    ((Mob) mob).setPrevXPosition(((Mob) mob).getxCoordinate());
                    ((Mob) mob).setPrevYPosition(((Mob) mob).getyCoordinate());

                     if (mob.getxCoordinate()<player.getxCoordinate()+2*UNIT && mob.getxCoordinate()>player.getxCoordinate()-2*UNIT &&mob.getyCoordinate()<player.getyCoordinate()+2*UNIT && mob.getyCoordinate()>player.getyCoordinate()-2*UNIT ) {
                         if ((int) mob.getxCoordinate() > (int) player.getxCoordinate()) {
                             ((Mob) mob).setCurrentDirection(Direction.LEFT);
                         } else if ((int) mob.getxCoordinate() < (int) player.getxCoordinate()) {
                             ((Mob) mob).setCurrentDirection(Direction.RIGHT);
                         } else if ((int) mob.getyCoordinate() > (int) player.getyCoordinate()) {
                             ((Mob) mob).setCurrentDirection(Direction.DOWN);
                         } else if ((int) mob.getyCoordinate() < (int) player.getyCoordinate()) {
                             ((Mob) mob).setCurrentDirection(Direction.UP);
                         }
                     }
//                     else {
//                         ((Mob) mob).setCurrentDirection(Direction.NONE);
//                     }
                    switch (((Mob) mob).getCurrentDirection()){
                        case LEFT ->(mob).setxCoordinate(mob.getxCoordinate() - 1f);
                        case RIGHT ->(mob).setxCoordinate(mob.getxCoordinate() + 1f);
                        case DOWN ->(mob).setyCoordinate(mob.getyCoordinate() - 1f);
                        case UP ->(mob).setyCoordinate(mob.getyCoordinate() + 1f);
                    }

                } else {
                    ((Mob) mob).setxCoordinate(((Mob) mob).getPrevXPosition());
                    ((Mob) mob).setyCoordinate(((Mob) mob).getPrevYPosition());
                    ((Mob) mob).mobRotate();
                }
            }
        }
    }


    /**
     * Character Damage function. Reduces lives, increases speed, and plays damage sound and animation
     */
    public void damageTaken(){
        if(!onCooldown) {
            onCooldown=true;
            player.setLives(player.getLives() - 1);
            if (player.getLives() <= 0) {
                game.goToDeath();
            }
            characterSpeed+=0.33;
            Music music = Gdx.audio.newMusic(Gdx.files.internal("Hurt-Sound.wav"));
            game.getCurrentMusic().setVolume(0.1f);
            music.play();
            music.setVolume(3f);
            music.setOnCompletionListener(listener -> {
                game.getCurrentMusic().setVolume(1f);
                onCooldown=false;
            });
        }
    }


    /**
     * Player movement function. Handles all possible player movement inputs and animations
     * @param runTime Total runtime of the game
     */
    public void playerMovement(float runTime){
        player.setPrevXPosition(player.getxCoordinate());
        player.setPrevYPosition(player.getyCoordinate());
        if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)||Gdx.input.isKeyPressed(Input.Keys.S))&&player.getyCoordinate()>=0) {
            player.setyCoordinate(player.getyCoordinate()-characterSpeed);
            direction = Direction.DOWN;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)||Gdx.input.isKeyPressed(Input.Keys.W))&&player.getyCoordinate()<=(gameArray.length-1)*UNIT) {
            player.setyCoordinate(player.getyCoordinate()+characterSpeed);
            direction = Direction.UP;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)||Gdx.input.isKeyPressed(Input.Keys.A))&&player.getxCoordinate()>=0) {
            player.setxCoordinate(player.getxCoordinate()-characterSpeed);
            direction = Direction.LEFT;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D))&&player.getxCoordinate()<=(gameArray[0].length-1)*UNIT) {
            player.setxCoordinate(player.getxCoordinate()+characterSpeed);
            direction = Direction.RIGHT;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.setFighting(true);
            game.getSpriteBatch().draw(
                    player.getCharacterFightAnimation().getKeyFrame(runTime, true),
                    player.getxCoordinate()-8,
                    player.getyCoordinate(),
                    UNIT*2,
                    UNIT*2
            );
        } else if (onCooldown) {
            player.setFighting(false);
            game.getSpriteBatch().draw(
                        player.getCharacterDamageAnimation().getKeyFrame(runTime, true),
                        player.getxCoordinate(),
                        player.getyCoordinate(),
                        UNIT,
                        UNIT*2
                );

        } else {
            player.setFighting(false);
            game.getSpriteBatch().draw(
                    player.getCharacterWalkAnimation(direction).getKeyFrame(runTime, true),
                    player.getxCoordinate(),
                    player.getyCoordinate(),
                    UNIT,
                    UNIT*2
            );
        }
        direction=Direction.NONE;
    }

    /**
     * Handles attacking and mob death
     * @return true if a mob was killed
     */
    public boolean handleAttacking(){
        Music music = Gdx.audio.newMusic(Gdx.files.internal("mobDeath.wav"));
        if (player.isFighting()) {
            Rectangle attackingHitbox = null;
            switch (player.getCurrentDirection()) {
                case DOWN -> {
                    attackingHitbox = new Rectangle((int) player.getxCoordinate(), (int) player.getyCoordinate() - 6, UNIT - 8, UNIT - 8);
                }
                case UP -> {
                    attackingHitbox = new Rectangle((int) player.getxCoordinate(), (int) player.getyCoordinate() + 8, UNIT - 8, UNIT - 8);
                }
                case LEFT -> {
                    attackingHitbox = new Rectangle((int) player.getxCoordinate() - 8, (int) player.getyCoordinate()+4, UNIT - 8, UNIT - 8);
                }
                case RIGHT -> {
                    attackingHitbox = new Rectangle((int) player.getxCoordinate() + 8, (int) player.getyCoordinate()+4, UNIT - 8, UNIT - 8);
                }
            }
            for (Entity entity : entityList) {
                if (entity.getClass().getSimpleName().equals("Mob")) {
                    Rectangle entityHitbox = new Rectangle((int) entity.getxCoordinate(), (int) entity.getyCoordinate(), UNIT - 4, UNIT - 4);
                    if (entityHitbox.overlaps(attackingHitbox)) {
                        entityList.remove(entity);
                        player.setPoints(player.getPoints()+50);
                        game.getCurrentMusic().setVolume(0.5f);
                        music.play();
                        music.setVolume(1f);
                            game.getSpriteBatch().draw(
                                    new TextureRegion(objectTextures, 5 * UNIT, 15 * UNIT, UNIT, UNIT),
                                    player.getxCoordinate() + UNIT,
                                    player.getyCoordinate() + UNIT,
                                    UNIT,
                                    UNIT
                            );
                            game.getSpriteBatch().draw(
                                    new TextureRegion(objectTextures, 6 * UNIT, 16 * UNIT, UNIT, UNIT),
                                    player.getxCoordinate() + 2*UNIT,
                                    player.getyCoordinate() + UNIT,
                                    UNIT,
                                    UNIT
                            );
                        music.setOnCompletionListener(listener -> {
                            game.getCurrentMusic().setVolume(1f);
                        });
                        return true;

                    }
                }
            }
        }
        return false;
    }

    /**
     * Renders the HUD for the game, showing time remaining, lives, points, and if the key was collected
     */
    public void renderHUD(){

        //Render the HUD
        HUDBatch.begin();

        //Hud Titles
        font.draw(HUDBatch, "Time remaining", 20, font.getRegion().getRegionY() * 2);
        font.draw(HUDBatch, "Lives Remaining :", (int) (font.getRegion().getRegionX() / 1.5), font.getRegion().getRegionY() * 2);
        font.draw(HUDBatch, "Keys Collected :", (int) (font.getRegion().getRegionX()*1.3), font.getRegion().getRegionY() * 2);
        font.draw(HUDBatch, "Points", (int) (font.getRegion().getRegionX()*2), font.getRegion().getRegionY() * 2);

        //Hud Values
        font.draw(HUDBatch, String.valueOf(timer), 100, (int) (font.getRegion().getRegionY() * 1.9));
        for(int i=0;i<player.getLives();i++){
            HUDBatch.draw(healthIcon,(int) (font.getRegion().getRegionX() / 1.5) + UNIT*i*4, (int) (font.getRegion().getRegionY() * 1.8),(int) (UNIT*3),(int) (UNIT*3));
        }
        if(player.isKeyCollected()) {
            HUDBatch.draw(keyIcon, (int) (font.getRegion().getRegionX()*1.3), (int) (font.getRegion().getRegionY() * 1.8), UNIT * 3, UNIT * 3);
        } else {
            font.draw(HUDBatch, "" + (player.isKeyCollected() ? "Yes" : "None Yet"), (int) (font.getRegion().getRegionX()*1.3), (int) (font.getRegion().getRegionY() * 1.9));
        }
        font.draw(HUDBatch, String.valueOf(player.getPoints()), (int) (font.getRegion().getRegionX()*2), (int) (font.getRegion().getRegionY() * 1.9));

        HUDBatch.end();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getFinalScore() {
        finalScore= player.getPoints() + (int) (10*Float.valueOf(timer)) + player.getLives()*200;
        return finalScore;
    }
}
