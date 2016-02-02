package longest_subsequence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {

	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		
		try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
			String line;
			
			while ((line = buffer.readLine()) != null) {
				line = line.trim();
				String[] lines = line.split(";");
				String lineOne = lines[0];
				String lineTwo = lines[1];
				
				String longerOne = lineOne.length() > lineTwo.length() ? lineOne : lineTwo;
				String shorterOne = lineOne.length() <= lineTwo.length() ? lineOne : lineTwo;
				
				char[] lline = copyToLargerArray(longerOne.toCharArray());
				char[] sline = copyToLargerArray(shorterOne.toCharArray());
				
				int[][] costTable = constructCostTable(lline, sline);
				
				System.out.println(printLongestSubsequence(lline, sline, costTable, longerOne.length(), shorterOne.length()));
				
			}
		} 
	}

	private static int[][] constructCostTable(char[] longerLine, char[] shorterLine) {
		int[][] costTable = new int[longerLine.length+1][shorterLine.length+1]; 
		
		for (int i=1;i<longerLine.length;i++) {
			for(int j=1;j<shorterLine.length;j++) {
				if(longerLine[i] == shorterLine[j]) {
					costTable[i][j] = costTable[i-1][j-1] + 1;
				} else {
					costTable[i][j] = Math.max(costTable[i][j-1], costTable[i-1][j]);
				}
			}
		}
		
		return costTable;
	}
	
	private static String printLongestSubsequence(char[] longerLine, char[] shorterLine, int[][] costTable, int longerIdx, int shorterIdx) {
		
		if(longerIdx==0 || shorterIdx==0) {
			return "";
		} else
		if(longerLine[longerIdx] == shorterLine[shorterIdx]) {
			return printLongestSubsequence(longerLine, shorterLine, costTable, longerIdx-1, shorterIdx-1) + longerLine[longerIdx];
		} else {
			if(costTable[longerIdx][shorterIdx-1] > costTable[longerIdx-1][shorterIdx]) {
				return printLongestSubsequence(longerLine, shorterLine, costTable, longerIdx, shorterIdx-1);
			} else {
				return printLongestSubsequence(longerLine, shorterLine, costTable, longerIdx-1, shorterIdx);
			}
		}
	}

	
	private static char[] copyToLargerArray(char[] original) {
		char[] newArray = new char[original.length + 1];
		
		for(int i=1;i<newArray.length;i++) {
			newArray[i] = original[i-1];
		}
		
		return newArray;
	}
}