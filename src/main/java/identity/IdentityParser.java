package identity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import opReturn.Feip;
import opReturn.OpReturn;
import start.Indices;
import start.Start;
import tools.BytesTools;
import tools.FchTools;
import tools.ParseTools;

public class IdentityParser {
	
	public IdentityHistory makeCid(OpReturn opre, Feip feip) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub

		Gson gson = new Gson();
		CidRaw cidRaw = gson.fromJson(gson.toJson(feip.getData()),CidRaw.class);
		if(cidRaw==null)return null;
		
		ParseTools.gsonPrint(cidRaw);
		
		if(cidRaw.getOp()==null)return null;
		
		IdentityHistory cidHist = new IdentityHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(feip.getSn());
		cidHist.setVer(feip.getVer());
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

	public IdentityHistory makeAbandon(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		AbandonRaw abandonRaw = gson.fromJson(gson.toJson(feip.getData()),AbandonRaw.class);
		if(! addrFromPriKey(abandonRaw.getPriKey()).equals(opre.getSigner()))return null;
		
		IdentityHistory cidHist = new IdentityHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(feip.getSn());
		cidHist.setVer(feip.getVer());
		cidHist.setHeight(opre.getHeight());
		cidHist.setId(opre.getId());
		cidHist.setIndex(opre.getTxIndex());
		cidHist.setTime(opre.getTime());
		cidHist.setData_priKey(abandonRaw.getPriKey());
	
		return cidHist; 
	}

	private String addrFromPriKey(String priKey) {
		// TODO Auto-generated method stub
		String addr = FchTools.pubKeyToFchAddr(FchTools.priKeyToPubKey(priKey)) ;
		return addr;
	}

	public IdentityHistory makeMaster(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		MasterRaw masterRaw = gson.fromJson(gson.toJson(feip.getData()),MasterRaw.class);
		if(masterRaw==null)return null;
		if(masterRaw.getPromise()==null)return null;
		if(!masterRaw.getPromise().equals("The master owns all my rights."))return null;
		
		if(!FchTools.isValidFchAddr(masterRaw.getMaster()))return null;
		
		IdentityHistory cidHist = new IdentityHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(feip.getSn());
		cidHist.setVer(feip.getVer());
		cidHist.setHeight(opre.getHeight());
		cidHist.setId(opre.getId());
		cidHist.setIndex(opre.getTxIndex());
		cidHist.setTime(opre.getTime());
		cidHist.setData_master(masterRaw.getMaster());
	
		return cidHist; 
	}
	
	public IdentityHistory makeHomepage(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		HomepageRaw homepageRaw = gson.fromJson(gson.toJson(feip.getData()),HomepageRaw.class);
		
		if(homepageRaw ==null)return null;
		
		if(homepageRaw.getHomepages()== null || homepageRaw.getHomepages()[0] == null || homepageRaw.getHomepages()[0].isBlank())return null;
		
		if(homepageRaw.getOp()==null)return null;
		
		if(!(homepageRaw.getOp().equals("register") || homepageRaw.getOp().equals("unregister"))) return null;
		
		IdentityHistory cidHist = new IdentityHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(feip.getSn());
		cidHist.setVer(feip.getVer());
		cidHist.setHeight(opre.getHeight());
		cidHist.setId(opre.getId());
		cidHist.setIndex(opre.getTxIndex());
		cidHist.setTime(opre.getTime());
		
		cidHist.setData_op(homepageRaw.getOp());
		cidHist.setData_homepages(homepageRaw.getHomepages());
		
		return cidHist; 
	}

	public IdentityHistory makeNoticeFee(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		NoticeFeeRaw noticeFeeRaw = gson.fromJson(gson.toJson(feip.getData()),NoticeFeeRaw.class);
		if(noticeFeeRaw ==null)return null;
		
		IdentityHistory cidHist = new IdentityHistory();
		
		cidHist.setSigner(opre.getSigner());
		cidHist.setSn(feip.getSn());
		cidHist.setVer(feip.getVer());
		cidHist.setHeight(opre.getHeight());
		cidHist.setId(opre.getId());
		cidHist.setIndex(opre.getTxIndex());
		cidHist.setTime(opre.getTime());
		
		cidHist.setData_noticeFee(noticeFeeRaw.getNoticeFee());
		
		return cidHist; 
	}
	public RepuHistory makeReputation(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		if(opre.getCdd()<Start.CddRequired)return null;
		Gson gson = new Gson();
		ReputationRaw reputationRaw = gson.fromJson(gson.toJson(feip.getData()),ReputationRaw.class);
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
		repuHist.setCause(reputationRaw.getCause());
		
		return repuHist; 
	}

