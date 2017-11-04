package com.mygdx.game.libgdx.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.libgdx.BodyEditorLoader;
import com.mygdx.game.libgdx.Screens.MyGdxGame;
import com.mygdx.game.libgdx.Weapons.LaserExplosion;
import com.mygdx.game.libgdx.Weapons.MeteorExplosion;

import java.util.HashMap;

/**
 * Created by YAN on 24/01/2017.
 */

public class Player {

        public HashMap<String, Sprite> shipSprites;
        public World world;

        //Player atributes
        public Sprite player;
        public Body body;
        public String name;
        public boolean isDead = false;
        public int life = 100;
        public int score = 0;
        private float defense;
        public Sound laserSound1;
        public boolean touched = false;

        //Laser atributes
        public MeteorExplosion playerExplosion = null;

    public Player(World world, String name, HashMap<String, Sprite> ship) {

        shipSprites = ship;
        this.name = name;
        this.world = world;
        laserSound1 = MyGdxGame.manager.get("data/sounds/sfx_laser1.ogg");
        player = shipSprites.get(name);
        defense = (player.getHeight() + player.getWidth()) * 0.01f;
        defineShip();

    }

    public void drawPlayer(SpriteBatch batch){
        if(life > 0){
            player.setPosition(body.getPosition().x, body.getPosition().y);
            player.setRotation((float) Math.toDegrees(body.getAngle()));
            player.setOrigin(0, 0);
            player.draw(batch);
        }
        if (isDead){
            playerExplosion = new MeteorExplosion(body.getPosition().x,
                    body.getPosition().y,
                    player.getWidth(),
                    player.getHeight(),
                    body.getAngle(),
                    body.getLinearVelocity().y,
                    body.getLinearVelocity().x);
            //world.destroyBody(body);
            playerExplosion.update(Gdx.graphics.getDeltaTime());
            playerExplosion.render(batch);
        }

    }


    public void update(){

        setPlayerToDeath();
    }
    public void defineShip(){
        //Load the fixture from Physics Object
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/ships/ships.json"));
        //Get the ship


        //Box2D defined
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0 - player.getWidth() * 0.5f, 0 - player.getHeight() * 0.5f);
        //Create a fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 20 * player.getHeight();
        fixtureDef.restitution = 0.9f;
        fixtureDef.friction = 0.8f;
        fixtureDef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        fixtureDef.filter.maskBits = MyGdxGame.METEOR_BIT;
        //Create a body in the world
        body = world.createBody(bodyDef);

        body.setUserData(this);
        body.setFixedRotation(true);
        body.setTransform(0, 0, 0);
        loader.attachFixture(body, name + ".png", fixtureDef, player.getWidth());

        //Create sensor
        fixtureDef.isSensor = true;
        FixtureDef fDef = fixtureDef;
        fDef.isSensor = true;

        fDef.filter.categoryBits = MyGdxGame.SHIP_BIT; // I am a...

        fDef.filter.maskBits = MyGdxGame.METEOR_BIT; //I will collide with...

        loader.attachFixture(body, name + ".png", fDef, player.getWidth());
        //body.getFixtureList().get(body.getFixtureList().size-1).setUserData("shipsensor");
        body.setUserData(this);
    }

    public void damageOnPlayer(float dmg){
        life -= dmg * defense;
    }
    public void setPlayerToDeath(){
        if (life <= 0){
            life = 0;
            isDead = true;
        }
    }
    public boolean isPlayerDead(){
        return isDead;
    }


}