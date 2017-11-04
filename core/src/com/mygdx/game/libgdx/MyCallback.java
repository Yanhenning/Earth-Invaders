package com.mygdx.game.libgdx;

/**
 * Created by YAN on 10/08/2017.
 */

public interface MyCallback {
    public int finishedLoading(int count);
            byte[] getGameData();
    boolean gameIsLoaded();

    }

