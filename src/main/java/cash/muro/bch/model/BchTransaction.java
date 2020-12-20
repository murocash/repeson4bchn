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
public class BchTransaction implements Serializable {
	
	private BchAddress address;
	private enum Category { send, receive };
	private Category category;
	private BigDecimal amount;
	private String label;
	private int vout;
	private BigDecimal fee;
	private int confirmations;
	private Boolean trusted;
	private String blockhash;
	private int blockindex;
	private Long blocktime;
	private String txid;
	private List<String> walletconflicts;
	private Long time;
	private Long timereceived;
	private String error;
	private Boolean abandoned;
	private String id;

	public void setAddress(String address) {
		this.address = new BchAddress(address);
	}
	
}
