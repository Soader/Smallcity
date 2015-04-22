package edu.simulator;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.awt.Graphics;
import java.io.InputStream;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.FontMetrics;

public class NString {


	public static TrueTypeFont standardFont;
	public static TrueTypeFont standardSmallFont;
	public static TrueTypeFont standardMediumFont;
	public static Font font;
	public String text;
	public float x;
	public float y;
	public int width;
	public int height;
	public TrueTypeFont ttf;
	public Color color;
	private static boolean antiAlias = true;
	
	public NString(String text, float x, float y, TrueTypeFont font, Color color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = font.getWidth(text);
		this.height = font.getHeight(text);
		this.ttf = font;
		this.color = color;
		
	}
	
	public NString(String text, float x, float y, TrueTypeFont font) {
		this(text, x , y, font, Color.black);
		
	}
	
	public NString(String text, float x, float y) {
		this(text, x , y, NString.standardFont, Color.black);
		
	}
	
	public static void initNString(){
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);        
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);                    
 
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                
        glClearDepth(1);                                       
 
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
 
        glViewport(0,0,Display.getWidth(),Display.getHeight());

	/*	Font awtFont = new Font("Monospaced", Font.PLAIN, 24);
		font = new TrueTypeFont(awtFont, true);*/	
		
		
		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream("Action_Man_Bold.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			font = font.deriveFont(24f); 
			standardFont = new TrueTypeFont(font, antiAlias);
			font = font.deriveFont(12f);
			standardSmallFont = new TrueTypeFont(font, antiAlias);
			font = font.deriveFont(18f);
			standardMediumFont = new TrueTypeFont(font, antiAlias);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void renderCenter(String s, float x, float y, TrueTypeFont font, Color color){
		int w = font.getWidth(s);
		render(s, x - w/2, y, font, color);
	}
	
	public static void renderCenter(String s, float x, float y){
		
		renderCenter(s, x, y, standardFont);
	}
	
	
	public static void renderCenter(String s, float x, float y, TrueTypeFont font){
		int w = font.getWidth(s);
		render(s, x - w/2, y, font, Color.black);
	}
	
	public static void renderCenter(NString str){
		int w = str.ttf.getWidth(str.text);
		render(str.text, str.x - w/2, str.y, str.ttf, str.color);
	}
	
	public static void render(String s, float x, float y, TrueTypeFont font){
		render(s, x, y, font, Color.black);
	}
	
	public static void render(String s, float x, float y){
		render(s, x, y, standardFont, Color.black);
	}
	
	
	public static void render(NString str){
		render(str.text, str.x, str.y, str.ttf, str.color);
		
		
	}
	
	public static void render(String s, float x, float y, TrueTypeFont font, Color color){
		int h = font.getHeight(s);
		int w = font.getWidth(s);
		Color.white.bind();
		glEnable(GL_TEXTURE_2D);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		font.drawString(x-4, Display.getHeight() - y - h/2, s, color);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(0.7f,1,0.6f,1);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_TEXTURE_2D);
		
		
	}

}
