package com.mygdx.game.libgdx.Scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

/**
 * Created by YAN on 12/02/2017.
 */

public class GameOverDialog extends Dialog{

    public GameOverDialog(String title, Skin skin) {
        super(title, skin);

        Label label = new Label("PAUSED", skin);
        label.setWrap(true);
        label.setFontScale(.8f);
        label.setAlignment(Align.center);


        new Dialog("", skin, "dialog"){
            protected void result(Object object){
                System.out.print("Chosen: " + object);
            }

        };
        padTop(50).padBottom(50);
        getContentTable().add(label).width(850).row();

        TextButton resumeButton = new TextButton("RESUME", skin, "dialog");
        button(resumeButton, false);

        TextButton quitButton = new TextButton("QUIT", skin, "dialog");
        button(quitButton, true);

        key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false);
        invalidateHierarchy();
        invalidate();
        layout();


    }
}
