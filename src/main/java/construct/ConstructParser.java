package construct;

import java.io.IOException;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import esClient.EsTools;
import opReturn.OpReturn;
import opReturn.Feip;
import start.Indices;
import tools.ParseTools;

public class ConstructParser {

	public FreeProtocolHistory makeFreeProtocol(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		FreeProtocolRaw freeProtocolRaw = new FreeProtocolRaw ();
		try {
		freeProtocolRaw = gson.fromJson(gson.toJson(feip.getData()),FreeProtocolRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return null;
		}
		
		ParseTools.gsonPrint(freeProtocolRaw);
		
		FreeProtocolHistory freeProtocolHist = new FreeProtocolHistory();
		
		if(freeProtocolRaw.getOp()==null)return null;
		
		freeProtocolHist.setOp(freeProtocolRaw.getOp());
		
		switch(freeProtocolRaw.getOp()) {

		case "publish":
			if(freeProtocolRaw.getSn()==null||freeProtocolRaw.getName()==null)	return null;
			freeProtocolHist.setId(opre.getId());
			
			freeProtocolHist.setPid(opre.getId());
			freeProtocolHist.setHeight(opre.getHeight());
			freeProtocolHist.setIndex(opre.getTxIndex());
			freeProtocolHist.setTime(opre.getTime());
			freeProtocolHist.setSigner(opre.getSigner());
			
			if(freeProtocolRaw.getType()!=null)freeProtocolHist.setType(freeProtocolRaw.getType());
			if(freeProtocolRaw.getSn()!=null)freeProtocolHist.setSn(freeProtocolRaw.getSn());
			if(freeProtocolRaw.getVer()!=null)freeProtocolHist.setVer(freeProtocolRaw.getVer());
			if(freeProtocolRaw.getHash()!=null)freeProtocolHist.setHash(freeProtocolRaw.getHash());
			if(freeProtocolRaw.getName()!=null)freeProtocolHist.setName(freeProtocolRaw.getName());
			if(freeProtocolRaw.getDesc()!=null)freeProtocolHist.setDesc(freeProtocolRaw.getDesc());
			if(freeProtocolRaw.getAuthors()!=null)freeProtocolHist.setAuthors(freeProtocolRaw.getAuthors());
			if(freeProtocolRaw.getLang()!=null)freeProtocolHist.setLang(freeProtocolRaw.getLang());
			
			if(freeProtocolRaw.getPrePid()!=null)freeProtocolHist.setPrePid(freeProtocolRaw.getPrePid());
			if(freeProtocolRaw.getFileUrls()!=null)freeProtocolHist.setFileUrls(freeProtocolRaw.getFileUrls());
			
			break;	
		case "update":
			
			if(freeProtocolRaw.getPid()==null||freeProtocolRaw.getSn()==null||freeProtocolRaw.getName()==null)	return null;
			freeProtocolHist.setId(opre.getId());
			freeProtocolHist.setHeight(opre.getHeight());
			freeProtocolHist.setIndex(opre.getTxIndex());
			freeProtocolHist.setTime(opre.getTime());
			freeProtocolHist.setSigner(opre.getSigner());
			
			freeProtocolHist.setPid(freeProtocolRaw.getPid());
			if(freeProtocolRaw.getType()!=null)freeProtocolHist.setType(freeProtocolRaw.getType());
			if(freeProtocolRaw.getSn()!=null)freeProtocolHist.setSn(freeProtocolRaw.getSn());
			if(freeProtocolRaw.getVer()!=null)freeProtocolHist.setVer(freeProtocolRaw.getVer());
			if(freeProtocolRaw.getHash()!=null)freeProtocolHist.setHash(freeProtocolRaw.getHash());
			if(freeProtocolRaw.getName()!=null)freeProtocolHist.setName(freeProtocolRaw.getName());
			if(freeProtocolRaw.getDesc()!=null)freeProtocolHist.setDesc(freeProtocolRaw.getDesc());
			if(freeProtocolRaw.getAuthors()!=null)freeProtocolHist.setAuthors(freeProtocolRaw.getAuthors());
			if(freeProtocolRaw.getLang()!=null)freeProtocolHist.setLang(freeProtocolRaw.getLang());
			
			if(freeProtocolRaw.getPrePid()!=null)freeProtocolHist.setPrePid(freeProtocolRaw.getPrePid());
			if(freeProtocolRaw.getFileUrls()!=null)freeProtocolHist.setFileUrls(freeProtocolRaw.getFileUrls());
			
			break;	
		case "stop":
			if(freeProtocolRaw.getPid()==null)return null;
			freeProtocolHist.setPid(freeProtocolRaw.getPid());

			freeProtocolHist.setId(opre.getId());
			freeProtocolHist.setHeight(opre.getHeight());
			freeProtocolHist.setIndex(opre.getTxIndex());
			freeProtocolHist.setTime(opre.getTime());
			freeProtocolHist.setSigner(opre.getSigner());
			break;
		case "recover":
			if(freeProtocolRaw.getPid()==null)return null;
			freeProtocolHist.setPid(freeProtocolRaw.getPid());

			freeProtocolHist.setId(opre.getId());
			freeProtocolHist.setHeight(opre.getHeight());
			freeProtocolHist.setIndex(opre.getTxIndex());
			freeProtocolHist.setTime(opre.getTime());
			freeProtocolHist.setSigner(opre.getSigner());
			break;
		case "rate":
			if(freeProtocolRaw.getPid()==null)return null;
			freeProtocolHist.setPid(freeProtocolRaw.getPid());
			freeProtocolHist.setRate(freeProtocolRaw.getRate());
			freeProtocolHist.setCdd(opre.getCdd());
			
			freeProtocolHist.setId(opre.getId());
			freeProtocolHist.setHeight(opre.getHeight());
			freeProtocolHist.setIndex(opre.getTxIndex());
			freeProtocolHist.setTime(opre.getTime());
			freeProtocolHist.setSigner(opre.getSigner());
			break;
		default:
			return null;
		}
		return freeProtocolHist; 
	}
	public ServiceHistory makeService(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		ServiceRaw serviceRaw = new ServiceRaw();
		
		try {
			serviceRaw = gson.fromJson(gson.toJson(feip.getData()),ServiceRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return null;
		}
		
		ParseTools.gsonPrint(serviceRaw);
		
		ServiceHistory serviceHist = new ServiceHistory();
		
		if(serviceRaw.getOp()==null)return null;
		
		serviceHist.setOp(serviceRaw.getOp());

		switch(serviceRaw.getOp()) {
		case "publish":
			if(serviceRaw.getStdName()==null)	return null;
			if(serviceRaw.getSid()!=null) return null;
			serviceHist.setId(opre.getId());
			serviceHist.setSid(opre.getId());
			serviceHist.setHeight(opre.getHeight());
			serviceHist.setIndex(opre.getTxIndex());
			serviceHist.setTime(opre.getTime());
			serviceHist.setSigner(opre.getSigner());

			if(serviceRaw.getStdName()!=null)serviceHist.setStdName(serviceRaw.getStdName());
			if(serviceRaw.getLocalNames()!=null)serviceHist.setLocalNames(serviceRaw.getLocalNames());
			if(serviceRaw.getDesc()!=null)serviceHist.setDesc(serviceRaw.getDesc());
			if(serviceRaw.getTypes()!=null)serviceHist.setTypes(serviceRaw.getTypes());
			if(serviceRaw.getUrls()!=null)serviceHist.setUrls(serviceRaw.getUrls());
			if(serviceRaw.getPubKeyAdmin()!=null)serviceHist.setPubKeyAdmin(serviceRaw.getPubKeyAdmin());
			if(serviceRaw.getProtocols()!=null)serviceHist.setProtocols(serviceRaw.getProtocols());
			if(serviceRaw.getParams()!=null) {
				serviceHist.setParams(serviceRaw.getParams());
			}
			break;
		case "update":
			if(serviceRaw.getSid()==null)	return null;
			if(serviceRaw.getStdName()==null)	return null;
			serviceHist.setId(opre.getId());
			serviceHist.setSid(serviceRaw.getSid());
			serviceHist.setHeight(opre.getHeight());
			serviceHist.setIndex(opre.getTxIndex());
			serviceHist.setTime(opre.getTime());
			serviceHist.setSigner(opre.getSigner());

			if(serviceRaw.getStdName()!=null)serviceHist.setStdName(serviceRaw.getStdName());
			if(serviceRaw.getLocalNames()!=null)serviceHist.setLocalNames(serviceRaw.getLocalNames());
			if(serviceRaw.getDesc()!=null)serviceHist.setDesc(serviceRaw.getDesc());
			if(serviceRaw.getTypes()!=null)serviceHist.setTypes(serviceRaw.getTypes());
			if(serviceRaw.getUrls()!=null)serviceHist.setUrls(serviceRaw.getUrls());
			if(serviceRaw.getPubKeyAdmin()!=null)serviceHist.setPubKeyAdmin(serviceRaw.getPubKeyAdmin());
			if(serviceRaw.getProtocols()!=null)serviceHist.setProtocols(serviceRaw.getProtocols());
			if(serviceRaw.getParams()!=null) {
				serviceHist.setParams(serviceRaw.getParams());
			}
			break;	
		case "stop":
			if(serviceRaw.getSid()==null)return null;
			serviceHist.setSid(serviceRaw.getSid());
			serviceHist.setId(opre.getId());
			serviceHist.setHeight(opre.getHeight());
			serviceHist.setIndex(opre.getTxIndex());
			serviceHist.setTime(opre.getTime());
			serviceHist.setSigner(opre.getSigner());
			break;
		case "recover":
			if(serviceRaw.getSid()==null)return null;
			serviceHist.setSid(serviceRaw.getSid());
			serviceHist.setId(opre.getId());
			serviceHist.setHeight(opre.getHeight());
			serviceHist.setIndex(opre.getTxIndex());
			serviceHist.setTime(opre.getTime());
			serviceHist.setSigner(opre.getSigner());
			serviceHist.setSid(serviceRaw.getSid());
			break;
		case "rate":
			if(serviceRaw.getSid()==null)return null;
			serviceHist.setSid(serviceRaw.getSid());
			serviceHist.setId(opre.getId());
			serviceHist.setHeight(opre.getHeight());
			serviceHist.setIndex(opre.getTxIndex());
			serviceHist.setTime(opre.getTime());
			serviceHist.setSigner(opre.getSigner());
			serviceHist.setRate(serviceRaw.getRate());
			serviceHist.setCdd(opre.getCdd());
			break;
		default:
			return null;
		}
		return serviceHist; 
	}
	public AppHistory makeApp(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		
		AppRaw appRaw = new AppRaw();
		
		try {
			appRaw = gson.fromJson(gson.toJson(feip.getData()),AppRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return null;
		}
		
		ParseTools.gsonPrint(appRaw);
		
		AppHistory appHist = new AppHistory();
		
		if(appRaw.getOp()==null)return null;
		appHist.setOp(appRaw.getOp());

		switch(appRaw.getOp()) {
		
		case "publish":
			if(appRaw.getStdName()==null)	return null;
			if(appRaw.getAid()!=null) return null;
			appHist.setId(opre.getId());
			appHist.setAid(opre.getId());
			appHist.setHeight(opre.getHeight());
			appHist.setIndex(opre.getTxIndex());
			appHist.setTime(opre.getTime());
			appHist.setSigner(opre.getSigner());

			if(appRaw.getStdName()!=null)appHist.setStdName(appRaw.getStdName());
			if(appRaw.getLocalNames()!=null)appHist.setLocalNames(appRaw.getLocalNames());
			if(appRaw.getDesc()!=null)appHist.setDesc(appRaw.getDesc());
			if(appRaw.getTypes()!=null)appHist.setTypes(appRaw.getTypes());
			if(appRaw.getUrls()!=null)appHist.setUrls(appRaw.getUrls());
			if(appRaw.getPubKeyAdmin()!=null)appHist.setPubKeyAdmin(appRaw.getPubKeyAdmin());
			if(appRaw.getProtocols()!=null)appHist.setProtocols(appRaw.getProtocols());
			if(appRaw.getServices() !=null)appHist.setServices(appRaw.getServices());

			break;	
			
		case "update":
			if(appRaw.getAid()==null)	return null;
			if(appRaw.getStdName()==null)	return null;
			appHist.setAid(appRaw.getAid());
			appHist.setId(opre.getId());
			appHist.setHeight(opre.getHeight());
			appHist.setIndex(opre.getTxIndex());
			appHist.setTime(opre.getTime());
			appHist.setSigner(opre.getSigner());

			if(appRaw.getStdName()!=null)appHist.setStdName(appRaw.getStdName());
			if(appRaw.getLocalNames()!=null)appHist.setLocalNames(appRaw.getLocalNames());
			if(appRaw.getDesc()!=null)appHist.setDesc(appRaw.getDesc());
			if(appRaw.getTypes()!=null)appHist.setTypes(appRaw.getTypes());
			if(appRaw.getUrls()!=null)appHist.setUrls(appRaw.getUrls());
			if(appRaw.getPubKeyAdmin()!=null)appHist.setPubKeyAdmin(appRaw.getPubKeyAdmin());
			if(appRaw.getProtocols()!=null)appHist.setProtocols(appRaw.getProtocols());
			if(appRaw.getServices() !=null)appHist.setServices(appRaw.getServices());

			break;	
			
		case "stop":
			if(appRaw.getAid()==null)return null;
			appHist.setAid(appRaw.getAid());
			
			appHist.setId(opre.getId());
			appHist.setHeight(opre.getHeight());
			appHist.setIndex(opre.getTxIndex());
			appHist.setTime(opre.getTime());
			appHist.setSigner(opre.getSigner());
			break;
		case "recover":
			if(appRaw.getAid()==null)return null;
			appHist.setAid(appRaw.getAid());
			
			appHist.setId(opre.getId());
			appHist.setHeight(opre.getHeight());
			appHist.setIndex(opre.getTxIndex());
			appHist.setTime(opre.getTime());
			appHist.setSigner(opre.getSigner());
			break;
		case "rate":
			if(appRaw.getAid()==null)return null;
			appHist.setAid(appRaw.getAid());
			appHist.setRate(appRaw.getRate());
			appHist.setCdd(opre.getCdd());
			
			appHist.setId(opre.getId());
			appHist.setHeight(opre.getHeight());
			appHist.setIndex(opre.getTxIndex());
			appHist.setTime(opre.getTime());
			appHist.setSigner(opre.getSigner());
			break;
		default:
			return null;
		}
		return appHist; 
	}
	public CodeHistory makeCode(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		CodeRaw codeRaw = new CodeRaw();
		
		try {
			codeRaw = gson.fromJson(gson.toJson(feip.getData()),CodeRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return null;
		}
		
		ParseTools.gsonPrint(codeRaw);
		
		CodeHistory codeHist = new CodeHistory();
		
		if(codeRaw.getOp()==null)return null;
		
		codeHist.setOp(codeRaw.getOp());

		switch(codeRaw.getOp()) {
		case "publish":
			if(codeRaw.getName()==null)	return null;
			if(codeRaw.getCoid()!=null) return null;
			codeHist.setId(opre.getId());
			codeHist.setCoid(opre.getId());
			codeHist.setHeight(opre.getHeight());
			codeHist.setIndex(opre.getTxIndex());
			codeHist.setTime(opre.getTime());
			codeHist.setSigner(opre.getSigner());

			if(codeRaw.getName()!=null)codeHist.setName(codeRaw.getName());
			if(codeRaw.getVersion()!=null)codeHist.setVersion(codeRaw.getVersion());
			if(codeRaw.getHash()!=null)codeHist.setHash(codeRaw.getHash());
			if(codeRaw.getDesc()!=null)codeHist.setDesc(codeRaw.getDesc());
			if(codeRaw.getUrls()!=null)codeHist.setUrls(codeRaw.getUrls());
			if(codeRaw.getLangs()!=null)codeHist.setLangs(codeRaw.getLangs());
			if(codeRaw.getProtocols()!=null)codeHist.setProtocols(codeRaw.getProtocols());
			if(codeRaw.getPubKeyAdmin()!=null)codeHist.setPubKeyAdmin(codeRaw.getPubKeyAdmin());

			break;
		case "update":
			if(codeRaw.getCoid()==null)	return null;
			if(codeRaw.getName()==null)	return null;
			codeHist.setId(opre.getId());
			codeHist.setCoid(codeRaw.getCoid());
			codeHist.setHeight(opre.getHeight());
			codeHist.setIndex(opre.getTxIndex());
			codeHist.setTime(opre.getTime());
			codeHist.setSigner(opre.getSigner());

			if(codeRaw.getName()!=null)codeHist.setName(codeRaw.getName());
			if(codeRaw.getVersion()!=null)codeHist.setVersion(codeRaw.getVersion());
			if(codeRaw.getHash()!=null)codeHist.setHash(codeRaw.getHash());
			if(codeRaw.getDesc()!=null)codeHist.setDesc(codeRaw.getDesc());
			if(codeRaw.getUrls()!=null)codeHist.setUrls(codeRaw.getUrls());
			if(codeRaw.getLangs()!=null)codeHist.setLangs(codeRaw.getLangs());
			if(codeRaw.getProtocols()!=null)codeHist.setProtocols(codeRaw.getProtocols());
			if(codeRaw.getPubKeyAdmin()!=null)codeHist.setPubKeyAdmin(codeRaw.getPubKeyAdmin());
			break;	
		case "stop":
			if(codeRaw.getCoid()==null)	return null;
			codeHist.setCoid(codeRaw.getCoid());
			codeHist.setId(opre.getId());
			codeHist.setHeight(opre.getHeight());
			codeHist.setIndex(opre.getTxIndex());
			codeHist.setTime(opre.getTime());
			codeHist.setSigner(opre.getSigner());
			break;
		case "recover":
			if(codeRaw.getCoid()==null)	return null;
			codeHist.setCoid(codeRaw.getCoid());
			codeHist.setId(opre.getId());
			codeHist.setHeight(opre.getHeight());
			codeHist.setIndex(opre.getTxIndex());
			codeHist.setTime(opre.getTime());
			codeHist.setSigner(opre.getSigner());

			break;
		case "rate":
			if(codeRaw.getCoid()==null)	return null;
			codeHist.setCoid(codeRaw.getCoid());
			codeHist.setId(opre.getId());
			codeHist.setHeight(opre.getHeight());
			codeHist.setIndex(opre.getTxIndex());
			codeHist.setTime(opre.getTime());
			codeHist.setSigner(opre.getSigner());
			codeHist.setRate(codeRaw.getRate());
			codeHist.setCdd(opre.getCdd());
			break;
		default:
			return null;
		}
		return codeHist; 
	}

	public boolean parseFreeProtocol(ElasticsearchClient esClient, FreeProtocolHistory freeProtocolHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		FreeProtocol freeProtocol;
		switch(freeProtocolHist.getOp()) {
		case "publish":			
			freeProtocol = EsTools.getById(esClient, Indices.FreeProtocolIndex, freeProtocolHist.getPid(), FreeProtocol.class);		
			if(freeProtocol==null) {
				freeProtocol = new FreeProtocol();
				freeProtocol.setPid(freeProtocolHist.getPid());
				freeProtocol.setType(freeProtocolHist.getType());
				freeProtocol.setSn(freeProtocolHist.getSn());
				freeProtocol.setVer(freeProtocolHist.getVer());
				freeProtocol.setHash(freeProtocolHist.getHash());
				freeProtocol.setName(freeProtocolHist.getName());
				
				freeProtocol.setLang(freeProtocolHist.getLang());
				freeProtocol.setDesc(freeProtocolHist.getDesc());
				freeProtocol.setAuthors(freeProtocolHist.getAuthors());
				freeProtocol.setPrePid(freeProtocolHist.getPrePid());
				freeProtocol.setFileUrls(freeProtocolHist.getFileUrls());
				
				freeProtocol.setTitle(freeProtocolHist.getType()+freeProtocolHist.getSn()+"V"+freeProtocolHist.getVer()+"_"+freeProtocolHist.getName()+"("+freeProtocolHist.getLang()+")");
				freeProtocol.setOwner(freeProtocolHist.getSigner());
				
				freeProtocol.setBirthTxid(freeProtocolHist.getId());
				freeProtocol.setBirthTime(freeProtocolHist.getTime());
				freeProtocol.setBirthHeight(freeProtocolHist.getHeight());
				
				freeProtocol.setLastTxid(freeProtocolHist.getId());
				freeProtocol.setLastTime(freeProtocolHist.getTime());
				freeProtocol.setLastHeight(freeProtocolHist.getHeight());
				
				freeProtocol.setActive(true);
				
				FreeProtocol freeProtocol1 = freeProtocol;
				
				esClient.index(i->i.index(Indices.FreeProtocolIndex).id(freeProtocolHist.getPid()).document(freeProtocol1));
				isValid = true;
			}else {
				isValid = false;
			}
			break;
			
		case "stop":
			
			freeProtocol = EsTools.getById(esClient, Indices.FreeProtocolIndex, freeProtocolHist.getPid(), FreeProtocol.class);
			
			if(freeProtocol==null) {
				isValid = false;
				break;
			}
			
			if(! freeProtocol.getOwner().equals(freeProtocolHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(freeProtocol.isActive()) {
				FreeProtocol freeProtocol1 = freeProtocol;
				freeProtocol1.setActive(false);
				freeProtocol1.setLastTxid(freeProtocolHist.getId());
				freeProtocol1.setLastTime(freeProtocolHist.getTime());
				freeProtocol1.setLastHeight(freeProtocolHist.getHeight());
				esClient.index(i->i.index(Indices.FreeProtocolIndex).id(freeProtocolHist.getPid()).document(freeProtocol1));
				isValid = true;
			}else isValid = false;

			break;
			
		case "recover":
			
			freeProtocol = EsTools.getById(esClient, Indices.FreeProtocolIndex, freeProtocolHist.getPid(), FreeProtocol.class);
			
			if(freeProtocol==null) {
				isValid = false;
				break;
			}
			
			if(! freeProtocol.getOwner().equals(freeProtocolHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(!freeProtocol.isActive()) {
				FreeProtocol freeProtocol1 = freeProtocol;
				freeProtocol1.setActive(true);
				freeProtocol1.setLastTxid(freeProtocolHist.getId());
				freeProtocol1.setLastTime(freeProtocolHist.getTime());
				freeProtocol1.setLastHeight(freeProtocolHist.getHeight());
				esClient.index(i->i.index(Indices.FreeProtocolIndex).id(freeProtocolHist.getPid()).document(freeProtocol1));
				isValid = true;
			}else isValid = false;
			break;	
		case "update":	
			freeProtocol = EsTools.getById(esClient, Indices.FreeProtocolIndex, freeProtocolHist.getPid(), FreeProtocol.class);
			
			if(freeProtocol==null) {
				isValid = false;
				break;
			}
			
			if(! freeProtocol.getOwner().equals(freeProtocolHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(!freeProtocol.isActive()) {
				isValid = false;
				break;
			}
			
			freeProtocol.setType(freeProtocolHist.getType());
			freeProtocol.setSn(freeProtocolHist.getSn());
			freeProtocol.setVer(freeProtocolHist.getVer());
			freeProtocol.setHash(freeProtocolHist.getHash());
			freeProtocol.setName(freeProtocolHist.getName());
			
			freeProtocol.setLang(freeProtocolHist.getLang());
			freeProtocol.setDesc(freeProtocolHist.getDesc());
			freeProtocol.setAuthors(freeProtocolHist.getAuthors());
			freeProtocol.setPrePid(freeProtocolHist.getPrePid());
			freeProtocol.setFileUrls(freeProtocolHist.getFileUrls());
			
			freeProtocol.setTitle(freeProtocolHist.getType()+freeProtocolHist.getSn()+"V"+freeProtocolHist.getVer()+"_"+freeProtocolHist.getName()+"("+freeProtocolHist.getLang()+")");
			
			freeProtocol.setLastTxid(freeProtocolHist.getId());
			freeProtocol.setLastTime(freeProtocolHist.getTime());
			freeProtocol.setLastHeight(freeProtocolHist.getHeight());
			
			FreeProtocol freeProtocol2 = freeProtocol;
			
			esClient.index(i->i.index(Indices.FreeProtocolIndex).id(freeProtocolHist.getPid()).document(freeProtocol2));
			isValid = true;
			break;
			
		case "rate":
			freeProtocol = EsTools.getById(esClient, Indices.FreeProtocolIndex, freeProtocolHist.getPid(), FreeProtocol.class);
			if(freeProtocol==null) {
				isValid = false;
				break;
			}
			if(freeProtocol.getOwner().equals(freeProtocolHist.getSigner())) {
				isValid = false;
				break;
			}
			if(freeProtocol.gettCdd()+freeProtocolHist.getCdd()==0) {
				freeProtocol.settRate(0);
			}else {
				freeProtocol.settRate(
						(freeProtocol.gettRate()*freeProtocol.gettCdd()+freeProtocolHist.getRate()*freeProtocolHist.getCdd())
						/(freeProtocol.gettCdd()+freeProtocolHist.getCdd())
						);
			}
			freeProtocol.settCdd(freeProtocol.gettCdd()+freeProtocolHist.getCdd());
			freeProtocol.setLastTxid(freeProtocolHist.getId());
			freeProtocol.setLastTime(freeProtocolHist.getTime());
			freeProtocol.setLastHeight(freeProtocolHist.getHeight());
			
			FreeProtocol freeProtocol3 = freeProtocol;
			esClient.index(i->i.index(Indices.FreeProtocolIndex).id(freeProtocolHist.getPid()).document(freeProtocol3));
			isValid = true;
			break;
			
		}
		
		return isValid;
	}

	public boolean parseService(ElasticsearchClient esClient, ServiceHistory serviceHist) throws ElasticsearchException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		Service service;
		switch(serviceHist.getOp()) {
		case "publish":
			service = EsTools.getById(esClient, Indices.ServiceIndex, serviceHist.getSid(), Service.class);	
			if(service==null) {
				service = new Service();
				service.setSid(serviceHist.getId());
				service.setStdName(serviceHist.getStdName());
				service.setLocalNames(serviceHist.getLocalNames());
				service.setDesc(serviceHist.getDesc());
				service.setTypes(serviceHist.getTypes());
				service.setUrls(serviceHist.getUrls());
				service.setPubKeyAdmin(serviceHist.getPubKeyAdmin());
				service.setProtocols(serviceHist.getProtocols());
				service.setParams(serviceHist.getParams());
				service.setOwner(serviceHist.getSigner());
	
				service.setLastTxid(serviceHist.getId());
				service.setLastTime(serviceHist.getTime());
				service.setLastHeight(serviceHist.getHeight());
				
				service.setBirthTime(serviceHist.getTime());
				service.setBirthHeight(serviceHist.getHeight());
				
				service.setActive(true);
				
				Service service1 = service;
				esClient.index(i->i.index(Indices.ServiceIndex).id(serviceHist.getSid()).document(service1));
				isValid = true;
			}else {
				isValid=false;
			}
			break;
		case "stop":
			service = EsTools.getById(esClient, Indices.ServiceIndex, serviceHist.getSid(), Service.class);
			
			if(service==null) {
				isValid = false;
				break;
			}
			
			if(! service.getOwner().equals(serviceHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(service.isActive()) {
				Service service2 = service;
				service2.setActive(false);
				service2.setLastTxid(serviceHist.getId());
				service2.setLastTime(serviceHist.getTime());
				service2.setLastHeight(serviceHist.getHeight());
				esClient.index(i->i.index(Indices.ServiceIndex).id(serviceHist.getSid()).document(service2));
				isValid = true;
			}else isValid = false;

			break;
			
		case "recover":
			service = EsTools.getById(esClient, Indices.ServiceIndex, serviceHist.getSid(), Service.class);
			
			if(service==null) {
				isValid = false;
				break;
			}
			
			if(! service.getOwner().equals(serviceHist.getSigner())) {
				isValid = false;
				break;
			}
			
			
			if(!service.isActive()) {
				Service service3 = service;
				service3.setActive(true);
				service3.setLastTxid(serviceHist.getId());
				service3.setLastTime(serviceHist.getTime());
				service3.setLastHeight(serviceHist.getHeight());
				esClient.index(i->i.index(Indices.ServiceIndex).id(serviceHist.getSid()).document(service3));
				isValid = true;
			}else isValid = false;

			break;
			
		case "update":
			service = EsTools.getById(esClient, Indices.ServiceIndex, serviceHist.getSid(), Service.class);
			
			if(service==null) {
				isValid = false;
				break;
			}
			
			if(! service.getOwner().equals(serviceHist.getSigner())) {
				isValid = false;
				break;
			}

			service.setStdName(serviceHist.getStdName());
			service.setLocalNames(serviceHist.getLocalNames());
			service.setDesc(serviceHist.getDesc());
			service.setTypes(serviceHist.getTypes());
			service.setUrls(serviceHist.getUrls());
			service.setPubKeyAdmin(serviceHist.getPubKeyAdmin());
			service.setProtocols(serviceHist.getProtocols());
			service.setParams(serviceHist.getParams());

			service.setLastTxid(serviceHist.getId());
			service.setLastTime(serviceHist.getTime());
			service.setLastHeight(serviceHist.getHeight());
			
			Service service4 = service;

			esClient.index(i->i.index(Indices.ServiceIndex).id(serviceHist.getSid()).document(service4));
			isValid = true;

			break;
		case "rate":
			ParseTools.gsonPrint(serviceHist);
			service = EsTools.getById(esClient, Indices.ServiceIndex, serviceHist.getSid(), Service.class);
			
			if(service==null) {
				isValid = false;
				break;
			}
			
			if(service.getOwner().equals(serviceHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(service.gettCdd()+serviceHist.getCdd()==0) {
				service.settRate(0);
			}else {
				service.settRate(
						(service.gettRate()*service.gettCdd()+serviceHist.getRate()*serviceHist.getCdd())
						/(service.gettCdd()+serviceHist.getCdd())
						);
			}
			service.settCdd(service.gettCdd()+serviceHist.getCdd());
			service.setLastTxid(serviceHist.getId());
			service.setLastTime(serviceHist.getTime());
			service.setLastHeight(serviceHist.getHeight());
			
			Service service5 = service;
			
			esClient.index(i->i.index(Indices.ServiceIndex).id(serviceHist.getSid()).document(service5));
			isValid = true;
			break;
		}
		return isValid;
	}

	public boolean parseApp(ElasticsearchClient esClient, construct.AppHistory appHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		App app;
		switch(appHist.getOp()) {
		case "publish":
			app = EsTools.getById(esClient, Indices.AppIndex, appHist.getAid(), App.class);		
			if(app==null) {
				app = new App();
				app.setAid(appHist.getId());
				app.setStdName(appHist.getStdName());
				app.setLocalNames(appHist.getLocalNames());
				app.setDesc(appHist.getDesc());
				app.setUrls(appHist.getUrls());
				app.setPubKeyAdmin(appHist.getPubKeyAdmin());
				app.setOwner(appHist.getSigner());
				app.setProtocols(appHist.getProtocols());
				app.setServices(appHist.getServices());
				
				app.setBirthTime(appHist.getTime());
				app.setBirthHeight(appHist.getHeight());
				
				app.setLastTxid(appHist.getId());
				app.setLastTime(appHist.getTime());
				app.setLastHeight(appHist.getHeight());
	
				app.setActive(true);
				
				App app1=app;
				esClient.index(i->i.index(Indices.AppIndex).id(appHist.getAid()).document(app1));
				isValid = true;
			}else {
				isValid = false;
			}
			break;
			
		case "stop":
			
			app = EsTools.getById(esClient, Indices.AppIndex, appHist.getAid(), App.class);
			
			if(app==null) {
				isValid = false;
				break;
			}
			
			if(! app.getOwner().equals(appHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(app.isActive()) {
				App app2 = app;
				app2.setActive(false);
				app2.setLastTxid(appHist.getId());
				app2.setLastTime(appHist.getTime());
				app2.setLastHeight(appHist.getHeight());
				esClient.index(i->i.index(Indices.AppIndex).id(appHist.getAid()).document(app2));
				isValid = true;
			}else isValid = false;

			break;
			
		case "recover":
			
			app = EsTools.getById(esClient, Indices.AppIndex, appHist.getAid(), App.class);
			
			if(app==null) {
				isValid = false;
				break;
			}
			
			if(! app.getOwner().equals(appHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(!app.isActive()) {
				App app2 = app;
				app2.setActive(true);
				app2.setLastTxid(appHist.getId());
				app2.setLastTime(appHist.getTime());
				app2.setLastHeight(appHist.getHeight());
				esClient.index(i->i.index(Indices.AppIndex).id(appHist.getAid()).document(app2));
				isValid = true;
			}else isValid = false;

			break;
			
		case "update":	
			app = EsTools.getById(esClient, Indices.AppIndex, appHist.getAid(), App.class);
			
			if(app==null) {
				isValid = false;
				break;
			}
			
			if(! app.getOwner().equals(appHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(!app.isActive()) {
				isValid = false;
				break;
			}
				
			app.setStdName(appHist.getStdName());
			app.setLocalNames(appHist.getLocalNames());
			app.setDesc(appHist.getDesc());
			app.setUrls(appHist.getUrls());
			app.setPubKeyAdmin(appHist.getPubKeyAdmin());
			app.setOwner(appHist.getSigner());
			app.setProtocols(appHist.getProtocols());
			app.setServices(appHist.getServices());
			
			app.setLastTxid(appHist.getId());
			app.setLastTime(appHist.getTime());
			app.setLastHeight(appHist.getHeight());
			
			App app2 = app;
			
			esClient.index(i->i.index(Indices.AppIndex).id(appHist.getAid()).document(app2));
			isValid = true;
			break;
			
		case "rate":
			app = EsTools.getById(esClient, Indices.AppIndex, appHist.getAid(), App.class);
			
			if(app==null) {
				isValid = false;
				break;
			}
			
			if(app.getOwner().equals(appHist.getSigner())) {
				isValid = false;
				break;
			}

			if(app.gettCdd()+appHist.getCdd()==0) {
				app.settRate(0);
			}else {
				app.settRate(
						(app.gettRate()*app.gettCdd()+appHist.getRate()*appHist.getCdd())
						/(app.gettCdd()+appHist.getCdd())
						);
			}
			app.settCdd(app.gettCdd()+appHist.getCdd());
			app.setLastTxid(appHist.getId());
			app.setLastTime(appHist.getTime());
			app.setLastHeight(appHist.getHeight());
			
			App app3 = app;
			
			esClient.index(i->i.index(Indices.AppIndex).id(appHist.getAid()).document(app3));
			isValid = true;
			break;
		}
		return isValid;
	}
	public boolean parseCode(ElasticsearchClient esClient, CodeHistory codeHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		Code code;
		switch(codeHist.getOp()) {
		case "publish":
			code = EsTools.getById(esClient, Indices.CodeIndex, codeHist.getCoid(), Code.class);		
			if(code==null) {
				code = new Code();
				code.setCoid(codeHist.getId());
				code.setName(codeHist.getName());
				code.setVersion(codeHist.getVersion());
				code.setHash(codeHist.getHash());
				code.setDesc(codeHist.getDesc());
				code.setLangs(codeHist.getLangs());
				code.setUrls(codeHist.getUrls());
				code.setProtocols(codeHist.getProtocols());
				code.setPubKeyAdmin(codeHist.getPubKeyAdmin());
				
				code.setOwner(codeHist.getSigner());
				code.setBirthTime(codeHist.getTime());
				code.setBirthHeight(codeHist.getHeight());
				
				code.setLastTxid(codeHist.getId());
				code.setLastTime(codeHist.getTime());
				code.setLastHeight(codeHist.getHeight());
	
				code.setActive(true);
				
				Code code1=code;
				esClient.index(i->i.index(Indices.CodeIndex).id(codeHist.getCoid()).document(code1));
				isValid = true;
			}else {
				isValid = false;
			}
			break;
			
		case "stop":
			
			code = EsTools.getById(esClient, Indices.CodeIndex, codeHist.getCoid(), Code.class);
			
			if(code==null) {
				isValid = false;
				break;
			}
			
			if(! code.getOwner().equals(codeHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(code.isActive()) {
				Code app2 = code;
				app2.setActive(false);
				app2.setLastTxid(codeHist.getId());
				app2.setLastTime(codeHist.getTime());
				app2.setLastHeight(codeHist.getHeight());
				esClient.index(i->i.index(Indices.CodeIndex).id(codeHist.getCoid()).document(app2));
				isValid = true;
			}else isValid = false;

			break;
			
		case "recover":
			
			code = EsTools.getById(esClient, Indices.CodeIndex, codeHist.getCoid(), Code.class);
			
			if(code==null) {
				isValid = false;
				break;
			}
			
			if(! code.getOwner().equals(codeHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(!code.isActive()) {
				Code code2 = code;
				code2.setActive(true);
				code2.setLastTxid(codeHist.getId());
				code2.setLastTime(codeHist.getTime());
				code2.setLastHeight(codeHist.getHeight());
				esClient.index(i->i.index(Indices.CodeIndex).id(codeHist.getCoid()).document(code2));
				isValid = true;
			}else isValid = false;

			break;
			
		case "update":	
			code = EsTools.getById(esClient, Indices.CodeIndex, codeHist.getCoid(), Code.class);
			
			if(code==null) {
				isValid = false;
				break;
			}
			
			if(! code.getOwner().equals(codeHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(!code.isActive()) {
				isValid = false;
				break;
			}		

			code.setName(codeHist.getName());
			code.setVersion(codeHist.getVersion());
			code.setHash(codeHist.getHash());
			code.setDesc(codeHist.getDesc());
			code.setLangs(codeHist.getLangs());
			code.setUrls(codeHist.getUrls());
			code.setProtocols(codeHist.getProtocols());
			code.setPubKeyAdmin(codeHist.getPubKeyAdmin());
			
			code.setLastTxid(codeHist.getId());
			code.setLastTime(codeHist.getTime());
			code.setLastHeight(codeHist.getHeight());

			
			Code app2 = code;
			
			esClient.index(i->i.index(Indices.CodeIndex).id(codeHist.getCoid()).document(app2));
			isValid = true;
			break;
			
		case "rate":
			code = EsTools.getById(esClient, Indices.CodeIndex, codeHist.getCoid(), Code.class);
			
			if(code==null) {
				isValid = false;
				break;
			}
			
			if(code.getOwner().equals(codeHist.getSigner())) {
				isValid = false;
				break;
			}

			if(code.gettCdd()+codeHist.getCdd()==0) {
				code.settRate(0);
			}else {
				code.settRate(
						(code.gettRate()*code.gettCdd()+codeHist.getRate()*codeHist.getCdd())
						/(code.gettCdd()+codeHist.getCdd())
						);
			}
			code.settCdd(code.gettCdd()+codeHist.getCdd());
			code.setLastTxid(codeHist.getId());
			code.setLastTime(codeHist.getTime());
			code.setLastHeight(codeHist.getHeight());
			
			Code code3 = code;
			
			esClient.index(i->i.index(Indices.CodeIndex).id(codeHist.getCoid()).document(code3));
			isValid = true;
			break;
		}
		return isValid;
	}
}
