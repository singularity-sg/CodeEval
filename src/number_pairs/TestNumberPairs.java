package number_pairs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestNumberPairs {

	@Test
	public void testCase1() throws Exception {
		number_pairs np = new number_pairs();
		
		String problem = "1,2,3,4,6;5\n2,4,5,6,9,11,15;20\n1,2,3,4;50";
		
		List<List<Integer>> list = number_pairs.readFile(new ByteArrayInputStream(problem.getBytes()));
		List<String> answer = new ArrayList<String>();
		
		number_pairs.solve(list, answer);
		
		for(String ans : answer) {
    		System.out.println(ans);
    	}
	}
	
}
