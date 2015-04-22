package edu.simulator;

import java.util.ArrayList;
import java.util.List;

public class Pathfinding {

	private Block[][] grid;
	private Block start;
	private Block goal;
	private List<Block> path = new ArrayList<Block>();
	private List<Block> openList = new ArrayList<Block>();
	private List<Block> closedList = new ArrayList<Block>();

	public List<Block> findPath(Block[][] gridS, Block startS, Block goalS) {
		grid = new Block[Simulator.GRID_WIDTH][Simulator.GRID_HEIGHT];
		for(int d = 0; d< Simulator.GRID_HEIGHT;d++)
			for(int c = 0; c< Simulator.GRID_WIDTH;c++)
				grid[c][d] = new Block(gridS[c][d]);
		
		start = grid[startS.i][startS.j];
		goal = grid[goalS.i][goalS.j];
		
		
		if (!(start.content == EnumContent.ROAD_HORIZONTAL
				|| start.content == EnumContent.ROAD_VERTICAL || start.content == EnumContent.ROAD_CROSS)) {
			return null;
		}
		if (!(goal.content == EnumContent.ROAD_HORIZONTAL
				|| goal.content == EnumContent.ROAD_VERTICAL || goal.content == EnumContent.ROAD_CROSS)) {
			return null;
		}
		boolean isClosedList = false;
		boolean isOpenList = false;
		boolean checking = true;
		Block lowerf;
		int pass = 0;
		int i;
		int j;
		Block b;
		i = start.x / Block.SIZE;
		j = start.y / Block.SIZE;
		int gx = goal.x / Block.SIZE;
		int gy = goal.y / Block.SIZE;
		// int diagonal = (int)Math.sqrt(Block.SIZE*Block.SIZE +
		// Block.SIZE*Block.SIZE);
		int diagonal = 150;
		for (int l = 0; l < Simulator.GRID_HEIGHT; l++)
			for (int k = 0; k < Simulator.GRID_WIDTH; k++)
				grid[k][l].h = 1 * (Math.abs(gx - k) + Math.abs(gy - l));
		
		while (checking) {

			if (!openList.isEmpty()) {

				lowerf = openList.get(0);
				for (Block go : openList)
					if (go.f < lowerf.f)
						lowerf = go;

				openList.remove(lowerf);

			} else {
				if(closedList.size()>0)
					return null;
				lowerf = grid[i][j];
			}
			closedList.add(lowerf);
			i = lowerf.x / Block.SIZE; 
			j = lowerf.y / Block.SIZE;

			for (int c = i - 1; c <= i + 1; c++)
				for (int d = j - 1; d <= j + 1; d++)
					if ((c >= 0 && d >= 0)
							&& (c < Simulator.GRID_WIDTH && d < Simulator.GRID_HEIGHT)
							&& (c != i || d != j) && (c == i || d == j)) {
						b = grid[c][d];
						isClosedList = false;
						isOpenList = false;
						if (b.equals(goal)) { // skosy
							goal.parent = lowerf;
							checking = false;
						}

						if (b.content == EnumContent.ROAD_CROSS
								|| b.content == EnumContent.ROAD_HORIZONTAL
								|| b.content == EnumContent.ROAD_VERTICAL) {
							// System.out.println(i +" " + j +" (" +
							// b.x/Block.SIZE + ", " + b.y/Block.SIZE + ")" +
							// " H: " + b.h + " F: " + b.f );
							for (Block go : closedList)
								if (go.equals(b)) {
									isClosedList = true;
									break;
								}

							for (Block go : openList)
								if (go.equals(b)) {
									isOpenList = true;
									break;
								}
							if (!isClosedList) {

								if (isOpenList) {
									if (c != i && d != j)
										pass = diagonal;
									else
										pass = Block.SIZE; // checking for
															// shorter path
									if (grid[i][j].g + pass <= b.g) {
										b.parent = grid[i][j];
										b.g = grid[i][j].g + pass;
										b.f = b.g + b.h;
									}

								} else {
									if (c != i && d != j)
										b.g = diagonal + grid[i][j].g;
									else
										b.g = Block.SIZE + grid[i][j].g;
									b.f = b.h + b.g;
									b.parent = grid[i][j];
									openList.add(b);
									
								}
							}

						}
					}
		}
		addParent(goal);
		
		return path;

	}

	private void addParent(Block b) {
		
			path.add(b);
			if (!b.equals(start)) {
			addParent(b.parent);
		}
	}
}
