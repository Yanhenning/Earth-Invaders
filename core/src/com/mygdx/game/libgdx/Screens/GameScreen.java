package com.mygdx.game.libgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.Actors.Controller;
import com.mygdx.game.libgdx.Actors.HUD;
import com.mygdx.game.libgdx.Actors.Laser;
import com.mygdx.game.libgdx.Actors.Meteor;
import com.mygdx.game.libgdx.Actors.Player;
import com.mygdx.game.libgdx.Scene.ContactGameListener;
import com.mygdx.game.libgdx.Weapons.LaserExplosion;
import com.mygdx.game.libgdx.Weapons.MeteorExplosion;

import java.util.Random;


/**
 * Created by YAN on 25/01/2017.
 */

public class GameScreen implements Screen {



    //Constant fields
    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;
    static final int GAME_READY = 0;
    static final int GAME_RUN = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;

    int state;
    float meteorAccumulator = 0.0f;
    float accumulator = 0.0f;
    float accX;
    float stageExp = 0;



    //HUD
    HUD hud;

    MyGdxGame game;
    OrthographicCamera camera;
    World world;
    private Viewport viewport;
    private Vector3 direction;

    //Stage
    private Stage stage;
    private InputMultiplexer inputMultiplexer;
    private Controller controller;


    //Actors
    private Player player;
    private Array<Meteor> meteors;
    private Array<LaserExplosion> explosions;
    private Array<Laser> lasers;
    private Array<MeteorExplosion> meteorExplosions;


    Box2DDebugRenderer debugRender;

