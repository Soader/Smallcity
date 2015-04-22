package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor4f;

import org.newdawn.slick.opengl.Texture;

public class Recipient extends GameObject {

	private Simulator simulator;
	public Block entrance;
	public Texture texture;
	
	public Recipient(int x, int y, int sx, int sy, Simulator simulator){
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		this.texture = EnumSim_Texture.RECIPIENT.texture;
		this.simulator = simulator;
		if (simulator.grid[x / Block.SIZE][y / Block.SIZE - 1].content != EnumContent.EMPTY)
			entrance = simulator.grid[x / Block.SIZE][y / Block.SIZE - 1];
		else if (simulator.grid[x / Block.SIZE][y / Block.SIZE + 1].content != EnumContent.EMPTY)
			entrance = simulator.grid[x / Block.SIZE][y / Block.SIZE + 1];
		else if (simulator.grid[x / Block.SIZE - 1][y / Block.SIZE].content != EnumContent.EMPTY)
			entrance = simulator.grid[x / Block.SIZE - 1][y / Block.SIZE];
		else if (simulator.grid[x / Block.SIZE + 1][y / Block.SIZE].content != EnumContent.EMPTY)
			entrance = simulator.grid[x / Block.SIZE + 1][y / Block.SIZE];
	}
	
	@Override
	void update() {
		// TODO Auto-generated method stub
		
	}
	
	public void render() {
		
		
		
		glColor4f(1f, 1f, 1f, 1f);
		Draw.Rect(x, y, sx, sy, texture);
		
	}
}
