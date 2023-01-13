package organization;

public class Team {

	private String tid;
	private String owner;
	private String stdName;
	private String[] localNames;
	private String consensusHash;
	private String desc;
	
	private String[] activeMembers;
	private String[] leftMembers;
	private String[] administrators;
	private String transferee;
	private String[] invitees;
	private String[] leavers;
	private String[] notAgreeMembers;
	
	private long birthTime;
	private long birthHeight;
	private String lastTxid;
	private long lastTime;
	private long lastHeight;
	private long tCdd;
	private float tRate;
	private boolean active;
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
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
	public String getConsensusHash() {
		return consensusHash;
	}
	public void setConsensusHash(String consensusHash) {
		this.consensusHash = consensusHash;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	public String[] getAdministrators() {
		return administrators;
	}
	public void setAdministrators(String[] administrators) {
		this.administrators = administrators;
	}
	public String getTransferee() {
		return transferee;
	}
	public void setTransferee(String transferee) {
		this.transferee = transferee;
	}
	public String[] getInvitees() {
		return invitees;
	}
	public void setInvitees(String[] invitees) {
		this.invitees = invitees;
	}
	public String[] getLeavers() {
		return leavers;
	}
	public void setLeavers(String[] leavers) {
		this.leavers = leavers;
	}
	public String[] getNotAgreeMembers() {
		return notAgreeMembers;
	}
	public void setNotAgreeMembers(String[] notAgreeMembers) {
		this.notAgreeMembers = notAgreeMembers;
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

	
	
}
