package cid;

public class Cid {
	
	private String id;
	private String cid;
	private String [] usedCids;
	private String master;
	private String[] homepage;
	private double noticFee;
	private long reputation;
	private long hot;
	private long nameTime;
	
	private long lastHeight;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String[] getUsedCids() {
		return usedCids;
	}

	public void setUsedCids(String[] usedCids) {
		this.usedCids = usedCids;
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

	public double getNoticFee() {
		return noticFee;
	}

	public void setNoticFee(double noticFee) {
		this.noticFee = noticFee;
	}

	public long getReputation() {
		return reputation;
	}

	public void setReputation(long reputation) {
		this.reputation = reputation;
	}

	public long getHot() {
		return hot;
	}

	public void setHot(long hot) {
		this.hot = hot;
	}

	public long getNameTime() {
		return nameTime;
	}

	public void setNameTime(long nameTime) {
		this.nameTime = nameTime;
	}

	public long getLastHeight() {
		return lastHeight;
	}

	public void setLastHeight(long lastHeight) {
		this.lastHeight = lastHeight;
	}
	

	
}
