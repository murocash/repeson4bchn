package cash.muro.bch.model;

import java.io.Serializable;
import java.util.regex.Pattern;

public class BchAddress implements Serializable {
	
	private static final Pattern ADDRESS_PATTERN = Pattern.compile("^(bitcoincash|bchtest|bchreg):[p|q][acdefghjklmnpqrstvuwxyz234567890]{41}$");
	
	private String address = "";

	public BchAddress(String address) {
		this.address = address;
	}
	
	public static boolean isCashAddress(String address) {
		return ADDRESS_PATTERN.matcher(address).find();
	}

	@Override
	public String toString() {
		return address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BchAddress other = (BchAddress) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}

}
