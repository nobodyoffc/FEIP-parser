package identity;

public class Multisig {

	private int m;
	private int n;
	private String pubKeys[];
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public String[] getPubKeys() {
		return pubKeys;
	}
	public void setPubKeys(String[] pubKeys) {
		this.pubKeys = pubKeys;
	}
}