    public GameScreen(final MyGdxGame gam){

        this.game = gam;
        state = GAME_READY;
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(50, 50, camera);
        direction = new Vector3();
        stage = new Stage();
        inputMultiplexer = new InputMultiplexer();
        Box2D.init();
        hud = new HUD(game.batch, game);
        controller = new Controller(game.batch, game.playerSave.controller);

        stage.addActor(new com.mygdx.game.libgdx.Actors.InfiniteScrollBg(Gdx.graphics.getWidth() , Gdx.graphics.getHeight()));

        inputMultiplexer.addProcessor(controller.stage);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(hud.stage);

        world = new World(new Vector2(0, 0), true);

        //Create meteors and player
        if(game.playerSave!=null){
            String ship = game.playerSave.getPlayerCurrentShip();
            player = new Player(world, ship, game.shipSprites);
            player.body.setUserData(player);
        }
        if (game.playerSave.firstPlay)
            game.playerSave.firstPlay = false;
        meteors = new Array<Meteor>();
        debugRender = new Box2DDebugRenderer();
        ContactGameListener cgListener = new ContactGameListener();
        cgListener.setPlayer(player);
        world.setContactListener(cgListener);
        explosions =  new Array<LaserExplosion>();
        lasers = new Array<Laser>();
        meteorExplosions = new Array<MeteorExplosion>();
        accX = game.playerSave.accX;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {

        if (hud.gameState == 2)
            state = GAME_PAUSED;

        switch (state){
            case GAME_READY:
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                stepWorld();
                stage.act(Gdx.graphics.getDeltaTime());
                stage.draw();
                direction.set( Gdx.input.getX(), Gdx.input.getY(), 0.0f);
                camera.unproject(direction);
                camera.update();
                handleShipControl();

                //updatePlayer();
                game.batch.setProjectionMatrix(camera.combined);

                game.batch.begin();


                player.update();
                player.drawPlayer(game.batch);
                update(delta);
                //game.font.draw(game.batch, Float.toString(player.body.getPosition().x) + ", " + Float.toString(player.body.getPosition().y), player.body.getPosition().x, player.body.getPosition().y);


                game.batch.end();
                //Set the hud camera and draw
                game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
                hud.stage.draw();
                game.batch.setProjectionMatrix(controller.stage.getCamera().combined);
                controller.draw();
                //debugRender.render(world, camera.combined);

                break;

            case GAME_PAUSED:

                game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
                stage.draw();
                hud.stage.draw();
                if (hud.gameState == GAME_READY)
                    state = GAME_READY;
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        viewport.update(width, height);
        //controller.resize(width, height);
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
        //backGroundRegion.dispose();

        world.dispose();
        debugRender.dispose();
        stage.dispose();
        meteorExplosions.clear();
        lasers.clear();
        meteors.clear();
        explosions.clear();
        controller.stage.dispose();
        hud.stage.dispose();
        hud.skin.dispose();

    }


    private void handleShipControl() {
        float VELOCITY_MULT = 12;
        if(controller.isAPressed()){
            if (!player.isPlayerDead()){
                lasers.add(new Laser(world, player.body.getPosition().x + player.player.getWidth() * 0.5f,
                        player.player.getHeight() + player.body.getPosition().y));
                if (game.playerSave.isSoundEffectsON()) player.laserSound1.play(1f);
            }
        }

        if (game.playerSave.controller && !player.touched){
            player.body.setLinearVelocity(VELOCITY_MULT * controller.touchpad.getKnobPercentX() * 2.5f, VELOCITY_MULT * controller.touchpad.getKnobPercentY() * 2.5f);
        }

        if (!game.playerSave.controller && !player.touched) {

            //For smartphones
            if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer) && !player.isDead)
            {

                //Move the ship based on accelerometer position
                player.body.setLinearVelocity(VELOCITY_MULT * Gdx.input.getAccelerometerY(), (accX - (Gdx.input.getAccelerometerX())) * VELOCITY_MULT);
            }
        }

        //For pcs
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            player.body.setLinearVelocity(player.body.getLinearVelocity().x += 5, player.body.getLinearVelocity().y);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)){
            player.body.setLinearVelocity(player.body.getLinearVelocity().x -= 5, player.body.getLinearVelocity().y);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            player.body.setLinearVelocity(player.body.getLinearVelocity().x , player.body.getLinearVelocity().y += 5);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            player.body.setLinearVelocity(player.body.getLinearVelocity().x , player.body.getLinearVelocity().y -=5);
        }

    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            //updateHudWithPlayerInfo();
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }

    }

    private void meteorStorm() {
        float delta =  Gdx.graphics.getDeltaTime();
        int MIN_TIME = 3, MAX_TIME = 31;
        meteorAccumulator += delta;

        Random random = new Random();
        int STOP_VALUE = random.nextInt(MAX_TIME - MIN_TIME) + MIN_TIME;


        if(meteorAccumulator >= STOP_VALUE){
            meteorAccumulator = 0;
            meteors.add(new Meteor(world, game.meteorSprites));
        }
    }

    private void updateHudWithPlayerInfo(){
        hud.score = player.score;
        hud.addScore();
        hud.changeLife(player.life);


    }

    private void updatePlayer(){
        if (player.isPlayerDead()){

            game.playerSave.setPlayerPoints(game.playerSave.getPlayerPoints() + player.score);
            game.playerSave.numberOfDeaths += 1;
            game.playLoseGameSound();
            if (player.score > game.playerSave.highestScore)
                game.playerSave.highestScore = player.score;
            game.playerSave.playerExpirience += stageExp;
            float expNextLevel = game.playerExperience.get(game.playerSave.playerLevel);
            if (game.playerSave.playerExpirience >= expNextLevel) game.playerSave.playerLevel += 1;
            game.saveToCloud();
            game.setScreen(new GameOverScreen(game, player.score));
        }
        isPlayerOnScreen();
    }

    public void updateMeteors(){
        //Handle meteors

        if (meteors.size > 0){
            for (Meteor m : meteors) {
                isMeteorOnScreen(m);
                if (m.isDestroyed()){
                    if (isMeteorOnScreen(m)){
                        game.playExplosion();
                        if(game.playerSave != null) game.playerSave.numberOfMeteorsDestroyed += 1;
                    }

                    meteors.removeValue(m, false);
                    Gdx.app.log("Removeu da Array","o meteoro foi deletado da array");

                    //m.body.setTransform(m.body.getMassData().center, m.body.getAngle());
                    meteorExplosions.add(new MeteorExplosion(
                            m.body.getPosition().x
                            , m.body.getPosition().y
                            , m.meteorSprite.getWidth()
                            , m.meteorSprite.getHeight()
                            , m.body.getAngle()
                            ,m.body.getLinearVelocity().y
                            ,m.body.getLinearVelocity().x));
                }
            }
        }
        destroyBodies();
        meteorStorm();

        for (Meteor m: meteors) {
            m.drawMeteor(game.batch);
            if (m.getMeteorLife() < 0) stageExp += m.giveExp();

        }

    }

    public void isPlayerOnScreen(){
        if((player.body.getPosition().x + player.player.getWidth() <  - viewport.getWorldWidth() * 0.5f
                || player.body.getPosition().x > viewport.getWorldWidth() * 0.5f
                || (player.body.getPosition().y + player.player.getHeight() < - viewport.getWorldHeight() * 0.5f
        || player.body.getPosition().y > viewport.getWorldHeight() * 0.5f))){
            player.isDead = true;
        }
    }

    public boolean isMeteorOnScreen(Meteor meteor){
        if((meteor.body.getPosition().x < -70 || meteor.body.getPosition().x > 70)||
                (meteor.body.getPosition().y < -44)){
            Gdx.app.log("Saiu da tela", "setToDestroy");
            meteor.setToDestroy();
            return false;
        }
        return true;
    }

    public void destroyBodies(){
        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);
        for(Body body : bodies){
            for (Fixture f: body.getFixtureList()) {
                if (f.getFilterData().categoryBits == MyGdxGame.METEOR_BIT && f.getFilterData().categoryBits != MyGdxGame.SHIP_BIT){
                    if(((Meteor)body.getUserData()).isDestroyed()){
                        world.destroyBody(body);
                        body = null;
                        Gdx.app.log("Destruindo corpo", "O corpo foi destruído");

                    }
                }else if(f.getFilterData().categoryBits == MyGdxGame.SHIP_BIT && f.getBody().getUserData().getClass().equals(Player.class)){
                    if(((Player)body.getUserData()).isPlayerDead()){
                        world.destroyBody(body);
                        body = null;
                        //implement gameOverScreen
                    }
                }

            }
        }
    }
    //Put between batch.begin and batch.end
    public void update(float dt){
        updateMeteors();
        updateMeteorExplosions(dt);
        updatePlayer();
        updateHudWithPlayerInfo();
        updateLasers();
        updateLaserExplosions(dt);
    }


    public void updateMeteorExplosions(float dt){
        for (MeteorExplosion mExplo: meteorExplosions) {
            mExplo.update(dt);
            mExplo.render(game.batch);
            if(mExplo.remove)
                meteorExplosions.removeValue(mExplo, true);
        }
    }


    public void updateLaserExplosions(float dt){
        for (LaserExplosion explosion: explosions) {
            explosion.update(dt);
            explosion.render(game.batch);
            if (explosion.remove)
                explosions.removeValue(explosion, true);

        }
    }
    public void updateLasers(){
        for (Laser laser:lasers) {
            laser.draw(game.batch);
            if (laser.body.getPosition().y > 25){
                laser.setToDestroy();
                if(game.playerSave != null)
                    game.playerSave.numberOfMissedShoots += 1;
            }
            if (laser.Impact)
                explosions.add(new LaserExplosion(laser.body.getPosition().x , laser.body.getPosition().y + laser.sprite.getHeight()));
            if (laser.isDestroyed()){
                lasers.removeValue(laser, true);
                world.destroyBody(laser.body);
            }

        }
    }

}
