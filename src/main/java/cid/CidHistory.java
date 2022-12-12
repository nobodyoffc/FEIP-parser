package cid;

public class CidHistory {
	
	private String txid;
	private long height;
	private int index;
	private long time;
	private String addr;
	private int feipNum;
	private int feipVer;
	private String op;
	private String name;
	private String master;
	private String homepage[];
	private String noticeFee;
	
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
	public int getFeipNum() {
		return feipNum;
	}
	public void setFeipNum(int feipNum) {
		this.feipNum = feipNum;
	}
	public int getFeipVer() {
		return feipVer;
	}
	public void setFeipVer(int feipVer) {
		this.feipVer = feipVer;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String[] getHomepage() {
		return homepage;
	}
	public void setHomepage(String[] homepage) {
		this.homepage = homepage;
	}
	public String getNoticeFee() {
		return noticeFee;
	}
	public void setNoticeFee(String noticeFee) {
		this.noticeFee = noticeFee;
	}
	
	
}
