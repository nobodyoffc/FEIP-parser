package construct;

public class Service {

	private String sid;
	private String stdName;
	private String[] localNames;
	private String desc;
	private String[] types;
	private String[] urls;
	private String pubKeyAdmin;
	private String[] protocols;
	private String[] codes;
	private Object params;
	private String owner;
	
	private long firstTime;
	private long firstHeight;
	private String lastTxid;
	private long lastTime;
	private long lastHeight;
	private long tCdd;
	private float tRate;
	private boolean active;
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String[] getLocalNames() {
		return localNames;
	}
	public void setLocalNames(String[] localNames) {
		this.localNames = localNames;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String[] getTypes() {
		return types;
	}
	public void setTypes(String[] types) {
		this.types = types;
	}
	public String[] getUrls() {
		return urls;
	}
	public void setUrls(String[] urls) {
		this.urls = urls;
	}
	public String getPubKeyAdmin() {
		return pubKeyAdmin;
	}
	public void setPubKeyAdmin(String pubKeyAdmin) {
		this.pubKeyAdmin = pubKeyAdmin;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String signer) {
		this.owner = signer;
	}
	public long getFirstTime() {
		return firstTime;
	}
	public void setFirstTime(long firstTime) {
		this.firstTime = firstTime;
	}
	public long getFirstHeight() {
		return firstHeight;
	}
	public void setFirstHeight(long firstHeight) {
		this.firstHeight = firstHeight;
	}
	public String getLastTxid() {
		return lastTxid;
	}
	public void setLastTxid(String lastTxid) {
		this.lastTxid = lastTxid;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	public long getLastHeight() {
		return lastHeight;
	}
	public void setLastHeight(long lastHeight) {
		this.lastHeight = lastHeight;
	}
	public long gettCdd() {
		return tCdd;
	}
	public void settCdd(long tCdd) {
		this.tCdd = tCdd;
	}
	public float gettRate() {
		return tRate;
	}
	public void settRate(float tRate) {
		this.tRate = tRate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String[] getProtocols() {
		return protocols;
	}
	public void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}
	public Object getParams() {
		return params;
	}
	public void setParams(Object params) {
		this.params = params;
	}
	public String[] getCodes() {
		return codes;
	}
	public void setCodes(String[] codes) {
		this.codes = codes;
	}
	
	
	
}
