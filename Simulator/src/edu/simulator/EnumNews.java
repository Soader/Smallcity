package edu.simulator;

import org.newdawn.slick.opengl.Texture;

public enum EnumNews {
	NORTH (EnumSim_Texture.ROAD_C.texture),
	EAST (EnumSim_Texture.ROAD_C.texture),
	WEST (EnumSim_Texture.ROAD_C.texture),
	SOUTH (EnumSim_Texture.ROAD_C.texture);
	
	public Texture texture;
	private EnumNews(Texture t){
		texture = t;
	}
}
