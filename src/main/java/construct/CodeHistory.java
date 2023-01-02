package construct;

public class CodeHistory {
	
	private String id;
	private long height;
	private int index;
	private long time;
	private String signer;

	private String coid;
	private String op;
	private String name;
	private String version;
	private String hash;
	private String desc;
	private String[] langs;
	private String[] urls;
	private String[] protocols;
	private String pubKeyAdmin;
	private int rate;
	
	private long cdd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getCoid() {
		return coid;
	}

	public void setCoid(String codeId) {
		this.coid = codeId;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String[] getLangs() {
		return langs;
	}

	public void setLangs(String[] langs) {
		this.langs = langs;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public String[] getProtocols() {
		return protocols;
	}

	public void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}

	public String getPubKeyAdmin() {
		return pubKeyAdmin;
	}

	public void setPubKeyAdmin(String pubKeyAdmin) {
		this.pubKeyAdmin = pubKeyAdmin;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public long getCdd() {
		return cdd;
	}

	public void setCdd(long cdd) {
		this.cdd = cdd;
	}
	
	
}
