package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class DestroyEditMode extends EditMode {

	private boolean creating;
	private boolean created;
	private List<Block> buffer = new ArrayList<Block>();
	private Block start;
	private Block end;
	private int RecX;
	private int RecY;
	private int RecSX;
	private int RecSY;

	public DestroyEditMode(Simulator simulator) {
		this.simulator = simulator;
		creating = false;
		created = false;
		buttons = new ArrayList<Button>();

	}

	public void addButtons() {
		for (Button go : simulator.editMode.buttons)
			buttons.add(new Button(go.x, go.y, go.sx, go.sy, go.mode,
					go.simulator, go.texture, go.texture_active));
	}

	@Override
	public void getInput() {
		Keyboard.next(); // clears keyboard buffer
		boolean skip = false;
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					for (Button go : buttons)
						if (go.isActive
								&& Physics.isCollided(Mouse.getEventX(),
										Mouse.getEventY(), go)) {
							go.isPressed = true;
							skip = true;
							break;
						}
					if (!skip) {
						pressed(Mouse.getEventX(), Mouse.getEventY());
					}
					skip = false;
				}
			} else {
				if (Mouse.getEventButton() == 0) {
					for (Button go : buttons)
						if (go.isPressed) {
							go.isPressed = false;
							if (Physics.isCollided(Mouse.getEventX(),
									Mouse.getEventY(), go)) {
								go.click();
								skip = true;
							}
							break;
						}
					if (!skip) {
						released(Mouse.getEventX(), Mouse.getEventY());
					}
					skip = false;
				}
			}
		}
	}

	private void pressed(int mouseX, int mouseY) {
		int i = mouseX / Block.SIZE;
		int j = mouseY / Block.SIZE;
		if (i < Simulator.GRID_WIDTH && j < Simulator.GRID_HEIGHT) {
			for (int d = 0; d < Simulator.GRID_HEIGHT; d++)
				for (int c = 0; c < Simulator.GRID_WIDTH; c++)
					simulator.gridBuffer[c][d].content = simulator.grid[c][d].content;
			start = new Block(i, j, simulator);
			creating = true;
		}
	}

	private void released(int mouseX, int mouseY) {
		int i = mouseX / Block.SIZE;
		int j = mouseY / Block.SIZE;
		if (i < Simulator.GRID_WIDTH && j < Simulator.GRID_HEIGHT) {
			end = new Block(i, j, simulator);
			setContent(start, end);
			destroy();
		}
		creating = false;

	}

	private void destroy() {
		int c;
		int d;
		for (int j = RecY; j < RecY + RecSY; j++)
			for (int i = RecX; i < RecX + RecSX; i++)
				if (simulator.grid[i][j].content == EnumContent.ROAD_CROSS
						|| simulator.grid[i][j].content == EnumContent.ROAD_HORIZONTAL
						|| simulator.grid[i][j].content == EnumContent.ROAD_VERTICAL) {
					simulator.grid[i][j].content = EnumContent.EMPTY;
				}

		
		Iterator<GameObject> itG = simulator.buildings.iterator();
		while (itG.hasNext()) {
			GameObject go = itG.next();
			if (Physics.isCollided(RecX * Block.SIZE, RecY * Block.SIZE, RecSX
					* Block.SIZE, RecSY * Block.SIZE, go)) {
				itG.remove();
			}
		}
		
		Iterator<Supplier> it = simulator.suppliers.iterator();
		while (it.hasNext()) {
			Supplier go = it.next();
			if (Physics.isCollided(RecX * Block.SIZE, RecY * Block.SIZE, RecSX
					* Block.SIZE, RecSY * Block.SIZE, go)) {
				it.remove();
			}
		}

		Iterator<Recipient> itR = simulator.recipients.iterator();
		while (itR.hasNext()) {
			Recipient go = itR.next();
			if (Physics.isCollided(RecX * Block.SIZE, RecY * Block.SIZE, RecSX
					* Block.SIZE, RecSY * Block.SIZE, go)) {
				itR.remove();
			}
		}

		Iterator<Petrol> itP = simulator.petrols.iterator();
		while (itP.hasNext()) {
			Petrol go = itP.next();
			if (Physics.isCollided(RecX * Block.SIZE, RecY * Block.SIZE, RecSX
					* Block.SIZE, RecSY * Block.SIZE, go)) {
				itP.remove();
			}
		}
		if (simulator.home != null) {
			if (Physics.isCollided(RecX * Block.SIZE, RecY * Block.SIZE, RecSX
					* Block.SIZE, RecSY * Block.SIZE, simulator.home)) {
				simulator.home = null;
				;
			}
		}

		for (d = RecY - 1; d < RecY + RecSY + 1; d++) { // sprawdzic dlugosc
			if (d == RecY - 1)
				for (c = RecX - 1; c < RecX + RecSX + 1; c++)
					if (simulator.grid[c][d].content == EnumContent.ROAD_CROSS)
						updateRoad(c, d);
			if (d == RecY + RecSY)
				for (c = RecX - 1; c < RecX + RecSX + 1; c++)
					if (simulator.grid[c][d].content == EnumContent.ROAD_CROSS)
						updateRoad(c, d);
			c = RecX - 1;
			if (simulator.grid[c][d].content == EnumContent.ROAD_CROSS)
				updateRoad(c, d);
			c = RecX + RecSX;
			if (simulator.grid[c][d].content == EnumContent.ROAD_CROSS)
				updateRoad(c, d);
		}
	}

	private void updateRoad(int i, int j) {
		if (simulator.grid[i - 1][j].content != EnumContent.EMPTY
				|| simulator.grid[i + 1][j].content != EnumContent.EMPTY) {
			if (simulator.grid[i][j + 1].content == EnumContent.EMPTY
					&& simulator.grid[i][j - 1].content == EnumContent.EMPTY) {
				simulator.grid[i][j].content = EnumContent.ROAD_HORIZONTAL;
			}
		} else
			simulator.grid[i][j].content = EnumContent.ROAD_VERTICAL;
	}

	private void setContent(Block start, Block end) {

		if (end.i < 1)
			end.i = 1;
		if (end.i > Simulator.GRID_WIDTH - 2)
			end.i = Simulator.GRID_WIDTH - 2;
		if (end.j < 1)
			end.j = 1;
		if (end.j > Simulator.GRID_HEIGHT - 2)
			end.j = Simulator.GRID_HEIGHT - 2;

		if (start.i < 1)
			start.i = 1;
		if (start.i > Simulator.GRID_WIDTH - 2)
			start.i = Simulator.GRID_WIDTH - 2;
		if (start.j < 1)
			start.j = 1;
		if (start.j > Simulator.GRID_HEIGHT - 2)
			start.j = Simulator.GRID_HEIGHT - 2;

		if (start.i <= end.i)
			RecX = start.i;
		else
			RecX = end.i;

		if (start.j <= end.j)
			RecY = start.j;
		else
			RecY = end.j;

		RecSX = (Math.abs(end.i - start.i) + 1);
		if (RecSX == 0)
			RecSX = 1;

		RecSY = (Math.abs(end.j - start.j) + 1);
		if (RecSY == 0)
			RecSY = 1;

	}

	@Override
	public void update() {

		if (creating) {

			end = new Block(Mouse.getX() / Block.SIZE, Mouse.getY()
					/ Block.SIZE, simulator);
			setContent(start, end);
		}

	}

	@Override
	public void render() {

		for (int j = 1; j < Simulator.GRID_HEIGHT - 1; j++)
			for (int i = 1; i < Simulator.GRID_WIDTH - 1; i++)
				simulator.grid[i][j].render();
		for (Button go : buttons)
			go.render();
		if (creating) {
			glColor4f(1f, 0f, 0f, 0.5f);
			Draw.Rect(RecX * Block.SIZE, RecY * Block.SIZE, RecSX * Block.SIZE,
					RecSY * Block.SIZE);
		}

	}

}
