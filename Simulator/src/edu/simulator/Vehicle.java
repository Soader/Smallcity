package edu.simulator;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.opengl.Texture;

import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;

public class Vehicle extends GameObject implements Runnable {

	int size = (Block.SIZE - 2) / 2 - 2;
	Pathfinding p;
	List<Block> path = new ArrayList<Block>();
	List<Block> pathA;
	List<Block> pathB;
	Simulator simulator;
	int i;
	int delay = 0;
	Texture texture;
	EnumNews lastStep;
	boolean isMoving;
	int edge;
	boolean isWaiting;
	private Block destination;
	private Supplier supplier;
	private Recipient recipient;
	public EnumDestinationState destState;
	public EnumDestinationState destStateBuffer;
	private boolean driveIn;
	private boolean driveOut;
	private int waitTime = 0;
	private int tempX;
	private int tempY;
	public float fuel;
	public final float fuelMax = 1000f;
	private Petrol petrol;
	private final float fuelConsumtion = 0.174f;
	private boolean refueling;
	private boolean changeX;
	private boolean needToFindPath;
	private Random rand = new Random();

	public Vehicle(Simulator simulator) {
		x = simulator.home.entrance.x;
		y = simulator.home.entrance.y;
		sx = Block.SIZE;
		sy = Block.SIZE;
		this.simulator = simulator;
		i = -1;
		isWaiting = true;
		destState = EnumDestinationState.NONE;
		fuel = rand.nextInt(600)+400;
		destination = new Block(0, 0, simulator);
		path.add(new Block(x/Block.SIZE, y/Block.SIZE, simulator));
		lastStep = EnumNews.EAST;
	}

	private void driveInFunc() {
		needToFindPath = true;
		driveIn = true;
		switch (destState) {
		case SUPPLIER: {
			if (Math.abs(x - (supplier.x + Block.SIZE/2)) > Math.abs(y - (supplier.y + Block.SIZE/2)))
				changeX = true;
			else
				changeX = false;
			destination.x = supplier.x + Block.SIZE/2;
			destination.y = supplier.y + Block.SIZE/2;
			waitTime = 60;
			break;
		}
		case RECIPIENT: {
			if (Math.abs(x - (recipient.x + Block.SIZE / 2)) > Math.abs(y
					- (recipient.y + Block.SIZE / 2)))
				changeX = true;
			else
				changeX = false;
			destination.x = recipient.x + Block.SIZE / 2;
			destination.y = recipient.y + Block.SIZE / 2;
			waitTime = 60;
			break;
		}
		case HOME: {
			if (Math.abs(x + size/2 - (simulator.home.x + Block.SIZE / 2)) > Math.abs(y + size/2
					- (simulator.home.y + Block.SIZE / 2)))
				changeX = true;
			else
				changeX = false;
			destination.x = simulator.home.x + Block.SIZE / 2;
			destination.y = simulator.home.y + Block.SIZE / 2;
			break;
		}
		case PETROL: {
			if (Math.abs(x - (petrol.x + Block.SIZE / 2)) > Math.abs(y
					- (petrol.y + Block.SIZE / 2)))
				changeX = true;
			else
				changeX = false;
			destination.x = petrol.x + Block.SIZE / 2;
			destination.y = petrol.y + Block.SIZE / 2;
			break;
		}
		}
	}

	private void driveOutFunc() {// tyl;ko dla i >2
		if (simulator.grid[destination.x / Block.SIZE][destination.y / Block.SIZE].x != path.get(path
				.size() - 2).x
				&& simulator.grid[destination.x / Block.SIZE][destination.y / Block.SIZE].y != path
						.get(path.size() - 2).y) {
			if(path.get(i-1).x > path.get(i).x){
				x = path.get(i).x+Block.SIZE/2 - size/2;
				if(y<(path.get(i).y + 1))
					y++;
				else if(y>(path.get(i).y + 1))
					y--;
				else{
					driveOut = false;
				}
			}
			else if(path.get(i-1).x < path.get(i).x){
				x = path.get(i).x+Block.SIZE/2;
				if(y<path.get(i).y + Block.SIZE - 1 - size)
					y++;
				else if(y>path.get(i).y + Block.SIZE - 1 - size)
					y--;
				else{
					driveOut = false;
				}
			}
			else if(path.get(i-1).y > path.get(i).y){
				y = path.get(i).y+Block.SIZE/2;
				if(x<path.get(i).x + Block.SIZE - 1 - size)
					x++;
				else if(x>path.get(i).x + Block.SIZE - 1 - size)
					x--;
				else{
					driveOut = false;
				}
			}
			else if(path.get(i-1).y < path.get(i).y){
				y = path.get(i).y+Block.SIZE/2;
				if(x<path.get(i).x + 1)
					x++;
				else if(x>path.get(i).x + 1)
					x--;
				else{
					driveOut = false;
				}
			}
			
		}
		else{	
			if(path.get(i-1).x > path.get(i).x){
				y = path.get(i).y+1;
				if(x<path.get(i).x)
					x++;
				else
					driveOut = false;
			}
			else if(path.get(i-1).x < path.get(i).x){
				y = path.get(i).y+Block.SIZE -1 - size;
				if(x>path.get(i).x)
					x--;
				else
					driveOut = false;
			}
			else if(path.get(i-1).y > path.get(i).y){
				x = path.get(i).x+Block.SIZE -1 - size;
				if(y<path.get(i).y)
					y++;
				else
					driveOut = false;
			}
			else if(path.get(i-1).y < path.get(i).y){
				x = path.get(i).x + 1;
				if(y>path.get(i).y + Block.SIZE - size)
					y--;
				else
					driveOut = false;
			}
		}

	}

