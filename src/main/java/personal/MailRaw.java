package personal;

public class MailRaw {

	private String op;
	private String sendTxid;
	private String alg;
	private String msg;
	private String ciphertextSend;
	private String ciphertextReci;
	private String textHash;
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getSendTxid() {
		return sendTxid;
	}
	public void setSendTxid(String sendTxid) {
		this.sendTxid = sendTxid;
	}
	public String getAlg() {
		return alg;
	}
	public void setAlg(String alg) {
		this.alg = alg;
	}
	public String getCiphertextSend() {
		return ciphertextSend;
	}
	public void setCiphertextSend(String ciphertextSend) {
		this.ciphertextSend = ciphertextSend;
	}
	public String getCiphertextReci() {
		return ciphertextReci;
	}
	public void setCiphertextReci(String ciphertextReci) {
		this.ciphertextReci = ciphertextReci;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTextHash() {
		return textHash;
	}
	public void setTextHash(String textHash) {
		this.textHash = textHash;
	}
	
	
}
