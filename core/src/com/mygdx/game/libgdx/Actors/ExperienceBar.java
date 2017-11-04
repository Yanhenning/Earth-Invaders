package com.mygdx.game.libgdx.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.reflectasm.PublicConstructorAccess;
import com.mygdx.game.libgdx.Screens.MyGdxGame;


/**
 * Created by YAN on 26/10/2017.
 */

public class ExperienceBar {
        ProgressBar bar;

    public ExperienceBar(MyGdxGame game, Skin skin) {
        float minExp = game.playerExperience.get(game.playerSave.playerLevel-1);
        float maxExp = game.playerExperience.get(game.playerSave.playerLevel);
        float currentExp = game.playerSave.playerExpirience;
        skin = new Skin();
        Pixmap pixmap = new Pixmap(10, 15, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        TextureRegionDrawable textureBar = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/ui2/barHorizontal_white_mid.png"))));
        textureBar.setMinHeight(Gdx.graphics.getHeight() * 0.08f);
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.WHITE), textureBar);
        barStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/ui2/barHorizontal_blue_mid.png"))));

        barStyle.knobBefore = barStyle.knob;
        barStyle.knob.setMinHeight(Gdx.graphics.getHeight() * 0.05f);
        barStyle.knob.setMinWidth(0);
        bar = new ProgressBar(minExp, maxExp, 1f, false, barStyle);
        bar.setSize(100, Gdx.graphics.getHeight() * 0.05f);
        bar.setValue(currentExp);
        bar.setStepSize(1);
        bar.setAnimateDuration(2);
        bar.getStyle().knob.setMinWidth(0);
    }
    public ProgressBar getExpBar(){
        return bar;
    }
}