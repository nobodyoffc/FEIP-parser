package opReturn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import Tools.BytesTools;

public class Methods {
	
	public static void writeOpReturnListIntoFile(ArrayList<OpReturn> opList) {

		if(opList.isEmpty())return;
		boolean error = false;
		do {
			try {
				File opFile = new File("opretune.byte");
				FileOutputStream opos;
				if(opFile.exists()) {
					opos = new FileOutputStream(opFile,true);
				}else {
					opos = new FileOutputStream(opFile);
				}
				Iterator<OpReturn> iterOp = opList.iterator();
				while(iterOp.hasNext()) {
					ArrayList<byte[]> opArrList = new ArrayList<byte[]>();
					OpReturn op = iterOp.next();
					
					opArrList.add(BytesTools.intToByteArray(120+op.getOpReturn().getBytes().length));
					opArrList.add(BytesTools.hexToByteArray(op.getId()));
					opArrList.add(BytesTools.longToBytes(op.getHeight()));
					opArrList.add(BytesTools.intToByteArray(op.getTxIndex()));
					opArrList.add(op.getSigner().getBytes());
					if(op.getRecipient()==null) {
						opArrList.add("                                  ".getBytes());
					}else {
						opArrList.add(op.getRecipient().getBytes());
					}
					opArrList.add(BytesTools.longToBytes(op.getCdd()));
					opArrList.add(op.getOpReturn().getBytes());
		
						opos.write(BytesTools.bytesMerger(opArrList));
					
				}
				opos.close();	
			} catch (IOException e) {
				e.printStackTrace();
				error=true;
			}
		}while(error==true);
	}
	
	public static ResultReadOpReFromFile readOpReturnFromFileToList(long pointer,int countWanted) throws IOException{

		File opFile = new File("opretune.byte");
		FileInputStream opis = new FileInputStream(opFile);
		ArrayList<OpReturn> opList = new ArrayList<OpReturn>();
		long pointerInFile = pointer;
		int count = 0;
		boolean fileEnd = false;
		boolean isRollback = false;
		
		while(pointerInFile < opFile.length() && count < countWanted) {
					
			OpReturn op = new OpReturn();
			
			byte[] length = new byte[4];
			int end = opis.read(length);
			if(end == -1) {
				System.out.println("OpReturn File was parsed completely.");
				fileEnd = true;
				break;
			}
			
			int opLength = BytesTools.bytesToIntBE(length);
			
			byte[] opbytes = new byte[opLength];
			opis.read(opbytes);
			
			pointerInFile += opLength;
			
			int offset=0;
			
			byte[] txidArr = Arrays.copyOfRange(opbytes, offset, offset+32);
			offset+=32;	
			op.setId(BytesTools.bytesToHexStringBE(txidArr));
			
			byte[] heiArr = Arrays.copyOfRange(opbytes, offset, offset+8);
			offset+=8;	
			op.setHeight(BytesTools.bytesToLongBE(heiArr));
			
			//If rollback record?
			//如果不是回滚记录点
			if(opLength>40) {
			
				byte[] txIndexArr = Arrays.copyOfRange(opbytes, offset, offset+4);
				offset+=4;	
				op.setTxIndex(BytesTools.bytesToIntBE(txIndexArr));
				
				byte[] signerArr = Arrays.copyOfRange(opbytes, offset, offset+34);
				offset+=34;	
				op.setSigner(new String(signerArr));
				
				byte[] recipientArr = Arrays.copyOfRange(opbytes, offset, offset+34);
				offset+=34;	
				op.setRecipient(new String(recipientArr));
				if(op.getRecipient()=="                                  ")op.setRecipient(null);
				
				byte[] cddArr = Arrays.copyOfRange(opbytes, offset, offset+8);
				offset+=8;	
				op.setCdd(BytesTools.bytesToLongBE(cddArr));
				
				byte[] opReArr = Arrays.copyOfRange(opbytes, offset, opLength);
				op.setOpReturn(new String(opReArr));
			}else {
				isRollback = true;
			}

			opList.add(op);
		
			count++;
			if(count==countWanted) {
				System.out.println(count + " opReturns had been pased.");
				break;
			}
		}

		opis.close();
		
		ResultReadOpReFromFile result = new ResultReadOpReFromFile();
		result.setOpReturnList(opList);
		result.setPointerInFile(pointerInFile);
		result.setCount(count);
		result.setFileEnd(fileEnd);
		result.setRollback(isRollback);

		return result;
	}
}
