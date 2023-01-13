package identity;

public class IdentityHistory {
	
	private String id;
	private long height;
	private int index;
	private long time;
	private String type;
	private String sn;
	private String ver;
	private String signer;
	private String data_op;
	private String data_name;
	
	private String data_priKey;
	private String data_master;
	private String[] data_homepages;
	private double data_noticeFee;
	
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getSigner() {
		return signer;
	}
	public void setSigner(String signer) {
		this.signer = signer;
	}
	public String getData_op() {
		return data_op;
	}
	public void setData_op(String data_op) {
		this.data_op = data_op;
	}
	public String getData_name() {
		return data_name;
	}
	public void setData_name(String data_name) {
		this.data_name = data_name;
	}
	public String getData_master() {
		return data_master;
	}
	public void setData_master(String data_master) {
		this.data_master = data_master;
	}
	public String[] getData_homepages() {
		return data_homepages;
	}
	public void setData_homepages(String[] data_homepage) {
		this.data_homepages = data_homepage;
	}
	public double getData_noticeFee() {
		return data_noticeFee;
	}
	public void setData_noticeFee(double d) {
		this.data_noticeFee = d;
	}
	public String getData_priKey() {
		return data_priKey;
	}
	public void setData_priKey(String data_priKey) {
		this.data_priKey = data_priKey;
	}


}
