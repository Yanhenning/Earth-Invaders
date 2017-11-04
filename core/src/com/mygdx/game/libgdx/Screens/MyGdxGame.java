package com.mygdx.game.libgdx.Screens;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.libgdx.*;
import com.mygdx.game.libgdx.Classes.*;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.libgdx.MyCallback;
import com.mygdx.game.libgdx.Scene.PlayerSave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class MyGdxGame extends Game{

        public boolean networkConnectionBeforeStart;

        public boolean networkConnectionAfterStart;

        public boolean autoLoad = true;

        private String NAME_GAME_SAVED = "mySave6";
        private static final String AUTO_SIGN_IN = "autosignin.bin";
        public byte[] savedGameData = null;

        public boolean NEW_GAME = true;

        private final static String buttonShape = "button_05";

        // filename to use
        public static final String fileName = "newPlayerSave13.bin";

        //Shape of menu buttons
        public static final String BUTTON_SHAPE = "button_05";

        //Camera menu dimensions
        public static final float CAMERA_WIDHT = 50;
        public static final float CAMERA_HEIGHT = 50;

        //Virtual dimensions
        public static final int VIRTUAL_WIDTH = 1280;
        public static final int VIRTUAL_HEIGHT = 720;

        //Buttons relative screen multiplier
        public static final float BUTTON_WIDHT_MULT = 0.35f;
        public static final float BUTTON_HEIGHT_MULT = 0.1f;
        //HUD multiplier
        public static final float TITLE_WIDHT_MULT = 0.7f;
        public static final float TITLE_HEIGHT_MULT = 0.18f;
        //debug all tables
        public static final boolean DEBUG = true;
        //Controller buttons relative screen multiplier
        public static final float BUTTON_SIZE = 0.08f;


        //Box2D Collision Bits
        public static final short NOTHING_BIT = 0;
        public static final short SHIP_BIT = 1;
        public static final short METEOR_BIT = 2;
        public static final short LASER_BIT = 4;
        public static final short COIN_BIT = 8;
        public static final short DESTROYED_BIT = 16;
        public static final short OBJECT_BIT = 32;
        public static final short ENEMY_BIT = 64;
        public static final short ENEMY_HEAD_BIT = 128;
        public static final short ITEM_BIT = 256;
        public static final short MARIO_HEAD_BIT = 512;
        public static final short FIREBALL_BIT = 1024;

        public boolean MUSIC = true;
        public boolean SOUND_EFFECTS = true;


        //GooglePlay services
        MyCallback myCallback;
        PlayServices playServices;
        public PlayerSave playerSave;
        public PlayerSave localSave = null;
        public PlayerSave cloudSave = null;



    public static final float SCALE = 0.06f;
    public static final String SKIN = "data/skin/ui-yellow.atlas";

    public static AssetManager manager;
    public SpriteBatch batch;

    public BitmapFont font;
    public BitmapFont tinyFont;
    public BitmapFont extraTinyFont;
    public BitmapFont title;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public FreeTypeFontGenerator generator;

    public Skin skin;

    public TextureAtlas shipsRegion;
    public TextureAtlas meteorsRegion;
    public TextureAtlas effectsRegion;
    public TextureAtlas medalRegion;
    public TextureAtlas ui2Region;
    public final HashMap<String, Sprite> meteorSprites = new HashMap<String, Sprite>();
    public final static HashMap<String, Sprite> shipSprites = new HashMap<String, Sprite>();
    public final HashMap<String, Drawable> shipDrawable = new HashMap<String, Drawable>();
    public final HashMap<String, Drawable> medalDrawable = new HashMap<>();
    public final HashMap<String, Sprite> medalSprites = new HashMap<>();

    public final Array<Label> shipStats = new Array<Label>();

    public final HashMap<String, Float> shipAttack = new HashMap<String, Float>();
    public final HashMap<String, Float> shipDefense = new HashMap<String, Float>();
    public final HashMap<String, Float> shipDensity = new HashMap<String, Float>();
    public final ArrayList<Float> playerExperience = new ArrayList<>();

    public String[] playerShips = new String[] {"playerShip1_red", "playerShip2_red", "playerShip3_red",
            "playerShip1_blue", "playerShip2_blue", "playerShip3_blue",
            "playerShip1_green", "playerShip2_green", "playerShip3_green",
            "playerShip1_orange", "playerShip2_orange", "playerShip3_orange",
            "playerShip1_black", "playerShip2_black", "playerShip3_black", "playerShip4_black", "playerShip5_black",
            "playerShip1_darkblue", "playerShip2_darkblue", "playerShip3_darkblue", "playerShip4_darkblue", "playerShip5_darkblue",
            "playerShip1_darkgreen", "playerShip2_darkgreen", "playerShip3_darkgreen", "playerShip4_darkgreen", "playerShip5_darkgreen",
            "playerShip1_darkorange",  "playerShip2_darkorange", "playerShip3_darkorange", "playerShip4_darkorange", "playerShip5_darkorange",
            "ufoBlue", "ufoGreen", "ufoRed", "ufoYellow",
            "playerspaceShips1", "playerspaceShips2", "playerspaceShips3","playerspaceShips4", "playerspaceShips5",
            "playerspaceShips6", "playerspaceShips7", "playerspaceShips8", "playerspaceShips9", ""};

    public String[] playerMedals = new String[] {"flat_medal2", "flat_medal4", "flat_medal6", "flat_medal8",
                "flat_medal3", "flat_medal5", "flat_medal7", "flat_medal9", "flat_medal1",
                "shaded_medal2", "shaded_medal4", "shaded_medal6", "shaded_medal8",
                "shaded_medal3", "shaded_medal5", "shaded_medal7", "shaded_medal9", "shaded_medal1"};

    Float[] playerExperiences = new Float[] {0f, 1000f,2000f,3000f,4000f,5000f,6000f,8500f,11000f,13500f,16000f
            ,18500f,22500f,26500f,30500f,34500f,38500f,44500f,50500f,56500f,62500f
            ,68500f,76500f,84500f,92500f,100500f,108500f,119500f,130500f,141500f
            ,152500f,163500f,176500f,189500f,202500f,215500f,228500f,244500f,260500f,276500f
            ,292500f,308500f,324500f,340500f,356500f,372500f,394500f,420500f,446500f,472500f
            ,498500f,524500f,555500f,587500f,620500f,654500f,689500f,725500f,762500f,800500f
            ,840500f,882500f,926500f,974500f,1024500f,1076500f,1130500f,1188500f,1250500f,1316500f
            ,1386500f,1460500f,1540500f,1626500f,1718500f,1816500f,1926500f,2046500f,2176500f,2316500f
            ,2466500f,2626500f,2796500f,2976500f,3166500f,3366500f,3586500f,3826500f,4106500f,4456500f,5000000f};

    Float[] shipsAtk = new Float[] {1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,10f,22f};
    Float[] shipsDef = new Float[] {1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,1f};
    Float[] shipsDensity = new Float[] {1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,
            1f,2f,3f,1f};
    public File file;
    public File signInFile;
    public AutoSignInClass autoSignInClass = null;

        //ShaderProgram
        public ShaderProgram shaderAsset;

        //Sounds
        Sound clickSound;
        Sound explosionSound;
        Sound loseGameSound;
    private MyCallback loadScreen;

    public MyGdxGame(PlayServices playServices) {
        this.playServices = playServices;
        this.myCallback = myCallback;
        }

        public MyGdxGame() {
            playServices = null;
        }

    @Override
        public void create() {
        System.out.println("Conectando MYGDXGAME");

            //
            batch = new SpriteBatch();
            skin = new Skin();
            loadAssets();

            clickSound = manager.get("data/sounds/click3.wav");
            explosionSound = manager.get("data/sounds/explosion.mp3");
            loseGameSound = manager.get("data/sounds/sfx_lose.ogg");
            skin.addRegions(manager.get("data/skin/ui-yellow.atlas", TextureAtlas.class));
            shipsRegion = manager.get("data/ships/ships.atlas");
            meteorsRegion = manager.get("data/meteors/meteors.atlas");
            effectsRegion =  manager.get("data/effects/effects.atlas");
            shaderAsset = new ShaderProgram(Gdx.files.internal("data/ui/grey.vsh"), Gdx.files.internal("data/ui/grey.fsh"));
            ui2Region = manager.get("data/ui2/ui2.atlas");
            createExperienceArray();
            addAllSprites();

            generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font/kenvector_future_thin.ttf"));
            parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = Math.round((Gdx.graphics.getHeight() * 0.15f)); //42

            title = (generator.generateFont(parameter));
            parameter.size = (int) (Gdx.graphics.getHeight() * 0.07f);

            font = generator.generateFont(parameter);

            parameter.size  = (int) (Gdx.graphics.getHeight() * 0.04f);
            tinyFont = generator.generateFont(parameter);

            parameter.size  = (int) (Gdx.graphics.getHeight() * 0.025f);
            extraTinyFont = generator.generateFont(parameter);


            file = new File(Gdx.files.getLocalStoragePath () + "/" + fileName);
            signInFile = new File(Gdx.files.getLocalStoragePath () + "/" + AUTO_SIGN_IN);

        createShipAttributes();
        //loadPlayerSave();
        loadLocalSave();
        createAutoSignInFile();
        loadAutoSignIn();
        //verify is has network connection before game starts
        //networkConnectionBeforeStart = playServices.checkNetworkStatus();

        if (playServices != null && autoSignInClass != null){
            if (playServices.isSignedIn() && autoSignInClass.isAutoSignInEnabled()){
                signIn();
            }
        }
        this.setScreen(new com.mygdx.game.libgdx.Screens.LoadingScreen(this));
        }


    @Override
        public void render() {
            super.render();
        }

        public void dispose() {
            title.dispose();
            shipsRegion.dispose();
            meteorsRegion.dispose();
            effectsRegion.dispose();
            batch.dispose();
            font.dispose();
            tinyFont.dispose();
            extraTinyFont.dispose();
            manager.dispose();
            generator.dispose();
            clickSound.dispose();
            explosionSound.dispose();
            medalRegion.dispose();

        }

        private void addMeteorSprites(){
            Array<TextureAtlas.AtlasRegion> regions = meteorsRegion.getRegions();
            for (TextureAtlas.AtlasRegion region: regions) {
                Sprite sprite = meteorsRegion.createSprite(region.name);

                float width = sprite.getWidth() * SCALE;
                float height = sprite.getHeight() * SCALE;
                sprite.setSize(width, height);

                meteorSprites.put(region.name, sprite);
            }
        }

    private void addMedalSprites(){

        medalRegion = manager.get("data/medals/medals.atlas", TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = medalRegion.getRegions();
        for (TextureAtlas.AtlasRegion region: regions) {
            Sprite sprite = medalRegion.createSprite(region.name);
            Drawable drawable = new TextureRegionDrawable(medalRegion.createSprite(region.name));
            float width = sprite.getWidth() * SCALE * 1.2f;
            float height = sprite.getHeight() * SCALE * 1.2f;
            sprite.setSize(width, height);
            medalSprites.put(region.name, sprite);
            medalDrawable.put(region.name, drawable);
        }
    }

        private void addShipSprites(){
            Array<TextureAtlas.AtlasRegion> regions = shipsRegion.getRegions();
            for (TextureAtlas.AtlasRegion region: regions) {
                Sprite sprite = shipsRegion.createSprite(region.name);
                Drawable drawable = new TextureRegionDrawable(shipsRegion.createSprite(region.name));
                float width = sprite.getWidth() * SCALE;
                float height = sprite.getHeight() * SCALE;
                sprite.setSize(width, height);
                shipDrawable.put(region.name, drawable);
                shipSprites.put(region.name, sprite);
            }
        }

    private void loadAssets(){
        manager = new AssetManager();
        //TextureAtlas
        manager.load("data/ships/ships.atlas", TextureAtlas.class);
        manager.load("data/meteors/meteors.atlas", TextureAtlas.class);
        manager.load("data/backgrounds/extended/background-atlas/background.atlas", TextureAtlas.class);
        manager.load("data/skin/ui-yellow.atlas", TextureAtlas.class);
        manager.load("data/effects/effects.atlas", TextureAtlas.class);
        manager.load("data/lasers/lasers.atlas", TextureAtlas.class);
        manager.load("data/lasers/blueLaserExplosion1/explosions.atlas", TextureAtlas.class);
        manager.load("data/effects/explosion/explosionAtlas.atlas", TextureAtlas.class);
        manager.load("data/medals/medals.atlas", TextureAtlas.class);
        manager.load("data/ui2/ui2.atlas", TextureAtlas.class);
        //Texture
        manager.load("data/controller/transparentDark34.png", Texture.class);
        manager.load("data/controller/transparentDark35.png", Texture.class);
        manager.load("data/controller/transparentDark37.png", Texture.class);
        manager.load("data/controller/transparentDark49.png", Texture.class);
        manager.load("data/controller/transparentDark09.png", Texture.class);
        //Sounds
        manager.load("data/sounds/click3.wav", Sound.class);
        manager.load("data/sounds/sfx_laser1.ogg", Sound.class);
        manager.load("data/sounds/explosion.mp3", Sound.class);
        manager.load("data/sounds/sfx_lose.ogg", Sound.class);
        //Shader
/*        manager.load("data/ui/grey.fsh", FileHandle.class);
        manager.load("data/ui/grey.vsh", FileHandle.class);*/


        manager.finishLoading();
    }

    private void loadPlayerSave(){
        // initialize kryo
        Kryo kryo = new Kryo();

        // create file in dedicated local storagex
        if(!file.exists()){

            playerSave = new PlayerSave("mysave");
            playerSave.setPlayerCurrentShip("playerShip1_red");
            playerSave.setSoundEffectsON(true);
            playerSave.setPlayerPoints( 0);
            playerSave.setSoundMusicON(true);
            playerSave.controller = false;
            playerSave.highestScore = 0;
            playerSave.firstPlay = true;
            playerSave.autoSignIn = true;
            playerSave.stardust = 0;
            playerSave.stardust2 = 0;
            playerSave.playerExpirience = 0;


            try {
                Output output = new Output(new FileOutputStream(file));
                // serialize object to file
                kryo.writeObject(output, playerSave);
                output.close();
                System.out.println("ok kyro1");
            }catch (Exception e){
                e.printStackTrace();
            }
        }


//Gdx.files.getLocalStoragePath () + "/" + fileName{

        }

    public void savePlayerFile(){
        try {
            Kryo kryo = new Kryo();
            Output output = new Output(new FileOutputStream(file));
            kryo.writeObject(output, playerSave);
            output.close();
            System.out.println("Dados salvos");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void playExplosion(){
        if (playerSave.isSoundEffectsON())
            explosionSound.play(0.5f);
    }
    public void playClick(){
        if (playerSave.isSoundEffectsON())
            clickSound.play(0.5f);
    }

    public void playLoseGameSound(){
        if (playerSave.isSoundEffectsON())
            loseGameSound.play(1f);
    }


    public void createShipAttributes(){
        for (int i = 0; i < playerShips.length -1 ; i++) {
            shipAttack.put(playerShips[i], shipsAtk[i]);
            shipDefense.put(playerShips[i], shipsDef[i]);
            shipDensity.put(playerShips[i], shipsDensity[i]);
            Label txt = new Label(String.format("%.0f", shipsAtk[i]) + " | " + String.format("%.0f",shipsDef[i]) + " | " + String.format("%.0f", shipsDensity[i]),
                    new Label.LabelStyle(extraTinyFont, Color.WHITE));
            shipStats.add(txt);
        }
    }

    private void addAllSprites() {
        addMeteorSprites();
        addShipSprites();
        addMedalSprites();
    }

    public void loadFromCloud(){
        playServices.loadGame();
    }


    public boolean isSignedIn(){
        if (playServices.isSignedIn()){
            System.out.println("signed ready to use PLAYSERVICES");
            return true;
        }
        else
            return false;
    }
    //always use this method to signIn
    public void signIn(){
        if (!isSignedIn()){
            playServices.signIn();
            if (autoSignInClass != null) {
                if(autoSignInClass.setTrue())saveAutoSignIn();
            }
        }
    }
    //always use this method to signout
    public void signOut(){
        if (isSignedIn()){
            playServices.signOut();
            if (autoSignInClass!=null){
                if (autoSignInClass.setFalse())saveAutoSignIn();

            }
        }

    }


    public void createFirstGameFile(){
        playerSave = new PlayerSave("mysave");
        playerSave.setPlayerCurrentShip("playerShip1_red");
        playerSave.setSoundEffectsON(true);
        playerSave.setPlayerPoints( 0);
        playerSave.setSoundMusicON(true);
        playerSave.controller = false;
        playerSave.highestScore = 0;
        playerSave.firstPlay = true;
        playerSave.autoSignIn = true;
        playerSave.saveDate = new Date(System.currentTimeMillis());
        final String[] playerName = {""};
        com.badlogic.gdx.Input.TextInputListener listener = new com.badlogic.gdx.Input.TextInputListener() {
            @Override
            public void input(String text) {
                playerSave.playerName = text;
            }

            @Override
            public void canceled() {

            }
        };
        //get text from simple text dialog
        Gdx.input.getTextInput(listener, "WRITE YOUR NAME", "", "Name");


        Random random = new Random();
        int r1;
        r1 = random.nextInt(5000);
        String playerId = playerName[0] + Integer.toString(r1);
        playerSave.playerId = playerId;
        Log.debug("createFirstGameFile", "Game created");
        saveToCloud();
        /*
        MySerializer serializer = new MySerializer();
        byte[] data = null;
        try {
            data = serializer.serialize(playerSave);
            //playServices.saveGame(fileName, data,"SAVE");
            System.out.println("SALVOU DADOS");
        } catch (IOException e) {
            e.printStackTrace();
        }
        playServices.saveGame(NAME_GAME_SAVED, data, "Your game save");
        savePlayerFile();
        System.out.println("loadGame" + ": CRIOU PRIMEIRO ARQUIVO ");*/
    }



    public void saveToCloud(){
        MySerializer serializer = new MySerializer();
        playerSave.saveDate = new Date(System.currentTimeMillis());
        byte[] data = null;
        try {
            data = serializer.serialize(playerSave);
            //playServices.saveGame(fileName, data,"SAVE");
            System.out.println("SALVOU DADOS");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isSignedIn() && playServices.checkNetworkStatus()){
            playServices.saveGame(NAME_GAME_SAVED, data, "Your game save");
            Log.debug("saveGame", "Cloud saved");
        }
        savePlayerFile();
        System.out.println("saveGame" + ": saveLocal and Cloud");
    }

    public void loadLocalSave(){
        Kryo kryo = new Kryo();
        if (file.exists()){
            try {
                Input input = new Input(new FileInputStream(file));
                // deserialize object from file, in this case LinkedList
                localSave = kryo.readObject(input, PlayerSave.class);
                input.close();
                Log.debug("loadSave", "loadLocalSave");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        //SOUND_EFFECTS = playerSave.isSoundEffectsON();
        //MUSIC = playerSave.isSoundMusicON();
    }

    public void snapToPlayerFile(){
        MySerializer serializer = new MySerializer();
        try {
            cloudSave = (PlayerSave) serializer.deserialize(savedGameData);
            //SOUND_EFFECTS = cloudSave.isSoundEffectsON();
            //MUSIC = cloudSave.isSoundMusicON();
            Log.debug("loadSave", "loadCloudSave");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int checkSaveVersionConflict(){
        if (cloudSave == null && localSave != null){
            playerSave = localSave;
            SOUND_EFFECTS = playerSave.isSoundEffectsON();
            MUSIC = playerSave.isSoundMusicON();
            return 0;
        }
        if (cloudSave != null && localSave == null){
            playerSave = cloudSave;
            SOUND_EFFECTS = playerSave.isSoundEffectsON();
            MUSIC = playerSave.isSoundMusicON();
            return 1;
        }
        if (cloudSave != null && localSave != null){
            Date cloudDate = cloudSave.saveDate;
            Date localDate = localSave.saveDate;
            if (localDate.after(cloudDate)){
                playerSave = localSave;
                SOUND_EFFECTS = playerSave.isSoundEffectsON();
                MUSIC = playerSave.isSoundMusicON();
                saveToCloud();
                return 2;
            }

            if (cloudDate.after(localDate)){
                playerSave = cloudSave;
                return 3;
            }
            if (cloudDate.equals(localDate)){
                playerSave = cloudSave;
                return 4;
            }

        }
            return 0;
    }

    public void createAutoSignInFile(){
        Kryo kryo = new Kryo();
        if(!file.exists()){
            autoSignInClass = new AutoSignInClass();
            try {
                Output output = new Output(new FileOutputStream(signInFile));
                // serialize object to file
                kryo.writeObject(output, autoSignInClass);
                output.close();
                System.out.println("ok kyro1");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void saveAutoSignIn(){
        Kryo kryo = new Kryo();
        try {
            Output output = new Output(new FileOutputStream(signInFile));
            kryo.writeObject(output, autoSignInClass);
            output.close();
            System.out.println("Dados salvos");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void loadAutoSignIn(){
        Kryo kryo = new Kryo();
        try {
            Input input = new Input(new FileInputStream(signInFile));
            // deserialize object from file, in this case LinkedList
            autoSignInClass = kryo.readObject(input, AutoSignInClass.class);
            input.close();
            System.out.println("ok autosignIn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /*
    "flat_medal2", "flat_medal4", "flat_medal6", "flat_medal8",
            "flat_medal3", "flat_medal5", "flat_medal7", "flat_medal9", "flat_medal1",
            "shaded_medal2", "shaded_medal4", "shaded_medal6", "shaded_medal8",
            "shaded_medal3", "shaded_medal5", "shaded_medal7", "shaded_medal9", "shaded_medal1"};*/

    public Drawable playerMedal(int level){
        String medalName = "flat_medal2";
        if (level <=5) medalName = "flat_medal2";
        if (level <= 10 && level > 5) medalName = "flat_medal4";
        if (level <= 15 && level > 10) medalName = "flat_medal6";
        if (level <= 20 && level > 15) medalName = "flat_medal8";
        if (level <= 25 && level > 20) medalName = "flat_medal3";
        if (level <= 30 && level > 25) medalName = "flat_medal5";
        if (level <= 35 && level > 30) medalName = "flat_medal7";
        if (level <= 40 && level > 35) medalName = "flat_medal9";
        if (level <= 45 && level > 40) medalName = "flat_medal1";
        if (level <= 50 && level > 45) medalName = "shaded_medal2";
        if (level <= 55 && level > 50) medalName = "shaded_medal4";
        if (level <= 60 && level > 55) medalName = "shaded_medal6";
        if (level <= 65 && level > 60) medalName = "shaded_medal8";
        if (level <= 70 && level > 65) medalName = "shaded_medal3";
        if (level <= 75 && level > 70) medalName = "shaded_medal5";
        if (level <= 80 && level > 75) medalName = "shaded_medal7";
        if (level <= 85 && level > 80) medalName = "shaded_medal9";
        if (level <= 90 && level > 85) medalName = "shaded_medal1";

        return medalDrawable.get(medalName);
    }

    public void createExperienceArray(){
        for (Float exp: playerExperiences) {
            playerExperience.add(exp);
        }
    }

}