	private void findPath() {
		needToFindPath = false;
		switch (destState) {
		case NONE:
			break;
		case SUPPLIER: {

			recipient = supplier.takeATask();
			p = new Pathfinding();
			path = p.findPath(simulator.grid, supplier.entrance,
					recipient.entrance);
			i = path.size() - 1;
			isWaiting = false;
			destState = EnumDestinationState.RECIPIENT;
			needPetrol();
			/*
			 * if (path.get(i).x < path.get(i - 1).x) { tempX = path.get(i).x;
			 * tempY = path.get(i).y + 1; } else if (path.get(i).x > path .get(i
			 * - 1).x) { tempX = path.get(i).x + Block.SIZE - size; tempY =
			 * path.get(i).y + Block.SIZE - size - 1; } else if (path.get(i).y <
			 * path .get(i - 1).y) { tempX = path.get(i).x + Block.SIZE - size -
			 * 1; tempY = path.get(i).y; } else if (path.get(i).y > path .get(i
			 * - 1).y) { tempX = path.get(i).x + 1; tempY = path.get(i).y +
			 * Block.SIZE - size; }
			 */
			break;
		}
		case RECIPIENT: {

			p = new Pathfinding();
			path = p.findPath(simulator.grid, recipient.entrance,
					simulator.home.entrance);
			i = path.size() - 1;
			isWaiting = false;
			destState = EnumDestinationState.HOME;
			needPetrol();
			break;
		}
		case PETROL: {
			refueling = true;
			break;
		}
		case HOME: {
			isWaiting = true;
			destState = EnumDestinationState.NONE;
			i = -1;
			break;
		}

		}
	}

