package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

/**
 * The Victory Screen class is responsible for displaying the Win Screen of the game.
 * It extends the LibGDX Screen class and sets up the UI components.
 */
public class VictoryScreen implements Screen {

    private final Stage stage;
    private Texture texture;
    private SpriteBatch spriteBatch;
    /**
     * Constructor for Victory Screen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public VictoryScreen(MazeRunnerGame game) {
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Victory-Music.mp3"));
        game.setMusic(backgroundMusic);

        texture=new Texture(Gdx.files.internal("Victory-Bg.jpg"));
        spriteBatch=new SpriteBatch();

        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view
        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements
        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage
        // Add a label as a title
        table.add(new Label("Score: " + game.getGameScreen().getFinalScore(), game.getSkin(), "title")).padBottom(50).row();
        table.add(new Label("Congratulations Brave Warrior", game.getSkin(), "title")).padBottom(50).row();
        table.add(new Label("YOU WIN", game.getSkin(), "title")).padBottom(50).row();

        TextButton exitButton = new TextButton("Return to Menu", game.getSkin());
        table.add(exitButton).width(450).padBottom(10).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.goToMenu();
            }
        });

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        spriteBatch.begin();

        //Draw the background art
        spriteBatch.draw(texture, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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

}
