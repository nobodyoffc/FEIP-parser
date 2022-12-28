package construct;

public class ServiceParamsRaw {
	
	private String urlHead;
	private String currency;
	private String account;
	private float pricePerRequest;
	private float minPayment;
	
	public String getUrlHead() {
		return urlHead;
	}
	public void setUrlHead(String urlHead) {
		this.urlHead = urlHead;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public float getPricePerRequest() {
		return pricePerRequest;
	}
	public void setPricePerRequest(float pricePerRequest) {
		this.pricePerRequest = pricePerRequest;
	}
	public float getMinPayment() {
		return minPayment;
	}
	public void setMinPayment(float minPayment) {
		this.minPayment = minPayment;
	}
	
}
