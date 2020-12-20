package cash.muro.bch.rpc.client.params;

public class ListTransactionsParams {

	private String label = "*";
	private int count = 10;
	private int skip = 0;
	private boolean watchOnly = false;	

	
	public void setLabel(String label) {
		this.label = label;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public void setWatchOnly(boolean watchOnly) {
		this.watchOnly = watchOnly;
	}

	public String getLabel() {
		return label;
	}

	public int getCount() {
		return count;
	}

	public int getSkip() {
		return skip;
	}

	public boolean isWatchOnly() {
		return watchOnly;
	}
}
