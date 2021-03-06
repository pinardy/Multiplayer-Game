package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MultiplayerGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MultiplayerGame(), config);
		config.width = MultiplayerGame.V_WIDTH;
		config.height = MultiplayerGame.V_HEIGHT;
		config.title = MultiplayerGame.TITLE;
	}
}
