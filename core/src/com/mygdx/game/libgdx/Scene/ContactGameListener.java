package com.mygdx.game.libgdx.Scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.libgdx.Actors.Laser;
import com.mygdx.game.libgdx.Actors.Meteor;
import com.mygdx.game.libgdx.Actors.Player;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

/**
 * Created by YAN on 13/02/2017.
 */

public class ContactGameListener implements com.badlogic.gdx.physics.box2d.ContactListener {
    Player player;

    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        playerAndMeteorBeginContact(fixA, fixB);
        laserHandle(fixA , fixB);
        laserAndMeteor(fixA, fixB);


    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA  = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        playerAndMeteorEndContact(fixA, fixB);

    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isTheObject(Fixture fixture, Object object){
        if(fixture.getBody().getUserData().getClass().equals(object)){
            return true;
        }
        else
            return false;
    }

    private void laserAndMeteor(Fixture fixA, Fixture fixB){
        if (isTheObject(fixA, Meteor.class) && isTheObject(fixB, Laser.class)){
            if (fixA.getBody().getUserData() != null)
            {
                ((Meteor)fixA.getBody().getUserData()).damageToMeteor(((Laser)fixB.getBody().getUserData()).laserAtk);
                player.score += 1;
            }

        }
        if (isTheObject(fixB, Meteor.class) && isTheObject(fixA, Laser.class)){
            if (fixB.getBody().getUserData() != null)
            {
                ((Meteor)fixB.getBody().getUserData()).damageToMeteor(((Laser)fixA.getBody().getUserData()).laserAtk);
                player.score += 1;
            }

        }
    }

    private void laserHandle(Fixture fixA, Fixture fixB){

        if(isTheObject(fixA, Laser.class)){
            Gdx.app.log("Laser", "Laser bateu");
            if (fixA.getFilterData().categoryBits == MyGdxGame.LASER_BIT){
                if (fixA.getBody().getUserData() != null){
                    ((Laser)fixA.getBody().getUserData()).impactAgainstObject();
                    //((Laser)fixA.getBody().getUserData()).setToDestroy();
                }
            }
        }
        if(isTheObject(fixB, Laser.class)){
            Gdx.app.log("Laser", "Laser bateu");
            if (fixB.getFilterData().categoryBits == MyGdxGame.LASER_BIT){
                if (fixB.getBody().getUserData() != null){
                    ((Laser)fixB.getBody().getUserData()).impactAgainstObject();
                    //((Laser)fixB.getBody().getUserData()).setToDestroy();
                }
            }
        }
    }

    private void playerAndMeteorEndContact(final Fixture fixA, final Fixture fixB){
        if (isTheObject(fixA, Player.class) && isTheObject(fixB, Meteor.class)){
            if (fixA.getBody().getUserData() != null && fixB.getBody().getUserData() != null){
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (!((Player)fixA.getBody().getUserData()).isDead)
                            ((Player)fixA.getBody().getUserData()).touched = false;
                    }
                }, 0.6f);
            }
        }
        if (isTheObject(fixB, Player.class) && isTheObject(fixA, Meteor.class)){
            if (fixA.getBody().getUserData() != null && fixB.getBody().getUserData() != null){
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (!((Player)fixB.getBody().getUserData()).isDead)
                            ((Player)fixB.getBody().getUserData()).touched = false;
                    }
                }, 0.6f);
            }
        }
    }

    private void playerAndMeteorBeginContact(Fixture fixA, Fixture fixB){
        if (isTheObject(fixA, Player.class) && isTheObject(fixB, Meteor.class)){
            if (fixA.getBody().getUserData() != null && fixB.getBody().getUserData() != null){
                ((Player)fixA.getBody().getUserData()).damageOnPlayer(((Meteor)fixB.getBody().getUserData()).meteorAtk());
                ((Player)fixA.getBody().getUserData()).touched = true;
            }
        }
        if (isTheObject(fixB, Player.class) && isTheObject(fixA, Meteor.class)){
            if (fixA.getBody().getUserData() != null && fixB.getBody().getUserData() != null){
                ((Player)fixB.getBody().getUserData()).damageOnPlayer(((Meteor)fixA.getBody().getUserData()).meteorAtk());
                ((Player)fixB.getBody().getUserData()).touched = true;
            }
        }
    }

    public void setPlayer(Player p){
        player = p;
    }
}
