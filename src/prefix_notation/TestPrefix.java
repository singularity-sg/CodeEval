package prefix_notation;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class TestPrefix {

	@Test
	public void testMain() {
		prefix.main(new String[]{"prefix.txt"});
	}
	
	@Test
	public void testProcess() {
		prefix.process("+ * / + 4 5 3 7 6");
	}

	@Test
	public void testCalculate() {
		assertEquals(9, prefix.calculate("+", "4", "5"));
		assertEquals(20, prefix.calculate("*", "4", "5"));
		assertEquals(4, prefix.calculate("/", "24", "6"));
	}
	
	@Test
	public void testStringToList() {
		
		LinkedList<String> list = prefix.stringToList("/ + * 5 6 6 6");
		
		assertEquals(7, list.size());
	}
	
	@Test
	public void testDecipher() {
		
		String test = "/ + * 5 6 6 6";
		
		assertEquals(6, prefix.decipher(prefix.stringToList(test)));
		
		test = "/ * + 2 3 30 50";
		
		assertEquals(3, prefix.decipher(prefix.stringToList(test)));
		
	}
}
