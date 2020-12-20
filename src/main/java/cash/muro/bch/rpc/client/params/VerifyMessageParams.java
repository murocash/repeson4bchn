package cash.muro.bch.rpc.client.params;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VerifyMessageParams {
	
	private String address;
	private String signature;
	private String message;
	

}