	public void update() {

		while (true) {
			synchronized (this) {

				if (fuel <= 0) {
					if(fuel<0)
						fuel =0;
					// dead
				} else {

					if (driveIn) {
						if ((x + size/2) != destination.x || (y + size/2) != destination.y) {
							if (changeX) {
								if (x + size/2 < destination.x)
									x++;
								else if (x + size/2 > destination.x)
									x--;
								else
									changeX = false;
							} else {
								if (y + size/2 < destination.y)
									y++;
								else if (y + size/2 > destination.y)
									y--;
								else
									changeX = true;
							}
						} else if (waitTime > 0)
							waitTime--;

						else {
							if (needToFindPath)
								findPath();
							if (refueling) {
								petrol.refuel(this);
								if (fuel == fuelMax) {
									petrol.refuelingVehicle = null;
									destState = destStateBuffer;
									destStateBuffer = null;
									refueling = false;
									repath();
									driveIn = false;
									driveOut = true;
								}
							} else {
								driveIn = false;
								driveOut = true;
							}
						}
					} else if (driveOut && !isWaiting && path.size()>1) {
						driveOutFunc();
					} else {

						if (isWaiting) {
							int index = rand.nextInt(simulator.suppliers.size());
							Supplier go = simulator.suppliers.get(index);
								if (go.go()) {
									supplier = go;
									p = new Pathfinding();
									path = p.findPath(simulator.grid,
											simulator.home.entrance,
											supplier.entrance);
									i = path.size() - 1;
									isWaiting = false;
									isMoving = true;
									destState = EnumDestinationState.SUPPLIER;
									needPetrol();
									
								}
							
						} else {
							if (!isMoving) {

								/*
								 * p = new Pathfinding(); path =
								 * p.findPath(simulator.grid, simulator.grid[x /
								 * Block.SIZE][y / Block.SIZE],
								 * simulator.grid[7][8]); i = path.size() - 1;
								 * edge = Block.SIZE; isMoving = true;
								 */
							}
							if (i > 0) {
								fuel = fuel - fuelConsumtion;
								if (path.get(i).x < path.get(i - 1).x)
									move(EnumNews.EAST, path.get(i));
								else if (path.get(i).x > path.get(i - 1).x)
									move(EnumNews.WEST, path.get(i));
								else if (path.get(i).y < path.get(i - 1).y)
									move(EnumNews.NORTH, path.get(i));
								else if (path.get(i).y > path.get(i - 1).y)
									move(EnumNews.SOUTH, path.get(i));
								if (path.get(i).x != simulator.grid[x
										/ Block.SIZE][y / Block.SIZE].x
										|| path.get(i).y != simulator.grid[x
												/ Block.SIZE][y / Block.SIZE].y)
									i--;

							} else if (i == 0) {
								fuel = fuel - fuelConsumtion;
								if(path.size()<2)
									driveInFunc();
								
								else if (x + size / 2 != path.get(0).x + Block.SIZE
										/ 2
										&& y + size / 2 != path.get(0).y
												+ Block.SIZE / 2)
									move(lastStep, path.get(0));
								else {
									driveInFunc();
									i--;
								}

							}
						}
					}
				}
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private void repath() {
		switch (destState) {
		case SUPPLIER: {
			p = new Pathfinding();
			path = p.findPath(simulator.grid, petrol.entrance,
					supplier.entrance);
			i = path.size() - 1;
			edge = Block.SIZE;
			break;
		}
		case RECIPIENT: {
			p = new Pathfinding();
			path = p.findPath(simulator.grid, petrol.entrance,
					recipient.entrance);
			i = path.size() - 1;
			edge = Block.SIZE;
			break;
		}
		case HOME: {
			p = new Pathfinding();
			path = p.findPath(simulator.grid, petrol.entrance,
					simulator.home.entrance);
			i = path.size() - 1;
			edge = Block.SIZE;
			break;
		}
		}

	}

	private void driveaway() {

	}

	public void move(EnumNews direction, Block b) {
		texture = direction.texture;
		lastStep = direction;

		if (b.content == EnumContent.ROAD_CROSS) {
			moveCross(direction, b);
		} else {

			if (direction == EnumNews.NORTH) {
				y++;
				x = b.x + Block.SIZE - size - 1;
			}
			if (direction == EnumNews.EAST) {
				x++;
				y = b.y + 1;
			}
			if (direction == EnumNews.SOUTH) {
				x = b.x + 1;
				y--;
			}
			if (direction == EnumNews.WEST) {
				x--;
				y = b.y + Block.SIZE - size - 1;

			}
		}
	}

	private void moveCross(EnumNews direction, Block b) {
		if (direction == EnumNews.EAST)
			moveCrossEast(b);
		if (direction == EnumNews.WEST)
			moveCrossWest(b);
		if (direction == EnumNews.NORTH)
			moveCrossNorth(b);
		if (direction == EnumNews.SOUTH)
			moveCrossSouth(b);

	}

	private void moveCrossEast(Block b) {
		if (y == b.y + 1) {
			if (x < b.x + Block.SIZE) {
				x++;

			} else
				edge = 0;
		} else if (y - (b.y + 1) > 0) {
			y--;
		} else if (y - (b.y + 1) < 0) {
			y++;
		}
	}

	private void moveCrossWest(Block b) {
		if (y == b.y + Block.SIZE - size - 1) {
			if (x >= b.x) {
				x--;
			} else
				edge = 0;
		} else if (y - (b.y + Block.SIZE - size - 1) > 0) {
			y--;
		} else if (y - (b.y + Block.SIZE - size - 1) < 0) {
			y++;
		}
	}

	private void moveCrossNorth(Block b) {
		if (x == b.x + Block.SIZE - size - 1) {
			if (y < b.y + Block.SIZE) {
				y++;
			} else
				edge = 0;
		} else if (x - (b.x + Block.SIZE - size - 1) > 0) {
			x--;
		} else if (x - (b.x + Block.SIZE - size - 1) < 0) {
			x++;
		}
	}

	private void moveCrossSouth(Block b) { // zrobic
		if (x == b.x + 1) {
			if (y >= b.y) { // tu
				y--;
			} else
				edge = 0;
		} else if (x - (b.x + 1) > 0) {
			x--;
		} else if (x - (b.x + 1) < 0) {
			x++;
		}
	}

	private void needPetrol() {
		int size = Integer.MAX_VALUE;
		for (Petrol go : simulator.petrols) {
			p = new Pathfinding();
			pathA = p.findPath(simulator.grid, path.get(0), go.entrance);
			if (pathA.size() < size)
				size = pathA.size();
		}
		if (((path.size() + size) * Block.SIZE * fuelConsumtion) * 1.1 > fuel) {
			destStateBuffer = destState;
			destState = EnumDestinationState.PETROL;
			size = Integer.MAX_VALUE;
			for (Petrol go : simulator.petrols) {
				p = new Pathfinding();
				pathA = p.findPath(simulator.grid, path.get(path.size() - 1),
						go.entrance);
				if (pathA.size() < size) {
					size = pathA.size();
					path = pathA;
					petrol = go;
				}
			}
			i = path.size() - 1;
		}

	}

	public void render() {
		/*if (!path.isEmpty()) {
			glColor4f(1f, 0f, 0f, 0.1f);
			for (Block go : path) {
				Draw.Rect(go.x, go.y, Block.SIZE, Block.SIZE);
			}
		}*/
		glColor3f(1f, fuel / fuelMax * 1f, fuel / fuelMax * 1f);
		Draw.Rect(x, y, size, size);
		if (destState == EnumDestinationState.RECIPIENT
				|| destStateBuffer == EnumDestinationState.RECIPIENT) {
			glColor3f(0f, 0f, 0f);
			Draw.Rect(x + size / 2 - 1, y + size / 2 - 1, 3, 3);
		}
	}

	@Override
	public void run() {
		update();

	}
}
