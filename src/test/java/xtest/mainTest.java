package xtest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tools.BytesTools;
import tools.FchTools;
import tools.Hash;
import tools.ParseTools;


public class mainTest {

	static final Logger log = LoggerFactory.getLogger(mainTest.class);

	public static void main(String[] args) throws Exception {

//		////////////////////
//		StartClient sc = new StartClient();
//		sc.setParams("192.168.10.88", 9200);
//
//		ElasticsearchClient esClient = sc.getClientHttp();
//		// createIndex(client,"test");
//		HealthResponse ch = esClient.cat().health();
//		List<HealthRecord> vb = ch.valueBody();
//		System.out.println("ES Client was created. The cluster is: " + vb.get(0).cluster());
//		////////////////////
		
		String hash = Hash.Sha256x2("test update.");
		System.out.println(hash);

		
		String priKey = "18e14a7b6a307f426a94f8114701e7c8e774e7f9a47e2c2035db29a206321725";//"L2bHRej6Fxxipvb4TiR5bu1rkT3tRp8yWEsUy4R1Zb8VMm2x7sd8";
		
		String pubKey = FchTools.priKeyToPubKey(priKey);
		
		String addr = FchTools.pubKeyToFchAddr(pubKey);
		
		String priKey32 = FchTools.getPriKey32(priKey);
		
		Map<String, String> addrs = FchTools.pkToAddresses(pubKey);
		
		
		String pubKey65 = FchTools.recoverPK33ToPK65(pubKey);
	
		System.out.println("pubKey65: "+pubKey65);
		
		String pukHa3 = Hash.sha3(pubKey65);
		
		System.out.println(" Keccak-256 1: "+pukHa3);

		
		String ethAddr = FchTools.pubKeyToEthAddr(pubKey65);
		
		
		ParseTools.gsonPrint(addrs);
		
		System.out.println("prikey32: "+priKey32);
		
		System.out.println("pubKey: "+pubKey);
		
		System.out.println("FCH addr: "+addr);
		
		System.out.println("ETH addr: "+ethAddr);
		
		//FCH addr: FCyZBfPA7wiEUvGZHs71sNYGAdwyFkLgE9
		
		
		//	System.out.println("result:" + gson.toJson(result));

		//esClient.shutdown();

	}
	
}
