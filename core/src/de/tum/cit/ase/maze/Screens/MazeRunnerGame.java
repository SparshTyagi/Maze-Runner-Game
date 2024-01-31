package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.tum.cit.ase.maze.Objects.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private MapSelectorScreen mapScreen;
    // Sprite Batch for rendering
    private VictoryScreen victoryScreen;
    private DeathScreen deathScreen;
    private SpriteBatch spriteBatch;
    // UI Skin
    private Skin skin;
    private String filePath;
    private NativeFileChooser fileChooser;
    private Properties mapProperties;
    private int[][] mapArray;
    private int gameType;
    private Music currentMusic;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser=fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        mapProperties=new Properties();
        filePath= "maps/level-1.properties";
        gameType=0;
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        //Set the map
        currentMusic=Gdx.audio.newMusic(Gdx.files.internal("MenuBgTrack.mp3"));
        currentMusic.play();
        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen, disposing of the victory/death screen if they exist.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (victoryScreen != null) {
            victoryScreen.dispose(); // Dispose the previous map screen if it exists
            victoryScreen = null;
        }
        if (deathScreen != null) {
            deathScreen.dispose(); // Dispose the game screen if it exists
            deathScreen = null;
        }
    }

    /**
     * Switches to the game screen (creating one if there's none present) and disposing of the menu screen.
     */
    public void goToGame() {
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
        if (gameScreen!=null){
            setMusic(Gdx.audio.newMusic(Gdx.files.internal("Game-Music.mp3")));
            gameScreen.setPaused(false);
            this.setScreen(gameScreen);
        } else {
            gameScreen = new GameScreen(this);
            this.setScreen(gameScreen); // Set the current screen to GameScreen
        }
    }
    /**
     * Switches to the map selector screen while disposing of redundant screens.
     */
    public void goToMaps() {
        this.setScreen(new MapSelectorScreen(this)); // Set the current screen to MenuScreen
        if (mapScreen != null) {
            mapScreen.dispose(); // Dispose the previous map screen if it exists
            mapScreen = null;
        }
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the Victory screen.
     */
    public void goToVictory() {
        this.setScreen(new VictoryScreen(this)); // Set the current screen to GameScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the menu screen if it exists
            gameScreen = null;
        }
    }
    /**
     * Switches to the Death screen.
     */
    public void goToDeath() {
        this.setScreen(new DeathScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    /**
     * Loads the map at the present filepath
     * @param filePath
     * @throws IOException
     */
    public void loadMap(String filePath) throws IOException {
        mapProperties.clear();
        FileInputStream fileInputStream= new FileInputStream(filePath);
        mapProperties.load(fileInputStream);
        System.out.println("File Loaded :"+ filePath);
        int[][] mapArray=new int[99][99];
        for (int i=0;i<99;i++){
            for (int j=0;j<99;j++){
                mapArray[i][j]=-1;
            }
        }
        int maxX=0;
        int maxY=0;
        for(var key:mapProperties.keySet()){
            String[] coords= ((String) key).split(",");
            int x=Integer.parseInt(coords[0]);
            int y=Integer.parseInt(coords[1]);
            if(x>=maxX) {
                maxX = x;
            }
            if(y>=maxY) {
                maxY = y;
            }
            mapArray[x][y]=Integer.parseInt(mapProperties.getProperty((String) key));
        }
        int[][] optimizedMapArray = new int[maxY+1][maxX+1];
        for (int i=0;i<=maxY;i++){
            for (int j=0;j<=maxX;j++){
                optimizedMapArray[i][j]=mapArray[i][j];
//                System.out.print(optimizedMapArray[i][j] + " ");
            }
//            System.out.println();
        }
        this.mapArray = optimizedMapArray;
    }

    /**
     * Sets the music to the desired track, stopping the prior music
     * @param music The desired music track
     */
    public void setMusic(Music music){
        currentMusic.stop();
        currentMusic=music;
        currentMusic.setLooping(true);
        currentMusic.play();
    }

    /**
     * Getters and Setters
     */
    public Music getCurrentMusic() {
        return currentMusic;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public Skin getSkin() {
        return skin;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int[][] getMapArray() {
        return mapArray;
    }

    public NativeFileChooser getFileChooser() {
        return fileChooser;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

}
