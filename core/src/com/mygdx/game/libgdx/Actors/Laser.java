package com.mygdx.game.libgdx.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.libgdx.BodyEditorLoader;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

/**
 * Created by YAN on 20/02/2017.
 */

public class Laser extends Sprite{

    String laserName = "laserBlue01";
    private static float SCALE = MyGdxGame.SCALE;


    float elapsedTime = 0;
    World world;
    TextureAtlas laserRegion;
    public Body body;
    public Sprite sprite;
    Float x, y;
    float stateTime = 0;

    public boolean Impact = false;
    boolean Destroyed = false;

    //Laser properties
    float laserVeloctyY = 900;
    public float laserAtk = 2;


    public Laser(World world, float x, float y) {
        laserRegion = MyGdxGame.manager.get("data/lasers/lasers.atlas");
        sprite = laserRegion.createSprite(laserName);
        float width = sprite.getWidth() * MyGdxGame.SCALE;
        float height = sprite.getHeight() * MyGdxGame.SCALE;
        sprite.setSize(width, height);
        this.x = x - sprite.getWidth() * 0.5f;
        this.y = y;
        this.world = world;

        defineLaser();

    }

    private void defineLaser(){
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/lasers/lasers"));

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        body.setLinearVelocity(0, laserVeloctyY);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1f;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = MyGdxGame.LASER_BIT;
        fixtureDef.filter.maskBits = MyGdxGame.METEOR_BIT | MyGdxGame.LASER_BIT | MyGdxGame.SHIP_BIT;

        loader.attachFixture(body, laserName + ".png", fixtureDef, sprite.getWidth());

        FixtureDef fDef = fixtureDef;
        fDef.isSensor = true;
        fDef.filter.categoryBits = MyGdxGame.LASER_BIT;
        fDef.filter.maskBits = MyGdxGame.METEOR_BIT | MyGdxGame.LASER_BIT | MyGdxGame.SHIP_BIT;

        loader.attachFixture(body, laserName + ".png", fDef, sprite.getWidth());

        body.setUserData(this);

    }

    public void setToDestroy(){
        Destroyed = true;
    }
    public boolean isDestroyed(){
        return Destroyed;
    }

    public void draw(SpriteBatch batch){
        if (Impact){
            setToDestroy();
        }
        if(!isDestroyed()&&!Impact){
            sprite.setPosition(body.getPosition().x, body.getPosition().y);
            sprite.setOrigin(0, 0);
            sprite.draw(batch);
            if (Impact)
                setToDestroy();
        }

    }
    public void impactAgainstObject(){
        Impact = true;
    }


}
