package com.mygdx.game.libgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.kryo.Kryo;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;
import com.mygdx.game.libgdx.Scene.SetAccelerometer;

/**
 * Created by YAN on 25/02/2017.
 */

public class OptionScreen implements Screen {

    private static float BUTTON_WIDTH = 250f;


    final MyGdxGame game;

    OrthographicCamera camera;

    Stage stage;
    Skin skin;
    Table table;
    Label optionMenuLabel;
    TextButton musicTextButton;
    TextButton effectTextButton;
    TextButton controllerTextButton;
    TextButton setAccelerometer;
    Button buttonAccept;


    public OptionScreen(MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        skin = game.skin;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        createOptionsMenu();

        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), "purple"));
        stage.addActor(table);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        game.batch.begin();
        stage.act();
        stage.draw();
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }


    private void createOptionsMenu(){

        Button.ButtonStyle checkbuttonStyle = new Button.ButtonStyle();
        checkbuttonStyle.checked = skin.newDrawable("radiobox_on");
        checkbuttonStyle.checkedOver = skin.getDrawable("radiobox_off");
        checkbuttonStyle.down = skin.getDrawable("radiobox_off");
        checkbuttonStyle.up = skin.getDrawable("radiobox_off");

        optionMenuLabel = new Label("OPTIONS", new Label.LabelStyle(game.title, Color.WHITE));
        //effectLabel = new Label()
        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button_02");
        textButtonStyle.down = skin.newDrawable("button_02", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("button_02", Color.DARK_GRAY);
        textButtonStyle.checkedOver = skin.getDrawable("button_02");
        textButtonStyle.font = game.font;


        musicTextButton = new TextButton("MUSIC ON", textButtonStyle);
        musicTextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
               if (musicTextButton.isChecked()){
                   musicTextButton.setText("MUSIC OFF");
                   game.playerSave.setSoundMusicON(!musicTextButton.isChecked());
               }
                else{
                   musicTextButton.setText("MUSIC ON");
                   game.playerSave.setSoundMusicON(!musicTextButton.isChecked());
               }
            }
        });
        effectTextButton = new TextButton("EFFECTS ON", textButtonStyle);
        effectTextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                if (effectTextButton.isChecked()){
                    effectTextButton.setText("EFFECTS OFF");
                    game.playerSave.setSoundEffectsON(!effectTextButton.isChecked());
                }
                else{
                    effectTextButton.setText("EFFECTS ON");
                    game.playerSave.setSoundEffectsON(!effectTextButton.isChecked());
                }
            }
        });


        TextButton.TextButtonStyle styleButton = new TextButton.TextButtonStyle();
        styleButton.up = skin.getDrawable("button_02");
        styleButton.down = skin.newDrawable("button_02", Color.DARK_GRAY);
        styleButton.checked = styleButton.up;
        styleButton.checkedOver = styleButton.up;
        styleButton.font = game.font;

        controllerTextButton = new TextButton("CONTROLLER ON", styleButton);
        controllerTextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                if (controllerTextButton.isChecked())
                    controllerTextButton.setText("ACCELEROMETER ON");
                else
                    controllerTextButton.setText("CONTROLLER ON");
            }
        });

        setAccelerometer = new TextButton("SET ACCELEROMETER", styleButton);
        setAccelerometer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                game.setScreen(new SetAccelerometer(game));
            }
        });

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skin.getDrawable("icon_check");
        buttonStyle.down = skin.newDrawable("icon_check", Color.LIGHT_GRAY);

        buttonAccept = new Button(buttonStyle);
        buttonAccept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.playClick();
                if (game.playerSave != null){
                    game.playerSave.controller = !controllerTextButton.isChecked();
                    game.playerSave.setSoundEffectsON(!effectTextButton.isChecked());
                    game.playerSave.setSoundMusicON(!musicTextButton.isChecked());
                    game.savePlayerFile();
                }
                game.setScreen(new MainMenuScreen(game));
            }
        });


        if (game.playerSave !=null){
            musicTextButton.setChecked(!game.playerSave.isSoundMusicON());
            effectTextButton.setChecked(!game.playerSave.isSoundEffectsON());
            controllerTextButton.setChecked(!game.playerSave.controller);
        }


        table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(optionMenuLabel).pad(Gdx.graphics.getHeight() * 0.1f).height(Gdx.graphics.getHeight() * MyGdxGame.TITLE_HEIGHT_MULT).width(Gdx.graphics.getWidth()* MyGdxGame.BUTTON_WIDHT_MULT).expandY().left().padLeft(Gdx.graphics.getWidth() * 0.33f);
        table.row();
        table.add(musicTextButton).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandY();
        table.row();
        table.add(effectTextButton).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandY();
        table.row();
        table.add(controllerTextButton).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandY();
        table.row();
        table.add(setAccelerometer).pad(Gdx.graphics.getHeight() * 0.05f).width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT).expandY();
        table.row();
        table.add(buttonAccept).bottom().right().pad(10).expandY().expandX().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).height(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).expandY();
        table.setDebug(MyGdxGame.DEBUG);



    }
}
