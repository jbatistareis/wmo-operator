package com.jbatista.wmo.operator.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jbatista.wmo.operator.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "WMO Operator";
		config.vSyncEnabled = true;
		config.resizable = true;
		config.width = 1280;
		config.height = 720;

		new LwjglApplication(new Main(), config);
	}
}
