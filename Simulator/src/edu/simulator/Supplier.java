package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.Random;

import org.newdawn.slick.opengl.Texture;

public class Supplier extends GameObject {

	private Simulator simulator;
	private boolean task;
	private boolean completed;
	public Block entrance;
	private Random rand;
	public Texture texture;

	public Supplier(int x, int y, int sx, int sy, Simulator simulator) {
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		this.texture = EnumSim_Texture.SUPPLIER.texture;
		this.simulator = simulator;
		task = false;
		completed = true;
		if (simulator.grid[x / Block.SIZE][y / Block.SIZE - 1].content != EnumContent.EMPTY)
			entrance = simulator.grid[x / Block.SIZE][y / Block.SIZE - 1];
		else if (simulator.grid[x / Block.SIZE][y / Block.SIZE + 1].content != EnumContent.EMPTY)
			entrance = simulator.grid[x / Block.SIZE][y / Block.SIZE + 1];
		else if (simulator.grid[x / Block.SIZE - 1][y / Block.SIZE].content != EnumContent.EMPTY)
			entrance = simulator.grid[x / Block.SIZE - 1][y / Block.SIZE];
		else if (simulator.grid[x / Block.SIZE + 1][y / Block.SIZE].content != EnumContent.EMPTY)
			entrance = simulator.grid[x / Block.SIZE + 1][y / Block.SIZE];
		rand = new Random();
	}

	public Recipient takeATask() {
		task = false;
		completed = true;
		return simulator.recipients.get(rand.nextInt(simulator.recipients
				.size()));
		
	}

	public synchronized boolean go() {
		if (completed) {
			if (task) {

				completed = false;
				return true;
			} else
				return false;
		} else
			return false;
	}

	@Override
	public void update() {
		if (completed) {
			if (!task) {
				if (rand.nextInt(1000) == 1)
					task = true;
			}
		}

	}

	public void render() {
		if (task)
			glColor4f(1f, 0f, 1f, 1f);
		else
			glColor4f(1f, 1f, 1f, 1f);
		Draw.Rect(x, y, sx, sy, texture);
		glColor4f(1f, 1f, 1f, 1f);
	}

}
