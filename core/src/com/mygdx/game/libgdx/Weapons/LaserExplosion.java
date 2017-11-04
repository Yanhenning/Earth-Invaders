package com.mygdx.game.libgdx.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

/**
 * Created by YAN on 23/02/2017.
 */

public class LaserExplosion {

    public static final float FRAME_DURATION = 0.1f;
    public static final float SCALE = 1.043f;
    public static final float SIZE_H = 3;
    public static final float SIZE_W = SIZE_H * SCALE;


    static TextureAtlas atlas;
    static Animation anim = null;
    float x, y, stateTime;

    public boolean remove = false;

    public LaserExplosion(float x, float y) {
        this.x = x;
        this.y = y;
        atlas = MyGdxGame.manager.get("data/lasers/blueLaserExplosion1/explosions.atlas");

        if (anim == null){
            anim = new Animation(FRAME_DURATION, atlas.findRegions("laserBlue"));
        }
    }


    public void update(float dt){
        stateTime += dt;
        if(anim.isAnimationFinished(stateTime)){
            remove = true;
            Gdx.app.log("Explosão destruida", "EXPLODIU");
        }
    }
    public void render (SpriteBatch batch){
        batch.draw((TextureRegion) anim.getKeyFrame(stateTime), x, y, SIZE_W, SIZE_H);
    }

}
