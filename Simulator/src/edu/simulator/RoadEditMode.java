package edu.simulator;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class RoadEditMode extends EditMode {

	private boolean creating;
	private boolean created;
	private List<Block> buffer = new ArrayList<Block>();
	private Block start;
	private Block end;
	private boolean isAllowed;

	public RoadEditMode(Simulator simulator) {
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
						pressedRoad(Mouse.getEventX(), Mouse.getEventY());
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
						// TODO delete
					}
					skip = false;
				}
			}
		}
	}

	private void pressedRoad(int mouseX, int mouseY) {
		int i = mouseX / Block.SIZE;
		int j = mouseY / Block.SIZE;
		if (i < Simulator.GRID_WIDTH && j < Simulator.GRID_HEIGHT) {
			if (!creating) {
				for (int d = 0; d < Simulator.GRID_HEIGHT; d++)
					for (int c = 0; c < Simulator.GRID_WIDTH; c++)
						simulator.gridBuffer[c][d].content = simulator.grid[c][d].content;
				start = new Block(i, j, simulator);
				creating = true;
			} else {
				end = new Block(i, j, simulator);
				creating = false;
				created = true;
			}

		}
	}

	private void setContent(Block start, Block end) {
		isAllowed = true;
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

		boolean isHorizontal = true;
		int diffX = end.i - start.i;
		if (diffX == 0)
			diffX = 1;
		else
			diffX = diffX + diffX / Math.abs(diffX);
		int diffY = end.j - start.j;
		if (diffY == 0)
			diffY = 1;
		else
			diffY = diffY + diffY / Math.abs(diffY);
		int length = 0;
		if (Math.abs(diffX) > Math.abs(diffY)) {
			// isHorizontal = true;
			length = Math.abs(diffX);
		} else if (Math.abs(diffX) < Math.abs(diffY)) {
			isHorizontal = false;
			length = Math.abs(diffY);
		} else {
			length = Math.abs(diffX);
			if (simulator.grid[start.i + 1][start.j].content == EnumContent.EMPTY // start
																					// sprawdzic
					&& simulator.grid[start.i - 1][start.j].content == EnumContent.EMPTY)
				isHorizontal = false;

		}

		if (isHorizontal) {
			for (int i = 0; i < length; i++) {
				if (simulator.home != null) {
					if (simulator.grid[start.i + i * (diffX / length)][start.j].x == simulator.home.x
							&& simulator.grid[start.i + i * (diffX / length)][start.j].y == simulator.home.y) {
						isAllowed = false;
						break;
					}
				}
				for(GameObject go : simulator.buildings){
					if (simulator.grid[start.i + i * (diffX / length)][start.j].x == go.x
							&& simulator.grid[start.i + i * (diffX / length)][start.j].y == go.y) {
						isAllowed = false;
						break;
					}
				}
				if(!isAllowed)
					break;

			}

			for (int i = 0; i < length; i++) {
				Block b = null;
				for (int k = 0; k < 5; k++) {
					switch (k) {
					case 0:
						b = simulator.grid[start.i + i * (diffX / length)][start.j + 1];
						break;
					case 1:
						b = simulator.grid[start.i + i * (diffX / length) + 1][start.j];
						break;
					case 2:
						b = simulator.grid[start.i + i * (diffX / length)][start.j - 1];
						break;
					case 3:
						b = simulator.grid[start.i + i * (diffX / length) - 1][start.j];
						break;
					case 4:
						b = simulator.grid[start.i + i * (diffX / length)][start.j];
						break;
					}
					if (!buffer.contains(b))
						buffer.add(b);

				}

				if (creating)
					simulator.grid[start.i + i * (diffX / length)][start.j]
							.setContent(EnumContent.ROAD_HORIZONTAL, creating,
									isAllowed);
				else if (created && isAllowed)
					simulator.grid[start.i + i * (diffX / length)][start.j]
							.setContent(EnumContent.ROAD_HORIZONTAL, creating);
				// else if(created && !isAllowed)
			}
		} else {

			for (int i = 0; i < length; i++) {
				if (simulator.home != null) {
					if (simulator.grid[start.i][start.j + i * (diffY / length)].x == simulator.home.x
							&& simulator.grid[start.i][start.j + i
									* (diffY / length)].y == simulator.home.y) {
						isAllowed = false;
						break;
					}
				}
				for(GameObject go : simulator.buildings){
					if (simulator.grid[start.i][start.j + i * (diffY / length)].x == go.x
							&& simulator.grid[start.i][start.j + i * (diffY / length)].y == go.y) {
						isAllowed = false;
						break;
					}
				}
				if(!isAllowed)
					break;

			}

			for (int i = 0; i < length; i++) {
				Block b = null;
				for (int k = 0; k < 5; k++) {
					switch (k) {
					case 0:
						b = simulator.grid[start.i][start.j + i
								* (diffY / length) + 1];
						break;
					case 1:
						b = simulator.grid[start.i + 1][start.j + i
								* (diffY / length)];
						break;
					case 2:
						b = simulator.grid[start.i][start.j + i
								* (diffY / length) - 1];
						break;
					case 3:
						b = simulator.grid[start.i - 1][start.j + i
								* (diffY / length)];
						break;
					case 4:
						b = simulator.grid[start.i][start.j + i
								* (diffY / length)];
						break;
					}
					if (!buffer.contains(b))
						buffer.add(b);

				}

				if (creating)
					simulator.grid[start.i][start.j + i * (diffY / length)]
							.setContent(EnumContent.ROAD_VERTICAL, creating,
									isAllowed);
				else if (created && isAllowed)
					simulator.grid[start.i][start.j + i * (diffY / length)]
							.setContent(EnumContent.ROAD_VERTICAL, creating);
			}

		}
	}

	@Override
	public void update() {
		if (creating) {
			for (Block go : buffer) {
				simulator.grid[go.i][go.j].content = simulator.gridBuffer[go.i][go.j].content;
				simulator.grid[go.i][go.j].highlight = false;
			}
			buffer.clear();
			end = new Block(Mouse.getX() / Block.SIZE, Mouse.getY()
					/ Block.SIZE, simulator);
			setContent(start, end);
		} else if (created) {
			for (Block go : buffer) {
				simulator.grid[go.i][go.j].content = simulator.gridBuffer[go.i][go.j].content;
				simulator.grid[go.i][go.j].highlight = false;
			}
			buffer.clear();
			setContent(start, end);
			created = false;
			buffer.clear();
		}
	}

	@Override
	public void render() {

		for (int j = 1; j < Simulator.GRID_HEIGHT - 1; j++)
			for (int i = 1; i < Simulator.GRID_WIDTH - 1; i++)
				simulator.grid[i][j].render();

		for (Button go : buttons)
			go.render();

	}

}
