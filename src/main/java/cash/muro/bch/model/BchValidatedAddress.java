package cash.muro.bch.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BchValidatedAddress {
	
	private boolean isvalid = false;
	private BchAddress address;
	private String scriptPubKey;
	private boolean isscript = false;

}
