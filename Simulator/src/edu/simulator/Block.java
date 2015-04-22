package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.awt.Color;
import java.util.Random;

import org.newdawn.slick.opengl.Texture;

public class Block {
	public int h = 0;
	public int g = 0;
	public int f = 0;
	public Block parent;
	public int x;
	public int y;
	public int i;
	public int j;
	private Simulator simulator;
	private Texture texture;
	public boolean highlight;
	public boolean isAllowed;
	Random rand = new Random();

	@Override
	protected void finalize() throws Throwable {
		count--;

	}

	public static final int SIZE = 20;
	public EnumContent content;
	public static int count = 0;
	{
		count++;
	}

	public Block(Block b) {
		this(b.i, b.j, b.content, b.simulator);
	}

	public Block(int i, int j, Simulator simulator) {
		this(i, j, EnumContent.EMPTY, simulator);
	}

	public Block(int i, int j, EnumContent content, Simulator simulator) {
		this.i = i;
		this.j = j;
		x = 0 + i * SIZE;
		y = 0 + j * SIZE;
		this.content = content;
		this.simulator = simulator;
		texture = EnumSim_Texture.BLOCK_GRID.texture;
	}

	public void setContent(EnumContent c, boolean highlight) {
		setContent(c, highlight, true);
	}

	public void setContent(EnumContent c) {
		setContent(c, false);
	}

	public void setContent(EnumContent c, boolean highlight, boolean isAllowed) {
		/*
		 * if (content == EnumContent.EMPTY) content = c; else if((content ==
		 * EnumContent.ROAD_HORIZONTAL || content == EnumContent.ROAD_VERTICAL)
		 * && content != c){ content = EnumContent.ROAD_CROSS;
		 */
		this.highlight = highlight;
		this.isAllowed = isAllowed;

		content = c;

		if (c == EnumContent.ROAD_HORIZONTAL) {
			if (simulator.grid[i][j + 1].content == EnumContent.ROAD_CROSS
					|| simulator.grid[i][j + 1].content == EnumContent.ROAD_HORIZONTAL
					|| simulator.grid[i][j + 1].content == EnumContent.ROAD_VERTICAL) {

				content = EnumContent.ROAD_CROSS;
				if (simulator.grid[i][j + 1].content != EnumContent.ROAD_CROSS)
					simulator.grid[i][j + 1]
							.setContent(simulator.grid[i][j + 1].content);

			}

			if (simulator.grid[i][j - 1].content == EnumContent.ROAD_CROSS
					|| simulator.grid[i][j - 1].content == EnumContent.ROAD_HORIZONTAL
					|| simulator.grid[i][j - 1].content == EnumContent.ROAD_VERTICAL) {

				content = EnumContent.ROAD_CROSS;
				if (simulator.grid[i][j - 1].content != EnumContent.ROAD_CROSS)
					simulator.grid[i][j - 1]
							.setContent(simulator.grid[i][j - 1].content);
			}

			if (simulator.grid[i + 1][j].content == EnumContent.ROAD_VERTICAL) {
				simulator.grid[i + 1][j].content = EnumContent.ROAD_CROSS;
			}
			if (simulator.grid[i - 1][j].content == EnumContent.ROAD_VERTICAL) {
				simulator.grid[i - 1][j].content = EnumContent.ROAD_CROSS;
			}

		} else if (c == EnumContent.ROAD_VERTICAL) {
			if (simulator.grid[i + 1][j].content == EnumContent.ROAD_CROSS
					|| simulator.grid[i + 1][j].content == EnumContent.ROAD_HORIZONTAL
					|| simulator.grid[i + 1][j].content == EnumContent.ROAD_VERTICAL) {

				content = EnumContent.ROAD_CROSS;
				if (simulator.grid[i + 1][j].content != EnumContent.ROAD_CROSS)
					simulator.grid[i + 1][j]
							.setContent(simulator.grid[i + 1][j].content);

			}

			if (simulator.grid[i - 1][j].content == EnumContent.ROAD_CROSS
					|| simulator.grid[i - 1][j].content == EnumContent.ROAD_HORIZONTAL
					|| simulator.grid[i - 1][j].content == EnumContent.ROAD_VERTICAL) {

				content = EnumContent.ROAD_CROSS;
				if (simulator.grid[i - 1][j].content != EnumContent.ROAD_CROSS)
					simulator.grid[i - 1][j]
							.setContent(simulator.grid[i - 1][j].content);
			}

			if (simulator.grid[i][j + 1].content == EnumContent.ROAD_HORIZONTAL) {
				simulator.grid[i][j + 1].content = EnumContent.ROAD_CROSS;
			}
			if (simulator.grid[i][j - 1].content == EnumContent.ROAD_HORIZONTAL) {
				simulator.grid[i][j - 1].content = EnumContent.ROAD_CROSS;
			}
		}

	}

	public void render() {
		if (highlight && !isAllowed)
			glColor3f(1f, 0f, 0f);
		else if (highlight && isAllowed)
			glColor3f(0f, 1f, 0f);
		else
			glColor3f(1f, 1f, 1f);
		Draw.Rect(x, y, SIZE, SIZE, content.texture);
		if (simulator.mode == simulator.roadEditMode
				|| simulator.mode == simulator.destroyEditMode
				|| simulator.mode == simulator.supplierEditMode
				|| simulator.mode == simulator.recipientEditMode
				|| simulator.mode == simulator.homeEditMode
				|| simulator.mode == simulator.petrolEditMode) {
			glColor3f(1f, 1f, 1f);
			Draw.Rect(x, y, SIZE, SIZE, texture);
		}

	}
}
