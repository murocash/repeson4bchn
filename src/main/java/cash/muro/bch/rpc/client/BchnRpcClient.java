package cash.muro.bch.rpc.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.emiperez.commons.idgenerators.ConstantIdGenerator;
import com.emiperez.commons.idgenerators.IdGenerator;
import com.emiperez.repeson.client.JsonRpcClient;
import com.emiperez.repeson.client.JsonRpcException;
import com.emiperez.repeson.client.JsonRpcResponse;
import com.emiperez.repeson.client.JsonRpcVersion;
import com.emiperez.repeson.transport.HttpTransport;
import com.emiperez.repeson.transport.Transport;

import cash.muro.bch.model.BchAddress;
import cash.muro.bch.model.BchTransaction;
import cash.muro.bch.model.BchReceived;
import cash.muro.bch.model.BchValidatedAddress;
import cash.muro.bch.rpc.client.params.ListReceivedByAddressParams;
import cash.muro.bch.rpc.client.params.ListTransactionsParams;
import cash.muro.bch.rpc.client.params.SendToAddressParams;
import cash.muro.bch.rpc.client.params.VerifyMessageParams;
import cash.muro.bch.rpc.client.responses.BchReceivedByAddressListResponse;
import cash.muro.bch.rpc.client.responses.BchTransactionListResponse;
import cash.muro.bch.rpc.client.responses.BchValidatedAddressResponse;

public class BchnRpcClient {

	private enum Method {
		getbalance, getnewaddress, listreceivedbyaddress, listtransactions, sendtoaddress, validateaddress, verifymessage;
	};

	private JsonRpcClient jsonRpcClient;

	public static class Builder {
		private URI uri;
		private String userName = "";
		private String password = "";
		private Version httpVersion = Version.HTTP_1_1;
		private JsonRpcVersion jsonRpcVersion = JsonRpcVersion.v1_1;
		private IdGenerator<?> idGenerator;
		private Duration timeout = Duration.ofSeconds(5);
		private String contentType = "plain/text";

		private Builder() {
		}

		public Builder(URI uri) {
			this();
			this.uri = uri;
		}

		public Builder uri(URI uri) {
			this.uri = uri;
			return this;
		}

		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder httpVersion(Version httpVersion) {
			this.httpVersion = httpVersion;
			return this;
		}

		public Builder timeout(Duration timeout) {
			this.timeout = timeout;
			return this;
		}

		public Builder contentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public Builder jsonRpcVersion(JsonRpcVersion jsonRpcVersion) {
			this.jsonRpcVersion = jsonRpcVersion;
			return this;
		}

		public Builder idGenerator(IdGenerator<?> idGenerator) {
			this.idGenerator = idGenerator;
			return this;
		}

