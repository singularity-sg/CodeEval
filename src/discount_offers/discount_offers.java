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
import java.util.TreeSet;

public class discount_offers {
	
	private static Map<String, Float> memoized = new HashMap<String, Float>();
	
	private static NumberFormat nf = NumberFormat.getInstance();
	
	{
		nf.setMinimumFractionDigits(2);
	}

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
		
		System.out.println(line);
		
		Map<String, List<String>> data = splitRecord(line);
		
		List<String> customers = data.get("customers");
		List<String> products = data.get("products");
		
		
		System.out.println(nf.format(calculateScore(customers, products)));
	}
	
	
	Float calculateScore(List<String> customers, List<String> products) {

        float score = 0.0f;
        
        TreeSet<String> set = new TreeSet<String>();
        set.addAll(customers);
        set.addAll(products);
        
        Float calcScore = memoized.get(set.toString());
        if(calcScore != null) {
        	return calcScore;
        }

        for(String customer : customers) {

            List<String> remainingCustomers = new ArrayList<String>();
            remainingCustomers.addAll(customers);
            remainingCustomers.remove(customer);

            for(String product : products) {
                List<String> remainingProducts = new ArrayList<String>();
                remainingProducts.addAll(products);
                remainingProducts.remove(product);

                Float myscore = memoized.get(customer+","+product);
                float curScore = 0.0f;
                if(myscore == null) {
                	myscore = calculateScoreForOne(customer, product);
                	memoized.put(customer+","+product, myscore);
                }
                curScore = myscore + calculateScore(remainingCustomers, remainingProducts);
                if(score < curScore) {
                    score = curScore;
                }
            }
        }
        
        memoized.put(set.toString(), score);
        
        return score;
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
		return string.replaceAll("[ \\-',\\.\\\\\\&0-9]", "");
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