	public boolean parseCidInfo(ElasticsearchClient esClient, IdentityHistory cidHist) throws ElasticsearchException, IOException, InterruptedException {
		
		if(cidHist.getSn().equals("3"))return parseCid(esClient, cidHist);
		if(cidHist.getSn().equals("4"))return parseAbandon(esClient, cidHist);
		if(cidHist.getSn().equals("6"))return parseMaster(esClient, cidHist);
		if(cidHist.getSn().equals("9"))return parseHomepage(esClient, cidHist);
		if(cidHist.getSn().equals("10"))return parseNoticeFee(esClient, cidHist);

		return false;
	}

	private boolean parseAbandon(ElasticsearchClient esClient, IdentityHistory cidHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		GetResponse<Cid> resultGetCid = esClient.get(g->g.index(Indices.CidIndex).id(cidHist.getSigner()), Cid.class);
		
		if(resultGetCid.found()) {
			Cid cid  = resultGetCid.source();	
			if(cid.getPriKey()==null) {
				cid.setPriKey(cidHist.getData_priKey());
				cid.setLastHeight(cidHist.getHeight());
				esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
				isValid = true;
			}
		}
		return isValid;
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
			
			cid.setNoticeFee(cidHist.getData_noticeFee());
			cid.setLastHeight(cidHist.getHeight());
			esClient.index(i->i.index(Indices.CidIndex).id(cidHist.getSigner()).document(cid));
			isValid = true;

		}else {
			Cid cid = new Cid();
			cid.setId(cidHist.getSigner());
			cid.setNoticeFee(cidHist.getData_noticeFee());
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

	public void parseP2SH(ElasticsearchClient esClient, OpReturn opre, Feip feip) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		P2SHRaw p2shRaw = gson.fromJson(gson.toJson(feip.getData()),P2SHRaw.class);
		if(! FchTools.scriptToMultiAddr(p2shRaw.getScript()).equals(opre.getSigner()))return;
		
		GetResponse<P2SH> resultGetP2SH = esClient.get(g->g.index(Indices.P2SHIndex).id(opre.getSigner()), P2SH.class);
		
		if(resultGetP2SH.found())return;
		
		P2SH p2sh = new P2SH();
		
		p2sh.setId(opre.getSigner());
		
		String script = p2shRaw.getScript();
		
		Multisig mutiSig = parseMultiSigScript(script);
		
		p2sh.setId(opre.getSigner());
		p2sh.setRedeemScript(p2shRaw.getScript());
		p2sh.setM(mutiSig.getM());
		p2sh.setN(mutiSig.getN());
		p2sh.setPubKeys(mutiSig.getPubKeys());
		p2sh.setBirthHeight(opre.getHeight());
		p2sh.setBirthTime(opre.getTime());
		p2sh.setBirthTxid(opre.getId());

		return;
	}

	public Multisig parseMultiSigScript(String script) throws IOException {
		// TODO Auto-generated method stub
		
		if(! script.substring(script.length()-2).equals("ae"))return null;
		
		InputStream scriptIs = new ByteArrayInputStream(script.getBytes());
		
		int b = scriptIs.read();
		int m = b-80;
		
		if(m>16 || m<0)return null;
		
		ArrayList<String> pukList = new ArrayList<String>();

		while(true) {
			b = scriptIs.read();
			int pkLen = b;
			if(pkLen>80 && pkLen<96)break;
			if(pkLen!=33 && pkLen!=65)return null;
			byte[] pkBytes = new byte[pkLen];
			scriptIs.read(pkBytes);
			pukList.add(BytesTools.bytesToHexStringBE(pkBytes));
		}

		int n = b-80;
		
		Multisig multiSig = new Multisig	();
		
		multiSig.setM(m);
		multiSig.setN(n);
		multiSig.setPubKeys(pukList.toArray(new String[pukList.size()]));
		
		return multiSig;
	}
}
