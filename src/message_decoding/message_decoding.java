package message_decoding;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class message_decoding {

	static final String regexStr = "[01]{3}";
	
	Pattern pattern;
	Matcher matcher;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		message_decoding md = new message_decoding();
		
		String filepath = args[0];
		
		if(args.length < 1) {
			return;
		}
		
		try {
			InputStream is = message_decoding.class.getClassLoader().getResourceAsStream(filepath);
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = br.readLine()) != null) {
				md.process(line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

	message_decoding() {
		pattern = Pattern.compile(regexStr);
	}
	
	
	void process(String line) {
		
		String[] tokens = splitRecord(line);
		
		Map<String, String> dict = generateDictionary(tokens[1]);
		
		String message = tokens[2];
		
		int codeLength = Integer.parseInt(tokens[0], 2);
		
		String output = decodeSegment(dict, message, codeLength);
		
		System.out.println(output);
	}
	
	String decodeSegment(Map<String, String> dict, String segment, int codeLen) {
		
		StringBuffer output = new StringBuffer();
		
		for(int i=0; (i+codeLen)<segment.length(); i+=codeLen) {
			String key = segment.substring(i, i+codeLen);
			if(!isAllOnes(key)) {
				output.append(dict.get(key));
			}else {
				String len = segment.substring(i+codeLen, i+codeLen+3);
				int len_i = Integer.parseInt(len, 2);
				if(len_i > 0) {
					output.append(decodeSegment(dict, segment.substring(i+codeLen+3), len_i));
				}
				break;
			}
		}
		
		return output.toString();
	}
	
	Map<String, String> generateDictionary(String dictString) {
		
		Map<String, String> dict = new HashMap<String, String>();
	
		int dictLength = dictString.length(); /** Durrtyy **/
		int i=0, keyLen = 1;
		String key = "0";
		
		dict.put(key, String.valueOf(dictString.charAt(i)));
		i++;
		
		while(i<dictLength) {
		
			int jLen = (int) Math.pow(2, keyLen);
			
			for(int j=1;j<jLen && i<dictLength;j++) {
			 
				key = String.format("%" + keyLen + "s", Integer.toBinaryString(j)).replace(' ', '0');
				if(isAllOnes(key)) {
					keyLen++;
					key = String.format("%0" + keyLen + "d", 0);
				}
				
				dict.put(key, String.valueOf(dictString.charAt(i)));
				i++;
			}	
		}
		
		return dict;
	}
	
	boolean isAllOnes(String in) {
		return in.matches("^[1]+$");
	}
	
	int binaryStringToInt(String string) throws NumberFormatException {
		return Integer.parseInt(string, 2);
	}
	
	String matchedString(String line) {
		
		Matcher matcher = pattern.matcher(line);
		
		if(line == null) {
			return null;
		}
		
		if(matcher.find()) {
			return matcher.group(0);
		}
		
		return "";
	}
	
	String[] splitRecord(String line) {
		
		String[] data = new String[3];
		
		if(line == null) {
			return null;
		}
		
		String count = matchedString(line);
		
		if("".equals(count)) {
			return new String[]{count,"",""};
		}
		
		String[] tmp = line.split(regexStr,2);
		
		data[0] = count;
		data[1] = tmp[0];
		data[2] = tmp[1];
		
		return data;
	}
}
