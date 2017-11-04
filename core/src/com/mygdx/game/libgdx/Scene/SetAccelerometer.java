package com.mygdx.game.libgdx.Scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;
import com.mygdx.game.libgdx.Screens.MyGdxGame;
import com.mygdx.game.libgdx.Screens.OptionScreen;


/**
 * Created by YAN on 27/02/2017.
 */

public class SetAccelerometer implements Screen{
    final MyGdxGame game;
    Stage stage;
    Viewport viewport;
    Sprite sprite;
    Table table;
    Button buttonAccept;
    Button backButton;
    Skin skin;
    Texture texture;
    Image image;
    Label instructions;


    public SetAccelerometer(MyGdxGame game) {
        this.game = game;
        skin = game.skin;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport);
        texture = new Texture(Gdx.files.internal("data/ships/playerspaceShips1.png"));
        image = new Image(texture);

        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        instructions = new Label("CHOOSE THE BEST ANGLE FOR YOU TO PLAY", new Label.LabelStyle(game.font, Color.WHITE));


        table = new Table();
        table.setFillParent(true);
        addButton();
        table.setDebug(MyGdxGame.DEBUG);
        table.add(instructions).expandY().padLeft(Gdx.graphics.getWidth() * 0.08f);
        table.row();
        table.add(image).center().expandY().padLeft(Gdx.graphics.getWidth() * 0.05f);
        table.row();
        table.add(backButton).bottom().left().pad(10).expandX().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).height(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).expandY().maxHeight(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE);
        table.add(buttonAccept).bottom().right().pad(10).expandX().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).height(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).expandY().maxHeight(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.batch.begin();
        stage.act();
        stage.draw();
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        texture.dispose();
    }

    public void addButton(){
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skin.getDrawable("icon_check");
        buttonStyle.down = skin.newDrawable("icon_check", Color.LIGHT_GRAY);

        buttonAccept = new Button(buttonStyle);
        buttonAccept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.playClick();
                if (game.playerSave != null){
                    game.playerSave.accX = Gdx.input.getAccelerometerX();
                    game.savePlayerFile();
                }
                game.setScreen(new OptionScreen(game));
            }
        });

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.newDrawable("icon_back");
        style.down = skin.newDrawable("icon_back");

        backButton = new Button(style);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionScreen(game));
            }
        });

    }



}
