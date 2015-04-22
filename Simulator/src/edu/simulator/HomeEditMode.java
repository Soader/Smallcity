package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class HomeEditMode extends EditMode{
	private static final int RecSX = 1;
	private static final int RecSY = 1;
	private boolean creating;
	private int RecX;
	private int RecY;
	private boolean isAllowed;
	private Button add;
	private Button sub;
	private List<Button> calcButtons;
	private boolean isOut;

	public HomeEditMode(Simulator simulator) {
		this.simulator = simulator;
		creating = false;
		buttons = new ArrayList<Button>();
		calcButtons = new ArrayList<Button>();
		
	}

	public void addButtons() {
		for (Button go : simulator.editMode.buttons)
			buttons.add(new Button(go.x, go.y, go.sx, go.sy, go.mode,
					go.simulator, go.texture, go.texture_active));
		add = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2 + 25, 400, 20, 20, true, simulator, EnumSim_Texture.BUTTON_PLUS.texture);
		calcButtons.add(add);
		sub = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2 - 25 -20, 400, 20, 20, false, simulator, EnumSim_Texture.BUTTON_MINUS.texture);
		calcButtons.add(sub);
	}

	@Override
	public void getInput() {
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
					for(Button go : calcButtons)
						if(Physics.isCollided(Mouse.getEventX(),
								Mouse.getEventY(), go)){
							go.isPressed = true;
							skip = true;
							break;
						}
					if (!skip) {
						pressedPetrol(Mouse.getEventX(), Mouse.getEventY());
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
					for(Button go : calcButtons)
						if (go.isPressed) {
							go.isPressed = false;
							if (Physics.isCollided(Mouse.getEventX(),
									Mouse.getEventY(), go)) {
								go.click2();
								skip = true;
							}
							break;
						}
					if (!skip) {
						releasedPetrol(Mouse.getEventX(), Mouse.getEventY());
					}
					skip = false;
				}
			}
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ADD) {
					if(Simulator.VEHICLES < 80)
						Simulator.VEHICLES++;	
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_SUBTRACT) {
					if(Simulator.VEHICLES > 1)
						Simulator.VEHICLES--;	

				}


			}
		}
	}
	
	
	private void checkNearby(GameObject go, int i, int j){
		if(go.x/Block.SIZE == i && go.y/Block.SIZE == j){
			isAllowed = false;
			return;
		}
		if(go.x/Block.SIZE == i-1 && go.y/Block.SIZE == j){
			isAllowed = false;
			return;
		}
		if(go.x/Block.SIZE == i+1 && go.y/Block.SIZE == j){
			isAllowed = false;
			return;
		}
		if(go.x/Block.SIZE == i && go.y/Block.SIZE == j-1){
			isAllowed = false;
			return;
		}
		if(go.x/Block.SIZE == i && go.y/Block.SIZE == j+1){
			isAllowed = false;
			return;
		}
		if(go.x/Block.SIZE == i-1 && go.y/Block.SIZE == j+1){
			isAllowed = false;
			return;
		}
		if(go.x/Block.SIZE == i+1 && go.y/Block.SIZE == j+1){
			isAllowed = false;
			return;
		}
		if(go.x/Block.SIZE == i-1 && go.y/Block.SIZE == j-1){
			isAllowed = false;
			return;
		}
		if(go.x/Block.SIZE == i+1 && go.y/Block.SIZE == j-1){
			isAllowed = false;
			return;
		}
	}

	private void pressedPetrol(int mouseX, int mouseY) {
		int i = mouseX / Block.SIZE;
		int j = mouseY / Block.SIZE;
		isAllowed = true;
		if (i < Simulator.GRID_WIDTH - 1 && i > 0
				&& j < Simulator.GRID_HEIGHT - 1 && j > 0) {

			
			for(GameObject go : simulator.buildings){
				checkNearby(go, i, j);
				if(!isAllowed)
					break;
			}
			
			if (isAllowed) {
				if (simulator.grid[i][j].content == EnumContent.EMPTY) {
					if (simulator.grid[i - 1][j].content != EnumContent.EMPTY
							|| simulator.grid[i + 1][j].content != EnumContent.EMPTY
							|| simulator.grid[i][j - 1].content != EnumContent.EMPTY
							|| simulator.grid[i][j + 1].content != EnumContent.EMPTY) {
						isAllowed = true;
					} else
						isAllowed = false;
				} else
					isAllowed = false;

			}
			if (isAllowed) {
				creating = true;
				RecX = i;
				RecY = j;
			}

		}
	}

	private void releasedPetrol(int mouseX, int mouseY) {
		int i = mouseX / Block.SIZE;
		int j = mouseY / Block.SIZE;
		if (creating) {
			if (i == RecX && j == RecY) {
				simulator.home = new Home(i * Block.SIZE, j
						* Block.SIZE, RecSX * Block.SIZE, RecSY * Block.SIZE, 
						simulator);

			}
		}
		creating = false;
	}

	@Override
	public void update() {
		isAllowed = true;
		isOut = false;
		if (!creating) {
			int i = Mouse.getX() / Block.SIZE;
			int j = Mouse.getY() / Block.SIZE;
			if (i > Simulator.GRID_WIDTH - 2){
				isOut = true;
				i = Simulator.GRID_WIDTH - 2;
			}
			if (i < 1){
				isOut = true;
				i = 1;
			}
			if (j > Simulator.GRID_HEIGHT - 2){
				isOut = true;
				j = Simulator.GRID_HEIGHT - 2;
			}
			if (j < 1){
				isOut = true;
				j = 1;
			}
			RecX = i;
			RecY = j;
			
			
			for(GameObject go : simulator.buildings){
				checkNearby(go, i, j);
				if(!isAllowed)
					break;
			}
			
			if (isAllowed) {
				if (simulator.grid[i][j].content == EnumContent.EMPTY) {
					if (simulator.grid[i - 1][j].content != EnumContent.EMPTY
							|| simulator.grid[i + 1][j].content != EnumContent.EMPTY
							|| simulator.grid[i][j - 1].content != EnumContent.EMPTY
							|| simulator.grid[i][j + 1].content != EnumContent.EMPTY) {
						isAllowed = true;
					} else
						isAllowed = false;
				} else
					isAllowed = false;

			}


		}
	}

	@Override
	public void render() {
		for (int j = 1; j < Simulator.GRID_HEIGHT - 1; j++)
			for (int i = 1; i < Simulator.GRID_WIDTH - 1; i++)
				simulator.grid[i][j].render();
		for (Button go : buttons)
			go.render();
		for (Button go : calcButtons)
			go.render();
		if (isAllowed)
			glColor4f(0f, 1f, 0f, 0.8f);
		else
			glColor4f(1f, 0f, 0f, 0.8f);
		if(!isOut)Draw.Rect(RecX * Block.SIZE, RecY * Block.SIZE, RecSX * Block.SIZE,
				RecSY * Block.SIZE, EnumSim_Texture.HOME.texture);
		glColor4f(1f, 1f, 1f, 1f);
		NString.renderCenter(""+simulator.VEHICLES, Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2, 410, NString.standardFont, Color.white);
		NString.renderCenter("Number of vehicles", Display.getWidth() - Simulator.PANEL_WIDTH/2 - Block.SIZE/2, 445, NString.standardMediumFont, Color.white);
	}
}
