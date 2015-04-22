package edu.simulator;

import java.awt.Rectangle;

import edu.simulator.GameObject;

public class Physics {
	
	public static boolean isCollided(int x, int y, int sx, int sy, GameObject go){
		Rectangle r1 = new Rectangle(x, y, sx, sy);
		Rectangle r2 = new Rectangle((int)go.x, (int)go.y, (int)go.sx, (int)go.sy);
		return r1.intersects(r2);
	}
	
	public static boolean isCollided(int x, int y, GameObject go2){
		return isCollided(x, y, 1, 1, go2);
		
	}
	
	public static boolean isCollided(GameObject go1, GameObject go2){
		
		Rectangle r1 = new Rectangle((int)go1.x, (int)go1.y, (int)go1.sx, (int)go1.sy);
		Rectangle r2 = new Rectangle((int)go2.x, (int)go2.y, (int)go2.sx, (int)go2.sy);
		return r1.intersects(r2);
	}
}
