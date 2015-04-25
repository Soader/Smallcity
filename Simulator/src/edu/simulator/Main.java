package edu.simulator;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Main {
	private Simulator simulator;
	
	public Main(Simulator simulator){
		this.simulator = simulator;
	}
	
	public static void main(String[] args) {
	
		initDisplay();
		initGL();
		NString.initNString();
		Main main = new Main(new Simulator());
		main.loop();
		cleanUp();

	}
	
	private static void initDisplay(){
		
		try{
			setDisplayMode(1100, 650);
			Display.setTitle("Simulator");
			Display.create();
			Display.setVSyncEnabled(true);
		}
		catch (LWJGLException ex){
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}						
	}
	
	private static void initGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,Display.getWidth(),0,Display.getHeight(),1,-1);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(0.7f,1,0.6f,1);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void loop(){
		while(!Display.isCloseRequested()){
			simulator.getInput();
			simulator.update();
			glClearColor(0.45f,0.71f,0.89f,1);
			glClear(GL_COLOR_BUFFER_BIT);
			glLoadIdentity();
			simulator.render();
			Display.update();
			Display.sync(60);
		}
	}
	
	public static void setDisplayMode(int width, int height) {

	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height)) {
		    return;
	    }

	    try {
	        DisplayMode targetDisplayMode = new DisplayMode(width,height); 
	        Display.setDisplayMode(targetDisplayMode);
	        
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+ e);
	    }
	}
	
	private static void cleanUp(){
		Display.destroy();
	}

}


