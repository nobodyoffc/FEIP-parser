package organization;

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
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;

import esClient.EsTools;
import start.Indices;

public class OrganizationRollbacker {

	public boolean rollback(ElasticsearchClient esClient, long lastHeight) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		error = rollbackGroup(esClient,lastHeight);
		//error = rollbackTeam(esClient,lastHeight);
		
		return error;
		
	}
	
	private boolean rollbackGroup(ElasticsearchClient esClient, long lastHeight) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedGroups(esClient,lastHeight);
		ArrayList<String> itemIdList = resultMap.get("itemIdList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(itemIdList==null||itemIdList.isEmpty())return error;
		deleteEffectedItems(esClient,Indices.GroupIndex, itemIdList);
		if(histIdList==null||histIdList.isEmpty())return error;
		deleteRolledHists(esClient,Indices.GroupHistIndex,histIdList);
		
		List<GroupHistory>reparseHistList = EsTools.getHistsForReparse(esClient,Indices.GroupHistIndex,"gid",itemIdList, GroupHistory.class);

		TimeUnit.SECONDS.sleep(20);
		
		reparseGroup(esClient,reparseHistList);
		
		return error;
	}

	private Map<String, ArrayList<String>> getEffectedGroups(ElasticsearchClient esClient,long height) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		SearchResponse<GroupHistory> resultSearch = esClient.search(s->s
				.index(Indices.GroupHistIndex)
				.query(q->q
						.range(r->r
								.field("height")
								.gt(JsonData.of(height)))),GroupHistory.class);
		
		Set<String> itemSet = new HashSet<String>();
		ArrayList<String> histList = new ArrayList<String>();

		for(Hit<GroupHistory> hit: resultSearch.hits().hits()) {
			
			GroupHistory item = hit.source();
			if(item.getOp().equals("create")) {
				itemSet.add(item.getId());
			}else {
				itemSet.add(item.getGid());
			}
			histList.add(hit.id());
		}
		

		ArrayList<String> itemList = new ArrayList<String>(itemSet);
		
		Map<String,ArrayList<String>> resultMap = new HashMap<String,ArrayList<String>>();
		resultMap.put("itemIdList", itemList);
		resultMap.put("histIdList", histList);
		
		return resultMap;
	}

	private void deleteEffectedItems(ElasticsearchClient esClient,String index, ArrayList<String> itemIdList) throws Exception {
		// TODO Auto-generated method stub
		EsTools.bulkDeleteList(esClient, index, itemIdList);
	}

	private void deleteRolledHists(ElasticsearchClient esClient, String index, ArrayList<String> histIdList) throws Exception {
		// TODO Auto-generated method stub
		EsTools.bulkDeleteList(esClient, index, histIdList);
	}
	
	private void reparseGroup(ElasticsearchClient esClient, List<GroupHistory> reparseHistList) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		if(reparseHistList==null)return;
		for(GroupHistory groupHist: reparseHistList) {
			new OrganizationParser().parseGroup(esClient, groupHist);
		}
	}
}
