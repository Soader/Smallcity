package edu.simulator;

public abstract class GameObject {

	public GameObject() {
		// TODO Auto-generated constructor stub
	}
	
	protected int  x;
	protected int y;
	protected int sx;
	protected int sy;
	
	abstract void update();
	
	public void render(){
		Draw.Rect(x, y, sx, sy);
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getSX(){
		return sx;
	}
	
	public float getSY(){
		return sy;
	}
}
