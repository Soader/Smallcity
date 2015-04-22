package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

public class Simulator {
	public static final int PANEL_WIDTH = 200;
	public static final int PANEL_HEIGHT = Display.getHeight();
	public static final int GRID_WIDTH = (Display.getWidth() - PANEL_WIDTH)
			/ Block.SIZE;
	public static final int GRID_HEIGHT = Display.getHeight() / Block.SIZE;
	public static int VEHICLES = 10;
	public Block[][] grid;
	public Block[][] gridBuffer;
	public List<Button> buttons = new ArrayList<Button>();
	public List<Supplier> suppliers = new ArrayList<Supplier>();
	public List<Recipient> recipients = new ArrayList<Recipient>();
	public List<Petrol> petrols = new ArrayList<Petrol>();
	public List<GameObject> buildings = new ArrayList<GameObject>();
	public Home home;
	Panel mainPan;
	Panel background;
	Panel gridBackground;
	Mode mode;
	EditMode editMode;
	RoadEditMode roadEditMode;
	PlayMode playMode;
	DestroyEditMode destroyEditMode;
	SupplierEditMode supplierEditMode;
	RecipientEditMode recipientEditMode;
	PetrolEditMode petrolEditMode;
	HomeEditMode homeEditMode;

	public Simulator() {
		grid = new Block[GRID_WIDTH][GRID_HEIGHT];
		gridBuffer = new Block[GRID_WIDTH][GRID_HEIGHT];
		for (int j = 0; j < GRID_HEIGHT; j++)
			for (int i = 0; i < GRID_WIDTH; i++) {
				grid[i][j] = new Block(i, j, this);
				gridBuffer[i][j] = new Block(i, j, this);
			}
		background = new Panel(0, 0, Display.getWidth(), Display.getHeight(),
				EnumSim_Texture.BACKGROUND.texture);
		mainPan = new Panel(Display.getWidth() - PANEL_WIDTH,
				Display.getHeight(), PANEL_WIDTH, PANEL_HEIGHT,
				EnumSim_Texture.ROAD_C.texture);
		gridBackground = new Panel(grid[1][1].x, grid[1][1].y, Block.SIZE
				* (GRID_WIDTH - 2), Block.SIZE * (GRID_HEIGHT - 2),
				EnumSim_Texture.GRIDBACKGROUND.texture);
		if (GRID_WIDTH >= 45 && GRID_HEIGHT >= 32) {
			addRoads();
			setBuildings();
		}

		editMode = new EditMode(this);
		playMode = new PlayMode(this);
		roadEditMode = new RoadEditMode(this);
		destroyEditMode = new DestroyEditMode(this);
		supplierEditMode = new SupplierEditMode(this);
		recipientEditMode = new RecipientEditMode(this);
		petrolEditMode = new PetrolEditMode(this);
		homeEditMode = new HomeEditMode(this);
		mode = editMode;
		editMode.addButtons();
		roadEditMode.addButtons();
		destroyEditMode.addButtons();
		supplierEditMode.addButtons();
		recipientEditMode.addButtons();
		petrolEditMode.addButtons();
		homeEditMode.addButtons();

	}

	public void getInput() {
		mode.getInput();
	}

	public void update() {
		if (mode != playMode)
			playTexture();
		mode.update();
	}

	private void playTexture() {
		if (home == null || suppliers.size() == 0 || recipients.size() == 0
				|| petrols.size() == 0) {
			for (Button go : mode.buttons)
				if (go.mode == playMode
						&& go.texture == EnumSim_Texture.BUTTON_PLAY.texture) {
					go.texture = EnumSim_Texture.BUTTON_PLAY_INACT.texture;
					go.isActive = false;
				}

		} else {
			for (Button go : mode.buttons)
				if (go.mode == playMode
						&& go.texture == EnumSim_Texture.BUTTON_PLAY_INACT.texture) {
					go.texture = EnumSim_Texture.BUTTON_PLAY.texture;
					go.isActive = true;
				}

		}
	}

	private void addRoads() {
		// Horizontal
		setContent(3, 4, 6, true);
		setContent(8, 2, 17, true);
		setContent(8, 10, 5, true);
		setContent(12, 9, 8, true);
		setContent(12, 12, 8, true);
		setContent(19, 10, 6, true);
		setContent(8, 6, 10, true);
		setContent(17, 5, 13, true);
		setContent(30, 9, 4, true);
		setContent(33, 4, 5, true);
		setContent(33, 12, 9, true);
		setContent(37, 7, 5, true);
		setContent(35, 15, 4, true);
		setContent(38, 17, 4, true);
		setContent(30, 22, 3, true);
		setContent(32, 24, 4, true);
		setContent(32, 20, 4, true);
		setContent(29, 28, 12, true);
		setContent(25, 25, 5, true);
		setContent(3, 15, 7, true);
		setContent(3, 18, 21, true);
		setContent(23, 14, 7, true);
		setContent(7, 24, 12, true);
		setContent(3, 28, 5, true);
		setContent(18, 22, 5, true);
		setContent(20, 27, 3, true);
		setContent(20, 29, 4, true);

		// Vertical
		setContent(3, 4, 20, false);
		setContent(8, 2, 9, false);
		setContent(24, 2, 9, false);
		setContent(12, 9, 4, false);
		setContent(19, 9, 4, false);
		setContent(21, 5, 3, false);
		setContent(29, 1, GRID_HEIGHT - 2, false);
		setContent(33, 4, 9, false);
		setContent(37, 4, 9, false);
		setContent(41, 7, 11, false);
		setContent(35, 12, 4, false);
		setContent(38, 16, 1, false);
		setContent(32, 20, 5, false);
		setContent(35, 20, 5, false);
		setContent(40, 21, 8, false);
		setContent(25, 21, 5, false);
		setContent(9, 15, 4, false);
		setContent(23, 14, 5, false);
		setContent(18, 18, 7, false);
		setContent(12, 22, 3, false);
		setContent(7, 24, 5, false);
		setContent(22, 22, 6, false);
		setContent(20, 27, 3, false);
	}

