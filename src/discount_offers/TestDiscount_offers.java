package discount_offers;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import org.junit.Test;

public class TestDiscount_offers {

	@Test
	public void testSplitRecord() {
		
		String test = "Jeffery Lebowski,Walter Sobchak;Half & Half,Colt M1911A1";
	
		discount_offers d = new discount_offers();
		
		Map<String, List<String>> res = d.splitRecord(test);
		
		assertTrue(res.containsKey("customers"));
		assertTrue(res.containsKey("products"));
		
		List<String> customers = res.get("customers");
		List<String> products = res.get("products");
		
		assertEquals(2, customers.size());
		assertEquals(2, products.size());
		
	}
	
	@Test
	public void testCalculateScore() {
		
		discount_offers d = new discount_offers();
		
		Map<String, List<String>> data = d.splitRecord("Jeffery Lebowski,Walter Sobchak;Half & Half,Colt M1911A1");
		
		List<String> customers = data.get("customers");
		List<String> products = data.get("products");
		
		assertEquals(19.5f, d.calculateScore(customers, products));
		
		data = d.splitRecord("Mahmoud Abdelkader,Gabriel Sinkin;Batman No. 1,Printer paper");
		
		customers = data.get("customers");
		products = data.get("products");
		
		assertEquals(18.0f, d.calculateScore(customers, products));
		
		data = d.splitRecord("Mahmoud Abdelkader,Gabriel Sinkin;Batman No. 1,Printer paper,Toilet Roll");
		
		customers = data.get("customers");
		products = data.get("products");
		
		assertEquals(18.0f, d.calculateScore(customers, products));
		
		data = d.splitRecord("Helsinski Ong,Mahmoud Abdelkader,Gabriel Sinkin;Batman No. 1,Printer paper");
		
		customers = data.get("customers");
		products = data.get("products");
		
		assertEquals(19.5f, d.calculateScore(customers, products));
		
		
		data = d.splitRecord("James Blunt,Tom Jones,Jimmy White,Casey Keller,Jose Mourinho,Wyatt Howard,Kevin Jones,Jim Morrison,Pink Floyd,David Viard,Joe Cole,Kim Gordon;Cool Aid,M16 Assault Rifle, Gel 'O,Crazy Snake,Band Aid 101");
		customers = data.get("customers");
		products = data.get("products");
		
		assertEquals(43.5f, d.calculateScore(customers, products));
	}
	
	@Test
	public void testCleanString() {
		discount_offers d = new discount_offers();
		
		assertEquals("abcdef", d.cleanString("a b,c'd e\\f"));
	}
	
	@Test
	public void testNoOfVowels() {
		discount_offers d = new discount_offers();
		
		assertEquals(5, d.noOfVowels("apepipOmU"));
		assertEquals(4, d.noOfVowels("GA IpipOm"));
	}
	
	@Test
	public void testNoOfConsonants() {
		discount_offers d = new discount_offers();
		
		assertEquals(9, d.noOfConsonants("12casfdgdsgh"));
		
	}
	
	@Test
	public void testCalculateScoreForOne() {
		discount_offers d = new discount_offers();
			
		assertEquals(10.5f, d.calculateScoreForOne("Mahmoud Abdelkader", "Batman No. 1"));
		assertEquals(10.5f, d.calculateScoreForOne("Mahmoud Abdelkader", "Printer paper"));
		assertEquals(10.5f, d.calculateScoreForOne("Mahmoud Abdelkader", "Toilet Roll"));
		assertEquals(7.5f, d.calculateScoreForOne("Gabriel Sinkin", "Batman No. 1"));
		assertEquals(7.5f, d.calculateScoreForOne("Gabriel Sinkin", "Printer paper"));
		assertEquals(7.5f, d.calculateScoreForOne("Gabriel Sinkin", "Toilet Roll"));
		assertEquals(9.0f, d.calculateScoreForOne("Helsinski Ong", "Batman No. 1"));
		assertEquals(9.0f, d.calculateScoreForOne("Helsinski Ong", "Printer paper"));
		assertEquals(9f, d.calculateScoreForOne("John Holsmani", "Another one & of the iproducts"));
		
	}
	
	@Test
	public void testGcd() {
		assertTrue(discount_offers.gcd(24, 12) > 1);
	}
}
