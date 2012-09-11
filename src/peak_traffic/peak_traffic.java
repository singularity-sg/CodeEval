package peak_traffic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class peak_traffic {

	Map<String, GoodFriend> everybody = new HashMap<String, GoodFriend>();

	Collection<List<GoodFriend>> cliques;

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
			InputStream is = peak_traffic.class.getClassLoader().getResourceAsStream(filepath);
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = br.readLine()) != null) {
				pt.process(line);
			}

			pt.runTheIngeniusAlgorithm();
			pt.printResults();

			System.exit(0);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printResults() {

		List<String> finalCollection = new ArrayList<String>();
		for(List<GoodFriend> s : cliques) {
			if(s.size() >= 3){
				Collections.sort(s);
				StringBuilder sb = new StringBuilder();
				for(GoodFriend gf : s) {
					sb.append(gf.toString()).append(", ");
				}
				sb.delete(sb.length()-2, sb.length());
				finalCollection.add(sb.toString());
			}
		}

		Collections.sort(finalCollection);
		for(String s : finalCollection) {
			System.out.println(s);
		}

	}

	private void runTheIngeniusAlgorithm() {
		List<GoodFriend> potential_clique = new ArrayList<GoodFriend>();
		List<GoodFriend> already_found = new ArrayList<GoodFriend>();
		List<GoodFriend> candidates = new ArrayList<GoodFriend>(everybody.values());

		cliques = new ArrayList<List<GoodFriend>>();
		findCliques(potential_clique, candidates, already_found);
	}

	private void process(String line) {
		String[] fields = line.split("\t");
		String from = fields[1].trim();
		String to = fields[2].trim();

		if(!everybody.containsKey(from)) {
			everybody.put(from, new GoodFriend(from));
		}

		((GoodFriend)everybody.get(from)).addFriend(to);
	}

	private void findCliques(List<GoodFriend> potential_clique, List<GoodFriend> candidates, List<GoodFriend> already_found) {
		List<GoodFriend> candidates_array = new ArrayList<GoodFriend>(candidates);
		if (!end(candidates, already_found)) {
			// for each candidate_node in candidates do
			for (GoodFriend candidate : candidates_array) {
				List<GoodFriend> new_candidates = new ArrayList<GoodFriend>();
				List<GoodFriend> new_already_found = new ArrayList<GoodFriend>();

				// move candidate node to potential_clique
				potential_clique.add(candidate);
				candidates.remove(candidate);

				// create new_candidates by removing nodes in candidates not
				// connected to candidate node
				for (GoodFriend new_candidate : candidates) {
					if (candidate.isNeighbour(new_candidate) && new_candidate.isNeighbour(candidate)) {
						new_candidates.add(new_candidate);
					} // of if
				} // of for

				// create new_already_found by removing nodes in already_found
				// not connected to candidate node
				for (GoodFriend new_found : already_found) {
					if (candidate.isNeighbour(new_found) && new_found.isNeighbour(candidate)) {
						new_already_found.add(new_found);
					} // of if
				} // of for

				// if new_candidates and new_already_found are empty
				if (new_candidates.isEmpty() && new_already_found.isEmpty()) {
					// potential_clique is maximal_clique
					cliques.add(new ArrayList<GoodFriend>(potential_clique));
				} // of if
				else {
					// recursive call
					findCliques(potential_clique, new_candidates,
							new_already_found);
				} // of else

				// move candidate_node from potential_clique to already_found;
				already_found.add(candidate);
				potential_clique.remove(candidate);
			} // of for
		} // of if
	}

	private boolean end(List<GoodFriend> candidates, List<GoodFriend> already_found) {
		// if a node in already_found is connected to all nodes in candidates
		boolean end = false;
		int edgecounter;
		for (GoodFriend found : already_found) {
			edgecounter = 0;
			for (GoodFriend candidate : candidates) {
				if (found.isNeighbour(candidate)) {
					edgecounter++;
				} // of if
			} // of for
			if (edgecounter == candidates.size()) {
				end = true;
			}
		} // of for
		return end;
	}


	private class GoodFriend implements Comparable<GoodFriend> {
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

		public boolean isNeighbour(GoodFriend myFriend) {
			for (GoodFriend friend : friends) {
				if (friend.toString().equalsIgnoreCase(myFriend.toString())) {
					return true;
				}
			}
			return false;
		}

		public String toString() {
			return this.name;
		}

		public int compareTo(GoodFriend other) {
			return this.toString().compareTo(other.toString());
		}

	}

}
