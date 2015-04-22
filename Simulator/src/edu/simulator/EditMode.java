package edu.simulator;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class EditMode extends Mode{
	
	
	public EditMode(){
		
	}
	
	
	public EditMode(Simulator simulator){
		this.simulator = simulator;
		this.buttons = new ArrayList<Button>();
		//addButtons();
	}

	public void addButtons(){
		Button but_road = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2 - 70 - Block.SIZE/2, 280, 40, 40, simulator.roadEditMode, simulator, EnumSim_Texture.BUTTON_ROAD.texture, EnumSim_Texture.BUTTON_ROAD_ACTIVE.texture); this.buttons.add(but_road);
		Button but_play = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2 - 20 - Block.SIZE/2, 130, 40, 40, simulator.playMode, simulator, EnumSim_Texture.BUTTON_PLAY.texture); this.buttons.add(but_play);
		Button but_destro = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2 - 70 - Block.SIZE/2, 230, 40, 40, simulator.destroyEditMode, simulator, EnumSim_Texture.BUTTON_DESTROY.texture, EnumSim_Texture.BUTTON_DESTROY_ACTIVE.texture); this.buttons.add(but_destro);
		Button but_supplier = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2 - 20 - Block.SIZE/2, 280, 40, 40, simulator.supplierEditMode, simulator, EnumSim_Texture.BUTTON_SUPPLIER.texture, EnumSim_Texture.BUTTON_SUPPLIER_ACTIVE.texture); this.buttons.add(but_supplier);
		Button but_recipient = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2  + 30 - Block.SIZE/2, 280, 40, 40, simulator.recipientEditMode, simulator, EnumSim_Texture.BUTTON_RECIPIENT.texture, EnumSim_Texture.BUTTON_RECIPIENT_ACTIVE.texture); this.buttons.add(but_recipient);
		Button but_petrol = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2 - 20 - Block.SIZE/2, 230, 40, 40, simulator.petrolEditMode, simulator, EnumSim_Texture.BUTTON_PETROL.texture, EnumSim_Texture.BUTTON_PETROL_ACTIVE.texture); this.buttons.add(but_petrol);
		Button but_home = new Button(Display.getWidth() - Simulator.PANEL_WIDTH/2 + 30 - Block.SIZE/2, 230, 40, 40, simulator.homeEditMode, simulator, EnumSim_Texture.BUTTON_HOME.texture, EnumSim_Texture.BUTTON_HOME_ACTIVE.texture); this.buttons.add(but_home);
	}
		
	@Override
	public void getInput() {
		Keyboard.next(); // clears keyboard buffer
		boolean skip = false;
		while (Mouse.next()){
		    if (Mouse.getEventButtonState()) {
		        if (Mouse.getEventButton() == 0) {
		        	for(Button go : this.buttons)
						if(go.isActive && Physics.isCollided(Mouse.getEventX(), Mouse.getEventY(), go)){
							go.isPressed = true;
							skip = true;
							break;
					}
		        	if(!skip){
		        		
		        	}
		        	skip = false;
		        }
		    }else {
		        if (Mouse.getEventButton() == 0) {
			        	for(Button go : this.buttons)
			        		if(go.isPressed){
			        			go.isPressed = false;
			        			if(Physics.isCollided(Mouse.getEventX(), Mouse.getEventY(), go)){
			        				go.click();
			        				skip = true;
								}
			        			break;
			        		}
			        	if(!skip){
			        		//TODO delete
			        	}
			        	skip = false;
		        }
		    }
		}
	}
	
	


	
	
		
	@Override
	public void update() {

	}

	@Override
	public void render() {
	
		for(int j = 1; j<Simulator.GRID_HEIGHT- 1;j++)
			for(int i = 1; i<Simulator.GRID_WIDTH - 1;i++)
				simulator.grid[i][j].render();
		
		
		for(Button go : buttons)
			go.render();
		
	}
}
