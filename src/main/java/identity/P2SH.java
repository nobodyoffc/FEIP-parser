package identity;

public class P2SH {
	private String id;
	private String redeemScript;
	private int m;
	private int n;
	private String pubKeys[];
	private long birthHeight;
	private long birthTime;
	private String birthTxid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRedeemScript() {
		return redeemScript;
	}
	public void setRedeemScript(String redeemScript) {
		this.redeemScript = redeemScript;
	}
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
	public long getBirthHeight() {
		return birthHeight;
	}
	public void setBirthHeight(long birthHeight) {
		this.birthHeight = birthHeight;
	}
	public long getBirthTime() {
		return birthTime;
	}
	public void setBirthTime(long birthTime) {
		this.birthTime = birthTime;
	}
	public String getBirthTxid() {
		return birthTxid;
	}
	public void setBirthTxid(String birthTxid) {
		this.birthTxid = birthTxid;
	}
	
	
}
