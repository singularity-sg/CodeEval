package prefix_notation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class prefix {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String filepath = args[0];
		
		if(args.length < 1) {
			return;
		}
		
		try {
			InputStream is = prefix.class.getClassLoader().getResourceAsStream(filepath);
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = br.readLine()) != null) {
				process(line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void process(String line) {
		
		int result = decipher(stringToList(line));
		
		System.out.println(result);
	}

	static LinkedList<String> stringToList(String line) {
		String[] tokens = line.split(" ");
		LinkedList<String> list = new LinkedList<String>();
	
		for(int i=0;i<tokens.length;i++) {
			list.add(tokens[i]);
		}
		return list;
	}
	
	static int decipher(LinkedList<String> items) {
		
		String operator = items.getFirst();
		String operand = items.getLast();
	
		if(items.size() == 3) {
			return calculate(items.get(0), items.get(1), items.get(2));
		}
		
		LinkedList<String> newList = (LinkedList<String>) items.clone();
		newList.removeFirst();
		newList.removeLast();
		
		return calculate(operator, String.valueOf(decipher(newList)), operand);
	}
	
	static int calculate(String operator, String operand1, String operand2) {
		
		int op1 = Integer.parseInt(operand1);
		int op2 = Integer.parseInt(operand2);
		
		if("+".equals(operator)) {
			 return op1 + op2;
		} else 
		if("/".equals(operator)) {
			return op1 / op2;
		} else
		if("*".equals(operator)) {
			return op1 * op2;
		}

		return -1;
	}
	
}
