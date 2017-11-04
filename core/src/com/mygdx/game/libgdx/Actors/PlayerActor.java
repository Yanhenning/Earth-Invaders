package com.mygdx.game.libgdx.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.mygdx.game.libgdx.BodyEditorLoader;

import java.util.HashMap;

import java.util.HashMap;

/**
 * Created by YAN on 08/02/2017.
 */

public class PlayerActor extends Actor {
    Body body;
    Sprite playerSprite;

    public PlayerActor(World world,String name, HashMap<String, Sprite> ship) {
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/ships/ships.json"));

        playerSprite  = ship.get(name);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 20 * playerSprite.getHeight();
        fixtureDef.restitution = 0.9f;
        fixtureDef.friction = 0.2f;

        body = world.createBody(bodyDef);
       // body.setUserData(playerSprite);
        body.setTransform(0, 0, 0);
        loader.attachFixture(body, name + ".png", fixtureDef, playerSprite.getWidth());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        playerSprite.setRotation((float) Math.toDegrees(body.getAngle()));
        playerSprite.setOrigin(0, 0);
        //playerSprite.setSize(playerSprite.getWidth(), playerSprite.getHeight());
        playerSprite.setPosition(body.getPosition().x + Gdx.graphics.getWidth() * 0.5f, body.getPosition().y + Gdx.graphics.getHeight() * 0.5f);
        playerSprite.setScale(10);
        playerSprite.draw(batch);

    }

}