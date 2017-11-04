package com.mygdx.game.libgdx.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.mygdx.game.libgdx.Screens.MyGdxGame.manager;

/**
 * Created by YAN on 08/02/2017.
 */

public class InfiniteScrollBg extends Actor {
    String bgName;
    Sprite backGround;
    TextureAtlas bgRegion;

    //TODO POP THE BLACK COLOR?
    String[] bgNames = new String[] { "blue", "darkPurple", "purple"};


    public InfiniteScrollBg(float width, float height, String name) {
        bgName = name;
        bgRegion = manager.get("data/backgrounds/extended/background-atlas/background.atlas");
        backGround = new Sprite(bgRegion.findRegion(bgName));
        setWidth(width);
        setHeight(height);
        setPosition(0, height);
        addAction(forever(sequence(moveTo(0, 0, 1f), moveTo(0, height))));
    }
    public InfiniteScrollBg(float width, float height) {
        bgRegion = manager.get("data/backgrounds/extended/background-atlas/background.atlas");

        Random random = new Random();
        bgName = bgNames[random.nextInt(3)];
        backGround = new Sprite(bgRegion.findRegion(bgName));
        setWidth(width);
        setHeight(height);
        setPosition(0, height);
        addAction(forever(sequence(moveTo(0, 0, 1f), moveTo(0, height))));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

       // if(manager.isLoaded(bgName))
            batch.draw(backGround ,getX(), getY() - getHeight(), getWidth(), getHeight() * 2);
            //batch.draw((Texture) manager.get(bgName), getX(), getY() - getHeight(), getWidth(), getHeight() * 2);
    }


}
