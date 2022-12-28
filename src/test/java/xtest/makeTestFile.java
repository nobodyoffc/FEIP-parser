package xtest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.cat.HealthResponse;
import co.elastic.clients.elasticsearch.cat.health.HealthRecord;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import construct.Service;
import esClient.StartClient;
import identity.Cid;
import identity.IdentityHistory;
import start.Configer;
import start.Indices;
import tools.ParseTools;

public class makeTestFile {

	public static void main(String[] args) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub

		////////////////////
		Configer configer = new Configer();
		configer.setIp("192.168.10.7");
		configer.setPort(9200);
		StartClient sc = new StartClient();

		ElasticsearchClient esClient = sc.getClientHttp(configer);
		HealthResponse ch = esClient.cat().health();
		List<HealthRecord> vb = ch.valueBody();
		System.out.println("ES Client was created. The cluster is: " + vb.get(0).cluster());
		////////////////////
		
		
		String serviceHist="FLyeRbrqjkCgxLVjBP819ArPMujwTspDga";
		GetResponse<Cid> result = esClient.get(g->g.index(Indices.CidIndex).id(serviceHist),Cid.class);
		
		ParseTools.gsonPrint(result);
		
//		
//		SearchResponse<IdentityHistory> result1 = esClient.search(s->s.index(Indices.CidHistIndex)
//				.size(30)
//				.sort(s1->s1
//						.field(f->f
//								.field("index").order(SortOrder.Desc)
//								.field("height").order(SortOrder.Asc)
//								)), IdentityHistory.class);
//		
//		IdentityHistory cidHist = result1.hits().hits().get(29).source();
//		
//		List<String> lastSort = result1.hits().hits().get(result1.hits().hits().size()-1).sort();
//		
//		ParseTools.gsonPrint(cidHist);
//		List<String> strList = new ArrayList<String>();
//		
//		strList.add(String.valueOf(cidHist.getIndex()));
//		strList.add(String.valueOf(cidHist.getHeight()));
//		
//		SearchResponse<IdentityHistory> result2 = esClient.search(s->s.index(Indices.CidHistIndex)
//				.size(20)
//				.sort(s1->s1
//						.field(f->f
//								.field("index").order(SortOrder.Desc)
//								.field("height").order(SortOrder.Asc)
//								))
//				.searchAfter(lastSort)
//				, IdentityHistory.class);
//		
//		IdentityHistory cidHist2 = result2.hits().hits().get(0).source();
//		
//		ParseTools.gsonPrint(cidHist2);
//		
		sc.shutdownClient();
	}

}
