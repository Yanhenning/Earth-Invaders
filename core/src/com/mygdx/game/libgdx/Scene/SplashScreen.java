package com.mygdx.game.libgdx.Scene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.AnimatedSprite;
import com.mygdx.game.libgdx.Screens.MainMenuScreen;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

/**
 * Created by YAN on 27/02/2017.
 */

public class SplashScreen implements Screen{

    static final float FRAME_DURATION = 0.7f;

    final MyGdxGame game;
    static TextureAtlas atlas;
    static AnimatedSprite animatedSprite;
    static Animation animation;
    Stage stage;
    Viewport viewport;
    Sprite logo;
    boolean change = true;
    BitmapFont font;


    public SplashScreen(MyGdxGame game) {
        this.game = game;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport);
        logo = new Sprite(new Texture(Gdx.files.internal("data/splashscreen/logo.png")));
        logo.setSize(logo.getWidth(), logo.getHeight());
        logo.setOrigin(0, 0);
        logo.setPosition(Gdx.graphics.getWidth() * 0.5f - logo.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f - logo.getHeight() * 0.5f);
        //logo.setPosition(0 , 0);
        createPhoneAnimation();
        font = game.font;
        font.setColor(Color.BLACK);
        //game.saveToCloud();

    }


    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();


            //game.batch.setProjectionMatrix(viewport.getCamera().combined);

        game.batch.begin();

        //stage.act();
        //stage.draw();
        if(change == true){
            animatedSprite.draw(game.batch);
            font.draw(game.batch,"MOVE YOUR DEVICE TO PLAY", Gdx.graphics.getWidth() * 0.16f, Gdx.graphics.getHeight() * 0.25f);
            font.draw(game.batch,"CONFIGURE IN THE OPTIONS MENU", Gdx.graphics.getWidth() * 0.11f, Gdx.graphics.getHeight() * 0.15f);

            //logo.draw(game.batch);
            System.out.println(game.savedGameData);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (change == true){
                        change = false;
                    }
                    //if (game.isSignedIn())
                }
            },2);
        } else{
            animatedSprite.draw(game.batch);
            font.draw(game.batch,"MOVE YOUR DEVICE TO PLAY", Gdx.graphics.getWidth() * 0.16f, Gdx.graphics.getHeight() * 0.25f);
            font.draw(game.batch,"CONFIGURE IN THE OPTIONS MENU", Gdx.graphics.getWidth() * 0.11f, Gdx.graphics.getHeight() * 0.15f);

        }



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
        atlas.dispose();
    }

    void handleInput(){
        if (Gdx.input.justTouched() && change == false)
            game.setScreen(new MainMenuScreen(game));
    }

    void waitForLogin(){

    }

    public void createPhoneAnimation(){
        atlas = new TextureAtlas(Gdx.files.internal("data/splashscreen/phone.atlas"));
        animation = new Animation(FRAME_DURATION, atlas.getRegions(), Animation.PlayMode.LOOP);
        animatedSprite = new AnimatedSprite(animation);
        animatedSprite.setSize(animatedSprite.getWidth() * 0.5f, animatedSprite.getHeight()* 0.5f);
        animatedSprite.setOrigin(0, 0);
        animatedSprite.setPosition(Gdx.graphics.getWidth() * 0.5f - animatedSprite.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.6f - animatedSprite.getHeight() * 0.5f);
    }

}
