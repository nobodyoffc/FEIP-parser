package cidInfo;

public class RepuRecord {
	private String txid;
	private long height;
	private int index;
	private long time;
	
	private String addr;
	private String ratingAddr;
	private long amount;
	
	
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getRatingAddr() {
		return ratingAddr;
	}
	public void setRatingAddr(String ratingAddr) {
		this.ratingAddr = ratingAddr;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	
	
}
