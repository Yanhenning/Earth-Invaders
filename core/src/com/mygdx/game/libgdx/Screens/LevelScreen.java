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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.libgdx.Actors.InfiniteScrollBg;


/**
 * Created by YAN on 23/10/2017.
 */

//TODO implement lvl screen

public class LevelScreen implements Screen {

    final MyGdxGame game;
    private Viewport viewport;
    OrthographicCamera camera;
    Stage stage;
    Table table;
    Table finalTable;
    ScrollPane scrollPane;
    Skin skin;
    Skin buttonSkin;
    Label stageLabel;
    Integer stageCount = 0;
    Button backButton;
    Skin skin2;
    Label space;

    public LevelScreen(final MyGdxGame game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        viewport = new FitViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
        stage = new Stage();

        skin2 = game.skin;
        stage.addActor(new InfiniteScrollBg(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stageLabel = new Label("STAGES", new Label.LabelStyle(game.title, Color.WHITE));
        space = new Label("", new Label.LabelStyle(game.tinyFont, Color.WHITE));
        Table hangarTitle = new Table();

        hangarTitle.add(stageLabel).expand().center().top();
        hangarTitle.setPosition(Gdx.graphics.getWidth()* 0.5f, Gdx.graphics.getHeight());
        stage.addActor(hangarTitle);



        //Add all buttons
        addButtons();
        createTable();
        //stage.addActor(table);

        //imageList();

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


    }



    //create the scrollable table
    private void createTable(){
        skin = new Skin(Gdx.files.internal("data/ui/level-plane-ui.json"));
        table = new Table(skin);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin2.newDrawable(MyGdxGame.BUTTON_SHAPE, Color.DARK_GRAY);
        textButtonStyle.up = skin2.newDrawable(MyGdxGame.BUTTON_SHAPE, Color.LIGHT_GRAY);
        textButtonStyle.font = game.font;

        TextButton.TextButtonStyle styleOff = new TextButton.TextButtonStyle();
        styleOff.up = skin2.newDrawable(MyGdxGame.BUTTON_SHAPE, Color.DARK_GRAY);
        styleOff.up = skin2.newDrawable(MyGdxGame.BUTTON_SHAPE, Color.DARK_GRAY);
        styleOff.font = game.font;



//create the table
        table.setDebug(MyGdxGame.DEBUG);
        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight() * 0.8f);
        table.setOrigin(0, 0);
        table.setPosition(0, 0);
//3 columns for a row

        for (int i = 0; i < 120; i+=5){
            //add ship's names
            for (int j = i; j < i + 5; j++) {
                final TextButton txt = new TextButton(Integer.toString(j+1), textButtonStyle);
                if(game.playerSave.stage < j+1){
                    txt.setStyle(styleOff);
                }
                txt.setName(Integer.toString(j + 1));
                if(game.playerSave.stage >= j+1){
                    txt.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            game.setScreen(new GameScreen(game, Integer.valueOf(txt.getName())));
                        }
                    });
                }

                table.add(txt).height(Gdx.graphics.getHeight() * 0.20f).width(Gdx.graphics.getHeight() * 0.20f).expandX().expandY();
                /*Label txt = new Label(playerMap.get(playerShips[j]), new Label.LabelStyle(game.tinyFont, Color.WHITE));
                txt.setName(playerMap.get(playerShips[j]));
                table.add(txt).height(txt.getHeight()*1f).width(txt.getWidth()* 1f);
*/
            }
            table.row();
            table.add(space).height(Gdx.graphics.getHeight()* 0.05f);
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

}