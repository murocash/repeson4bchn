package cash.muro.bch.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BchAddressTest {

	@Test
	void whenBchtestPrefixIsCashAddressTest() {
		assertTrue(BchAddress.isCashAddress("bchtest:qr944f7cznz78mmd2f8w455l2azcxptylyj4xdeevf"));
	}
	
	@Test
	void whenBitcoincashPrefixIsCashAddressTest() {
		assertTrue(BchAddress.isCashAddress("bitcoincash:qpscef2g644pe6d6mzxafa9j0araydt8vgw9g9uv2x"));
	}
	
	@Test
	void whenNoPrefixIsNotCashAddressTest() {
		assertFalse(BchAddress.isCashAddress("qr944f7cznz78mmd2f8w455l2azcxptylyj4xdeevf"));
	}
	
	@Test
	void whenLessThan42CharacterIsNotCashAddressTest() {
		assertFalse(BchAddress.isCashAddress("bitcoincash:qpscef2g644pe6d6mzxafa9j0araydt8vgw9g9uv2"));
	}
	
	@Test
	void whenMoreThan42CharacterIsNotCashAddressTest() {
		assertFalse(BchAddress.isCashAddress("bitcoincash:qpscef2g644pe6d6mzxafa9j0araydt8vgw9g9uv2x7"));
	}
	
	@Test
	void whenHasBCharacterIsNotCashAddressTest() {
		assertFalse(BchAddress.isCashAddress("bitcoincash:qpscef2g644pe6d6mzxafa9j0araydt8vgw9g9uv2b"));
	}

}
