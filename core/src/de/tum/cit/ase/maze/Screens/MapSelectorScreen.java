package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;
import games.spooky.gdx.nativefilechooser.NativeFileChooserIntent;

/**
 * The Map Selector Screen class is responsible for displaying the map selecting screen of the game and allowing the user to choose their map.
 * It extends the LibGDX Screen class and sets up the UI components.
 */
public class MapSelectorScreen implements Screen {

    private final Stage stage;
    private String filePath;
    private final SpriteBatch spriteBatch;
    private Texture texture;
    private NativeFileChooser fileChooser;
    /**
     * Constructor for Map Selector Screen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MapSelectorScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view
        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements
        texture=new Texture(Gdx.files.internal("Menu-Bg.jpg"));
        spriteBatch=new SpriteBatch();
        fileChooser= game.getFileChooser();
        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage
        // Add a label as a title
        table.add(new Label("Which Path do you choose?", game.getSkin(), "title")).padBottom(50).row();

        // Button for player to add custom map
        TextButton customMapButton = new TextButton("I have my own", game.getSkin());
        table.add(customMapButton).width(600).padBottom(20).row();
        customMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                chooseFile();
                game.setFilePath(filePath);
                game.setGameType(9);
                game.goToGame();
            }
        });

        // Buttons for player to play pre-set maps

        TextButton mapButton1 = new TextButton("The Brick House", game.getSkin());
        table.add(mapButton1).width(450).padBottom(10).row();
        mapButton1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setFilePath("maps/level-1.properties");
                game.setGameType(1);
                game.goToGame();
            }
        });

        TextButton mapButton2 = new TextButton("The Caverns", game.getSkin());
        table.add(mapButton2).width(450).padBottom(10).row();
        mapButton2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setFilePath("maps/level-2.properties");
                game.setGameType(2);
                dispose();
                game.goToGame();
            }
        });

        TextButton mapButton3 = new TextButton("The Haunted House", game.getSkin());
        table.add(mapButton3).width(450).padBottom(10).row();
        mapButton3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setFilePath("maps/level-3.properties");
                game.setGameType(3);
                dispose();
                game.goToGame();
            }
        });
        TextButton mapButton4 = new TextButton("The Grasslands", game.getSkin());
        table.add(mapButton4).width(450).padBottom(10).row();
        mapButton4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setFilePath("maps/level-4.properties");
                game.setGameType(4);
                dispose();
                game.goToGame();
            }
        });
        TextButton mapButton5 = new TextButton("The Ancient Tomb", game.getSkin());
        table.add(mapButton5).width(450).padBottom(10).row();
        mapButton5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setFilePath("maps/level-5.properties");
                game.setGameType(5);
                dispose();
                game.goToGame();
            }
        });

        //Direct Victory and Death Screen Test Buttons

//        TextButton winButton = new TextButton("I wanna Win", game.getSkin());
//        table.add(winButton).width(450).padBottom(10).row();
//        winButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                dispose();
//                game.goToVictory();
//            }
//        });
//        TextButton dieButton = new TextButton("I wanna Die", game.getSkin());
//        table.add(dieButton).width(450).padBottom(10).row();
//        dieButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                dispose();
//                game.goToDeath();
//            }
//        });

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        spriteBatch.begin();

        //Draw the background art
        spriteBatch.draw(texture, -600,-600, 3*Gdx.graphics.getWidth(), 3*Gdx.graphics.getHeight());

        spriteBatch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage

        stage.draw(); // Draw the stage

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    /**
     * Allows user to import and set their own .properties file as a map
     *
     */
    public void chooseFile() {
        var fileChooserConfig = new NativeFileChooserConfiguration();
        fileChooserConfig.title = "Pick a maze file"; // Title of the window that will be opened
        fileChooserConfig.intent = NativeFileChooserIntent.OPEN; // We want to open a file
        fileChooserConfig.nameFilter = (file, name) -> name.endsWith("properties"); // Only accept .properties files
        fileChooserConfig.directory = Gdx.files.absolute(System.getProperty("user.home")); // Open at the user's home directory
        fileChooser.chooseFile(fileChooserConfig, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle file) {
                filePath=file.path();
            }

            @Override
            public void onCancellation() {
                // User closed the window, don't need to do anything
            }

            @Override
            public void onError(Exception exception) {
                System.err.println("Error picking maze file: " + exception.getMessage());
            }
        });
    }

}
