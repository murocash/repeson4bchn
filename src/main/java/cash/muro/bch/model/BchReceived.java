package cash.muro.bch.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BchReceived implements Serializable{
	private boolean involvesWatchonly;
	private BchAddress address;
	private BigDecimal amount;
	private int confirmations;
	private String account;
	private String label;
	private List<String> txids;
	
	public void setAddress(String address) {
		this.address = new BchAddress(address);
	}
}
