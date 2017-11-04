package com.mygdx.game.libgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ByteArray;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;
import com.mygdx.game.libgdx.Scene.PlayerSave;
import com.mygdx.game.libgdx.Scene.SplashScreen;

/**
 * Created by YAN on 08/08/2017.
 */

public class LoadingScreen implements Screen {

    private final static String buttonShape = "button_05";
    final MyGdxGame game;
    OrthographicCamera camera;

    Stage stage;
    TextButton singInButton;
    TextButton gameButton;
    Table table;
    Skin skin;
    Label gameName;
    ChangeListener signInListener;
    ChangeListener signOutListner;
    Sprite logo;
    boolean change = false;
    boolean readyToLoad = true;
    boolean loadLocalBool = true;
    boolean changeState = false;


    public LoadingScreen(final MyGdxGame game){
        System.out.println("Conectando loadingScreen");
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        //Load skin from assets
        skin = game.skin;

        logo = new Sprite(new Texture(Gdx.files.internal("data/splashscreen/logo.png")));
        logo.setSize(logo.getWidth(), logo.getHeight());
        logo.setOrigin(0, 0);
        logo.setPosition(Gdx.graphics.getWidth() * 0.5f - logo.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f - logo.getHeight() * 0.5f);

        createListeners();
        createButtons();

        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.addActor(table);

    }

    private void createButtons() {
        System.out.println("signInButton createButtons");
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.getDrawable(buttonShape);
        style.down = skin.newDrawable(buttonShape, Color.LIGHT_GRAY);
        style.font = game.font;


        System.out.println("signInButton playService conectado: " + Boolean.toString(game.isSignedIn()));

        gameButton = new TextButton("START",style);
        gameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.playServices.checkNetworkStatus()){
                    //game.loadLocalSave();
                }
                game.setScreen(new SplashScreen(game));
            }
        });

        if (!game.isSignedIn()){
            singInButton = new TextButton("Sign In", style);
            singInButton.addListener(signInListener);
            System.out.println("signInButton SingInCreated");
        } else if (game.isSignedIn()){
            singInButton = new TextButton("Sign Out", style);
            singInButton.addListener(signOutListner);
            System.out.println("signInButton SingOutCreated");
        }

        gameName = new Label("EARTH INVADERS", new Label.LabelStyle(game.title, Color.WHITE));

        table = new Table(skin);
        table.setFillParent(true);

        table.setDebug(MyGdxGame.DEBUG);
        table.center();
        table.add(gameName).pad(10).height(Gdx.graphics.getHeight() * 0.25f).expandY().width(Gdx.graphics.getHeight() * 0.7f).center().padRight(gameName.getWidth() * 0.42f);
        table.row();
        table.add(gameButton).center().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT * 2);
        table.row();
        table.add(singInButton).center().width(Gdx.graphics.getWidth() * MyGdxGame.BUTTON_WIDHT_MULT).expandY().height(Gdx.graphics.getHeight() * MyGdxGame.BUTTON_HEIGHT_MULT * 2);
        table.row();

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       // updateStatusLogIn();
        autoLoad();
        updateButtonsState();
        camera.update();
        game.batch.begin();
        if (change == false){
            logo.draw(game.batch);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (change == false){
                        change = true;
                    }
                }
            },4);
        } else{
            stage.act();
            stage.draw();
        }

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

    public void updateButtonsState(){
        if (true){
            if (game.isSignedIn()){
                singInButton.setText("Sign OUT");
                singInButton.removeListener(signInListener);
                singInButton.addListener(signOutListner);
                changeState = false;
            } else if (!game.isSignedIn()){
                singInButton.setText("Sign in");
                Log.debug("signInButton", "SignOut to SingIn");
                singInButton.removeListener(signOutListner);
                singInButton.addListener(signInListener);
                changeState = false;
            }
        }

    }

    private void createListeners(){
        Log.debug("signInButton", "Listeners Created");
        signInListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!game.playServices.isSignedIn() && game.playServices.checkNetworkStatus()){
                    changeState = true;
                    game.signIn();
                    Log.debug("signInButton", "SingInClicked");
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                         game.loadFromCloud();
                        }
                    },3);
                }

            }
        };

        signOutListner = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isSignedIn()){
                    changeState = true;
                    game.signOut();
                    Log.debug("signInButton", "SingOutClicked");
                }
            }
        };
    }

    void autoLoad(){
        if (game.autoLoad&&readyToLoad){
            if (game.isSignedIn()){
                game.loadFromCloud();
                readyToLoad = false;
            }
        }
        //load local save if has no internet
        if ((!game.playServices.checkNetworkStatus() && loadLocalBool)||loadLocalBool && !game.isSignedIn()){
            game.loadLocalSave();
            loadLocalBool = false;
        }
    }


}
