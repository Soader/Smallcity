package edu.simulator;

public class Road {
	public int x;
	public int y;
	private int length;
	private boolean isHorizontal;
	private Block[][] grid;
	public Block end;
	public Block start;
	
	
	
	public Road(int x, int y, int length, boolean isHorizontal, Simulator simulator){
		if(x>=Simulator.GRID_WIDTH || x <0)
			this.x = 0;
		else 
			this.x = x;	
		if(y>=Simulator.GRID_HEIGHT || y < 0)
			this.y = 0;
		else 
			this.y = y;
		
		
		
		this.length = length;
		this.isHorizontal = isHorizontal;
		this.grid = simulator.grid;
		start = grid[this.x][this.y];
		setContent();
	}
	
	public Road(Block b, int length, boolean isHorizontal, Simulator simulator){
		this(b.x/Block.SIZE, b.y/Block.SIZE, length, isHorizontal, simulator);
	}
	
	private void setContent(){
		
		
		if(isHorizontal){
			for(int i=0; i<length;i++)
				if(x+i< Simulator.GRID_WIDTH){
						grid[x+i][y].setContent(EnumContent.ROAD_HORIZONTAL);
						end = grid[x+i][y];
				}
		}
		else{
			for(int i=0; i<length;i++)
				if(y+i< Simulator.GRID_HEIGHT){
						grid[x][y+i].setContent(EnumContent.ROAD_VERTICAL);
						end = grid[x][y+i];
				}
		}
	}
	
	public void render(){
		if(isHorizontal){
			for(int i=0; i<length;i++)
				if(x+i< Simulator.GRID_WIDTH)
				grid[x+i][y].render();
			
		}
		else{
			for(int i=0; i<length;i++)
				if(y+i< Simulator.GRID_HEIGHT)
				grid[x][y+i].render();
		}
	}
}
