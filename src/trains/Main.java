package trains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Main {

	Map<Character, Set<Character>> links = new HashMap<>();
	Map<String, Integer> routes = new HashMap<>();

	public Main(Map<String, Integer> routes) {
		this.routes.putAll(routes);
		parseTowns(routes);
	}

	private void parseTowns(Map<String, Integer> routes) {

		for (String route : routes.keySet()) {
			char[] towns = route.toCharArray();
			assert towns.length == 2;
			Set<Character> curLink = this.links.get(towns[0]);
			if (curLink == null) {
				curLink = new HashSet<Character>();
				this.links.put(towns[0], curLink);
			}
			curLink.add(towns[1]);
		}

	}

	public int distanceOf(String route) {

		char[] towns = route.toCharArray();
		int totalDist = 0;

		if (towns.length > 1) {
			for (int i = 1; i < towns.length; i++) {
				StringBuffer r = new StringBuffer().append(towns[i - 1]).append(towns[i]);
				Integer distance = this.routes.get(r.toString());
				if (distance == null) {
					return -1;
				}
				totalDist += distance;
			}
		}

		return totalDist;
	}

	public int noOfRoutesWithLimitStops(char origin, char destination, int limit, Stack<Character> currentRoute,
			int count) {

		currentRoute.push(origin);

		Set<Character> listOfLinks = links.get(origin);

		try {
			if (listOfLinks == null) {
				return count;
			}

			if (currentRoute.size() >= limit) {
				return count;
			}

			for (Character link : listOfLinks) {
				if (link == destination) {
					List<Character> foundRoute = new ArrayList<>(currentRoute);
					foundRoute.add(link);
					System.out.println(foundRoute);
					count++;
					continue;
				}

				if(!currentRoute.contains(link)) {
					count = noOfRoutesWithLimitStops(link, destination, limit, currentRoute, count);
				}
			}
		} finally {
			currentRoute.pop();
		}

		return count;
	}

	public int noOfRoutesWithExactStops(char origin, char destination, int exactNumber, Stack<Character> currentRoute,
			int count) {

		currentRoute.push(origin);

		Set<Character> listOfLinks = links.get(origin);

		try {
			if (listOfLinks == null) {
				return count;
			}

			if (currentRoute.size() == exactNumber) {
				return count;
			}

			for (Character link : listOfLinks) {
				if (link == destination && currentRoute.size() == (exactNumber - 1)) {
					List<Character> foundRoute = new ArrayList<>(currentRoute);
					foundRoute.add(link);
					System.out.println(foundRoute);
					count++;
					continue;
				}

				count = noOfRoutesWithExactStops(link, destination, exactNumber, currentRoute, count);
			}
		} finally {
			currentRoute.pop();
		}

		return count;
	}

	public int shortestRoute(char origin, char destination) {
		List<String> routes = new ArrayList<>();
		getAllPossibleRoutes(origin, destination, new Stack<Character>(), routes);
		
		int shortestRoute = Integer.MAX_VALUE;
		
		for(String route : routes) {
			int distance = distanceOf(route);
			if(distance < shortestRoute) {
				shortestRoute = distance;
			}
		}
		
		return shortestRoute;
	}
	
	public void getAllPossibleRoutes(char origin, char destination, Stack<Character> currentRoute, List<String> routes) {

		currentRoute.push(origin);

		Set<Character> listOfLinks = links.get(origin);

		try {
			if (listOfLinks == null) {
				return;
			}

			for (Character link : listOfLinks) {
				if (link == destination) {
					List<Character> foundRoute = new ArrayList<>(currentRoute);
					foundRoute.add(link);
					StringBuffer buffer = new StringBuffer();
					for(char c : foundRoute) {
						buffer.append(c);
					}
					routes.add(buffer.toString());
					continue;
				}

				if(!currentRoute.contains(link)) {
					getAllPossibleRoutes(link, destination, currentRoute, routes);
				}
			}
		} finally {
			currentRoute.pop();
		}

		return;
	}

	public void getAllPossibleRoutesWithMaxDistance(char origin, char destination, Stack<Character> currentRoute, List<String> routes, int maxDistance) {

		currentRoute.push(origin);

		Set<Character> listOfLinks = links.get(origin);

		try {
			if (listOfLinks == null) {
				return;
			}

			for (Character link : listOfLinks) {
				if (link == destination) {
					List<Character> foundRoute = new ArrayList<>(currentRoute);
					foundRoute.add(link);
					StringBuffer buffer = new StringBuffer();
					for(char c : foundRoute) {
						buffer.append(c);
					}
					String eligibleRoute = buffer.toString();
					if(distanceOf(eligibleRoute) < 30) {
						routes.add(eligibleRoute);
					} else {
						return;
					}
				}

				getAllPossibleRoutesWithMaxDistance(link, destination, currentRoute, routes, maxDistance);
			}
		} finally {
			currentRoute.pop();
		}

		return;
	}

}
