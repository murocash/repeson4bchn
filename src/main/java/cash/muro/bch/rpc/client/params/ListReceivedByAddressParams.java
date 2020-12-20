package cash.muro.bch.rpc.client.params;

import cash.muro.bch.model.BchAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ListReceivedByAddressParams {
	private int minConfirmations =3;
	private boolean includeEmpty = false;
	private boolean includeWatchonly = false;
	private final BchAddress address;
	
	public ListReceivedByAddressParams(BchAddress address) {
		this.address = address;
	}
	
	public ListReceivedByAddressParams(int minConfirmations, BchAddress address) {
		this.minConfirmations = minConfirmations;
		this.address = address;
	}
	
	public String getAddress() {
		return address.toString();
	}
	
}
