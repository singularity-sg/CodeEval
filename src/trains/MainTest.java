package trains;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.junit.Test;

public class MainTest {

	public Map<String, Integer> buildRoutes() throws IOException {
		
		Map<String, Integer> routes = new HashMap<>();
		routes.put("AB", 5);
		routes.put("BC", 4);
		routes.put("CD", 8);
		routes.put("DC", 8);
		routes.put("DE", 6);
		routes.put("AD", 5);
		routes.put("CE", 2);
		routes.put("EB", 3);
		routes.put("AE", 7);
		
		return routes;
		
	}
	
	@Test
	public void testDistance() throws IOException {
		Main main = new Main(buildRoutes());
		
		assertEquals(9, main.distanceOf("ABC"));
		assertEquals(5, main.distanceOf("AD"));
		assertEquals(13, main.distanceOf("ADC"));
		assertEquals(22, main.distanceOf("AEBCD"));
		assertEquals(-1, main.distanceOf("AED"));
	}
	
	@Test
	public void testSpecificOriginAndDestination() throws Exception{
		Main main = new Main(buildRoutes());
		
		assertEquals(2, main.noOfRoutesWithLimitStops('C','C', 4, new Stack<Character>(), 0));
		assertEquals(3, main.noOfRoutesWithExactStops('A','C', 5, new Stack<Character>(), 0));
	}
	
	
	@Test
	public void testGetShortestRoute() throws Exception {
		Main main = new Main(buildRoutes());
		
		assertEquals(9, main.shortestRoute('A','C'));
		assertEquals(9, main.shortestRoute('B','B'));
	}
	
	@Test
	public void testNumberOfRoutesWithDistanceLessThan() throws Exception {
		Main main = new Main(buildRoutes());
		
		List<String> validRoutes = new ArrayList<>();
		main.getAllPossibleRoutesWithMaxDistance('C','C', new Stack<Character>(), validRoutes, 30);
		
		assertEquals(7, validRoutes.size());
	}
}
