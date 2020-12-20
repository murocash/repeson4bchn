package cash.muro.bch.rpc.client.params;

import java.math.BigDecimal;

import cash.muro.bch.model.BchAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SendToAddressParams {

	private BchAddress address;
	private BigDecimal amount = new BigDecimal(0);
	private String comment = "";
	private String commentTo = "";
	private boolean substractFeeFromAmount = false;
	
	public SendToAddressParams(BchAddress address, BigDecimal amount) {
		this.address = address;
		this.amount = amount;
	}
	
	public SendToAddressParams(BchAddress address, BigDecimal amount, boolean substractFeeFromAmount) {
		this.address = address;
		this.amount = amount;
		this.substractFeeFromAmount = substractFeeFromAmount;
	}	
	
	public String getAddress() {
		return address.toString();
	}
}
