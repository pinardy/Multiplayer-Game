package com.mygdx.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;

/**
 * InteractiveTileObject iis for handling objects in the game
 * that can be interacted with
 */


public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected PlayScreen screen;
    protected Fixture fixture;
    protected Filter filter;


    public InteractiveTileObject(PlayScreen screen, Rectangle bounds){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
        this.screen = screen;
        this.filter = new Filter();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) , (bounds.getY() + bounds.getHeight() / 2) );

        body = world.createBody(bdef);

        // define polygon shape (divide by 2 because starts in centre and goes both direction)
        shape.setAsBox((bounds.getWidth() / 2) , (bounds.getHeight() /2));
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }


    public void setCategoryFilter(short filterBit){
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public Filter getFilterData() {
        return fixture.getFilterData();
    }
}
