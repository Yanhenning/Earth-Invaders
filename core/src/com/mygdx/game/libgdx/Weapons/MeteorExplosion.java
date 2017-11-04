package com.mygdx.game.libgdx.Weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 * Created by YAN on 24/02/2017.
 */

public class MeteorExplosion {
    public static final float FRAME_DURATION = 0.08f;
    private static final float SCALE = 2;
    public float x, y, stateTime, width, height, angle, vY, vX;

    static  Animation anim = null;
    static TextureAtlas atlas;
    public boolean remove = false;
    static com.mygdx.game.libgdx.AnimatedSprite animatedSprite;

    public MeteorExplosion(float x, float y, float width, float height, float angle, float vY, float vX) {

        this.width = width;
        this.height = height;
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.vX = vX * 0.02f;
        this.vY = vY * 0.02f;
        atlas = MyGdxGame.manager.get("data/effects/explosion/explosionAtlas.atlas");
        anim = new Animation(FRAME_DURATION, atlas.getRegions());
        animatedSprite = new com.mygdx.game.libgdx.AnimatedSprite(anim);
        animatedSprite.setSize(width * 0.8f, height * 0.8f);
        animatedSprite.setPosition(x, y);
        animatedSprite.setOrigin(0, 0);

    }

    public void update(float dt){
        stateTime += dt;
        if(anim.isAnimationFinished(stateTime)){
            remove = true;
        }
    }

    public void render(SpriteBatch batch){

        //Sprite sprite = new Sprite((TextureRegion) anim.getKeyFrame(stateTime));
        animatedSprite.setPosition(animatedSprite.getX() + vX, animatedSprite.getY() + vY);
        animatedSprite.setRotation((float) Math.toDegrees(angle));
        animatedSprite.setSize(animatedSprite.getWidth() * 1.01f, animatedSprite.getHeight() * 1.01f);
        animatedSprite.draw(batch);

    }
}
