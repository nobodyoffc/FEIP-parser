package xtest;

import java.security.SignatureException;
import java.util.List;
import java.util.Map;

import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.json.JsonData;
import esClient.StartClient;
import tools.BytesTools;
import tools.FchTools;
import tools.Hash;
import tools.ParseTools;


public class mainTest {

	static final Logger log = LoggerFactory.getLogger(mainTest.class);

	public static void main(String[] args) throws Exception {

//		////////////////////
//
//		StartClient sc = new StartClient();
//		sc.setParams("192.168.101.12", 9200);
//
//		ElasticsearchClient esClient = sc.getClientHttp(null);
//		// createIndex(client,"test");
//		HealthResponse ch = esClient.cat().health();
//		List<HealthRecord> vb = ch.valueBody();
//		System.out.println("ES Client was created. The cluster is: " + vb.get(0).cluster());
//		////////////////////
//		
//		String script = "5221030be1d7e633feb2338a74a860e76d893bac525f35a5813cb7b21e27ba1bc8312a2102536e4f3a6871831fa91089a5d5a950b96a31c861956f01459c0cd4f4374b2f672103f0145ddf5debc7169952b17b5c6a8a566b38742b6aa7b33b667c0a7fa73762e253ae";
//		
//		String addr = FchTools.scriptToMultiAddr(script);
//		
//		System.out.println("addr:" + addr);
		
//		String prikey = "KyCzHvuyLRwqUcBHfSAhw8iu1eVoYb57enbL4uyh7WmC9hC5RNSc";
//
//
//		System.out.println(tools.FchTools.getPriKey32(prikey));
//
//        SearchResponse<Service> result = esClient.search(s -> s.index(TxHasIndex).query(q -> q.term(t -> t.field("owner").value(str))), Service.class);
//
//		Object height;
//		esClient.search(s->s.index("tx_has").query(q->q.range(r->r.field("height").gt(JsonData.of(height)))), Order.class);

		//esClient.shutdown();
//
//        Class clazz;
//		List<String> index;
//		String sortField;
//		SortOrder order;
//		String queryField;
//		Object value;
//		String filterField;
//		FieldValue filterValue;
//		@SuppressWarnings("unchecked")
//		Object result = esClient.search(s->s
//                .index(index)
//                .sort(so->so.field(f->f
//                        .field(sortField)
//                        .order(order)))
//                .size(1000)
//                .query(q->q
//                		.bool(b->b
//                				.filter(f->f
//                						.term(t->t
//                								.field(filterField).value(filterValue)))
//                				.must(m->m
//                						.range(r->r
//                								.field(queryField).gt(JsonData.of(value))))))
//                , clazz);

	}
	@Test
	public void sign() throws SignatureException {
		String addr = "FEk41Kqjar45fLDriztUDTUkdki7mmcjWK";
		String pubKey = "030be1d7e633feb2338a74a860e76d893bac525f35a5813cb7b21e27ba1bc8312a";
		String message = "test";
		String signature = "HzNqRToLHsY6TrhVB+RjWuk7PqeJhUESINxcL0521mWqU3+rw+NeIGNnOV06ngCLwHD69jCfqHcXWCXuCnIkCGo\u003d";
		byte[] pubKeyBytes = BytesTools.hexToByteArray (pubKey);
		ECKey ecKey = ECKey.fromPublicOnly(pubKeyBytes);

		String signPubKey = ECKey.signedMessageToKey(message, signature).getPublicKeyAsHex();
		System.out.println("sign public key: "+ signPubKey);

		// ecKey.verify();
	}
	
}
