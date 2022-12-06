package cidInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import opReturn.*;

public class Parser {
	
	public void parseFromFile() throws IOException {
		
		boolean isMore = true;
		
		while(isMore) {
		
		ResultReadOpReFromFile readResult = Methods.readOpReturnFromFileToList(0, 0);
		
		isMore = !readResult.isFileEnd();
	
		//Map<String,Object> parseResult = parse(readResult.getOpReturnList());
		
		//writeToEs(parseResult);
		
		}
	}



	private void writeToEs(Map<String, Object> parseResult) {
		// TODO Auto-generated method stub
		
	}
}
