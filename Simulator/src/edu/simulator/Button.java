package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor3f;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Button extends GameObject {

	public boolean isPressed;
	public Simulator simulator;
	public Mode mode;
	public boolean isActive;
	private boolean add;
	public Texture texture;
	public Texture texture_active;
	private boolean isProper;

	public Button(int x, int y, int sx, int sy, Mode mode, Simulator simulator,
			Texture texture, Texture texture_active) {
		this(x, y, sx, sy, mode, false, simulator, texture, texture_active);
	}

	public Button(int x, int y, int sx, int sy, boolean add,
			Simulator simulator, Texture texture) {
		this(x, y, sx, sy, null, add, simulator, texture, null);
	}

	public Button(int x, int y, int sx, int sy, Mode mode, Simulator simulator,
			Texture texture) {
		this(x, y, sx, sy, mode, false, simulator, texture, null);
	}

	public Button(int x, int y, int sx, int sy, Mode mode, boolean add,
			Simulator simulator, Texture texture, Texture texture_active) {
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		this.simulator = simulator;
		this.mode = mode;
		isActive = true;
		this.texture = texture;
		this.texture_active = texture_active;
		this.add = add;
		isProper = true;
	}

	public void click() {
		if (mode == simulator.playMode) {
			isProper = true;
			for (Supplier go : simulator.suppliers) {
				Pathfinding p = new Pathfinding();
				if (p.findPath(simulator.grid, simulator.home.entrance,
						go.entrance) == null) {
					go.texture = EnumSim_Texture.BLOCKED.texture;
					isProper = false;
				} else {
					go.texture = EnumSim_Texture.SUPPLIER.texture;
				}
			}
			for (Recipient go : simulator.recipients) {
				Pathfinding p = new Pathfinding();
				if (p.findPath(simulator.grid, simulator.home.entrance,
						go.entrance) == null) {
					go.texture = EnumSim_Texture.BLOCKED.texture;
					isProper = false;
				} else {
					go.texture = EnumSim_Texture.RECIPIENT.texture;
				}
			}
			for (Petrol go : simulator.petrols) {
				Pathfinding p = new Pathfinding();
				if (p.findPath(simulator.grid, simulator.home.entrance,
						go.entrance) == null) {
					go.texture = EnumSim_Texture.BLOCKED.texture;
					isProper = false;
				} else {
					go.texture = EnumSim_Texture.PETROL.texture;
				}
			}
			if (isProper) {
				simulator.mode = mode;
			}

		} else {
			if (simulator.mode != mode)
				simulator.mode = mode;
			else
				simulator.mode = simulator.editMode;
		}
	}

	public void click2() {
		if (add) {
			Simulator.VEHICLES++;
			if (Simulator.VEHICLES > 50)
				Simulator.VEHICLES = 50;
		} else {
			Simulator.VEHICLES--;
			if (Simulator.VEHICLES < 1)
				Simulator.VEHICLES = 1;
		}
	}

	@Override
	void update() {
		// TODO Auto-generated method stub

	}

	public void render() { // texture
		if (!isProper)
			NString.renderCenter("Some objects are unavailable!", x + sx / 2,
					y - 30, NString.standardSmallFont, Color.red);

		glColor3f(1f, 1f, 1f);
		if (simulator.mode == mode)
			Draw.Rect(x, y, sx, sy, texture_active);
		else
			Draw.Rect(x, y, sx, sy, texture);
	}

}
