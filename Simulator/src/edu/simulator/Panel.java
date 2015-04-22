package edu.simulator;

import org.newdawn.slick.opengl.Texture;

public class Panel extends GameObject{
	
	Texture texture;
	
	public Panel(int x, int y, int sx, int sy){
		this(x, y, sx, sy, null);
	}	

	public Panel(int x, int y, int sx, int sy, Texture texture){
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		this.texture = texture;
	}
	
	
	
	
	@Override
	public void render() {
		if(texture == null)
			Draw.Rect(x, y, sx, sy);
		else
			Draw.Rect(x, y, sx, sy, texture);
		
	}

	@Override
	void update() {
		// TODO Auto-generated method stub
		
	}
	
	

}