		public BchnRpcClient build() {

			HttpClient httpClient = HttpClient.newBuilder().authenticator(new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password.toCharArray());
				}
			}).version(this.httpVersion).connectTimeout(this.timeout).build();

			Transport transport = HttpTransport.builder(httpClient).uri(uri).contentType(contentType).build();

			if (idGenerator == null) {
				idGenerator = new ConstantIdGenerator<String>("BCHN4j");
			}

			JsonRpcClient jsonRpcClient = JsonRpcClient.builder().transport(transport).version(jsonRpcVersion).idGenerator(idGenerator).build();

			BchnRpcClient client = new BchnRpcClient();
			client.jsonRpcClient = jsonRpcClient;
			return client;
		}

	}

	public BigDecimal getBalance() throws IOException, InterruptedException, JsonRpcException {
		return (BigDecimal) jsonRpcClient.sendRequestWithDefaults(Method.getbalance.toString()).getResult();
	}

	public CompletableFuture<BigDecimal> getBalanceAsync() throws JsonRpcException {
		return jsonRpcClient.sendRequestWithDefaultsAsync(Method.getbalance.toString()).thenApply(r -> (BigDecimal) r.getResult());
	}

	public BchAddress getNewAddress() throws IOException, InterruptedException, JsonRpcException {
		return new BchAddress((String) jsonRpcClient.sendRequestWithDefaults(Method.getnewaddress.toString()).getResult());
	}

	public CompletableFuture<BchAddress> getNewAddressAsync() throws JsonRpcException {
		return jsonRpcClient.sendRequestWithDefaultsAsync(Method.getnewaddress.toString()).thenApply(r -> new BchAddress((String) r.getResult()));
	}

	public BchValidatedAddress validateAddress(BchAddress address) throws IOException, InterruptedException, JsonRpcException {
		return (BchValidatedAddress) jsonRpcClient
				.sendRequestWithDefaults(Method.validateaddress.toString(), address.toString(), BchValidatedAddressResponse.class).getResult();
	}

	public CompletableFuture<BchValidatedAddress> validateAddressAsync(BchAddress address) throws JsonRpcException {
		return jsonRpcClient.sendRequestWithDefaultsAsync(Method.validateaddress.toString(), address.toString(), BchValidatedAddressResponse.class)
				.thenApply(JsonRpcResponse::getResult);
	}

	public ArrayList<BchTransaction> listTransactions(int count, int skip) throws IOException, InterruptedException, JsonRpcException {
		ListTransactionsParams params = new ListTransactionsParams();
		params.setCount(count);
		params.setSkip(skip);
		return jsonRpcClient.sendRequestWithDefaults(Method.listtransactions.toString(), params, BchTransactionListResponse.class).getResult();
	}

	public CompletableFuture<ArrayList<BchTransaction>> listTransactionsAsync(int count, int skip) throws JsonRpcException {
		ListTransactionsParams params = new ListTransactionsParams();
		params.setCount(count);
		params.setSkip(skip);
		return jsonRpcClient.sendRequestWithDefaultsAsync(Method.listtransactions.toString(), params, BchTransactionListResponse.class)
				.thenApply(JsonRpcResponse::getResult);
	}

	public ArrayList<BchReceived> listReceivedByAddress(int minConfirmations, BchAddress address) throws IOException, InterruptedException, JsonRpcException {
		ListReceivedByAddressParams params = new ListReceivedByAddressParams(minConfirmations, address);
		return jsonRpcClient.sendRequestWithDefaults(Method.listreceivedbyaddress.toString(), params, BchReceivedByAddressListResponse.class).getResult();
	}

	public CompletableFuture<ArrayList<BchReceived>> listReceivedByAddressAsync(int minConfirmations, BchAddress address) throws JsonRpcException {
		ListReceivedByAddressParams params = new ListReceivedByAddressParams(minConfirmations, address);
		return jsonRpcClient.sendRequestWithDefaultsAsync(Method.listreceivedbyaddress.toString(), params, BchReceivedByAddressListResponse.class)
				.thenApply(JsonRpcResponse::getResult);
	}

	public boolean verifyMessage(BchAddress address, String signature, String message) throws IOException, InterruptedException, JsonRpcException {
		VerifyMessageParams params = new VerifyMessageParams(address.toString(), signature, message);
		return (Boolean) jsonRpcClient.sendRequestWithDefaults(Method.verifymessage.toString(), params).getResult();
	}

	public CompletableFuture<Boolean> verifyMessageAsync(BchAddress address, String signature, String message)
			throws IOException, InterruptedException, JsonRpcException {
		VerifyMessageParams params = new VerifyMessageParams(address.toString(), signature, message);
		return jsonRpcClient.sendRequestWithDefaultsAsync(Method.verifymessage.toString(), params).thenApply(r -> (Boolean) r.getResult());
	}

	public String sendToAddress(BchAddress address, BigDecimal amount, boolean substractFeeFromAmount)
			throws IOException, InterruptedException, JsonRpcException {
		SendToAddressParams params = new SendToAddressParams(address, amount, substractFeeFromAmount);
		return (String) jsonRpcClient.sendRequestWithDefaults(Method.sendtoaddress.toString(), params).getResult();
	}

	public CompletableFuture<String> sendToAddressAsync(BchAddress address, BigDecimal amount, boolean substractFeeFromAmount) throws JsonRpcException {
		SendToAddressParams params = new SendToAddressParams(address, amount, substractFeeFromAmount);
		return jsonRpcClient.sendRequestWithDefaultsAsync(Method.sendtoaddress.toString(), params).thenApply(r -> (String) r.getResult());

	}

}
