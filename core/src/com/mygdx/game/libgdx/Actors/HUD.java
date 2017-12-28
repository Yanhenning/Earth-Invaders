package com.mygdx.game.libgdx.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.Screens.MainMenuScreen;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

/**
 * Created by YAN on 11/02/2017.
 */

public class HUD {

    private static int CELL_SIZE = 46;

    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private Integer lifeCount;
    public Integer score;
    public int gameState = 0;
    public ProgressBar healthBar;
    MyGdxGame game;

    public Skin skin;


    Label scoreLabel;
    Label playerLife;
    Label playerLifeCount;
    Button buttonPause;

    public Integer score2 = 0;

    public HUD(SpriteBatch spriteBatch, final com.mygdx.game.libgdx.Screens.MyGdxGame game){
        worldTimer = 300;
        score = 0;
        lifeCount = 100;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);
        this.game = game;


        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(game.title, Color.WHITE));
        scoreLabel.setFontScale(0.8f);
        playerLife = new Label("PLAYER LIFE:", new Label.LabelStyle(game.font, Color.WHITE));
        playerLifeCount = new Label(String.format("%03d", lifeCount), new Label.LabelStyle(game.title, Color.WHITE));
        playerLifeCount.setFontScale(0.8f);


        Button.ButtonStyle pauseStyle = new Button.ButtonStyle();




        skin = new Skin();
        //skin.addRegions(game.manager.get("data/skin/ui-yellow.atlas", TextureAtlas.class));
        skin = game.skin;

        pauseStyle.up = skin.getDrawable("icon_pause");
        pauseStyle.down = skin.newDrawable("icon_pause", Color.LIGHT_GRAY);

        buttonPause = new Button(pauseStyle);
        //buttonPause.setDebug(true);
        buttonPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.setScreen(new com.mygdx.game.libgdx.Screens.MainMenuScreen(game));
                game.playClick();
                gameState = 2;
                score+= 1;
                PauseDialog();

            }
        });

        table.add(buttonPause).expandX().pad(2).left().width(Gdx.graphics.getWidth() * 0.08f).height(Gdx.graphics.getWidth()* 0.08f);

        //createHealthBar();

        table.setDebug(MyGdxGame.DEBUG);
        table.add(scoreLabel).expandX().pad(2).right().width(Gdx.graphics.getWidth() * 0.08f).height(Gdx.graphics.getWidth() * 0.08f).padRight(Gdx.graphics.getWidth() * 0.25f);
        table.add(playerLife).expandX().pad(2).right();
       // table.row();
        table.add(playerLifeCount).pad(2).right().right().right();
        stage.addActor(table);

    }

    private void PauseDialog(){
        Label label = new Label("PAUSED", new Label.LabelStyle(game.title, Color.BLACK));
        label.setWrap(true);
        //label.setFontScale(2f);
        label.setColor(Color.BLACK);
        label.setAlignment(Align.center);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.title;
        textButtonStyle.font.setColor(Color.BLACK);
        textButtonStyle.up = skin.getDrawable("button_05");
        textButtonStyle.down = skin.newDrawable("button_05", Color.LIGHT_GRAY);

        TextButton resumeButton = new TextButton("RESUME", textButtonStyle);
        resumeButton.getLabel().setFontScale(0.65f);

        TextButton exitButton = new TextButton("MAIN MENU", textButtonStyle);
        exitButton.getLabel().setFontScale(0.65f);

        Skin skinDialog = new Skin(Gdx.files.internal("data/skin/uiskin.json"));

        final Dialog dialog = new Dialog("", skinDialog) {
            @Override
            public float getPrefWidth() {
                return Gdx.graphics.getWidth() * 0.6f;
            }

            @Override
            public float getPrefHeight() {
                return Gdx.graphics.getHeight() * 0.6f;
            }
        };

        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                if(gameState == 2)
                    gameState = 0;
                dialog.hide();
                dialog.cancel();
                dialog.remove();

            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                dialog.hide();
                dialog.cancel();
                dialog.remove();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Table t = new Table();
        t.setDebug(MyGdxGame.DEBUG);
        dialog.getContentTable().add(label).padTop(1f).top();
        dialog.getTitleTable().clear();
        dialog.getTitleTable().setHeight(0);
        dialog.getTitleTable().remove();
        dialog.setDebug(MyGdxGame.DEBUG);

        t.add(resumeButton).width(dialog.getPrefWidth() * 0.85f).height(dialog.getPrefHeight() * 0.2f).pad(4);
        t.row();
        t.add(exitButton).width(dialog.getPrefWidth() * 0.85f).height(dialog.getPrefHeight() * 0.2f).pad(4);
        dialog.getButtonTable().add(t).center().expandY();

        dialog.show(stage).setPosition(Gdx.graphics.getWidth() * 0.5f - Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.5f - Gdx.graphics.getHeight() * 0.3f);
        stage.addActor(dialog);


    }

    public void addScore2(){
        scoreLabel.setText(String.format("%06d", score2));
    }


    public void addScore(){
        //score+=1;
        scoreLabel.setText(String.format("%06d", score));
    }
    public void changeLife(Integer value){
        lifeCount = value;
        playerLifeCount.setText(String.format("%03d", lifeCount));
    }

    public void createHealthBar(){
        Color bgColor = new Color(100/256f, 100/256f, 100/256f, 1f);
        Skin skinn = new Skin();
        Pixmap pixmap = new Pixmap(1, 100, Pixmap.Format.RGBA8888);
        pixmap.fill();
        skinn.add("white", new Texture(pixmap));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skinn.newDrawable("white", bgColor), skinn.newDrawable("white", Color.BLUE));
        barStyle.knobAfter = skinn.newDrawable("white", Color.CORAL);
        healthBar = new ProgressBar(0, 100, 0.5f, true, skinn);
    }

}
