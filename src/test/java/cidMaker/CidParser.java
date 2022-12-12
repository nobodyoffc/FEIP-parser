package cidMaker;

import java.io.IOException;
import java.util.Map;

import opReturn.*;

public class CidParser {
	
	public void parseFromFile() throws IOException {
		
		boolean isMore = true;
		
		while(isMore) {
		
		opReReadResult readResult = new OpReFileTools().readOpReturnFromFileToList(0, 0);
		
		isMore = !readResult.isFileEnd();
	
		//Map<String,Object> parseResult = parse(readResult.getOpReturnList());
		
		//writeToEs(parseResult);
		
		writeToEs(null);
		}
	}



	private void writeToEs(Map<String, Object> parseResult) {
		// TODO Auto-generated method stub
		
	}
}
