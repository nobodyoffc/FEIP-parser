package xtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.cat.HealthResponse;
import co.elastic.clients.elasticsearch.cat.health.HealthRecord;
import data.OpReturn;
import esClient.StartClient;
import parser.OpReFileTools;
import parser.opReReadResult;
import start.Configer;
import tools.ParseTools;

public class makeTestFile {

	public static void main(String[] args) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub

		////////////////////
		Configer configer = new Configer();
		configer.setIp("192.168.31.193");
		configer.setPort(9200);
		StartClient sc = new StartClient();

		ElasticsearchClient esClient = sc.getClientHttp(configer);
		HealthResponse ch = esClient.cat().health();
		List<HealthRecord> vb = ch.valueBody();
		System.out.println("ES Client was created. The cluster is: " + vb.get(0).cluster());
		////////////////////
		ArrayList<OpReturn> opList = new ArrayList<OpReturn> ();
		
		//cid register
		OpReturn opre = new OpReturn();		
		opre.setCdd(100);
		opre.setHeight(1);
		opre.setId("1");
		opre.setOpReturn("{\"type\":\"FEIP\",\"sn\":\"3\",\"ver\":\"4\",\"name\":\"CID\",\"data\":{\"name\":\"test0\",\"op\":\"register\"}}");
		opre.setSigner("FEk41Kqjar45fLDriztUDTUkdki7mmcjWK");
		opre.setTxIndex(0);
		//
		opList.add(opre);
		
		OpReturn opre1 = new OpReturn();		
		//cid unregister
		opre1.setCdd(100);
		opre1.setHeight(2);
		opre1.setId("2");
		opre1.setOpReturn("{\"type\":\"FEIP\",\"sn\":\"3\",\"ver\":\"4\",\"name\":\"CID\",\"data\":{\"op\":\"unregister\"}}");
		opre1.setSigner("FEk41Kqjar45fLDriztUDTUkdki7mmcjWK");
		opre1.setTxIndex(0);
		//
		opList.add(opre1);
		
		//master register
		OpReturn opre2 = new OpReturn();	
		opre2.setCdd(100);
		opre2.setHeight(3);
		opre2.setId("3");
		opre2.setOpReturn("{\"type\":\"FEIP\",\"sn\":\"6\",\"ver\":\"4\",\"name\":\"Master\",\"data\":{\"master\":\"FMZsWGT5hEUqhnZhLhXrxNXXG6uDHcarmX\",\"promise\":\"The master owns all my rights.\"}}");
		opre2.setSigner("FEk41Kqjar45fLDriztUDTUkdki7mmcjWK");
		opre2.setTxIndex(0);
		//
		opList.add(opre2);
		
		//master register
		OpReturn opre3 = new OpReturn();	
		opre3.setCdd(100);
		opre3.setHeight(4);
		opre3.setId("4");
		opre3.setOpReturn("{\"type\":\"FEIP\",\"sn\":\"6\",\"ver\":\"4\",\"name\":\"Master\",\"data\":{\"master\":\"FPL44YJRwPdd2ipziFvqq6y2tw4VnVvpAv\",\"promise\":\"The master owns all my rights.\"}}");
		opre3.setSigner("FEk41Kqjar45fLDriztUDTUkdki7mmcjWK");
		opre3.setTxIndex(0);
		//
		opList.add(opre3);
		
		
		//cid register
		OpReturn opre4 = new OpReturn();		
		opre4.setCdd(100);
		opre4.setHeight(5);
		opre4.setId("5");
		opre4.setOpReturn("{\"type\":\"FEIP\",\"sn\":\"26\",\"ver\":\"4\",\"name\":\"Homepage\",\"data\":{\"homepage\":[\"http://192.163.121.31\",\"https://twitter.com/first\"],\"op \":\"register \"}}");
		opre4.setSigner("FEk41Kqjar45fLDriztUDTUkdki7mmcjWK");
		opre4.setTxIndex(0);
		//
		opList.add(opre4);
		
		//noticeFee register
		OpReturn opre5 = new OpReturn();		
		opre5.setCdd(100);
		opre5.setHeight(6);
		opre5.setId("6");
		opre5.setOpReturn("{\"data\":{\"noticeFee\":0.0001},\"name\":\"NoticeFee\",\"pid\":\"\",\"sn\":27,\"type\":\"FEIP\",\"ver\":1}");
		opre5.setSigner("FEk41Kqjar45fLDriztUDTUkdki7mmcjWK");
		opre5.setTxIndex(0);
		//
		opList.add(opre5);
		
		//reputation register
		OpReturn opre6 = new OpReturn();		
		opre6.setCdd(100);
		opre6.setHeight(6);
		opre6.setId("6");
		opre6.setOpReturn("{\"type\":\"FEIP\",\"sn\":16,\"ver\":1,\"name\":\"Reputation\",\"pid\":\"\",\"data\":{\"sign\":\"-\"}}");
		opre6.setSigner("FEk41Kqjar45fLDriztUDTUkdki7mmcjWK");
		opre6.setTxIndex(0);
		//
		opList.add(opre);
		
		OpReFileTools.writeOpReturnListIntoFile("opreturn0.byte",opList);
		
		for(;;) {
			File file = new File("opreturn0.byte");
			FileInputStream fis = new FileInputStream(file);
			
			opReReadResult result = OpReFileTools.readOpReFromFile(fis);
			ParseTools.gsonPrint(result.getOpReturn());
			if(result.isFileEnd())break;
		}
		
		
		sc.shutdownClient();
	}

}
