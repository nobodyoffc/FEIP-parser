package identity;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import parser.OpReturn;
import parser.Protocol;
import start.Indices;
import tools.FchTools;
import tools.ParseTools;

public class CidParser {
	
	public IdentityHistory makeCid(OpReturn opre, Protocol prot) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub

		Gson gson = new Gson();
		CidRaw cidRaw = gson.fromJson(gson.toJson(prot.getData()),CidRaw.class);
		if(cidRaw==null)return null;
		
		ParseTools.gsonPrint(cidRaw);
		
		if(cidRaw.getOp()==null)return null;
		
		IdentityHistory cidHist = new IdentityHistory();
		
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

	public IdentityHistory makeMaster(OpReturn opre, Protocol prot) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		MasterRaw masterRaw = gson.fromJson(gson.toJson(prot.getData()),MasterRaw.class);
		if(masterRaw==null)return null;
		if(masterRaw.getPromise()==null)return null;
		if(!masterRaw.getPromise().equals("The master owns all my rights."))return null;
		
		if(!FchTools.isValidFchAddr(masterRaw.getMaster()))return null;
		
		IdentityHistory cidHist = new IdentityHistory();
		
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
	
	public IdentityHistory makeHomepage(OpReturn opre, Protocol prot) {
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
		
		IdentityHistory cidHist = new IdentityHistory();
		
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
	public IdentityHistory makeNoticeFee(OpReturn opre, Protocol prot) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		NoticeFeeRaw noticeFeeRaw = gson.fromJson(gson.toJson(prot.getData()),NoticeFeeRaw.class);
		if(noticeFeeRaw ==null)return null;
		
		IdentityHistory cidHist = new IdentityHistory();
		
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
	public RepuHistory makeReputation(OpReturn opre, Protocol prot) {
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

	public boolean parseCidInfo(ElasticsearchClient esClient, IdentityHistory cidHist) throws ElasticsearchException, IOException, InterruptedException {
		
		if(cidHist.getSn().equals("3"))return parseCid(esClient, cidHist);
		if(cidHist.getSn().equals("6"))return parseMaster(esClient, cidHist);
		if(cidHist.getSn().equals("26"))return parseHomepage(esClient, cidHist);
		if(cidHist.getSn().equals("27"))return parseNoticeFee(esClient, cidHist);
		return false;
	}

	private boolean parseCid(ElasticsearchClient esClient, IdentityHistory cidHist) throws ElasticsearchException, IOException {
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

	private boolean parseMaster(ElasticsearchClient esClient, IdentityHistory cidHist) throws ElasticsearchException, IOException {
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
	
	private boolean parseHomepage(ElasticsearchClient esClient, IdentityHistory cidHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		GetResponse<Cid> resultGetCid = esClient.get(g->g.index(Indices.CidIndex).id(cidHist.getSigner()), Cid.class);
		
		if(cidHist.getData_op().equals("register")) {
			if(resultGetCid.found()) {
				Cid cid  = resultGetCid.source();	
				
				cid.setHomepages(cidHist.getData_homepages());
				cid.setLastHeight(cidHist.getHeight());
				esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
				isValid = true;
				
			}else {
				Cid cid = new Cid();
				cid.setId(cidHist.getSigner());
				cid.setHomepages(cidHist.getData_homepages());
				cid.setLastHeight(cidHist.getHeight());
				esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
				isValid = true;
			}
		}else if(cidHist.getData_op().equals("unregister")) {
			if(resultGetCid.found()) {
				Cid cid  = resultGetCid.source();	
				if(cid.getHomepages() ==null || cid.getHomepages()[0].isBlank()) {
					isValid = false;
				}else {
					cid.setHomepages(null);
					cid.setLastHeight(cidHist.getHeight());
					esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
					isValid = true;
				}
			}
		}
		

		return isValid;
	}

	private boolean parseNoticeFee(ElasticsearchClient esClient, IdentityHistory cidHist) throws ElasticsearchException, IOException {
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

	public boolean parseReputation(ElasticsearchClient esClient, RepuHistory repuHist) throws ElasticsearchException, IOException {
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


}
