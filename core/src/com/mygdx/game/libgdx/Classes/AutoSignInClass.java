package com.mygdx.game.libgdx.Classes;

import java.io.Serializable;

/**
 * Created by YAN on 19/08/2017.
 */

public class AutoSignInClass implements Serializable {
    boolean auto = false;
    public AutoSignInClass(){
        this.auto = false;
    }

    public boolean isAutoSignInEnabled(){
        return auto;
    }
    public boolean setTrue(){
        auto = true;
        return true;
    }
    public boolean setFalse(){
        auto = false;
        return true;
    }
}
