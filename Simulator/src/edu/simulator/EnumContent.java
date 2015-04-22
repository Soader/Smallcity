package edu.simulator;

import org.newdawn.slick.opengl.Texture;


public enum EnumContent {	
	EMPTY (EnumSim_Texture.TRANSPARENT.texture), 
	ROAD_HORIZONTAL (EnumSim_Texture.ROAD_H.texture), 
	ROAD_VERTICAL (EnumSim_Texture.ROAD_V.texture),
	ROAD_CROSS (EnumSim_Texture.ROAD_C.texture);
	
	public Texture texture;
	
	private EnumContent(){
		
	}
	private EnumContent(Texture texture){
		this.texture = texture;
	}
	
}
