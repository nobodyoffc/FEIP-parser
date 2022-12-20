package parser;

import java.util.ArrayList;

import data.OpReturn;

public class opReReadResult {
	private OpReturn opReturn;
	private long length;
	private boolean fileEnd;
	private boolean rollback;
	
	public OpReturn getOpReturn() {
		return opReturn;
	}
	public void setOpReturn(OpReturn opReturn) {
		this.opReturn = opReturn;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public boolean isFileEnd() {
		return fileEnd;
	}
	public void setFileEnd(boolean fileEnd) {
		this.fileEnd = fileEnd;
	}
	public boolean isRollback() {
		return rollback;
	}
	public void setRollback(boolean rollback) {
		this.rollback = rollback;
	}
	

}
