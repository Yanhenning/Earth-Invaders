package com.mygdx.game.libgdx;

/**
 * Created by YAN on 21/02/2017.
 */

public interface PlayServices {
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(String achievementId);
    public boolean submitScore(int highScore);
    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();
    public void toast(final String text);
    public void saveGame(String snapName, byte[] data, String desc);
    public void loadGame();
    boolean checkNetworkStatus();
}
