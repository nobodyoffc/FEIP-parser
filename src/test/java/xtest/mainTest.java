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
		
//		String script = "5221030be1d7e633feb2338a74a860e76d893bac525f35a5813cb7b21e27ba1bc8312a2102536e4f3a6871831fa91089a5d5a950b96a31c861956f01459c0cd4f4374b2f672103f0145ddf5debc7169952b17b5c6a8a566b38742b6aa7b33b667c0a7fa73762e253ae";
//		
//		String addr = FchTools.scriptToMultiAddr(script);
//		
//		System.out.println("addr:" + addr);
		
		String prikey = "KyCzHvuyLRwqUcBHfSAhw8iu1eVoYb57enbL4uyh7WmC9hC5RNSc";
		
		
		System.out.println(tools.FchTools.getPriKey32(prikey));
		

		//esClient.shutdown();

	}
	
}
