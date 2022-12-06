package opReturn;

import java.util.ArrayList;

public class ResultReadOpReFromFile {
	private ArrayList<OpReturn> opReturnList;
	private long pointerInFile;
	private int count;
	private boolean fileEnd;
	private boolean rollback;
	public ArrayList<OpReturn> getOpReturnList() {
		return opReturnList;
	}
	public void setOpReturnList(ArrayList<OpReturn> opReturnList) {
		this.opReturnList = opReturnList;
	}
	public long getPointerInFile() {
		return pointerInFile;
	}
	public void setPointerInFile(long pointerInFile) {
		this.pointerInFile = pointerInFile;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
