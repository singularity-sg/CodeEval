package number_pairs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class number_pairs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    String filename = args[0];
	    List<List<Integer>> list;
	    List<String> answer = new ArrayList<String>();
	    try {
		    if(filename != null) {
		    	list = readFile(new FileInputStream(args[0]));
		    	solve(list, answer);
		    	
		    	for(String ans : answer) {
		    		System.out.println(ans);
		    	}
		    }
	    }catch(Exception e) {}
	}

	protected static void solve(List<List<Integer>> list, List<String> answer) {
		for (List<Integer> problem : list) {
			
			Integer X = problem.remove(problem.size()-1);
			Integer[] number = problem.toArray(new Integer[]{});
			StringBuffer ans = new StringBuffer();
			
			for(int i=0;i<number.length;i++) {
				for(int j=i+1;j<number.length;j++) {
					if((number[i] + number[j]) == X) {
						ans.append(number[i]).append(",").append(number[j]).append(";");
					}
				}
			}
			
			if(ans.length() > 0) {
				ans.delete(ans.length()-1, ans.length());
			} else {
				ans.append("NULL");
			}
			
			answer.add(ans.toString());
		}
	}

	protected static List<List<Integer>> readFile(InputStream is) throws Exception {
	    BufferedReader in = new BufferedReader(new InputStreamReader(is));
	    String line;
	    List<List<Integer>> allList = new ArrayList<List<Integer>>();
	    while ((line = in.readLine()) != null) {
	        String[] lineArray = line.split(";");
	        if (lineArray.length > 0) {
	        	List<Integer> list = new ArrayList<Integer>();
	        	String[] numbers = lineArray[0].split(",");
	        	for (String number : numbers) {
					list.add(Integer.valueOf(number));
				}
	        	list.add(Integer.valueOf(lineArray[1]));
	        	allList.add(list);
	        }
	    }
	    
	    return allList;
	}
}
