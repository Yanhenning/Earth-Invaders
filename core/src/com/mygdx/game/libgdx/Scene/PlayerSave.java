package com.mygdx.game.libgdx.Scene;

import com.mygdx.game.libgdx.Actors.Player;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by YAN on 23/02/2017.
 */

public class PlayerSave implements Serializable{

    long playerPoints;
    String playerCurrentShip;
    String saveName;
    boolean soundEffectsON;
    boolean soundMusicON;
    public boolean controller = true;
    public boolean firstPlay = true;
    public boolean autoSignIn = false;
    public long numberOfMissedShoots = 0;
    public long numberOfMeteorsDestroyed = 0;
    public long highestScore = 0;
    public long numberOfPlays = 0;
    public long numberOfDeaths = 0;
    public long stardust = 0;
    public long stardust2 = 0;
    public int numberOfCompletedStages = 0;
    public int numberOfDefeteadBosses = 0;
    public int numberOfMultiplayerMatchesWon = 0;
    public int numberOfShipsUnlocked = 0;
    public float playerExpirience = 0;
    public int playerLevel = 1;
    public String playerName;
    public String playerId;
    public float accX = 6;
    public Date saveDate;
    public String weapon1;
    public String weapon2;
    public String weapon3;
    public String defense1;
    public String defense2;
    public int weaponUpgrade1 = 1;
    public int weaponUpgrade2 = 1;
    public int weaponUpgrade3 = 1;
    public int defenseUpgrade1 = 1;
    public int defenseUpgrade2 = 1;
    public long stage;
    public HashMap<String, Boolean> playerOwnShips;

    String[] playerShips = new String[] {"playerShip1_red","playerShip1_blue","playerShip1_green",
            "playerShip1_orange","playerShip1_black","playerShip1_darkblue",
            "playerShip1_darkgreen","playerShip1_darkorange","ufoBlue",
            "playerspaceShips1","playerShip2_red","playerShip2_orange",
            "playerShip2_blue","playerShip2_green","playerShip2_darkorange",
            "ufoGreen","ufoRed","ufoYellow",
            "playerShip2_black","playerShip2_darkblue","playerShip2_darkgreen",
            "playerspaceShips2","playerShip3_red","playerShip3_blue",
            "playerShip3_green","playerShip3_orange","playerShip3_black",
            "playerShip3_darkblue","playerShip3_darkgreen","playerShip3_darkorange",
            "playerShip4_black","playerShip4_darkblue","playerShip4_darkgreen",
            "playerShip4_darkorange","playerShip5_black","playerShip5_darkblue",
            "playerShip5_darkgreen","playerShip5_darkorange","playerspaceShips3",
            "playerspaceShips4","playerspaceShips5","playerspaceShips6",
            "playerspaceShips7","playerspaceShips8","playerspaceShips9",
            ""};
    String[] initialShips = new String[] {"playerShip1_red",  "playerShip1_blue", "playerShip1_green", ""};

    public PlayerSave(String saveName) {
        playerOwnShips = new HashMap<String, Boolean>();
        for (int i = 0; i < playerShips.length -1; i++){
            for (int j = 0; j < initialShips.length; j++){
                if (playerShips[i] == initialShips[j]) playerOwnShips.put(playerShips[i], true);
            }
            if (!playerOwnShips.containsKey(playerShips[i])) playerOwnShips.put(playerShips[i], false);
            /*
            if (playerShips[i]==initialShips[0]||playerShips[i]==initialShips[1]||playerShips[i]==initialShips[2]){
                playerOwnShips.put(playerShips[i], true);
            } else {
                playerOwnShips.put(playerShips[i], false);
            }*/
        }
        this.saveName = saveName;
        this.numberOfDeaths = 0;
        this.firstPlay = true;
        this.numberOfCompletedStages = 0;
        this.numberOfDefeteadBosses = 0;
        this.numberOfMeteorsDestroyed = 0;
        this.numberOfMissedShoots = 0;
        this.numberOfMultiplayerMatchesWon = 0;
        this.numberOfPlays = 0;
        this.numberOfShipsUnlocked = 0;
        this.numberOfMissedShoots = 0;
        this.playerLevel = 1;
        this.weapon1 = "basicLaser";
        this.weapon2 = "";
        this.weapon3 = "";
        this.defense1 = "";
        this.defense2 = "";
        this.stage = 1;
    }

    public PlayerSave( byte  b, boolean b1) {

    }

    public PlayerSave(){

    }
    public long getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(long playerPoints) {
        this.playerPoints = playerPoints;
    }

    public String getPlayerCurrentShip() {
        return playerCurrentShip;
    }

    public void setPlayerCurrentShip(String playerCurrentShip) {
        this.playerCurrentShip = playerCurrentShip;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public boolean isSoundEffectsON() {
        return soundEffectsON;
    }

    public void setSoundEffectsON(boolean soundEffectsON) {
        this.soundEffectsON = soundEffectsON;
    }

    public boolean isSoundMusicON() {
        return soundMusicON;
    }

    public void setSoundMusicON(boolean soundMusicON) {
        this.soundMusicON = soundMusicON;
    }

    public boolean playerHasShip(String ship){
        return playerOwnShips.get(ship);
    }

}
