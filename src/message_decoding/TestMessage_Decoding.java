package message_decoding;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class TestMessage_Decoding {

	@Test
	public void testMain() {
		message_decoding.main(new String[]{"message_decoding.txt"});
	}
	
	@Test
	public void testDecodeSegment() {
		
		message_decoding decoder = new message_decoding();
		
		Map<String, String> dict = decoder.generateDictionary("$#**\\\\");
		
		String output = decoder.decodeSegment(dict, "1000101101100000111100101000", 2);
		
		assertEquals("*#*\\\\$", output);
	}
	
	@Test
	public void testRegex() {
		
		message_decoding decoder = new message_decoding();
		String data = "+++$#**\\0100000101101100011100101000---";
		
		String[] tokens = decoder.splitRecord(data);
		
		assertEquals("010", tokens[0]);
		assertEquals("+++$#**\\",tokens[1]);
		assertEquals("0000101101100011100101000---", tokens[2]);
		
		data = "+++$#**\\1110000101101100011100101000---";
		
		tokens = decoder.splitRecord(data);
		
		assertEquals("111", tokens[0]);
		assertEquals("+++$#**\\", tokens[1]);
		assertEquals("0000101101100011100101000---", tokens[2]);
		
	}
	
	
	@Test
	public void testConversion() {
		message_decoding decoder = new message_decoding();
		
		assertEquals(7, decoder.binaryStringToInt("111"));
	}
	
	@Test
	public void testCheckForAllOnes() {
		message_decoding decoder = new message_decoding();
		
		assertTrue(decoder.isAllOnes("1111111"));
		assertTrue(decoder.isAllOnes("1"));
		assertFalse(decoder.isAllOnes("000001"));
		assertFalse(decoder.isAllOnes("0000010"));
	}
	
	@Test
	public void testGenerateDict() {
		
		message_decoding decoder = new message_decoding();
		
		Map<String, String> dict = decoder.generateDictionary("$2512gs/sgj");
		
		assertEquals("2",dict.get("00"));
		assertEquals("5",dict.get("01"));
		assertEquals("$",dict.get("0"));
		assertEquals("1",dict.get("10"));
		assertEquals("2",dict.get("000"));
	}
	
}
