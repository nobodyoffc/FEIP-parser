package cidInfo;

public class CidInfo {
	private String id;
	private String cid;
	private String [] usedCids;
	private long nameTime;
	private String master;
	private String[] homePage;
	private long noticFee;
	private long reputation;
	private long hot;
	private long height;
	
	
	public long getHot() {
		return hot;
	}
	public void setHot(long hot) {
		this.hot = hot;
	}
	public long getReputation() {
		return reputation;
	}
	public void setReputation(long reputation) {
		this.reputation = reputation;
	}
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
	public long getNameTime() {
		return nameTime;
	}
	public void setNameTime(long nameTime) {
		this.nameTime = nameTime;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String[] getHomePage() {
		return homePage;
	}
	public void setHomePage(String[] homePage) {
		this.homePage = homePage;
	}
	public long getNoticFee() {
		return noticFee;
	}
	public void setNoticFee(long noticFee) {
		this.noticFee = noticFee;
	}
	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	
	
}
