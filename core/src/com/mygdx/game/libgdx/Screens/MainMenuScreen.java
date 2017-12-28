package com.mygdx.game.libgdx.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.libgdx.Actors.ExperienceBar;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;

import java.io.FileOutputStream;

/**
 * Created by YAN on 25/01/2017.
 */

public class MainMenuScreen implements Screen {


    private final static String buttonShape = "button_05";
    final MyGdxGame game;


    PolygonSpriteBatch polygonSpriteBatch;

    OrthographicCamera camera;

    Stage stage;
    TextButton buttonStartGame;
    TextButton buttonSelectPlayerShip;
    TextButton buttonOptions;
    TextButton buttonMultiplayer;
    TextButton buttonShowAchievement;
    TextButton buttonShowLeaderboard;
    Skin skin;
    Label playerPoints;
    long points;
    Label gameName;
    Table table;
    Label playerName;
    Label playerStardust1;
    Label playerStartdust2;
    Label playerLevel;

    ProgressBar experienceBar;

    ImageButton medalButton;

    Kryo kryo = new Kryo();
    ExperienceBar bar;

    public MainMenuScreen(final MyGdxGame game) {


        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        //Load skin from assets
        skin = game.skin;
        points = 0;
        createMainMenuButtons();


        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.addActor(table);

        createPlayerPointsLabel();
        if (Gdx.app.getType() == Application.ApplicationType.Android) unlockAchievement();
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);


//Gdx.files.getLocalStoragePath () + "/" + fileName

        try {
            Output output = new Output(new FileOutputStream(Gdx.files.getLocalStoragePath () + "/" + game.fileName));
            // serialize object to file
            kryo.writeObject(output, game.playerSave);
            output.close();
            System.out.println("ok kyro1");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playerPoints.setText(String.format("%06d", points));
        camera.update();
        game.batch.begin();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        //game.title.draw(game.batch, "EARTH INVADERS", camera.viewportWidth * 0.3f, Gdx.graphics.getHeight() * 0.75f);
        //drawButtons();
        game.batch.end();

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


    }

    private void createMainMenuButtons() {

        //Create button's Style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.getDrawable(buttonShape);
        style.down = skin.newDrawable(buttonShape, Color.LIGHT_GRAY);
        style.font = game.font;


        //get text from simple text dialog


        buttonStartGame = new TextButton("STAGES",style);
        buttonStartGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                //game.setScreen(new GameScreen(game));
                game.setScreen(new LevelScreen(game));
            }
        });

        buttonSelectPlayerShip = new TextButton("Hangar", style);
        buttonSelectPlayerShip.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                game.setScreen(new ShipSelectionScreen(game, MainMenuScreen.this));
            }
        });

        buttonOptions = new TextButton("Options", style);
        buttonOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                game.setScreen(new OptionScreen(game));
            }
        });

        buttonMultiplayer = new TextButton("Multiplayer", style);
        buttonMultiplayer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                game.playServices.toast("COMING SOON");

                //game.setScreen(new NewGameScreen(game));
            }
        });

        buttonShowAchievement = new TextButton("PROFILE", style);
        buttonShowAchievement.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                //game.playServices.showAchievement();
            }
        });

        buttonShowLeaderboard = new TextButton("My score", style);
        buttonShowLeaderboard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                //if (game.playServices.isSignedIn())
                    //game.playServices.showScore();
            }
        });

        medalButton = new ImageButton(game.playerMedal(game.playerSave.playerLevel));

        medalButton.setWidth(medalButton.getWidth());
        medalButton.setHeight(medalButton.getHeight());

        playerPoints = new Label(String.format("%06d", points), new Label.LabelStyle(game.font, Color.WHITE));
        gameName = new Label("EARTH INVADERS", new Label.LabelStyle(game.title, Color.WHITE));
        playerName = new Label(game.playerSave.playerName, new Label.LabelStyle(game.tinyFont, Color.WHITE));
        System.out.print("playerName:" + game.playerSave.playerName);
        playerStardust1 = new Label(String.format("%04d", game.playerSave.stardust), new Label.LabelStyle(game.font, Color.WHITE));
        playerStartdust2 = new Label(String.format("%04d", game.playerSave.stardust), new Label.LabelStyle(game.font, Color.WHITE));
        playerLevel = new Label(Integer.toString(game.playerSave.playerLevel), new Label.LabelStyle(game.title, Color.WHITE));

        table = new Table(skin);
        table.setFillParent(true);
        table.setDebug(MyGdxGame.DEBUG);

        Table header = new Table(skin);
        header.add(medalButton).left().pad(4).top().height(Gdx.graphics.getHeight() * 0.14f);
        header.add(playerLevel).left().pad(4).top().height(Gdx.graphics.getHeight() * 0.14f);

        Table nameExpirience = new Table(skin);
        nameExpirience.add(playerName).left().pad(1).top().expandX().height(Gdx.graphics.getHeight() * 0.05f).top().width(Gdx.graphics.getWidth()*0.14f);
        nameExpirience.row();


        bar = new ExperienceBar(game, skin);
        ProgressBar experienceBar = bar.getExpBar();

        nameExpirience.add(experienceBar).left().pad(1).top().expandX().height(Gdx.graphics.getHeight() * 0.05f).bottom().width(Gdx.graphics.getWidth()*0.14f);
        nameExpirience.setDebug(MyGdxGame.DEBUG);

        header.add(nameExpirience);
        header.setDebug(MyGdxGame.DEBUG);
        header.add(playerStardust1).right().pad(4).height(Gdx.graphics.getHeight() * 0.10f).center();
        header.add(playerPoints).right().pad(4).height(Gdx.graphics.getHeight() * 0.10f).center();

        table.add(header).expandX().left();
        table.row();
        table.center();
        table.add(gameName).pad(6).height(Gdx.graphics.getHeight() * 0.18f).expandY().width(Gdx.graphics.getHeight() * 0.7f).center().padRight(gameName.getWidth() * 0.42f).expandX();
        table.row();
        table.add(buttonStartGame).center().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().expandX().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandX();
        table.row();
        table.add(buttonSelectPlayerShip).center().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().fill().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandX();
        table.row();
        table.add(buttonOptions).center().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().fillX().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandX();
        table.row();
        table.add(buttonMultiplayer).uniformX().center().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandX();
        table.row();
        table.add(buttonShowAchievement).growX().center().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandX();
        table.row();
        table.add(buttonShowLeaderboard).center().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandX();

    }

    private void createPlayerPointsLabel(){
        if(game.playerSave != null)
            points = game.playerSave.getPlayerPoints();
        else
            points = 0;
        playerPoints.setText(String.format("%06d", points));
    }

    public void unlockAchievement(){
        if (game.playServices.isSignedIn() && game.playServices!=null){
            if (game.playerSave.numberOfDeaths > 50)
                game.playServices.unlockAchievement("CgkIprypw6QXEAIQAg");
            if (game.playerSave.numberOfMissedShoots > 500)
                game.playServices.unlockAchievement("CgkIprypw6QXEAIQAw");
            if (game.playerSave.numberOfMeteorsDestroyed > 100)
                game.playServices.unlockAchievement("CgkIprypw6QXEAIQBA");
            if (game.playerSave.firstPlay)
                game.playServices.unlockAchievement("CgkIprypw6QXEAIQBQ");
            if(game.playerSave.highestScore > 10000)
                game.playServices.unlockAchievement("CgkIprypw6QXEAIQBw");
        }
    }


}
