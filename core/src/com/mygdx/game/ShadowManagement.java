package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Shadow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * Created by kennethlimcp on 08/Mar/2017.
 */

public class ShadowManagement extends Thread {
    private Queue<Shadow> serverShadows;
    private boolean multiPlayer;

    private MultiplayerGame game = null;
    private CopyOnWriteArrayList<Shadow> shadows = new CopyOnWriteArrayList<Shadow>();
    private final Object shadowsLock = new Object();
    private float shadowX = 0f;
    private float shadowY = 0f;

    public ShadowManagement(MultiplayerGame game) {
        this.game = game;
        serverShadows = new LinkedList<Shadow>();
    }

    public ShadowManagement(MultiplayerGame game, boolean multiPlayer) {
        this(game);
        this.multiPlayer = multiPlayer;
    }

    @Override
    public void run() {
//        calculateShadowStartPosition();
        Random rand = new Random();

        while (true) {
            if (multiPlayer) {
                while (serverShadows.peek() != null && serverShadows.peek().getServerTime() <= Hud.timePassed) {
                    shadows.add(serverShadows.poll());
                    Gdx.app.log("Spawning new shadow", "sm thread");
                }
            }else {
                if (shadows.size() == 0) {
                    int randomShadow = rand.nextInt(game.getPillarPositions().size());
                    Rectangle r = game.getPillarPositions().get(randomShadow);
                    shadows.add(new Shadow((PlayScreen) game.getScreen(), r.getX(), r.getY()));
                    Gdx.app.log("Spawning new shadow", "sm thread");
                }
            }

            synchronized (shadowsLock) {
                for (Shadow s : shadows) {
                    if (!s.isAlive()) {
                        shadows.remove(s);
                    }
                }
            }
        }
    }

    public void calculateShadowStartPosition() {
        float coreX = game.corePosition.getX() + game.corePosition.getWidth()/2;
        float coreY = game.corePosition.getY() + game.corePosition.getHeight()/2;

        // create a shadow in our game world
        for(Rectangle r: game.getPillarPositions()) {
            float x = r.getX() + r.getWidth()/2;
            float y = r.getY() + r.getHeight()/2;

            float adjustedX = x + (x-coreX);
            float adjustedY = y + (y-coreY);

            r.setX(adjustedX);
            r.setY(adjustedY);
        }
    }

    public void update(float dt) {
        synchronized (shadowsLock) {
            for (Shadow s : shadows) {
                s.update(dt);
            }
        }
    }

    public Sprite getShadows() {
        for (Shadow x : shadows) {
            return x;
        }
        return null;
    }

    public ArrayList<Shadow> getMShadows(){
        ArrayList<Shadow> sprites = new ArrayList<Shadow>();
        for (Shadow x : shadows){
            sprites.add(x);
        }
        return sprites ;
    }



    public void addServerShadows(Shadow shadow){
        serverShadows.offer(shadow);
    }
}
