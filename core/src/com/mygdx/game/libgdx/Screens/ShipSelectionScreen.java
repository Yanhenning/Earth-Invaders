package com.mygdx.game.libgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;

import java.util.HashMap;

/**
 * Created by YAN on 10/02/2017.
 */

//TODO add locked ships and unlock them

public class ShipSelectionScreen implements Screen {
    final MyGdxGame game;
    final Screen parent;
    private Viewport viewport;
    OrthographicCamera camera;

    Stage stage;
    Table table;
    ScrollPane scrollPane;
    List<String> list;
    Button backButton;
    Button selectButton;
    ImageButton[] imageButtonList;
    String ship;
    Skin skinList;
    Label hangarLabel;
    Skin skin;
    Array<Label> shipsLabel;

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

    String [] playerRealNameShips = new String[] {"Red Small Ship", "Red Medium Ship", "Red Large Ship",
            "Blue Small Ship", "Blue Medium Ship", "Blue Large Ship",
            "Green Small Ship", "Green Medium Ship", "Green Large Ship",
            "Orange Small Ship", "Orange Medium Ship", "Orange Large Ship",
            "Black Small Ship", "Black Medium Ship", "Black Large Ship", "Black Special Ship ", "Extra Black Ship",
            "DarkBlue Small Ship", "Darkblue Medium Ship", "Darkblue Large Ship", "Darkblue Special Ship ", "Extra Darkblue Ship",
            "Darkgreen Small Ship", "Darkgreen Medium Ship", "Darkgreen Large Ship", "Darkgreen Special Ship ", "Extra Darkgreen Ship",
            "Darkorange Small Ship", "Darkorange Medium Ship", "Darkorange Large Ship", "Darkorange Special Ship ", "Extra Darkorange Ship",
            "UFO Blue", "UFO Green", "UFO Red", "UFO Yellow",
            "Boss ship 1", "Boss ship 2", "Boss ship 3", "Boss ship 4", "Boss ship 5",
            "Boss ship 6", "Boss ship 7", "Boss ship 8", "Boss ship 9", ""};

    final HashMap<String, String> playerMap = new HashMap<String, String>();

    public ShipSelectionScreen(final MyGdxGame game, final Screen parent){
        this.game = game;
        this.parent = parent;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        viewport = new FitViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        stage = new Stage();
        shipsLabel = new Array<Label>();

        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        hangarLabel = new Label("HANGAR", new Label.LabelStyle(game.title, Color.WHITE));

        Table hangarTitle = new Table();

        hangarTitle.add(hangarLabel).expand().center().top();
        hangarTitle.setPosition(Gdx.graphics.getWidth()* 0.5f, Gdx.graphics.getHeight());
        stage.addActor(hangarTitle);


        if (game.playerSave.getPlayerCurrentShip()!=null){
            ship = game.playerSave.getPlayerCurrentShip();
        }
        else {
            ship = "playerShip1_red";
        }

        createHashShipMap();


        //Add all buttons
        addButtons();
        createTable();
        //stage.addActor(table);

        //imageList();

        stage.addActor(selectButton);
        stage.addActor(backButton);
        //Last line code on stage

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.begin();

        game.batch.end();
        //table.clearChildren();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        skinList.dispose();
    }

    private void addButtons(){

        Skin buttonSkin = new Skin();
        buttonSkin.addRegions(game.manager.get("data/skin/ui-yellow.atlas", TextureAtlas.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = buttonSkin.newDrawable("icon_back");
        style.down = buttonSkin.newDrawable("icon_back");

        backButton = new Button(style);
        backButton.setPosition(0, 0);
        backButton.setSize(Gdx.graphics.getHeight() * 0.1f, Gdx.graphics.getHeight() * 0.1f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();
                game.setScreen(new com.mygdx.game.libgdx.Screens.MainMenuScreen(game));
            }
        });

        Button.ButtonStyle style_check = new Button.ButtonStyle();
        style_check.up = buttonSkin.newDrawable("icon_check");
        style_check.down = buttonSkin.newDrawable("icon_check");

        selectButton = new Button(style_check);
        selectButton.setSize(Gdx.graphics.getHeight() * 0.1f, Gdx.graphics.getHeight() * 0.1f);
        selectButton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getHeight() * 0.1f, 0);
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.playerPref.putString("playerShip", ship);
                //game.playerPref.flush();
                game.playClick();
                game.playerSave.setPlayerCurrentShip(ship);
                game.setScreen(new com.mygdx.game.libgdx.Screens.MainMenuScreen(game));
            }
        });

    }


