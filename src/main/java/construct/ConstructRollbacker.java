package construct;

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
import tools.ParseTools;

public class ConstructRollbacker {

	public boolean rollback(ElasticsearchClient esClient, long lastHeight) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		error = rollbackFreeProtocol(esClient,lastHeight);
		error = rollbackService(esClient,lastHeight);
		error = rollbackApp(esClient,lastHeight);
		error = rollbackCode(esClient,lastHeight);
		
		return error;
	}

	private boolean rollbackFreeProtocol(ElasticsearchClient esClient, long lastHeight) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedFreeProtocols(esClient,lastHeight);
		ArrayList<String> itemIdList = resultMap.get("itemIdList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(itemIdList==null||itemIdList.isEmpty())return error;
		System.out.println("If Rollbacking is interrupted, reparse all effected ids of index 'protocol': ");
		ParseTools.gsonPrint(itemIdList);
		deleteEffectedItems(esClient,Indices.FreeProtocolIndex, itemIdList);
		if(histIdList==null||histIdList.isEmpty())return error;
		deleteRolledHists(esClient,Indices.FreeProtocolHistIndex,histIdList);
		
		TimeUnit.SECONDS.sleep(2);
		
		List<FreeProtocolHistory>reparseHistList = EsTools.getHistsForReparse(esClient,Indices.FreeProtocolHistIndex,"pid",itemIdList, FreeProtocolHistory.class);

		reparseFreeProtocol(esClient,reparseHistList);
		
		return error;
	}

	private Map<String, ArrayList<String>> getEffectedFreeProtocols(ElasticsearchClient esClient,long height) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		SearchResponse<FreeProtocolHistory> resultSearch = esClient.search(s->s
				.index(Indices.FreeProtocolHistIndex)
				.query(q->q
						.range(r->r
								.field("height")
								.gt(JsonData.of(height)))),FreeProtocolHistory.class);
		
		Set<String> itemSet = new HashSet<String>();
		ArrayList<String> histList = new ArrayList<String>();

		for(Hit<FreeProtocolHistory> hit: resultSearch.hits().hits()) {
			
			FreeProtocolHistory item = hit.source();
			if(item.getOp().equals("publish")) {
				itemSet.add(item.getId());
			}else {
				itemSet.add(item.getPid());
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
	
	private void reparseFreeProtocol(ElasticsearchClient esClient, List<FreeProtocolHistory> reparseHistList) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		if(reparseHistList==null)return;
		for(FreeProtocolHistory freeProtocolHist: reparseHistList) {
			new ConstructParser().parseFreeProtocol(esClient, freeProtocolHist);
		}
	}

	private boolean rollbackService(ElasticsearchClient esClient, long lastHeight) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedServices(esClient,lastHeight);
		ArrayList<String> itemIdList = resultMap.get("itemIdList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(itemIdList==null||itemIdList.isEmpty())return error;
		System.out.println("If Rollbacking is interrupted, reparse all effected ids of index 'service': ");
		ParseTools.gsonPrint(itemIdList);
		deleteEffectedItems(esClient,Indices.ServiceIndex,itemIdList);
		if(histIdList==null||histIdList.isEmpty())return error;
		deleteRolledHists(esClient,Indices.ServiceHistIndex,histIdList);
		TimeUnit.SECONDS.sleep(2);
		
		List<ServiceHistory>reparseHistList = EsTools.getHistsForReparse(esClient,Indices.ServiceHistIndex,"sid",itemIdList,ServiceHistory.class);

		reparseService(esClient,reparseHistList);
		
		return error;
	}

	private Map<String, ArrayList<String>> getEffectedServices(ElasticsearchClient esClient, long lastHeight) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		SearchResponse<ServiceHistory> resultSearch = esClient.search(s->s
				.index(Indices.ServiceHistIndex)
				.query(q->q
						.range(r->r
								.field("height")
								.gt(JsonData.of(lastHeight)))),ServiceHistory.class);
		
		Set<String> itemSet = new HashSet<String>();
		ArrayList<String> histList = new ArrayList<String>();

		for(Hit<ServiceHistory> hit: resultSearch.hits().hits()) {
			
			ServiceHistory item = hit.source();
			if(item.getOp().equals("publish")) {
				itemSet.add(item.getId());
			}else {
				itemSet.add(item.getSid());
			}
			histList.add(hit.id());
		}
		

		ArrayList<String> itemList = new ArrayList<String>(itemSet);
		
		Map<String,ArrayList<String>> resultMap = new HashMap<String,ArrayList<String>>();
		resultMap.put("itemIdList", itemList);
		resultMap.put("histIdList", histList);
		
		return resultMap;
	}

	private void reparseService(ElasticsearchClient esClient, List<ServiceHistory> reparseHistList) throws ElasticsearchException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		if(reparseHistList==null)return;
		for(ServiceHistory serviceHist: reparseHistList) {
			new ConstructParser().parseService(esClient, serviceHist);
		}
	}

	private boolean rollbackApp(ElasticsearchClient esClient, long lastHeight) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedApps(esClient,lastHeight);
		ArrayList<String> itemIdList = resultMap.get("itemIdList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(itemIdList==null||itemIdList.isEmpty())return error;
		System.out.println("If Rollbacking is interrupted, reparse all effected ids of index 'app': ");
		ParseTools.gsonPrint(itemIdList);
		deleteEffectedItems(esClient,Indices.AppIndex,itemIdList);
		if(histIdList==null||histIdList.isEmpty())return error;
		deleteRolledHists(esClient,Indices.AppHistIndex,histIdList);
		TimeUnit.SECONDS.sleep(2);
		
		List<AppHistory>reparseHistList = EsTools.getHistsForReparse(esClient,Indices.AppHistIndex,"aid",itemIdList,AppHistory.class);

		reparseApp(esClient,reparseHistList);
		
		return error;
	}

	private Map<String, ArrayList<String>> getEffectedApps(ElasticsearchClient esClient, long lastHeight) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		SearchResponse<AppHistory> resultSearch = esClient.search(s->s
				.index(Indices.AppHistIndex)
				.query(q->q
						.range(r->r
								.field("height")
								.gt(JsonData.of(lastHeight)))),AppHistory.class);
		
		Set<String> itemSet = new HashSet<String>();
		ArrayList<String> histList = new ArrayList<String>();

		for(Hit<AppHistory> hit: resultSearch.hits().hits()) {
			
			AppHistory item = hit.source();
			if(item.getOp().equals("publish")) {
				itemSet.add(item.getId());
			}else {
				itemSet.add(item.getAid());
			}
			histList.add(hit.id());
		}
		

		ArrayList<String> itemList = new ArrayList<String>(itemSet);
		
		Map<String,ArrayList<String>> resultMap = new HashMap<String,ArrayList<String>>();
		resultMap.put("itemIdList", itemList);
		resultMap.put("histIdList", histList);
		
		return resultMap;
	}

	private void reparseApp(ElasticsearchClient esClient, List<AppHistory> reparseHistList) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		if(reparseHistList==null)return;
		for(AppHistory appHist: reparseHistList) {
			new ConstructParser().parseApp(esClient, appHist);
		}
	}

	private boolean rollbackCode(ElasticsearchClient esClient, long lastHeight) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedCodes(esClient,lastHeight);
		ArrayList<String> itemIdList = resultMap.get("itemIdList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(itemIdList==null||itemIdList.isEmpty())return error;
		System.out.println("If Rollbacking is interrupted, reparse all effected ids of index 'code': ");
		ParseTools.gsonPrint(itemIdList);
		deleteEffectedItems(esClient,Indices.CodeIndex,itemIdList);
		if(histIdList==null||histIdList.isEmpty())return error;
		deleteRolledHists(esClient,Indices.CodeHistIndex,histIdList);
		TimeUnit.SECONDS.sleep(2);
		
		List<CodeHistory>reparseHistList = EsTools.getHistsForReparse(esClient,Indices.CodeHistIndex,"codeId",itemIdList,CodeHistory.class);

		reparseCode(esClient,reparseHistList);
		
		return error;
	}

	private Map<String, ArrayList<String>> getEffectedCodes(ElasticsearchClient esClient, long lastHeight) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		SearchResponse<CodeHistory> resultSearch = esClient.search(s->s
				.index(Indices.CodeHistIndex)
				.query(q->q
						.range(r->r
								.field("height")
								.gt(JsonData.of(lastHeight)))),CodeHistory.class);
		
		Set<String> itemSet = new HashSet<String>();
		ArrayList<String> histList = new ArrayList<String>();

		for(Hit<CodeHistory> hit: resultSearch.hits().hits()) {
			
			CodeHistory item = hit.source();
			if(item.getOp().equals("publish")) {
				itemSet.add(item.getId());
			}else {
				itemSet.add(item.getCoid());
			}
			histList.add(hit.id());
		}
		

		ArrayList<String> itemList = new ArrayList<String>(itemSet);
		
		Map<String,ArrayList<String>> resultMap = new HashMap<String,ArrayList<String>>();
		resultMap.put("itemIdList", itemList);
		resultMap.put("histIdList", histList);
		
		return resultMap;
	}

	private void reparseCode(ElasticsearchClient esClient, List<CodeHistory> reparseHistList) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		if(reparseHistList==null)return;
		for(CodeHistory codeHist: reparseHistList) {
			new ConstructParser().parseCode(esClient, codeHist);
		}
	}

}
