package redmart_problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		SkiPoint[][] skiGrid;
		
		try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
			String firstLine = buffer.readLine();
			
			String[] sizeStr = firstLine.trim().split(" ");
			
			int[] size = new int[] { Integer.valueOf(sizeStr[0]), Integer.valueOf(sizeStr[1]) };
			
			skiGrid = new SkiPoint[size[0]][size[1]];
			
			for(int i=0;i<size[0];i++) {
				String row = buffer.readLine();
				String[] col = row.split(" ");
				
				for(int j=0;j<col.length;j++) {
					skiGrid[i][j] = new SkiPoint(i, j, Integer.valueOf(col[j]));
				}
			}
		}
		
		List<SkiPoint> solution = solve(skiGrid);
		
		System.out.println(solution);
		System.out.println(String.format("%d%d", solution.size(), solution.get(0).height - solution.get(solution.size() - 1).height));
	}

	
	private static List<SkiPoint> solve(SkiPoint[][] skiGrid) {
		
		List<SkiPoint> maxRoute = new ArrayList<>();
		
		List<SkiPoint> points = new ArrayList<>(skiGrid.length * skiGrid[0].length);
		for(int i=0;i<skiGrid.length;i++) {
			for(int j=0;j<skiGrid[0].length;j++) {
				points.add(skiGrid[i][j]);
			}
		}
		
		Collections.sort(points);

		for(SkiPoint point : points) {
			List<SkiPoint> curRoute = findRoute(skiGrid, point, new ArrayList<SkiPoint>());
			if(curRoute.size() > maxRoute.size()) {
				maxRoute = curRoute;
			} else if(curRoute.size() == maxRoute.size()) {
				
				int curRouteDiff = curRoute.get(0).height - curRoute.get(curRoute.size() - 1).height; 
				int maxRouteDiff = maxRoute.get(0).height - maxRoute.get(maxRoute.size() - 1).height;
				
				if(curRouteDiff > maxRouteDiff) {
					maxRoute = curRoute;
				}
			}
		}
		
		return maxRoute;
	}
	
	
	private static List<SkiPoint> findRoute(SkiPoint[][] skiGrid, SkiPoint curPoint, List<SkiPoint> route) {
		List<SkiPoint> maxRoute = new ArrayList<>();
		
		route.add(curPoint);
		
		List<SkiPoint> possiblePaths = getPossiblePaths(skiGrid, curPoint, route);
		Collections.sort(possiblePaths);
		
		if(possiblePaths.size() == 0) {
			return route;
		} else {
			for(SkiPoint point : possiblePaths) {
				List<SkiPoint> possibleRoute = findRoute(skiGrid, point, new ArrayList<>(route));
				if(possibleRoute.size() > maxRoute.size()) {
					maxRoute = possibleRoute;
				} else if(possibleRoute.size() == maxRoute.size()) {
					
					int curRouteDiff = possibleRoute.get(0).height - possibleRoute.get(possibleRoute.size() - 1).height; 
					int maxRouteDiff = maxRoute.get(0).height - maxRoute.get(maxRoute.size() - 1).height;
					
					if(curRouteDiff > maxRouteDiff) {
						maxRoute = possibleRoute;
					}
				}
			}
		}
		
		return maxRoute;
	}
	
	private static List<SkiPoint> getPossiblePaths(SkiPoint[][] skiGrid, SkiPoint curPoint, List<SkiPoint> route) {
		
		final int[] NORTH = new int[] { -1 , 0 };
		final int[] SOUTH = new int[] { 1 , 0 };
		final int[] EAST = new int[] { 0 , 1 };
		final int[] WEST = new int[] { 0 , -1 };
		
		List<SkiPoint> possiblePaths = new ArrayList<>();
		
		int i = curPoint.i + NORTH[0];
		int j = curPoint.j + NORTH[1];
		
		if(i < skiGrid.length && i >= 0 && j < skiGrid[0].length && j >= 0) {
			if(skiGrid[i][j].height < curPoint.height && !route.contains(skiGrid[i][j])) {
				possiblePaths.add(skiGrid[i][j]);
			}
		}

		i = curPoint.i + SOUTH[0];
		j = curPoint.j + SOUTH[1];
		
		if(i < skiGrid.length && i >= 0 && j < skiGrid[0].length && j >= 0) {
			if(skiGrid[i][j].height < curPoint.height && !route.contains(skiGrid[i][j])) {
				possiblePaths.add(skiGrid[i][j]);
			}
		}
		
		i = curPoint.i + EAST[0];
		j = curPoint.j + EAST[1];
		
		if(i < skiGrid.length && i >= 0 && j < skiGrid[0].length && j >= 0) {
			if(skiGrid[i][j].height < curPoint.height && !route.contains(skiGrid[i][j])) {
				possiblePaths.add(skiGrid[i][j]);
			}
		}
		
		i = curPoint.i + WEST[0];
		j = curPoint.j + WEST[1];
		
		if(i < skiGrid.length && i >= 0 && j < skiGrid[0].length && j >= 0) {
			if(skiGrid[i][j].height < curPoint.height && !route.contains(skiGrid[i][j])) {
				possiblePaths.add(skiGrid[i][j]);
			}
		}
		
		return possiblePaths;
	}
	
	private static class SkiPoint implements Comparable<SkiPoint> {
		
		public int i;
		public int j;
		public int height;
		
		public SkiPoint(int i, int j, int height) {
			this.i = i;
			this.j = j;
			this.height = height;
		}

		@Override
		public int compareTo(SkiPoint o) {
			if (this.height > o.height) {
				return -1;
			} else 
			if (this.height < o.height) {
				return 1;
			} else {
				return 0;
			}
		}
		
		@Override
		public String toString() {
			return String.format("(%d:[%d,%d])", this.height, this.i, this.j);
		}
	}
	
}