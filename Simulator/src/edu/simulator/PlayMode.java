package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColor3f;

import java.lang.Thread.State;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class PlayMode extends Mode {

	private boolean init;
	private List<Vehicle> vehicle = new ArrayList<Vehicle>();
	private List<Thread> threads = new ArrayList<Thread>();
	private boolean isWaiting;
	private boolean tracking;
	private boolean trackAll;
	private int vehicleNum;
	private boolean autor;

	public PlayMode(Simulator simulator) {
		init = true;
		this.simulator = simulator;
		vehicleNum = 0;
	}

	@Override
	public void getInput() {
		boolean skip = false;
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					/*
					 * for(Button go : buttons) if(go.isActive &&
					 * Physics.isCollided(Mouse.getEventX(), Mouse.getEventY(),
					 * go)){ go.isPressed = true; skip = true; break; }
					 */
					if (!skip) {

					}
					skip = false;
				}
			} else {
				if (Mouse.getEventButton() == 0) {
					/*
					 * for(Button go : buttons) if(go.isPressed){ go.isPressed =
					 * false; if(Physics.isCollided(Mouse.getEventX(),
					 * Mouse.getEventY(), go)){ go.click(); skip = true; }
					 * break; }
					 */
					if (!skip) {
						// TODO delete
					}
					skip = false;
				}
			}
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_TAB) {
					if (!tracking)
						tracking = true;
					else {
						vehicleNum++;
						if (vehicleNum > vehicle.size() - 1)
							vehicleNum = 0;
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					tracking = false;

				}
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
						autor = true;
				}
			}
			else{
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
						autor = false;
				}
			}
		}
	}

	@Override
	public void update() {

		if (init)
			init();
		else {
			for (Supplier go : simulator.suppliers)
				go.update();

			for (Vehicle go : vehicle)
				synchronized (go) {
					go.notify();
				}

		}
	}

	private void init() {
		for (int i = 0; i < Simulator.VEHICLES; i++)
			vehicle.add(new Vehicle(simulator));
		for (Vehicle go : vehicle)
			threads.add(new Thread(go));
		for (Thread go : threads) {
			go.setDaemon(true);
			go.start();
		}
		init = false;
	}

	private void tracking() {
		DecimalFormat f = new DecimalFormat("##.#");
	     
		NString.render("Fuel: " + f.format(vehicle.get(vehicleNum).fuel/10),
				Display.getWidth() - Simulator.PANEL_WIDTH / 2 - 60, 150,
				NString.standardFont, Color.white);
		glColor4f(0f, 0f, 0f, 0.5f);
		Draw.Rect(Display.getWidth() - Simulator.PANEL_WIDTH / 2  - Block.SIZE/2- 33, 247, 66, 156);
		glColor4f(1f, 1f, 1f, 0.2f);
		Draw.Rect(Display.getWidth() - Simulator.PANEL_WIDTH / 2 - Block.SIZE/2 - 30, 250, 60, 150);
		glColor3f(1f, vehicle.get(vehicleNum).fuel/vehicle.get(vehicleNum).fuelMax*1f, vehicle.get(vehicleNum).fuel/vehicle.get(vehicleNum).fuelMax*1f);
		Draw.Rect(Display.getWidth() - Simulator.PANEL_WIDTH / 2 - Block.SIZE/2 - 30, 250, 60, vehicle.get(vehicleNum).fuel/vehicle.get(vehicleNum).fuelMax*150);
		

		if (vehicle.get(vehicleNum).i - 1 >= 0) {
			glColor4f(1f, 0f, 0f, 0.5f);
			if (vehicle.get(vehicleNum).path.get(vehicle.get(vehicleNum).i - 1).y == vehicle
					.get(vehicleNum).path.get(vehicle.get(vehicleNum).i).y) {
				int diff = vehicle.get(vehicleNum).x
						- vehicle.get(vehicleNum).path.get(vehicle
								.get(vehicleNum).i - 1).x;
				if (diff >= 0) {
					Draw.Rect(vehicle.get(vehicleNum).path.get(vehicle
							.get(vehicleNum).i - 1).x,
							vehicle.get(vehicleNum).path.get(vehicle
									.get(vehicleNum).i - 1).y,
							diff + vehicle.get(vehicleNum).size + 2, Block.SIZE);
				} else {
					Draw.Rect(
							vehicle.get(vehicleNum).path.get(vehicle
									.get(vehicleNum).i - 1).x + diff - 2, vehicle
									.get(vehicleNum).path.get(vehicle
									.get(vehicleNum).i - 1).y, Block.SIZE
									- diff +2, Block.SIZE);
				}
			} else {
				int diff = vehicle.get(vehicleNum).y
						- vehicle.get(vehicleNum).path.get(vehicle
								.get(vehicleNum).i - 1).y;
				if (diff >= 0) {
					Draw.Rect(vehicle.get(vehicleNum).path.get(vehicle
							.get(vehicleNum).i - 1).x,
							vehicle.get(vehicleNum).path.get(vehicle
									.get(vehicleNum).i - 1).y, Block.SIZE, diff
									+ vehicle.get(vehicleNum).size + 2);
				} else {
					Draw.Rect(
							vehicle.get(vehicleNum).path.get(vehicle
									.get(vehicleNum).i - 1).x,
							vehicle.get(vehicleNum).path.get(vehicle
									.get(vehicleNum).i - 1).y + diff -2,
							Block.SIZE, Block.SIZE - diff + 2);
				}
			}
			for (int z = vehicle.get(vehicleNum).i - 2; z >= 0; z--)
				Draw.Rect(vehicle.get(vehicleNum).path.get(z).x,
						vehicle.get(vehicleNum).path.get(z).y, Block.SIZE,
						Block.SIZE);

		}

	//	glColor3f(0f, 0f, 0f);
		///Draw.Rect(vehicle.get(vehicleNum).x - 2, vehicle.get(vehicleNum).y - 2,
			///	vehicle.get(vehicleNum).size + 4,
				//vehicle.get(vehicleNum).size + 4);

		glColor3f(1f, vehicle.get(vehicleNum).fuel/vehicle.get(vehicleNum).fuelMax*1f, vehicle.get(vehicleNum).fuel/vehicle.get(vehicleNum).fuelMax*1f);
		Draw.Rect(vehicle.get(vehicleNum).x - 2, vehicle.get(vehicleNum).y - 2,
				vehicle.get(vehicleNum).size + 4,
				vehicle.get(vehicleNum).size + 4);
	}

	@Override
	public void render() {
		isWaiting = true;
		while (isWaiting) {
			for (Thread go : threads)
				if (go.getState() != State.WAITING
						&& go.getState() != State.BLOCKED) {

					isWaiting = true;
					break;
				} else
					isWaiting = false;

		}
		for (int j = 0; j < Simulator.GRID_HEIGHT; j++)
			for (int i = 0; i < Simulator.GRID_WIDTH; i++)
				if (simulator.grid[i][j].content != EnumContent.EMPTY)
					simulator.grid[i][j].render();

		// for(Button go : buttons)
		// go.render();

		if (tracking)
			tracking();

		for (Vehicle go : vehicle)
			go.render();
		if(autor){
			NString.renderCenter("TAB - track (on/change) vehicle", Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2, 560, NString.standardSmallFont, Color.white);
			NString.renderCenter("ESC - track (off) vehicle", Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2, 530, NString.standardSmallFont, Color.white);
			NString.renderCenter("by Pawel Jasik", Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2, 35, NString.standardSmallFont, Color.white);
			NString.renderCenter("Release Date: 7. April 2014", Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2, 15, NString.standardSmallFont, Color.white);
		
		}
		else {
			NString.renderCenter("A - more info", Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2, 30, NString.standardSmallFont, Color.white);
		}
	}

}
