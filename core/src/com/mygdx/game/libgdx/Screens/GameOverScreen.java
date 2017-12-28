package com.mygdx.game.libgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;

/**
 * Created by YAN on 21/02/2017.
 */

public class GameOverScreen implements Screen {




    float CAM_WIDHT = MyGdxGame.CAMERA_WIDHT;
    float CAM_HEIGHT = MyGdxGame.CAMERA_HEIGHT;

    float CELL_HEIGHT = (Gdx.graphics.getHeight() - 60) * 0.2f;
    final MyGdxGame game;
    int score;
    OrthographicCamera camera;
    Stage stage;
    //Buttons
    Table table;

    TextButton.TextButtonStyle buttonStyle;

    TextButton mainMenuButton;
    TextButton tryAgainButton;
    TextButton exitButton;
    TextButton viewScore;
    TextButton submitHighScore;
    Label scoreLabel;
    Label gameOver;
    Integer level;

    InputMultiplexer inputMultiplexer;

    public GameOverScreen(MyGdxGame game, int score, int level) {
        this.game = game;
        this.score = score;
        this.level = level;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAM_WIDHT, CAM_HEIGHT);


        createTable();
        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.addActor(table);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.4f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.begin();

        stage.act();
        stage.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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

    private void createTable(){

        gameOver = new Label("GAME OVER!", new Label.LabelStyle(game.title, Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(game.title, Color.WHITE));

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = game.skin.getDrawable(MyGdxGame.BUTTON_SHAPE);
        buttonStyle.down = game.skin.newDrawable(MyGdxGame.BUTTON_SHAPE, Color.LIGHT_GRAY);
        buttonStyle.font = game.font;

        mainMenuButton = new TextButton("MAIN MENU", buttonStyle);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                //game.saveToCloud();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        exitButton = new TextButton("EXIT GAME", buttonStyle);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playerSave.setPlayerPoints(game.playerSave.getPlayerPoints() + score);
                //game.savePlayerFile();
                //game.saveToCloud();
                game.playClick();
                game.dispose();
            }
        });

        tryAgainButton = new TextButton("TRY AGAIN", buttonStyle);
        tryAgainButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                game.setScreen(new GameScreen(game, level));
            }
        });

        viewScore = new TextButton("VIEW SCORE", buttonStyle);
        viewScore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                game.playServices.showScore();
            }
        });


        submitHighScore = new TextButton("SUBMIT SCORE", buttonStyle);
        submitHighScore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                    if (game.playServices.submitScore(score))
                        game.playServices.toast("DONE");
                    else
                        game.playServices.toast("FAILED");
            }
        });

        table = new Table(game.skin);
        table.center();
        table.setFillParent(true);
        table.add(gameOver).pad(Gdx.graphics.getHeight() * 0.1f).width(Gdx.graphics.getWidth() * 0.4f).height(Gdx.graphics.getHeight() * 0.25f).expandY().left().padLeft(Gdx.graphics.getWidth() * 0.14f);
        table.row();
        table.add(scoreLabel).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).left().padLeft(Gdx.graphics.getWidth() * 0.25f - Gdx.graphics.getHeight() * 0.05f);
        table.row();
        table.add(submitHighScore).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT);
        table.row();
        table.add(mainMenuButton).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT);
        table.row();
        table.add(tryAgainButton).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT);
        table.row();
        table.add(viewScore).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT);

        table.setDebug(MyGdxGame.DEBUG);
    }

    private void addSubmitHighScoreButton(){
        if (game.playServices != null){
            if (game.playServices.isSignedIn()){
                if (score >  game.playerSave.highestScore){

                }
            }
        }

    }
}
