package construct;

public class RateHistory {
	
	private String id;
	private long height;
	private int index;
	private long time;
	private String rater;
	
	private String type;
	private String rateeId;
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

	public String getRater() {
		return rater;
	}

	public void setRater(String rater) {
		this.rater = rater;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRateeId() {
		return rateeId;
	}

	public void setRateeId(String rateeId) {
		this.rateeId = rateeId;
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
