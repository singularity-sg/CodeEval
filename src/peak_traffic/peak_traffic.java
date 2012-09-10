package peak_traffic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import message_decoding.message_decoding;

public class peak_traffic {
	
	Map<String, GoodFriend> everybody = new HashMap<String, GoodFriend>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		peak_traffic pt = new peak_traffic();
		
		String filepath = args[0];
		
		if(args.length < 1) {
			return;
		}
		
		try {
			InputStream is = message_decoding.class.getClassLoader().getResourceAsStream(filepath);
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = br.readLine()) != null) {
				pt.process(line);
			}
			
			pt.runTheIngeniusAlgorithm();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void runTheIngeniusAlgorithm() {
		// TODO Auto-generated method stub
		
	}

	private void process(String line) {
		String[] fields = line.split("\t");
		String from = fields[1].split("@")[0].trim();
		String to = fields[2].split("@")[0].trim();
		
		if(!everybody.containsKey(from)) {
			everybody.put(from, new GoodFriend(from));
		} 

		((GoodFriend)everybody.get(from)).addFriend(to);
	}
	
	
	private class GoodFriend {
		private String name;
		private List<GoodFriend> friends;
		
		public GoodFriend(String name) {
			friends = new ArrayList<GoodFriend>();
			this.name = name;
		}
		
		public void addFriend(String friend) {
			if(!everybody.containsKey(friend)) {
				everybody.put(friend, new GoodFriend(friend));
			}
			this.friends.add(everybody.get(friend));
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}

}
