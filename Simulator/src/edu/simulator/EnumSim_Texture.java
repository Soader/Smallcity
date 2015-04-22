package edu.simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public enum EnumSim_Texture {
	ROAD_H ("Road_h"), 
	ROAD_V ("Road_v"),
	ROAD_C ("Road_c"),
	BLOCK_GRID ("block_grid"),
	TRANSPARENT ("transparent"),
	GRIDBACKGROUND ("grass"),
	BACKGROUND ("wood03"),
	FRAME_LEFT ("frame"),
	FRAME_UP ("frame_up"),
	FRAME_RIGHT ("frame_right"),
	FRAME_DOWN ("frame_down"),
	BUTTON_ROAD ("button_road"),
	BUTTON_PLAY ("button_play"),
	BUTTON_PLAY_INACT ("button_play_inactive"),
	BUTTON_DESTROY ("button_destroy"),
	BUTTON_PETROL ("button_petrol"),
	BUTTON_SUPPLIER ("button_supplier"),
	BUTTON_RECIPIENT ("button_recipient"),
	BUTTON_HOME ("button_home"),
	BUTTON_ROAD_ACTIVE ("button_road_active"),
	BUTTON_DESTROY_ACTIVE ("button_destroy_active"),
	BUTTON_PETROL_ACTIVE ("button_petrol_active"),
	BUTTON_SUPPLIER_ACTIVE ("button_supplier_active"),
	BUTTON_RECIPIENT_ACTIVE ("button_recipient_active"),
	BUTTON_HOME_ACTIVE ("button_home_active"),
	BUTTON_PLUS ("button_plus"),
	BUTTON_MINUS ("button_min"),
	PETROL ("petrol_obj"),
	SUPPLIER ("factory-icon"),
	RECIPIENT ("shop-icon"),
	HOME ("home"),
	BLOCKED ("blocked");
	
	public Texture texture;
	
	private EnumSim_Texture(String name){
		texture = loadTexture(name);
	}
	
public Texture loadTexture(String name){
		
		try {
			return TextureLoader.getTexture("png", EnumSim_Texture.class.getClassLoader().getResourceAsStream(name + ".png"));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return null;
	}
}
