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
		Float highestTotal = 0f;
		
		String[] customersArr = customers.toArray(new String[customers.size()]);
		String[] productsArr =  products.toArray(new String[products.size()]);

		Float[][] scoreTable = new Float[customersArr.length][productsArr.length];
		int[] p=null, c=null;
		
		c = new int[customersArr.length];
		for(int i=0,len=customersArr.length;i<len;i++) {
			c[i] = i;
			p = new int[productsArr.length];
			for(int j=0,jlen=productsArr.length;j<jlen;j++) {
				p[j] = j;
				scoreTable[i][j] = calculateScoreForOne(customersArr[i], productsArr[j]);	
			}
		}
	
		List<int[]> prodPerms = new ArrayList<int[]>();
		List<int[]> custPerms = new ArrayList<int[]>();
		
		if(customersArr.length > productsArr.length) {
			comb2(c, productsArr.length, custPerms);
			perm2(p, prodPerms);
			
			for (int[] cs : custPerms) {
				for (int[] ps: prodPerms) {
					
					Float totalScore = 0f;
					for(int i=0,len=productsArr.length;i<len;i++) {
						totalScore += scoreTable[cs[i]][ps[i]];
					}
					if(totalScore > highestTotal) {
						highestTotal = totalScore;
					}
				}
			}
			
		} else {
			comb2(p, customersArr.length, prodPerms);
			perm2(c, custPerms);
			
			for (int[] ps : prodPerms) {
				for (int[] cs: custPerms) {
					
					Float totalScore = 0f;
					for(int i=0,len=customersArr.length;i<len;i++) {
						totalScore += scoreTable[cs[i]][ps[i]];
					}
					if(totalScore > highestTotal) {
						highestTotal = totalScore;
					}
				}
			}
		}
		
		
		System.out.println("Time -> " + (System.currentTimeMillis() - time) + ", Loops -> " + loops);				
		return highestTotal;
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

    // print all subsets that take k of the remaining elements, with given prefix 
    public  static void comb2(int[] s, int k, List<int[]> results) { 
    	comb2(s, new int[]{}, k, results); 
    }
    
    private static void comb2(int[] s, int[] prefix, int k, List<int[]> results) {
        if (k == 0) {
        	results.add(prefix);
        }
        else {
            for (int i = 0; i < s.length; i++) {
            		
            	int[] tmp = new int[s.length - (i+1)];
            	for(int j=0;j<tmp.length;j++) {
            		tmp[j] = s[j+(i+1)];
            	}
            	
            	int[] preTmp = new int[prefix.length+1];
            	for(int g=0;g<prefix.length;g++) {
            		preTmp[g] = prefix[g];
            	}
            	preTmp[prefix.length] = s[i];
            	
                comb2(tmp, preTmp, k-1, results);
            }
        }  
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
