package cash.muro.bch.rpc.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.emiperez.commons.idgenerators.DateTimeIdGenerator;
import com.emiperez.repeson.client.JsonRpcException;

import cash.muro.bch.model.BchAddress;
import cash.muro.bch.model.BchReceived;
import cash.muro.bch.model.BchTransaction;
import cash.muro.bch.model.BchValidatedAddress;

class BchnRpcClientIT {

	private static final BchAddress COMMON_ADDRESS = new BchAddress("bchtest:qpsypwgap4v32fu7jh2rq8ejggh47rawqq85578sv2");
	private static final BchAddress INCORRECT_ADDRESS = new BchAddress("bchtest:qpsypwgap4v12fu7jh2rq8ejggh47rawqq85578sv2");
	private static final BchAddress ADDRESS_WITH_RECEIVED = new BchAddress("bchtest:qrv68tpxfdhy0ky69suqvmj3kcqucxv8rc7nlxq8lw");
	private static final BigDecimal RECEIVED_AMOUNT = new BigDecimal("0.00015000");
	private static final String SIGNATURE = "H3ZjfBk6uuwQwukV3vBx1WNMo4CfRlDIh6q59yGq7Yd+BAKQv8+ujYrD7j47ro6k02ICdEwKBKeEErgijqZVAeA=";
	private static final String CHANGED_SIGNATURE = "H3ZjfBk6uuwQwukV3vBx1WNMo4AfRlDIh6q59yGq7Yd+BAKQv8+ujYrD7j47ro6k02ICdEwKBKeEErgijqZVAeA=";
	private static final String MESSAGE = "my message";
	
	private static BchnRpcClient client;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		InputStream inputStream = BchnRpcClientIT.class.getClassLoader().getResourceAsStream("it.properties");
		Properties prop = new Properties();
		prop.load(inputStream);
		client = new BchnRpcClient.Builder(new URI(prop.getProperty("node"))).userName(prop.getProperty("username")).password(prop.getProperty("password"))
				.idGenerator(new DateTimeIdGenerator()).build();
	}

	@Test
	void testGetBalance() throws IOException, InterruptedException, JsonRpcException {
		assertTrue(client.getBalance().compareTo(BigDecimal.valueOf(0)) > -1);
	}

	@Test
	void testGetNewAddress() throws IOException, InterruptedException, JsonRpcException {
		BchAddress address = client.getNewAddress();
		assertTrue(address.toString().startsWith("bchtest:") && address.toString().length() > 9);
	}

	@Test
	void testValidateAddress() throws IOException, InterruptedException, JsonRpcException {
		BchValidatedAddress vAddress = client.validateAddress(COMMON_ADDRESS);
		assertTrue(vAddress.isIsvalid() && !vAddress.isIsscript());

		vAddress = client.validateAddress(INCORRECT_ADDRESS);
		assertTrue(!vAddress.isIsvalid() && !vAddress.isIsscript());
	}

	@Test
	void testListTransactions() throws IOException, InterruptedException, JsonRpcException {
		ArrayList<BchTransaction> txs = client.listTransactions(2, 0);
		assertEquals(txs.size(), 2);
	}

	@Test
	void testListReceivedByAddres() throws IOException, InterruptedException, JsonRpcException {
		BchAddress address = ADDRESS_WITH_RECEIVED;
		BchReceived received = client.listReceivedByAddress(0, address).get(0);
		assertEquals(received.getAddress(), address);
		assertEquals(received.getAmount().compareTo(RECEIVED_AMOUNT), 0);
	}

	@Test
	void testVerifyMessage() throws IOException, InterruptedException, JsonRpcException {
		assertTrue(client.verifyMessage(COMMON_ADDRESS, SIGNATURE, MESSAGE));
		assertFalse(client.verifyMessage(COMMON_ADDRESS, CHANGED_SIGNATURE, MESSAGE));
	}
	
	@Test
	void testSendToAddress() throws IOException, InterruptedException, JsonRpcException {
		BchAddress address = client.getNewAddress();
		BigDecimal amount = client.getBalance();
		String txId = client.sendToAddress(address, amount, true);
		assertTrue(txId != null && txId.matches("^[0-9a-fA-F]{64}"));
	}

	@Test
	void testGetBalanceAsync() throws JsonRpcException, InterruptedException, ExecutionException {
		CompletableFuture<BigDecimal> getBalance = client.getBalanceAsync();
		assertTrue(getBalance.get().compareTo(BigDecimal.valueOf(0)) > 0);

	}

	@Test
	void testGetNewAddressAsync() throws JsonRpcException, InterruptedException, ExecutionException {
		CompletableFuture<BchAddress> getNewAddress = client.getNewAddressAsync();
		String address = getNewAddress.get().toString();
		assertTrue(address.startsWith("bchtest:") && address.toString().length() > 9);
	}

	@Test
	void testValidateAddressAsync() throws JsonRpcException, InterruptedException, ExecutionException {
		CompletableFuture<BchValidatedAddress> validateAddress = client.validateAddressAsync(COMMON_ADDRESS);
		BchValidatedAddress vAddress = validateAddress.get();
		assertTrue(vAddress.isIsvalid() && !vAddress.isIsscript());

		validateAddress = client.validateAddressAsync(INCORRECT_ADDRESS);
		vAddress = validateAddress.get();
		assertTrue(!vAddress.isIsvalid() && !vAddress.isIsscript());
	}

	@Test
	void testListTransactionsAsync() throws JsonRpcException, InterruptedException, ExecutionException {
		CompletableFuture<ArrayList<BchTransaction>> transactionList = client.listTransactionsAsync(3, 0);
		ArrayList<BchTransaction> txs = transactionList.get();
		assertEquals(txs.size(), 3);
	}

	@Test
	void testListReceivedByAddresAsync() throws JsonRpcException, InterruptedException, ExecutionException {
		BchAddress address = ADDRESS_WITH_RECEIVED;
		CompletableFuture<ArrayList<BchReceived>> received = client.listReceivedByAddressAsync(0, address);
		BchReceived totalReceived = received.get().get(0);
		assertEquals(totalReceived.getAddress(), address);
		assertEquals(totalReceived.getAmount().compareTo(RECEIVED_AMOUNT), 0);
	}

	@Test
	void testVerifyMessageAsync() throws IOException, InterruptedException, JsonRpcException, ExecutionException {
		CompletableFuture<Boolean> verifyMessage = client.verifyMessageAsync(COMMON_ADDRESS, SIGNATURE, MESSAGE);
		assertTrue(verifyMessage.get());
		CompletableFuture<Boolean> falseMessage = client.verifyMessageAsync(COMMON_ADDRESS, CHANGED_SIGNATURE, MESSAGE);
		assertFalse(falseMessage.get());
	}

	@Test
	void testSendToAddressAsync() throws InterruptedException, ExecutionException, JsonRpcException {
		CompletableFuture<BchAddress> getNewAddress = client.getNewAddressAsync();
		CompletableFuture<BigDecimal> getBalance = client.getBalanceAsync();
		CompletableFuture<String> sendToAddress = getNewAddress.thenCombine(getBalance, (address, amount) -> {
			try {
				return client.sendToAddressAsync(address, amount, true).get();
			} catch (InterruptedException | ExecutionException | JsonRpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		});
		String txId = sendToAddress.get();
		assertTrue(txId != null && txId.matches("^[0-9a-fA-F]{64}"));
	}
}