	private void setBuildings() {
		// Supliers
		Supplier s = new Supplier(4 * Block.SIZE, 29 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		suppliers.add(s);
		buildings.add(s);
		s = new Supplier(3 * Block.SIZE, 24 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		suppliers.add(s);
		buildings.add(s);
		s = new Supplier(12 * Block.SIZE, 21 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		suppliers.add(s);
		buildings.add(s);
		s = new Supplier(23 * Block.SIZE, 30 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		suppliers.add(s);
		buildings.add(s);
		s = new Supplier(8 * Block.SIZE, 16 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		suppliers.add(s);
		buildings.add(s);
		s = new Supplier(39 * Block.SIZE, 25 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		suppliers.add(s);
		buildings.add(s);
		s = new Supplier(41 * Block.SIZE, 22 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		suppliers.add(s);
		buildings.add(s);

		// Recipients
		Recipient r = new Recipient(13 * Block.SIZE, 11 * Block.SIZE,
				Block.SIZE, Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(9 * Block.SIZE, 1 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(16 * Block.SIZE, 5 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(21 * Block.SIZE, 8 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(22 * Block.SIZE, 11 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(34 * Block.SIZE, 10 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(35 * Block.SIZE, 5 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(40 * Block.SIZE, 8 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(40 * Block.SIZE, 14 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);
		r = new Recipient(37 * Block.SIZE, 16 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		recipients.add(r);
		buildings.add(r);

		// Petrols
		Petrol p = new Petrol(26 * Block.SIZE, 21 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		petrols.add(p);
		buildings.add(p);
		p = new Petrol(4 * Block.SIZE, 10 * Block.SIZE, Block.SIZE, Block.SIZE,
				this);
		petrols.add(p);
		buildings.add(p);
		p = new Petrol(39 * Block.SIZE, 11 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
		petrols.add(p);
		buildings.add(p);

		// Home
		home = new Home(33 * Block.SIZE, 22 * Block.SIZE, Block.SIZE,
				Block.SIZE, this);
	}

	private void setContent(int x, int y, int length, boolean isHorizontal) { // do
																				// wywalenia

		if (isHorizontal) {
			for (int i = 0; i < length; i++)
				if (x + i < Simulator.GRID_WIDTH) {
					grid[x + i][y].setContent(EnumContent.ROAD_HORIZONTAL);
					// end = grid[x+i][y];
				}
		} else {
			for (int i = 0; i < length; i++)
				if (y + i < Simulator.GRID_HEIGHT) {
					grid[x][y + i].setContent(EnumContent.ROAD_VERTICAL);
					// end = grid[x][y+i];
				}
		}
	}

	public void drawFrames() {
		float size = 6;
		glColor4f(1, 1, 1, 1f); // left, up, right, down
		Draw.Rect(grid[1][1].x - size / 2, grid[1][1].y - size / 2, size,
				(GRID_HEIGHT - 2) * Block.SIZE + size,
				EnumSim_Texture.FRAME_LEFT.texture);
		Draw.Rect(grid[1][GRID_HEIGHT - 1].x - size / 2,
				grid[1][GRID_HEIGHT - 1].y - size / 2, (GRID_WIDTH - 2)
						* Block.SIZE + size, size,
				EnumSim_Texture.FRAME_UP.texture);
		Draw.Rect(grid[GRID_WIDTH - 1][1].x - size / 2,
				grid[GRID_WIDTH - 1][1].y - size / 2, size, (GRID_HEIGHT - 2)
						* Block.SIZE + size,
				EnumSim_Texture.FRAME_RIGHT.texture);
		Draw.Rect(grid[1][1].x - size / 2, grid[1][1].y - size / 2,
				(GRID_WIDTH - 2) * Block.SIZE + size, size,
				EnumSim_Texture.FRAME_DOWN.texture);

	}

	public void render() {
		background.render();
		gridBackground.render();
		mode.render();
		
		for (GameObject go : buildings)
			go.render();
		/*
		 * for(Supplier go : suppliers) go.render(); for(Recipient go :
		 * recipients) go.render(); for(Petrol go : petrols) go.render();
		 */
		if (home != null)
			home.render();
		drawFrames();

	}
}
