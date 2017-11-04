package com.mygdx.game.libgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.Actors.HUD;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;
import com.mygdx.game.libgdx.Weapons.LaserExplosion;

/**
 * Created by YAN on 12/02/2017.
 */

public class NewGameScreen implements Screen {

    final MyGdxGame game;

    private static int VIRTUAL_WIDTH = 50;
    private static int VIRTUAL_HEIGHT = 50;


    Stage stage;
    InputMultiplexer inputMultiplexer;


    private OrthographicCamera camera;
    private Viewport gameViewport;


    HUD hud;

    LaserExplosion explosion;


    public NewGameScreen(MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        camera = new OrthographicCamera();
        gameViewport = new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT,camera);
        inputMultiplexer = new InputMultiplexer();

        hud = new HUD(game.batch, game);

        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth() , Gdx.graphics.getHeight(), "purple"));

        inputMultiplexer.addProcessor(hud.stage);
        inputMultiplexer.addProcessor(stage);


        explosion = new LaserExplosion(-20, -20);



    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        explosion.update(delta);
        explosion.render(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
