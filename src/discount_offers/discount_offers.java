package discount_offers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class discount_offers {
	
//	private static Map<String, Float> scorecache = new HashMap<String,Float>();

	public static void main(String[] args) throws Exception {
		
		discount_offers d = new discount_offers();
		
		String filepath = args[0];
		
		if(args.length < 1) {
			return;
		}
		
		try {
			InputStream is = discount_offers.class.getClassLoader().getResourceAsStream(filepath);
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = br.readLine()) != null) {
				d.process(line);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void process(String line) throws Exception {
		
		Map<String, List<String>> data = splitRecord(line);
		
		List<String> customers = data.get("customers");
		List<String> products = data.get("products");
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		System.out.println(nf.format(calculateScore(customers, products)));
	}
	
	
	Float calculateScore(List<String> customers, List<String> products) {
		
		long time = System.currentTimeMillis();
		long loops = 0;
		
		String[] customersArr = customers.toArray(new String[customers.size()]);
		String[] productsArr =  products.toArray(new String[products.size()]);
		
		Float[][] scoreTable = null, transScoreTable = null;

		
		scoreTable = new Float[customersArr.length][productsArr.length];
		
		for(int i=0,len=customersArr.length;i<len;i++) {
			for(int j=0,jlen=productsArr.length;j<jlen;j++) {
				scoreTable[i][j] = calculateScoreForOne(customersArr[i], productsArr[j]);	
			}
		}
		
//		System.out.println("-----Before Sort-----");
//		
//		for (int i = 0; i < scoreTable.length; i++) {
//			System.out.println(Arrays.toString(scoreTable[i]));
//		}
		
		for (int i = 0; i < scoreTable.length; i++) {
			Arrays.sort(scoreTable[i]);
		}
		
//		System.out.println("-----After Sort-----");
//		
//		for (int i = 0; i < scoreTable.length; i++) {
//			System.out.println(Arrays.toString(scoreTable[i]));
//		}

		transScoreTable = transverse(scoreTable);
	
//		System.out.println("-----After Transverse-----");
//		
//		for (int i = 0; i < transScoreTable.length; i++) {
//			System.out.println(Arrays.toString(transScoreTable[i]));
//		}
		
		for (int i = 0; i < transScoreTable.length; i++) {
			Arrays.sort(transScoreTable[i]);
		}
		
//		System.out.println("-----After Transverse Sort-----");
//		
//		for (int i = 0; i < transScoreTable.length; i++) {
//			System.out.println(Arrays.toString(transScoreTable[i]));
//		}
		
		scoreTable = transverse(transScoreTable);
		
//		System.out.println("-----Sorted Table-----");
//		
//		for (int i = 0; i < scoreTable.length; i++) {
//			System.out.println(Arrays.toString(scoreTable[i]));
//		}
		
		Float[][] finalTable = truncate(scoreTable);
		
//		System.out.println("-----Truncated Table-----");
//		
//		for (int i = 0; i < finalTable.length; i++) {
//			System.out.println(Arrays.toString(finalTable[i]));
//		}
		
		Float score = subdivide(finalTable);
		
		System.out.println("Time -> " + (System.currentTimeMillis() - time) + ", Loops -> " + loops);				
		return score;
	}
	
	private Float subdivide(Float[][] table) {
		
		int len = table.length;
		int workingLen = len;
		Float[][] workingTable = table.clone();
		
		if(len == 2) {
			
			Float highestTotal = 0f;
			List<int[]> perm = new ArrayList<int[]>();
			
			perm2(new int[]{0,1}, perm);
			
			for (int[] p : perm) {
				Float total = 0f;
				
				for(int i=0;i<len;i++) {
					total += workingTable[i][p[i]];
				}
				
				if(total > highestTotal) {
					highestTotal = total;
				}
			}
			
//			System.out.println("----- Working Table -----");
//			for (int i = 0; i < workingTable.length; i++) {
//				System.out.println(Arrays.toString(workingTable[i]));
//			}
//			System.out.println("----- Total: " + highestTotal + "-----");
						
			return highestTotal;
		}
		
		if(len > 2 && len%2 != 0) {
			workingLen = len+1;
			workingTable = new Float[workingLen][workingLen];
			for(int i=0;i<len;i++) {
				for(int j=0;j<len;j++) {
					workingTable[i][j] = table[i][j];
				}
			}
			
			for (int i = 0; i < len; i++) {
				workingTable[i][len] = Float.valueOf(0f); 
			}
			Float[] tmp = workingTable[len];
			Arrays.fill(tmp, Float.valueOf(0f));

		} 
		
		workingLen /= 2;
		
		Float[][] finalScore = new Float[2][2];
		
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				
				Float[][] subpart = new Float[workingLen][workingLen];
				int x_idx = i * workingLen;
				int y_idx = j * workingLen;
						
				for(int x=0; x<workingLen; x++) {
					for(int y=0; y<workingLen; y++) {
						subpart[x][y] = workingTable[x_idx+x][y_idx+y];
					}
				}
				
				Float score = subdivide(subpart);
				finalScore[i][j] = score;
			}
		}
		
		Float highestTotal = finalScore[0][0] + finalScore[1][1];
		
		return highestTotal;
		
	}
	
	private Float[][] truncate(Float[][] scoreTable) {
		int height = scoreTable.length;
		int length = scoreTable[0].length;
		
		Float[][] newTable = null;
		
		if(height > length) {
			
			newTable = new Float[length][length];
			
			for(int i=0,x=height-1; i<length; i++, x--) {
				
				for(int j=0,y=length-1; j<length; j++, y--) {
					
						newTable[i][j] = scoreTable[x][y];
					
				}
				
			}
			
		} else {
			
			newTable = new Float[height][height];
			
			for(int i=0,x=height-1; i<height; i++, x--) {
				
				for(int j=0,y=length-1; j<height; j++, y--) {
					
						newTable[i][j] = scoreTable[x][y];
					
				}
				
			}
		}
		
		return newTable;
		
	}	

	private Float[][] transverse(Float[][] scoreTable) {
		
		Float[][] newMatrix = new Float[scoreTable[0].length][scoreTable.length];
		
		for (int i = 0; i < newMatrix.length; i++) {
			for (int j = 0; j < newMatrix[0].length; j++) {
				newMatrix[i][j] = scoreTable[j][i];
			}
		}
		
		return newMatrix;
	}

	// print N! permutation of the elements of array a (not in order)
    public static void perm2(int[] s, List<int[]> results) {
       int N = s.length;
       int[] a = new int[N];
       for (int i = 0; i < N; i++)
           a[i] = s[i];
       perm2(a, N, results);
    }
    
    private static Map<String, int[]> cache = new HashMap<String, int[]>();

    private static void perm2(int[] a, int n, List<int[]> results) {
        if (n == 1) {
        	String key = Arrays.toString(a);
        	if(cache.containsKey(key)) {
        		results.add(cache.get(key));
        	} else {
        		int[] tmp = a.clone();
        		cache.put(key, tmp);
        		results.add(tmp);
        	}
            
            return;
        }
        for (int i = 0; i < n; i++) {
            swap(a, i, n-1);
            perm2(a, n-1, results);
            swap(a, i, n-1);
        }
    }  

    // swap the String at indices i and j
    private static void swap(int[] a, int i, int j) {
        int s;
        s = a[i]; a[i] = a[j]; a[j] = s;
    }


	Float calculateScoreForOne(String customer, String product) {
		
		Float score = 0f;
		
		String c = cleanString(customer);
		String p = cleanString(product);
		
		if(isEven(p)) {
			
			score = Float.valueOf(noOfVowels(c) * 1.5f);
			
		} else {
			
			score = Float.valueOf(noOfConsonants(c));
			
		}
		
		if(gcd(c.length(), p.length()) > 1) {
			score = score * 1.5f;
		}
		
		return score;
	}
	
	// Courtesy of http://www.java-tips.org/java-se-tips/java.lang/finding-greatest-common-divisor-recursively.html
	public static long gcd(long a, long b) {
	   if (b==0) 
	     return a;
	   else
	     return gcd(b, a % b);
	 } 
	

	boolean isEven(String string) {
		return string.length()%2 == 0;
	}

	int noOfVowels(String product_cleaned) {
		
		char[] vowels = new char[] {'a','e','i','o','u','y','A','E','I','O','U','Y'};
		
		char[] letters = product_cleaned.toCharArray();
		
		int noOfVowels = 0;
		
		for (int i = 0; i < letters.length; i++) {
			for (int j = 0; j < vowels.length; j++) {
				if(letters[i] == vowels[j]) {
					noOfVowels++;
				}
			}
		}
		
		return noOfVowels;
	}
	
	int noOfConsonants(String product_cleaned) {
		
		String consonantsRegex = "[a-zA-Z&&[^aeiouyAEIOUY]]";
		
		char[] letters = product_cleaned.toCharArray();
		
		int noOfConsonants = 0;
		
		for (int i = 0; i < letters.length; i++) {
			if(String.valueOf(letters[i]).matches(consonantsRegex)){
				noOfConsonants++;
			}
		}
		
		return noOfConsonants;
	}

	String cleanString(String string) {
		return string.replaceAll("[ ',\\.\\\\\\&0-9]", "");
	}
	
	Map<String, List<String>> splitRecord(String line) {
		
		String[] data = null;
		
		if(line == null) {
			return null;
		}
		
		data = line.split(";",2);
		
		
		String[] customers = data[0] != null ? data[0].split(",") : null;
		String[] products = data[1] != null ? data[1].split(",") : null;
		
		Map<String, List<String>> res = new HashMap<String, List<String>>();
		res.put("customers", Arrays.asList(customers));
		res.put("products",	 Arrays.asList(products));
		
		return res;
		
	}
	
}