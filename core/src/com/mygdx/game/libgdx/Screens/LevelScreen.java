package com.mygdx.game.libgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;


/**
 * Created by YAN on 23/10/2017.
 */

//TODO implement lvl screen

public class LevelScreen implements Screen {

    final MyGdxGame game;
    private Viewport viewport;
    OrthographicCamera camera;
    Stage stage;
    Table table;
    Table finalTable;
    ScrollPane scrollPane;
    Skin skin;
    Skin skinButton;
    Label stageLabel;

    public LevelScreen(final MyGdxGame game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        viewport = new FitViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        stage = new Stage();
        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        skinButton = game.skin;


        createTable();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.begin();

        game.batch.end();
        //table.clearChildren();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        skin.dispose();
        skinButton.dispose();
    }

    void createTable(){
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skinButton.getDrawable(MyGdxGame.BUTTON_SHAPE);
        textButtonStyle.up = skinButton.newDrawable(MyGdxGame.BUTTON_SHAPE, Color.LIGHT_GRAY);
        textButtonStyle.font = game.font;

        stageLabel = new Label("STAGES", new Label.LabelStyle(game.font, Color.WHITE));

        finalTable = new Table();
        finalTable.setFillParent(true);

        finalTable.add(stageLabel).height(Gdx.graphics.getHeight() * 0.2f).center().expandX();
        finalTable.row();

        skin = new Skin(Gdx.files.internal("data/ui/level-plane-ui.json"));
        table = new Table(skin);
//create the table
        table.setDebug(MyGdxGame.DEBUG);
        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight() * 0.8f);
        table.setOrigin(0, 0);
        table.setPosition(0, 0);


        for (int i = 0; i < 120; i++){
            TextButton textButton = new TextButton(Integer.toString(i + 1), skinButton);
            for(int j = i; j < 3; j++){
                table.add(textButton).height(Gdx.graphics.getHeight() * 0.25f).expandX();
            }
            table.row();

        }

        scrollPane = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(table);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth()* 0.5f, Gdx.graphics.getHeight() * 0.5f);
        scrollPane.setPosition(0, Gdx.graphics.getHeight() * 0.5f);
        scrollPane.setTransform(true);
        scrollPane.setSmoothScrolling(true);

        finalTable.add(scrollPane).height(Gdx.graphics.getHeight()* 0.8f).expand();
        stage.addActor(finalTable);


    }




}
