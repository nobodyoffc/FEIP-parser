package organization;

public class Group {
	private String gid;
	private String name;
	private String desc;
	
	private String[] namers;
	private String[] activeMembers;
	private String[] leftMembers;

	private long birthTime;
	private long birthHeight;
	private String lastTxid;
	private long lastTime;
	private long lastHeight;
	private long cddToUpdate;
	private long tCdd;
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long getBirthTime() {
		return birthTime;
	}
	public void setBirthTime(long birthTime) {
		this.birthTime = birthTime;
	}
	public long getBirthHeight() {
		return birthHeight;
	}
	public void setBirthHeight(long birthHeight) {
		this.birthHeight = birthHeight;
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
	public long getCddToUpdate() {
		return cddToUpdate;
	}
	public void setCddToUpdate(long requiredCdd) {
		this.cddToUpdate = requiredCdd;
	}
	public long gettCdd() {
		return tCdd;
	}
	public void settCdd(long tCdd) {
		this.tCdd = tCdd;
	}
	public String[] getNamers() {
		return namers;
	}
	public void setNamers(String[] namers) {
		this.namers = namers;
	}
	public String[] getActiveMembers() {
		return activeMembers;
	}
	public void setActiveMembers(String[] activeMembers) {
		this.activeMembers = activeMembers;
	}
	public String[] getLeftMembers() {
		return leftMembers;
	}
	public void setLeftMembers(String[] leftMembers) {
		this.leftMembers = leftMembers;
	}
}
