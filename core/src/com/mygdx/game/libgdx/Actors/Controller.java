package com.mygdx.game.libgdx.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

import javax.swing.text.View;

/**
 * Created by YAN on 25/02/2017.
 */

public class Controller {
    private static final float BUTTON_SIZE = 65;

    OrthographicCamera camera;
    Viewport viewport;
    public Stage stage;
    boolean aPressed, bPressed, yPressed, joystick;
    Image buttonA, buttonB, buttonY;
    public Touchpad touchpad;

    Table table;

    public Controller(SpriteBatch batch, boolean joystick) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new Stage(viewport, batch);
        this.joystick = joystick;

        table = new Table();
        table.right().bottom();

        addButton();

        table.add(buttonA).width(Gdx.graphics.getWidth()* MyGdxGame.BUTTON_SIZE).height(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).pad(10).padBottom(15);
        table.add(buttonB).width(Gdx.graphics.getWidth()* MyGdxGame.BUTTON_SIZE).height(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).pad(10).padBottom(15);
        table.add(buttonY).width(Gdx.graphics.getWidth()* MyGdxGame.BUTTON_SIZE).height(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_SIZE).pad(10).padBottom(15).padRight(15);

        table.setFillParent(true);
        table.setDebug(MyGdxGame.DEBUG);
        stage.addActor(table);
        if (joystick)
            createTouchPad();

        //Gdx.input.setInputProcessor(stage);
    }

    public void draw(){
        stage.act();
        stage.draw();
    }

    private void addButton(){

        Texture texture = MyGdxGame.manager.get("data/controller/transparentDark34.png");

        buttonA = new Image(texture);
        buttonA.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                aPressed = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        aPressed = false;
                    }
                },0.01f);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //aPressed = false;
            }
        });

        texture = MyGdxGame.manager.get("data/controller/transparentDark35.png");

        buttonB = new Image(texture);
        buttonB.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bPressed = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        bPressed = false;
                    }
                },0.01f);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        texture = MyGdxGame.manager.get("data/controller/transparentDark37.png");

        buttonY = new Image(texture);
        buttonY.setSize(BUTTON_SIZE, BUTTON_SIZE);
        buttonY.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                yPressed = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        yPressed = false;
                    }
                },0.01f);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                yPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }



    public void createTouchPad(){


        Skin touchPadSkin = new Skin();



        Texture texture = MyGdxGame.manager.get("data/controller/transparentDark09.png", Texture.class);
        touchPadSkin.add("background", texture);

        texture = MyGdxGame.manager.get("data/controller/transparentDark49.png", Texture.class);

        touchPadSkin.add("knob", texture);

        Touchpad.TouchpadStyle tpStyle = new Touchpad.TouchpadStyle();

        tpStyle.background = touchPadSkin.getDrawable("background");
        tpStyle.knob = touchPadSkin.newDrawable("knob", Color.WHITE);
        tpStyle.knob.setBottomHeight(tpStyle.knob.getBottomHeight() * 0.5f);
        tpStyle.knob.setLeftWidth(tpStyle.knob.getLeftWidth() * 0.5f);
        tpStyle.knob.setRightWidth(tpStyle.knob.getRightWidth() * 0.5f);
        tpStyle.knob.setTopHeight(tpStyle.knob.getTopHeight() * 0.5f);

        touchpad = new Touchpad(10, tpStyle);
        touchpad.setBounds(15, 15, 60, 60);
        Table table2 = new Table();
        table2.setFillParent(true);
        table2.bottom().left();
        table2.add(touchpad).pad(10).width(Gdx.graphics.getWidth()* 0.21f).height(Gdx.graphics.getWidth() * 0.21f);
        stage.addActor(table2);

    }

    public boolean isAPressed() {
        return aPressed;
    }

    public boolean isBPressed() {
        return bPressed;
    }

    public boolean isYPressed() {
        return yPressed;
    }
}
