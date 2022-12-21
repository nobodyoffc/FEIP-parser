package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bitcoinj.core.Base58;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cid.Cid;
import cid.CidHistory;
import cid.RepuHistory;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.cat.HealthResponse;
import co.elastic.clients.elasticsearch.cat.health.HealthRecord;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import data.CidRaw;
import data.HomepageRaw;
import data.MasterRaw;
import data.NoticeFeeRaw;
import data.OpReturn;
import data.Protocol;
import data.ReputationRaw;
import esClient.EsTools;
import esClient.StartClient;
import start.Configer;
import startTest.Indices;
import startTest.ParseMark;
import tools.FchTools;
import tools.Hash;
import tools.ParseTools;

public class FileParser {
	private String path = null;
	private  String fileName = null;
	private long pointer =0;
	private int length =0;
	private long lastHeight = 0;
	private int lastIndex = 0;
	private String lastId = null;
	
	enum FEIP_NAME{
		CID,MASTER,HOMEPAGE,NOTICE_FEE,REPUTATION,SERVICE
	}
	
	private static final Logger log = LoggerFactory.getLogger(FileParser.class);
	
	//private ElasticsearchClient esClient = null;
	
	public boolean parseFile(ElasticsearchClient esClient, boolean isRollback) throws Exception {
	
		//this.esClient = esClient;
		
		if(isRollback)rollback(esClient, lastHeight);
		
		FileInputStream fis = openFile();
		
		pointer += length;

		fis.skip(pointer);
		
		System.out.println("Start parse "+fileName+ " form "+pointer);
		log.info("Start parse {} from {}",fileName,pointer);
		
		TimeUnit.SECONDS.sleep(5);
		
		boolean error = false;
		
		while(!error) {
			
			opReReadResult readOpResult = OpReFileTools.readOpReFromFile(fis);
			length = readOpResult.getLength();
			pointer += length;
			
			boolean isValid= false;
			
			ParseTools.gsonPrint(readOpResult);

			
			if(readOpResult.isFileEnd()) {
				if(pointer>251658240) {
					fileName = OpReFileTools.getNextFile(fileName);	
					while(!new File(fileName).exists()) {
						System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
						System.out.println(" Waiting 60 seconds for new file ...");	
						TimeUnit.SECONDS.sleep(60);
					}
					pointer = 0;
					fis = openFile();
					continue;
				}else {
					System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
					System.out.println(" Waiting 30 seconds for new item ...");	
					TimeUnit.SECONDS.sleep(30);
					fis.close();
					fis = openFile();
					fis.skip(pointer);
					continue;
				}
			}
			
			
			if(readOpResult.isRollback()) {
				rollback(esClient,readOpResult.getOpReturn().getHeight());
				continue;
			}

			OpReturn opre = readOpResult.getOpReturn();
			lastHeight = opre.getHeight();
			lastIndex = opre.getTxIndex();
			lastId = opre.getId();
			
			Protocol prot = parseProtocol(opre);
			if(prot==null)continue;
			if(prot.getType()==null)continue;
			if(!prot.getType().equals("FEIP"))continue;
			
			//Todo
			ParseTools.gsonPrint(opre);
			ParseTools.gsonPrint(prot);
			///
			
			FEIP_NAME feipName = checkProtName(prot);
			if(feipName == null)continue;

			switch(feipName) {
			
			case CID:
				System.out.println("Cid.");
				CidHistory cidHist = makeCid(opre,prot);	
				if(cidHist==null)break;
				isValid = parseCidInfo(esClient,cidHist);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist.getId()).document(cidHist));
				break;
			case MASTER:
				System.out.println("master.");
				CidHistory cidHist1 = makeMaster(opre,prot);
				if(cidHist1==null)break;
				isValid = parseCidInfo(esClient,cidHist1);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist1.getId()).document(cidHist1));
				break;
			case HOMEPAGE:
				System.out.println("homepage.");
				CidHistory cidHist2 = makeHomepage(opre,prot);
				if(cidHist2==null)break;
				isValid = parseCidInfo(esClient,cidHist2);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist2.getId()).document(cidHist2));
				break;
			case NOTICE_FEE:
				System.out.println("notice fee.");
				CidHistory cidHist3 = makeNoticeFee(opre,prot);
				if(cidHist3==null)break;
				isValid = parseCidInfo(esClient,cidHist3);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist3.getId()).document(cidHist3));
				break;
			case REPUTATION:
				System.out.println("reputation.");
				RepuHistory repuHist = makeReputation(opre,prot);
				if(repuHist==null)break;
				isValid = parseReputation(esClient,repuHist);
				if(isValid)esClient.index(i->i.index(Indices.RepuHistIndex).id(repuHist.getId()).document(repuHist));
				break;
			case SERVICE:
				System.out.println("Service.");

				break;
			default:
				break;
			}
			
			if(isValid)writeParseMark(esClient,readOpResult.getLength());

		}
		return error;
	}
	
	private void writeParseMark(ElasticsearchClient esClient, int length) throws IOException {
		// TODO Auto-generated method stub
		ParseMark parseMark= new ParseMark();
		
		parseMark.setFileName(fileName);
		parseMark.setPointer(pointer-length);
		parseMark.setLength(length);;
		parseMark.setLastHeight(lastHeight);
		parseMark.setLastIndex(lastIndex);
		parseMark.setLastId(lastId);
		
		esClient.index(i->i.index(Indices.ParseMark).id(parseMark.getLastId()).document(parseMark));
	}
	
	private FileInputStream openFile() throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file = new File(path,fileName);	
		return new FileInputStream(file);
	}

	private Protocol parseProtocol(OpReturn opre) {
		// TODO Auto-generated method stub
		if(opre.getOpReturn()==null)return null;
		
		String rawProt = opre.getOpReturn();
		
		if(!rawProt.contains("{")) return null;
		
		int begin = rawProt.indexOf("{");	
		String protStr = rawProt.substring(begin);
		
		Protocol prot = null;
		try {
			prot = new Gson().fromJson(protStr, Protocol.class);
		}catch(JsonSyntaxException e) {
			System.out.println("Invalid opReturn content.");
		}
		return  prot;
	}

	private FEIP_NAME checkProtName(Protocol prot) {
		// TODO Auto-generated method stub
		String sn = prot.getSn();
		if(sn.equals("3"))return FEIP_NAME.CID;
		if(sn.equals("6"))return FEIP_NAME.MASTER;
		if(sn.equals("26"))return FEIP_NAME.HOMEPAGE;
		if(sn.equals("27"))return FEIP_NAME.NOTICE_FEE;
		if(sn.equals("16"))return FEIP_NAME.REPUTATION;
		
		return null;
	}

	private boolean rollback(ElasticsearchClient esClient, long height) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		//Cid rollback
		
	
		error = rollbackCid(esClient,height);
		
		error = rollbackRepu(esClient,height);

		return error;
	}
	@Test
	public void test() throws Exception {
		
//		////////////////////
//		Configer configer = new Configer();
//		configer.setIp("192.168.31.193");
//		configer.setPort(9200);
//		StartClient sc = new StartClient();
//
//		ElasticsearchClient esClient = sc.getClientHttp(configer);
//		HealthResponse ch = esClient.cat().health();
//		List<HealthRecord> vb = ch.valueBody();
//		System.out.println("ES Client was created. The cluster is: " + vb.get(0).cluster());
//		////////////////////
//		long height1 = 1045000;
//		
//		rollbackCid(esClient,height1);
		
		System.out.println("test: FTqiqAyXHnK7uDTXzMap3acvqADK4ZGzts");
		
		if(FchTools.isValidFchAddr("FTqiqAyXHnK7uDTXzMap3acvqADK4ZGzts"))
			System.out.println("FTqiqAyXHnK7uDTXzMap3acvqADK4ZGzts is valid");
		

	}
	
	private boolean rollbackCid(ElasticsearchClient esClient, long height) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedCidAndHistory(esClient,height);
		ArrayList<String> signerList = resultMap.get("signerList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(signerList==null)return error;
		
		deleteEffectedCids(esClient, signerList);
		
		deleteRolledHists(esClient,Indices.CidHistIndex,histIdList);
		
		List<CidHistory>reparseList = getCidHistoryForReparse(esClient,signerList);
		
		reparse(esClient,reparseList);
		
		return error;
	}

	private Map<String, ArrayList<String>> getEffectedCidAndHistory(ElasticsearchClient esClient, long height) throws Exception {
		// TODO Auto-generated method stub
		
		SearchResponse<CidHistory> resultSearch = esClient.search(s->s
				.index(Indices.CidHistIndex)
				.query(q->q
						.range(r->r
								.field("height")
								.gte(JsonData.of(height)))), CidHistory.class);
		
		Set<String> signerSet = new HashSet<String>();
		ArrayList<String> idList = new ArrayList<String>();

		for(Hit<CidHistory> hit: resultSearch.hits().hits()) {
			signerSet.add(hit.source().getSigner());
			idList.add(hit.id());
		}
		

		ArrayList<String> signerList = new ArrayList<String>(signerSet);
		
		Map<String,ArrayList<String>> resultMap = new HashMap<String,ArrayList<String>>();
		resultMap.put("signerList", signerList);
		resultMap.put("histIdList", idList);
		
		//TODO
		ParseTools.gsonPrint(resultMap);
		
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

	private List<CidHistory> getCidHistoryForReparse(ElasticsearchClient esClient, List<String> signerList) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		List<FieldValue> signerValueList = new ArrayList<FieldValue>();
		for(String v :signerList) {
			signerValueList.add(FieldValue.of(v));
		}
		
		SearchResponse<CidHistory> result = esClient.search(s->s.index(Indices.CidHistIndex)
				.query(q->q.terms(t->t.field("signer").terms(t1->t1.value(signerValueList))))
				.size(EsTools.READ_MAX)
				.sort(s1->s1
						.field(f->f
								.field("height").order(SortOrder.Asc)
								.field("index").order(SortOrder.Asc))), CidHistory.class);
		
		ArrayList<CidHistory> historyList = new ArrayList<CidHistory>();
		
		for(Hit<CidHistory> hit : result.hits().hits()) {
			historyList.add(hit.source());
		}
		while(true) {
			if(result.hits().total().value()==EsTools.READ_MAX) {
				String lastHeight = String.valueOf(historyList.get(historyList.size()-1).getHeight());
				String lastIndex = String.valueOf(historyList.get(historyList.size()-1).getIndex());
				
				result = esClient.search(s->s.index(Indices.CidHistIndex)
						.query(q->q.terms(t->t.field("signer").terms(t1->t1.value(signerValueList))))
						.size(EsTools.READ_MAX)
						.sort(s1->s1
								.field(f->f
										.field("height").order(SortOrder.Asc)
										.field("index").order(SortOrder.Asc)))
						.searchAfter(lastHeight, lastIndex), CidHistory.class);
				for(Hit<CidHistory> hit : result.hits().hits()) {
					historyList.add(hit.source());
				}
			}else break;
		}	
		
		//TODO
		ParseTools.gsonPrint(historyList);
		
		return historyList;
	}

	private void reparse(ElasticsearchClient esClient, List<CidHistory> reparseList) throws ElasticsearchException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		for(CidHistory cidHist: reparseList) {
			parseCidInfo(esClient,cidHist);
		}
	}

	private boolean rollbackRepu(ElasticsearchClient esClient, long height) throws Exception {
		// TODO Auto-generated method stub
		boolean error = false;
		Map<String, ArrayList<String>> resultMap = getEffectedCidAndRepuHistory(esClient,height);
		ArrayList<String> rateeList = resultMap.get("rateeList");
		ArrayList<String> histIdList = resultMap.get("histIdList");
		
		if(rateeList==null)return error;
		
		deleteRolledHists(esClient,Indices.RepuHistIndex, histIdList);
		
		reviseCidRepuAndHot(esClient,rateeList);
		
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
		
		//TODO
		ParseTools.gsonPrint(resultMap);
		
		return resultMap;
	}
	private void reviseCidRepuAndHot(ElasticsearchClient esClient, ArrayList<String> rateeList) throws ElasticsearchException, IOException {
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

	private CidHistory makeCid(OpReturn opre, Protocol prot) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub

		Gson gson = new Gson();
		CidRaw cidRaw = gson.fromJson(gson.toJson(prot.getData()),CidRaw.class);
		if(cidRaw==null)return null;
		
		ParseTools.gsonPrint(cidRaw);
		
		if(cidRaw.getOp()==null)return null;
		
		CidHistory cidHist = new CidHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(prot.getSn());
		cidHist.setVer(prot.getVer());
		cidHist.setHeight(opre.getHeight());
		cidHist.setId(opre.getId());
		cidHist.setIndex(opre.getTxIndex());
		cidHist.setTime(opre.getTime());
		if(cidRaw.getOp().equals("register")||cidRaw.getOp().equals("unregister")) {		
			cidHist.setData_op(cidRaw.getOp());
			if(cidRaw.getOp().equals("register")) {
				if(cidRaw.getName()==null ||cidRaw.getName().equals(""))return null;
				cidHist.setData_name(cidRaw.getName());
			}
		}else return null;
		
		return cidHist; 
	}

	private CidHistory makeMaster(OpReturn opre, Protocol prot) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		MasterRaw masterRaw = gson.fromJson(gson.toJson(prot.getData()),MasterRaw.class);
		if(masterRaw==null)return null;
		if(masterRaw.getPromise()==null)return null;
		if(!masterRaw.getPromise().equals("The master owns all my rights."))return null;
		
		if(!FchTools.isValidFchAddr(masterRaw.getMaster()))return null;
		
		CidHistory cidHist = new CidHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(prot.getSn());
		cidHist.setVer(prot.getVer());
		cidHist.setHeight(opre.getHeight());
		cidHist.setId(opre.getId());
		cidHist.setIndex(opre.getTxIndex());
		cidHist.setTime(opre.getTime());
		cidHist.setData_master(masterRaw.getMaster());
	
		return cidHist; 
	}
	
	private CidHistory makeHomepage(OpReturn opre, Protocol prot) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		HomepageRaw homepageRaw = new HomepageRaw();

		try {
			homepageRaw = gson.fromJson(gson.toJson(prot.getData()),HomepageRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			HomepageRaw1 homepageRaw1 = gson.fromJson(gson.toJson(prot.getData()),HomepageRaw1.class);
			homepageRaw.setOp(homepageRaw1.op);
			String[] homepages = new String[1];
			homepages[0]=homepageRaw1.homepage;
			homepageRaw.setHomepages(homepages);	
		}
		
		if(homepageRaw ==null)return null;
		
		if(homepageRaw.getHomepages()== null || homepageRaw.getHomepages()[0] == null || homepageRaw.getHomepages()[0].isBlank())return null;
		
		if(homepageRaw.getOp()==null)return null;
		
		if(!(homepageRaw.getOp().equals("register") || homepageRaw.getOp().equals("unregister"))) return null;
		
		CidHistory cidHist = new CidHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(prot.getSn());
		cidHist.setVer(prot.getVer());
		cidHist.setHeight(opre.getHeight());
		cidHist.setId(opre.getId());
		cidHist.setIndex(opre.getTxIndex());
		cidHist.setTime(opre.getTime());
		
		cidHist.setData_op(homepageRaw.getOp());
		cidHist.setData_homepages(homepageRaw.getHomepages());
		
		return cidHist; 
	}
	private class HomepageRaw1{
		String op;
		String homepage;
	}
	private CidHistory makeNoticeFee(OpReturn opre, Protocol prot) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		NoticeFeeRaw noticeFeeRaw = gson.fromJson(gson.toJson(prot.getData()),NoticeFeeRaw.class);
		if(noticeFeeRaw ==null)return null;
		
		CidHistory cidHist = new CidHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(prot.getSn());
		cidHist.setVer(prot.getVer());
		cidHist.setHeight(opre.getHeight());
		cidHist.setId(opre.getId());
		cidHist.setIndex(opre.getTxIndex());
		cidHist.setTime(opre.getTime());
		
		cidHist.setData_noticeFee(noticeFeeRaw.getNoticeFee());
		
		return cidHist; 
	}
	private RepuHistory makeReputation(OpReturn opre, Protocol prot) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		ReputationRaw reputationRaw = gson.fromJson(gson.toJson(prot.getData()),ReputationRaw.class);
		if(reputationRaw ==null)return null;
		if(reputationRaw.getSign()==null)return null;
		
		RepuHistory repuHist = new RepuHistory();
		
		repuHist.setHeight(opre.getHeight());
		repuHist.setId(opre.getId());
		repuHist.setIndex(opre.getTxIndex());
		repuHist.setTime(opre.getTime());
		
		repuHist.setRater(opre.getSigner());
		repuHist.setRatee(opre.getRecipient());
		
		repuHist.setHot(opre.getCdd());
		
		if(reputationRaw.getSign().equals("+"))repuHist.setReputation(opre.getCdd());
		if(reputationRaw.getSign().equals("-"))repuHist.setReputation(opre.getCdd()*(-1));
		
		return repuHist; 
	}

	private boolean parseCidInfo(ElasticsearchClient esClient, CidHistory cidHist) throws ElasticsearchException, IOException, InterruptedException {
		
		if(cidHist.getSn().equals("3"))return parseCid(esClient, cidHist);
		if(cidHist.getSn().equals("6"))return parseMaster(esClient, cidHist);
		if(cidHist.getSn().equals("26"))return parseHomepage(esClient, cidHist);
		if(cidHist.getSn().equals("27"))return parseNoticeFee(esClient, cidHist);
		return false;
	}

	private boolean parseCid(ElasticsearchClient esClient, CidHistory cidHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		
		if(cidHist.getData_op().equals("register")) {
			
			//Rule 1
			int suffixLength = 4;
			String cidStr = cidHist.getData_name()+"_"+cidHist.getSigner().substring(34-suffixLength);
			
			while(true) {
				String cidStr1 = cidStr;
				SearchResponse<Cid> resultCidSearch = esClient.search(s->s
						.query(q->q.term(t->t.field("usedCids").value(cidStr1)))
						.index(Indices.CidIndex)
						, Cid.class);
				
				
				if(resultCidSearch.hits().total().value()==0) {
					Cid cid = new Cid();
					GetResponse<Cid> resultGetCid = esClient.get(g->g.index(Indices.CidIndex).id(cidHist.getSigner()), Cid.class);
					
					if(resultGetCid.found()) cid = resultGetCid.source();	
					
					//rule 7

					cid.setCid(cidStr1);
					cid.setId(cidHist.getSigner());
					
					int nameCount = 0;
					if(cid.getUsedCids()!=null) {
						nameCount = cid.getUsedCids().length;
					}
					if(nameCount>=4)return isValid;
					
					Set<String> usedCidSet = new HashSet<String>();
					
					if(cid.getUsedCids()==null || cid.getUsedCids().length==0)
						cid.setNameTime(cidHist.getTime());
					
					for(int i=0; i<nameCount;i++) {
						usedCidSet.add(cid.getUsedCids()[i]);
					}
					usedCidSet.add(cidStr1);
					
					String [] usedCids = new String[usedCidSet.size()];
					usedCidSet.toArray(usedCids);
					
					if(usedCids.length>4)return isValid;
					cid.setUsedCids(usedCids);
					
					cid.setLastHeight(cidHist.getHeight());

					
					Cid cid1 = cid;
					//rule 3
					esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid1));
					
					isValid = true;
					
					break;
				}else if(resultCidSearch.hits().total().value()==1 && 
						resultCidSearch.hits().hits().get(0).source().getId().equals(cidHist.getSigner())) {
					
					//rule 4,5	
					Cid cid = resultCidSearch.hits().hits().get(0).source();
					cid.setCid(cidStr1);
					cid.setLastHeight(cidHist.getHeight());
					
					//rule 3
					esClient.index(i->i.index(Indices.CidIndex).id(cid.getId()).document(cid));
					isValid = true;
					
					break;
				}
				
				//rule 2
				suffixLength ++;
				cidStr = cidHist.getData_name()+"_"+cidHist.getSigner().substring(34-suffixLength);
				//esClient.get(g->g.index(Indices.CidIndex).id(opre.getSigner()), Cid.class);
			}
			
		}else if(cidHist.getData_op().equals("unregister")) {
			Map<String,Object> updata = new HashMap<String,Object>();
			updata.put("cid", "");
			updata.put("lastHeight",cidHist.getHeight());
			
			//rule 6
			esClient.update(u->u.index(Indices.CidIndex).id(cidHist.getSigner()).doc(updata), Cid.class);
			isValid = true;
		}
		
		System.out.println("parsed Cid.");
		
		return isValid;
	}

	private boolean parseMaster(ElasticsearchClient esClient, CidHistory cidHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		GetResponse<Cid> resultGetCid = esClient.get(g->g.index(Indices.CidIndex).id(cidHist.getSigner()), Cid.class);
		
		if(resultGetCid.found()) {
			Cid cid  = resultGetCid.source();	
			if(cid.getMaster()==null || cid.getMaster().isBlank()) {
				cid.setMaster(cidHist.getData_master());
				cid.setLastHeight(cidHist.getHeight());
				esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
				isValid = true;
			}
		}else {
			Cid cid = new Cid();
			cid.setId(cidHist.getSigner());
			cid.setMaster(cidHist.getData_master());
			cid.setLastHeight(cidHist.getHeight());
			esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
			isValid = true;
		}
		return isValid;
	}
	
	private boolean parseHomepage(ElasticsearchClient esClient, CidHistory cidHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		GetResponse<Cid> resultGetCid = esClient.get(g->g.index(Indices.CidIndex).id(cidHist.getSigner()), Cid.class);
		
		if(cidHist.getData_op().equals("register")) {
			if(resultGetCid.found()) {
				Cid cid  = resultGetCid.source();	
				
				cid.setHomepage(cidHist.getData_homepages());
				cid.setLastHeight(cidHist.getHeight());
				esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
				isValid = true;
				
			}else {
				Cid cid = new Cid();
				cid.setId(cidHist.getSigner());
				cid.setHomepage(cidHist.getData_homepages());
				cid.setLastHeight(cidHist.getHeight());
				esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
				isValid = true;
			}
		}else if(cidHist.getData_op().equals("unregister")) {
			if(resultGetCid.found()) {
				Cid cid  = resultGetCid.source();	
				if(cid.getHomepage() ==null || cid.getHomepage()[0].isBlank()) {
					isValid = false;
				}else {
					cid.setHomepage(null);
					cid.setLastHeight(cidHist.getHeight());
					esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
					isValid = true;
				}
			}
		}
		

		return isValid;
	}

	private boolean parseNoticeFee(ElasticsearchClient esClient, CidHistory cidHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		GetResponse<Cid> resultGetCid = esClient.get(g->g.index(Indices.CidIndex).id(cidHist.getSigner()), Cid.class);
		
		if(resultGetCid.found()) {
			Cid cid  = resultGetCid.source();	
			
			cid.setNoticFee(cidHist.getData_noticeFee());
			cid.setLastHeight(cidHist.getHeight());
			esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
			isValid = true;

		}else {
			Cid cid = new Cid();
			cid.setId(cidHist.getSigner());
			cid.setNoticFee(cidHist.getData_noticeFee());
			cid.setLastHeight(cidHist.getHeight());
			esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
			isValid = true;
		}
		return isValid;
	}


	private boolean parseReputation(ElasticsearchClient esClient, RepuHistory repuHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		GetResponse<Cid> resultGetCid = esClient.get(g->g.index(Indices.CidIndex).id(repuHist.getRatee()), Cid.class);
		
		if(resultGetCid.found()) {
			Cid cid  = resultGetCid.source();	
			
			cid.setReputation(cid.getReputation()+repuHist.getReputation());
			cid.setHot(cid.getHot()+ repuHist.getHot());
			cid.setLastHeight(repuHist.getHeight());
			
			esClient.index(i->i.index(Indices.CidIndex).id(repuHist.getRatee()).document(cid));
			isValid = true;
		}else{
			Cid cid = new Cid();
			cid.setId(repuHist.getRatee());
			cid.setReputation(repuHist.getReputation());
			cid.setHot(repuHist.getHot());
			cid.setLastHeight(repuHist.getHeight());
			esClient.index(i->i.index(Indices.CidIndex).id(repuHist.getRatee()).document(cid));
			isValid = true;
		}
		System.out.println("parseReputation.");
		return isValid;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getPointer() {
		return pointer;
	}

	public void setPointer(long pointer) {
		this.pointer = pointer;
	}

	public long getLastHeight() {
		return lastHeight;
	}

	public void setLastHeight(long lastHeight) {
		this.lastHeight = lastHeight;
	}

	public long getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		this.lastId = lastId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}


}