/*    private void imageList(){
        imageButtonList = new ImageButton[game.shipDrawable.size()];
        for (int i = 0; i < game.shipDrawable.size(); i++) {
            ImageButton button = new ImageButton(game.shipDrawable.get(i));
            imageButtonList[i] = button;
        }


        skinList = new Skin(Gdx.files.internal("data/ui/level-plane-ui.json"));

        list = new List<String>(skinList);

        list.setItems(playerShips);
        scrollPane = new ScrollPane(list);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth()* 0.12f, Gdx.graphics.getHeight() * 0.29f);
        //scrollPane.setScale(3);
        scrollPane.setPosition(0, 100);
        scrollPane.setTransform(true);
        scrollPane.setSmoothScrolling(true);

        scrollPane.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ship = list.getSelected();
            }
        });
        stage.addActor(scrollPane);
    }*/


    //create the scrollable table
    private void createTable(){
        skin = new Skin(Gdx.files.internal("data/ui/level-plane-ui.json"));
        table = new Table(skin);
//create the table
        table.setDebug(MyGdxGame.DEBUG);
        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight() * 0.8f);
        table.setOrigin(0, 0);
        table.setPosition(0, 0);
//3 columns for a row
        for (int i = 0; i < playerShips.length - 1; i+=3){
            //add ship's names
            for (int j = i; j < i + 3; j++) {
                Label txt = new Label(playerMap.get(playerShips[j]), new Label.LabelStyle(game.tinyFont, Color.WHITE));
                txt.setName(playerMap.get(playerShips[j]));
                table.add(txt).height(txt.getHeight()*1f).width(txt.getWidth()* 1f);

               /* if (!game.playerSave.playerHasShip(playerShips[j])){
                    txt.setColor(Color.GRAY);
                }*/
                shipsLabel.add(txt);
            }
            table.row();
            //add image ship's buttons
            for (int j = i; j < i + 3; j++) {
                    final ImageButton imgbt = new ImageButton(game.shipDrawable.get(playerShips[j]));
                imgbt.setName(playerShips[j]);
                if (!game.playerSave.playerHasShip(playerShips[j])) imgbt.getImage().setColor(Color.BLACK);

                table.add(imgbt).height(imgbt.getHeight()).width(imgbt.getWidth());

               if (game.playerSave.playerHasShip(playerShips[j])){
                   //select the ship
                   imgbt.addListener(new ChangeListener() {
                       @Override
                       public void changed(ChangeEvent event, Actor actor) {
                           game.playClick();
                           //tint with yellow only the selected ship and white all the other
                           for (Label l :shipsLabel) {
                               if (playerMap.get(imgbt.getName()) == l.getName()){
                                   table.findActor(l.getName()).setColor(Color.YELLOW);
                               } else {
                                    table.findActor(l.getName()).setColor(Color.WHITE);
                               }
                           }
                           ship = imgbt.getName();
                           System.out.println(imgbt.getName());
                       }
                   });
               }
            }
            table.row();

            for (int j = i; j < i + 3; j++) {
                table.add(game.shipStats.get(j)).height(game.shipStats.get(j).getHeight()).width(game.shipStats.get(j).getWidth());
            }
            table.row();


        }
        scrollPane = new ScrollPane(table);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth()* 0.5f, Gdx.graphics.getHeight() * 0.5f);
        scrollPane.setPosition(0, Gdx.graphics.getHeight() * 0.5f);
        scrollPane.setTransform(true);
        scrollPane.setSmoothScrolling(true);

        Table finalTable =  new Table();
        finalTable.setDebug(MyGdxGame.DEBUG);
        finalTable.setWidth(Gdx.graphics.getWidth());
        finalTable.setHeight(Gdx.graphics.getHeight() * 0.8f);
        finalTable.setOrigin(0, 0);
        finalTable.setPosition(0, 0);

        finalTable.add(scrollPane).fill().expand();

        finalTable.setPosition(0,0);
        stage.addActor(finalTable);
    }

    public void createHashShipMap(){


        System.out.println(playerShips.length);
        System.out.println(playerRealNameShips.length);
        for (int i = 0; i <= playerShips.length - 1; i++){
            playerMap.put(playerShips[i], playerRealNameShips[i]);
            System.out.println(playerRealNameShips[i]);
        }
    }
}
