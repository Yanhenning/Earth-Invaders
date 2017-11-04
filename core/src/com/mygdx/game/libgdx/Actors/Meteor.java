package com.mygdx.game.libgdx.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.libgdx.BodyEditorLoader;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by YAN on 27/01/2017.
 */

public class Meteor  {

    String[] meteorNames = new String[]{"meteorBrown_big1", "meteorBrown_big2", "meteorBrown_big3",
            "meteorBrown_big4", "meteorBrown_med3", "meteorGrey_big1", "meteorGrey_big2",
            "meteorGrey_big3", "meteorGrey_big4", "meteorGrey_med2", "spaceMeteors1",
            "spaceMeteors2", "spaceMeteors3", "spaceMeteors4"};

    HashMap<String, Sprite> map;
        World world;
        public Body body;
        public Sprite meteorSprite;
        String name;
        public Boolean destroyed = false;
        public Boolean isHited = false;
        public Animation meteorExplode;
        private float meteorLife;
        private float meteorDamage;




    //Build a specific meteor
    public Meteor(World world, String name, HashMap<String, Sprite> map) {
        this.map = map;
        this.world = world;
        this.name = name;
        meteorSprite = map.get(name);
        defineMeteor();
        defineExplosion();

    }

    public Meteor(World world, HashMap<String, Sprite> map){

        this.world = world;
        this.map = map;
        Random random = new Random();
        name = meteorNames[random.nextInt(meteorNames.length)];
        meteorSprite = map.get(name);
        meteorLife = meteorSprite.getWidth() * 5f;
        meteorDamage = meteorSprite.getWidth();
        meteorSprite.setOrigin(0, 0);
        defineMeteor();

    }


    public void drawMeteor(SpriteBatch batch){
        if(!isTouched()){
            meteorSprite.setPosition(body.getPosition().x, body.getPosition().y);
            meteorSprite.setRotation((float) Math.toDegrees(body.getAngle()));
            meteorSprite.draw(batch);
        }
        if (meteorLife <= 0) {
            setToDestroy();
        }



    }

    private void defineMeteor(){

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/meteors/meteors.json"));
        //Create the body
        BodyDef bodyDef =  new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //Random location to create a meteor in x axis
        Random r1 = new Random();
        int High = ( 20);
        int Low = (-20);
        int r2 = r1.nextInt(High - Low) + Low;

        bodyDef.position.set(r2, 30);

        //Set the physics properties
        FixtureDef fixtureDef = new FixtureDef();
        if(name == "spaceMeteors1" || name == "spaceMeteors2"
            || name == "spaceMeteors3" || name == "spaceMeteors4")
            fixtureDef.density = 4 * meteorSprite.getWidth() * 0.7f;
        else
            fixtureDef.density = 5 * meteorSprite.getWidth();
        fixtureDef.restitution = 0.6f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = MyGdxGame.METEOR_BIT;
        fixtureDef.filter.maskBits = MyGdxGame.SHIP_BIT | MyGdxGame.METEOR_BIT | MyGdxGame.LASER_BIT;

        body = world.createBody(bodyDef);


        //body.setTransform(0, 50 * 0.5f, 0);
        body.setUserData(Meteor.this);

        //Random Vx and Vy
        r1 = new Random();
        r2 = r1.nextInt(3 -1) + 1;
        r1 = new Random();
        int r3 = r1.nextInt(40);
        if (body.getPosition().x > 0)
            body.setAngularVelocity(r2);
        else
            body.setAngularVelocity(-r2);
        body.setLinearVelocity(0, -r3);


        loader.attachFixture(body, name + ".png", fixtureDef, meteorSprite.getWidth());

        //Create sensor
        fixtureDef.isSensor = true;

        FixtureDef fDef = fixtureDef;
        fDef.isSensor = true;
        fDef.filter.categoryBits = MyGdxGame.METEOR_BIT;
        fDef.filter.maskBits = MyGdxGame.SHIP_BIT | MyGdxGame.METEOR_BIT | MyGdxGame.LASER_BIT;
        loader.attachFixture(body, name + ".png", fDef, meteorSprite.getWidth());

    }

    public void defineExplosion(){


    }

    public void setToDestroy(){
        destroyed = true;
    }
    public void setIsHited() {
        isHited = true;
    }

    public Boolean isDestroyed(){
        return destroyed;
    }
    public Boolean isTouched(){ return isHited;}
    public void damageToMeteor(float dmg){
        meteorLife -= dmg;
    }
    public float meteorAtk(){
        return meteorDamage;
    }


    public float giveExp(){
        float Exp = 100;
        return Exp;
    }
    public float getMeteorLife() {return meteorLife;}
}
