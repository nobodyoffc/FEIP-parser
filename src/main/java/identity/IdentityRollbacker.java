package identity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import esClient.EsTools;
import start.Indices;
import tools.ParseTools;

public class IdentityRollbacker {

	public boolean rollback(ElasticsearchClient esClient, long height) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;		
	
		error = rollbackCid(esClient,height);
		
		error = rollbackRepu(esClient,height);

		return error;
	}

	private boolean rollbackCid(ElasticsearchClient esClient, long height) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedCidAndHistory(esClient,height);
		ArrayList<String> signerList = resultMap.get("signerList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(signerList==null || signerList.isEmpty())return error;
		
		System.out.println("If Rollbacking is interrupted, reparse all effected ids of index 'cid': ");
		ParseTools.gsonPrint(signerList);
		
		deleteEffectedCids(esClient, signerList);
		
		deleteRolledHists(esClient,Indices.CidHistIndex,histIdList);
		
		TimeUnit.SECONDS.sleep(3);
		
		List<IdentityHistory>reparseList = 	EsTools.getHistsForReparse(esClient,Indices.CidHistIndex,"signer",signerList, IdentityHistory.class);
;
		
		reparse(esClient,reparseList);
		
		return error;
	}

	private Map<String, ArrayList<String>> getEffectedCidAndHistory(ElasticsearchClient esClient, long height) throws Exception {
		// TODO Auto-generated method stub
		
		SearchResponse<IdentityHistory> resultSearch = esClient.search(s->s
				.index(Indices.CidHistIndex)
				.query(q->q
						.range(r->r
								.field("height")
								.gt(JsonData.of(height)))), IdentityHistory.class);
		
		Set<String> signerSet = new HashSet<String>();
		ArrayList<String> idList = new ArrayList<String>();

		for(Hit<IdentityHistory> hit: resultSearch.hits().hits()) {
			signerSet.add(hit.source().getSigner());
			idList.add(hit.id());
		}
		

		ArrayList<String> signerList = new ArrayList<String>(signerSet);
		
		Map<String,ArrayList<String>> resultMap = new HashMap<String,ArrayList<String>>();
		resultMap.put("signerList", signerList);
		resultMap.put("histIdList", idList);
		
		return resultMap;
	}

	private void deleteEffectedCids(ElasticsearchClient esClient, ArrayList<String> signerList) throws Exception {
		// TODO Auto-generated method stub
		EsTools.bulkDeleteList(esClient, Indices.CidIndex, signerList);
		
	}

	private void deleteRolledHists(ElasticsearchClient esClient, String index, ArrayList<String> histIdList) throws Exception {
		// TODO Auto-generated method stub
		EsTools.bulkDeleteList(esClient, index, histIdList);
		
	}

	private void reparse(ElasticsearchClient esClient, List<IdentityHistory> reparseList) throws ElasticsearchException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		if(reparseList==null)return;
		for(IdentityHistory cidHist: reparseList) {
			new IdentityParser().parseCidInfo(esClient,cidHist);
		}
	}

	private boolean rollbackRepu(ElasticsearchClient esClient, long height) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedCidAndRepuHistory(esClient,height);
		ArrayList<String> rateeList = resultMap.get("rateeList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(rateeList==null || rateeList.isEmpty())return error;
		
		deleteRolledHists(esClient,Indices.RepuHistIndex, histIdList);
		
		reviseCidRepuAndHot(esClient,rateeList);
		
		TimeUnit.SECONDS.sleep(3);
		
		return error;

	}

	private Map<String, ArrayList<String>> getEffectedCidAndRepuHistory(ElasticsearchClient esClient, long height) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		SearchResponse<RepuHistory> resultSearch = esClient.search(s->s
				.index(Indices.RepuHistIndex)
				.query(q->q
						.range(r->r
								.field("height")
								.gte(JsonData.of(height))
								)), RepuHistory.class);
		
		Set<String> rateeSet = new HashSet<String>();
		ArrayList<String> idList = new ArrayList<String>();

		for(Hit<RepuHistory> hit: resultSearch.hits().hits()) {
			rateeSet.add(hit.source().getRatee());
			idList.add(hit.id());
		}
		

		ArrayList<String> rateeList = new ArrayList<String>(rateeSet);
		
		Map<String,ArrayList<String>> resultMap = new HashMap<String,ArrayList<String>>();
		resultMap.put("rateeList", rateeList);
		resultMap.put("histIdList", idList);
		
		return resultMap;
	}
	public void reviseCidRepuAndHot(ElasticsearchClient esClient, ArrayList<String> rateeList) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		int i = 0;
		while(true) {
			ArrayList<String> rateeSubList = new ArrayList<String> ();
			for(int j = i; j<i+EsTools.WRITE_MAX;j++) {
				if(j>=rateeList.size())break;
				rateeSubList.add(rateeList.get(j));
			}
			Map<String,HashMap<String,Long>> reviseMapMap 
				= aggsRepuAndHot(esClient,rateeSubList);
			
			updataRepuAndHot(esClient,reviseMapMap);

			i += rateeSubList.size();
			if(i>=rateeList.size())break;
		}

	}
	private Map<String, HashMap<String, Long>> aggsRepuAndHot(ElasticsearchClient esClient,
			ArrayList<String> rateeSubList) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub

		List<FieldValue> fieldValueList = new ArrayList<FieldValue>();
		for(String ratee:rateeSubList) {
			fieldValueList.add(FieldValue.of(ratee));
		}
		
		SearchResponse<Void> response = esClient.search(s->s
				.index(Indices.RepuHistIndex)
				.size(0)
				.aggregations("rateeFilter",a->a
						.filter(f->f
								.terms(t->t
										.field("ratee")
										.terms(t1->t1.value(fieldValueList))))
						.aggregations("rateeTerm",a1->a1
								.terms(t2->t2
										.field("ratee"))
								.aggregations("repuSum",a2->a2.sum(s1->s1.field("reputation")))
								.aggregations("hotSum",a2->a2.sum(s1->s1.field("hot")))
								))
				, void.class);
		
		
		 List<StringTermsBucket> rateeBucketList = response.aggregations().get("rateeFilter").filter().aggregations().get("rateeTerm").sterms().buckets().array();

		 Map<String,HashMap<String,Long>> reviseMapMap = new  HashMap<String,HashMap<String,Long>>();
		 
		 for(StringTermsBucket bucket:rateeBucketList) {
			String ratee = bucket.key();
			HashMap<String,Long> values = new HashMap<String,Long>();
			long repuSum = 0;
			long hotSum = 0;
			repuSum = (long) bucket.aggregations().get("repuSum").sum().value();
			hotSum = (long) bucket.aggregations().get("hotSum").sum().value();
			
			values.put("reputation", repuSum);
			values.put("hot", hotSum);
			
			reviseMapMap.put(ratee, values);
		 }
		return reviseMapMap;
	}
	private void updataRepuAndHot(ElasticsearchClient esClient, Map<String, HashMap<String, Long>> reviseMapMap) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		if(reviseMapMap.isEmpty())return;
		BulkRequest.Builder br = new BulkRequest.Builder();
		
		Set<String> rateeSet = reviseMapMap.keySet();
		for(String ratee:rateeSet) {
			br.operations(o->o
					.update(u->u
							.index(Indices.CidIndex)
							.id(ratee)
							.action(a->a
									.doc(reviseMapMap.get(ratee)))));
		}
		br.timeout(t->t.time("600s"));			
		esClient.bulk(br.build());
		 
	}


}
