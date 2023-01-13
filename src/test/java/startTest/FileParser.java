package startTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import construct.AppHistory;
import construct.CodeHistory;
import construct.ConstructParser;
import construct.ConstructRollbacker;
import construct.FreeProtocolHistory;
import construct.ServiceHistory;
import esClient.EsTools;
import esClient.EsTools.MgetResult;
import identity.IdentityHistory;
import identity.IdentityParser;
import identity.IdentityRollbacker;
import identity.RepuHistory;
import opReturn.Feip;
import opReturn.OpReFileTools;
import opReturn.OpReturn;
import opReturn.opReReadResult;
import organization.GroupHistory;
import organization.OrganizationParser;
import organization.OrganizationRollbacker;
import organization.TeamHistory;
import personal.PersonalParser;
import personal.PersonalRollbacker;
import start.Indices;
import start.ParseMark;
import start.Start;
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
		CID,ABANDON,MASTER,HOMEPAGE,NOTICE_FEE,REPUTATION,SERVICE,PROTOCOL,APP,CODE,CONTACTS,MAIL,SAFE,STATEMENT,GROUP,TEAM
	}
	
	private static final Logger log = LoggerFactory.getLogger(FileParser.class);
	
	public boolean parseFile(ElasticsearchClient esClient, boolean isRollback) throws Exception {
	
		IdentityRollbacker cidRollbacker = new IdentityRollbacker();
		IdentityParser cidParser = new IdentityParser();

		ConstructParser constructParser = new ConstructParser();
		ConstructRollbacker constructRollbacker = new ConstructRollbacker();
		
		PersonalParser personalParser = new PersonalParser();
		PersonalRollbacker personalRollbacker = new PersonalRollbacker();
		
		OrganizationParser organizationParser = new OrganizationParser();
		OrganizationRollbacker organizationRollbacker = new OrganizationRollbacker();
		
		if(isRollback) {
			cidRollbacker.rollback(esClient, lastHeight);
			constructRollbacker.rollback(esClient, lastHeight);
			personalRollbacker.rollback(esClient, lastHeight);
			organizationRollbacker.rollback(esClient, lastHeight);
		}
		
		FileInputStream fis;
		
		pointer += length;
		
		System.out.println("Start parse "+fileName+ " form "+pointer);
		log.info("Start parse {} from {}",fileName,pointer);
		
		TimeUnit.SECONDS.sleep(2);
		
		boolean error = false;
		
		while(!error) {
			fis = openFile();
			fis.skip(pointer);
			opReReadResult readOpResult = OpReFileTools.readOpReFromFile(fis);
			fis.close();
			length = readOpResult.getLength();
			pointer += length;
			
			boolean isValid= false;
			
			ParseTools.gsonPrint(readOpResult);
			
			if(readOpResult.isFileEnd()) {
				if(pointer>251658240) {
					fileName = OpReFileTools.getNextFile(fileName);	
					while(!new File(fileName).exists()) {
						System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
						System.out.println(" Waiting 30 seconds for new file ...");	
						TimeUnit.SECONDS.sleep(30);
					}
					pointer = 0;
					fis = openFile();
					continue;
				}else {
					System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
					System.out.println(" Waiting 30 seconds for new item ...");	
					fis.close();
					TimeUnit.SECONDS.sleep(30);
					fis = openFile();
					fis.skip(pointer);
					continue;
				}
			}
			
			
			if(readOpResult.isRollback()) {
				cidRollbacker.rollback(esClient,readOpResult.getOpReturn().getHeight());
				constructRollbacker.rollback(esClient, readOpResult.getOpReturn().getHeight());
				continue;
			}

			OpReturn opre = readOpResult.getOpReturn();
			lastHeight = opre.getHeight();
			lastIndex = opre.getTxIndex();
			lastId = opre.getId();
			
			if(opre.getHeight()>Start.CddCheckHeight && opre.getCdd()<Start.CddRequired)continue;
			
			Feip feip = parseFeip(opre);
			if(feip==null)continue;
			if(feip.getType()==null)continue;
			if(!feip.getType().equals("FEIP"))continue;
			
			//TODO
			ParseTools.gsonPrint(feip);
			
			FEIP_NAME feipName = checkFeipSn(feip);
			if(feipName == null)continue;

			switch(feipName) {
			
			case CID:
				System.out.println("Cid.");
				IdentityHistory cidHist = cidParser.makeCid(opre,feip);	
				if(cidHist==null)break;
				isValid = cidParser.parseCidInfo(esClient,cidHist);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist.getId()).document(cidHist));
				break;
			case ABANDON:
				System.out.println("abandon.");
				IdentityHistory cidHist4 = cidParser.makeAbandon(opre,feip);
				if(cidHist4==null)break;
				isValid = cidParser.parseCidInfo(esClient,cidHist4);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist4.getId()).document(cidHist4));
				break;
			case MASTER:
				System.out.println("master.");
				IdentityHistory cidHist1 = cidParser.makeMaster(opre,feip);
				if(cidHist1==null)break;
				isValid = cidParser.parseCidInfo(esClient,cidHist1);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist1.getId()).document(cidHist1));
				break;
			case HOMEPAGE:
				System.out.println("homepage.");
				IdentityHistory cidHist2 = cidParser.makeHomepage(opre,feip);
				if(cidHist2==null)break;
				isValid = cidParser.parseCidInfo(esClient,cidHist2);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist2.getId()).document(cidHist2));
				break;
			case NOTICE_FEE:
				System.out.println("notice fee.");
				IdentityHistory cidHist3 = cidParser.makeNoticeFee(opre,feip);
				if(cidHist3==null)break;
				isValid = cidParser.parseCidInfo(esClient,cidHist3);	
				if(isValid)esClient.index(i->i.index(Indices.CidHistIndex).id(cidHist3.getId()).document(cidHist3));
				break;
			case REPUTATION:
				System.out.println("reputation.");
				RepuHistory repuHist = cidParser.makeReputation(opre,feip);
				if(repuHist==null)break;
				isValid = cidParser.parseReputation(esClient,repuHist);
				if(isValid)esClient.index(i->i.index(Indices.RepuHistIndex).id(repuHist.getId()).document(repuHist));
				break;
			case PROTOCOL:
				System.out.println("FreeProtocol.");
				FreeProtocolHistory freeProtocolHist = constructParser.makeFreeProtocol(opre,feip);
				if(freeProtocolHist==null)break;
				isValid = constructParser.parseFreeProtocol(esClient,freeProtocolHist);	
				if(isValid)esClient.index(i->i.index(Indices.FreeProtocolHistIndex).id(freeProtocolHist.getId()).document(freeProtocolHist));
				break;
			case SERVICE:
				System.out.println("Service.");
				ServiceHistory serviceHist = constructParser.makeService(opre,feip);
				if(serviceHist==null)break;
				isValid = constructParser.parseService(esClient,serviceHist);	
				if(isValid)esClient.index(i->i.index(Indices.ServiceHistIndex).id(serviceHist.getId()).document(serviceHist));
				break;
			case APP:
				System.out.println("APP.");
				AppHistory appHist = constructParser.makeApp(opre,feip);
				if(appHist==null)break;
				isValid = constructParser.parseApp(esClient,appHist);	
				if(isValid)esClient.index(i->i.index(Indices.AppHistIndex).id(appHist.getId()).document(appHist));
				break;
			case CODE:
				System.out.println("Code.");
				CodeHistory codeHist = constructParser.makeCode(opre,feip);
				if(codeHist==null)break;
				isValid = constructParser.parseCode(esClient,codeHist);	
				if(isValid)esClient.index(i->i.index(Indices.CodeHistIndex).id(codeHist.getId()).document(codeHist));
				break;
			case CONTACTS:
				System.out.println("Contacts.");
				isValid = personalParser.parseConcern(esClient,opre,feip);	
				break;
			case MAIL:
				System.out.println("Mail.");
				isValid = personalParser.parseMail(esClient,opre,feip);	
				break;
			case SAFE:
				System.out.println("Safe.");
				isValid = personalParser.parseSafe(esClient,opre,feip);	
				break;
			case STATEMENT:
				System.out.println("Statement.");
				isValid = personalParser.parseStatement(esClient,opre,feip);	
				break;
			case GROUP:
				System.out.println("Group.");
				GroupHistory groupHist = organizationParser.makeGroup(opre,feip);
				if(groupHist==null)break;
				isValid = organizationParser.parseGroup(esClient,groupHist);	
				if(isValid)esClient.index(i->i.index(Indices.GroupHistIndex).id(groupHist.getId()).document(groupHist));
				break;
			case TEAM:
				System.out.println("Team.");
				TeamHistory teamHist = organizationParser.makeTeam(opre,feip);
				if(teamHist==null)break;
				isValid = organizationParser.parseTeam(esClient,teamHist);	
				if(isValid)esClient.index(i->i.index(Indices.TeamHistIndex).id(teamHist.getId()).document(teamHist));
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
		parseMark.setLength(length);
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

	private Feip parseFeip(OpReturn opre) {
		// TODO Auto-generated method stub
		if(opre.getOpReturn()==null)return null;
		
		String rawProt = opre.getOpReturn();
		
		if(!rawProt.contains("{")) return null;
		
		int begin = rawProt.indexOf("{");	
		String protStr = rawProt.substring(begin);
		
		protStr.replaceAll("\r|\n|\t", "");
		
		Feip prot = null;
		try {
			prot = new Gson().fromJson(protStr, Feip.class);
		}catch(JsonSyntaxException e) {
			System.out.println("Invalid opReturn content.");
		}
		return  prot;
	}

	private FEIP_NAME checkFeipSn(Feip feip) {
		// TODO Auto-generated method stub
		String sn = feip.getSn();
		if(sn.equals("1"))return FEIP_NAME.PROTOCOL;
		if(sn.equals("2"))return FEIP_NAME.CODE;
		if(sn.equals("3"))return FEIP_NAME.CID;
		if(sn.equals("4"))return FEIP_NAME.ABANDON;
		if(sn.equals("5"))return FEIP_NAME.SERVICE;
		if(sn.equals("6"))return FEIP_NAME.MASTER;
		if(sn.equals("7"))return FEIP_NAME.MAIL;
		if(sn.equals("8"))return FEIP_NAME.STATEMENT;
		if(sn.equals("9"))return FEIP_NAME.HOMEPAGE;
		if(sn.equals("10"))return FEIP_NAME.NOTICE_FEE;
		if(sn.equals("12"))return FEIP_NAME.CONTACTS;
	
		if(sn.equals("15"))return FEIP_NAME.APP;
		if(sn.equals("16"))return FEIP_NAME.REPUTATION;
		if(sn.equals("17"))return FEIP_NAME.SAFE;
		if(sn.equals("18"))return FEIP_NAME.TEAM;
		if(sn.equals("19"))return FEIP_NAME.GROUP;

		return null;
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

	public void reparseIdList(ElasticsearchClient esClient, String index, List<String> idList) throws Exception {
		// TODO Auto-generated method stub
		if(idList==null || idList.isEmpty())return;
		switch (index) {
		case Indices.CidIndex:
			EsTools.bulkDeleteList(esClient, Indices.CidIndex, (ArrayList<String>) idList);
			TimeUnit.SECONDS.sleep(2);
			
			ArrayList<IdentityHistory> reparseCidList = getReparseHistList(esClient,Indices.CidHistIndex,idList,"signer",IdentityHistory.class);
			
			for(IdentityHistory idHist: reparseCidList) {
				new IdentityParser().parseCidInfo(esClient,idHist);
			}
			new IdentityRollbacker().reviseCidRepuAndHot(esClient, (ArrayList<String>) idList);
			break;
		case Indices.FreeProtocolIndex:
			EsTools.bulkDeleteList(esClient, Indices.FreeProtocolIndex, (ArrayList<String>) idList);
			TimeUnit.SECONDS.sleep(2);
			
			ArrayList<FreeProtocolHistory> reparseFreeProtocolList = getReparseHistList(esClient,Indices.FreeProtocolHistIndex,idList,"pid",FreeProtocolHistory.class);
			
			for(FreeProtocolHistory idHist: reparseFreeProtocolList) {
				new ConstructParser().parseFreeProtocol(esClient, idHist);
			}
			break;
		case Indices.CodeIndex:
			EsTools.bulkDeleteList(esClient, Indices.CodeIndex, (ArrayList<String>) idList);
			TimeUnit.SECONDS.sleep(2);
			
			ArrayList<CodeHistory> reparseCodeList = getReparseHistList(esClient,Indices.CodeHistIndex,idList,"coid",CodeHistory.class);
			
			for(CodeHistory idHist: reparseCodeList) {
				new ConstructParser().parseCode(esClient, idHist);
			}
			break;
		case Indices.AppIndex:
			EsTools.bulkDeleteList(esClient, Indices.AppIndex, (ArrayList<String>) idList);
			TimeUnit.SECONDS.sleep(2);
			
			ArrayList<AppHistory> reparseAppList = getReparseHistList(esClient,Indices.AppHistIndex,idList,"aid",AppHistory.class);
			
			for(AppHistory idHist: reparseAppList) {
				new ConstructParser().parseApp(esClient, idHist);
			}
			break;
		case Indices.ServiceIndex:
			EsTools.bulkDeleteList(esClient, Indices.ServiceIndex, (ArrayList<String>) idList);
			TimeUnit.SECONDS.sleep(2);
			ArrayList<ServiceHistory> reparseServiceList = getReparseHistList(esClient,Indices.ServiceHistIndex,idList,"sid",ServiceHistory.class);
			
			for(ServiceHistory idHist: reparseServiceList) {
				new ConstructParser().parseService(esClient, idHist);
			}
			break;
		case Indices.GroupIndex:
			EsTools.bulkDeleteList(esClient, Indices.GroupIndex, (ArrayList<String>) idList);
			TimeUnit.SECONDS.sleep(2);
			ArrayList<GroupHistory> reparseGroupList = getReparseHistList(esClient,Indices.GroupHistIndex,idList,"gid",GroupHistory.class);
			
			for(GroupHistory idHist: reparseGroupList) {
				new OrganizationParser().parseGroup(esClient, idHist);
			}
			break;
		case Indices.TeamIndex:
			EsTools.bulkDeleteList(esClient, Indices.TeamIndex, (ArrayList<String>) idList);
			TimeUnit.SECONDS.sleep(2);
			ArrayList<TeamHistory> reparseTeamList = getReparseHistList(esClient,Indices.TeamHistIndex,idList,"tid",TeamHistory.class);
			
			for(TeamHistory idHist: reparseTeamList) {
				new OrganizationParser().parseTeam(esClient, idHist);
			}
			break;
		default:
			break;
		}
	}

	private <T>ArrayList<T> getReparseHistList(ElasticsearchClient esClient, String histIndex,
			List<String> idList, String idField, Class<T> clazz) 
					throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		List<FieldValue> fieldValueList = new ArrayList<FieldValue>();
		for(String id:idList) {
			fieldValueList.add(FieldValue.of(id));
		}
		
		SearchResponse<T> result = esClient.search(s->s
				.index(histIndex)
				.query(q->q
						.terms(t->t
								.field(idField)
								.terms(t1->t1.value(fieldValueList))))
				, clazz);
		if(result.hits().total().value()==0)return null;
		List<Hit<T>> hitList = result.hits().hits();
		ArrayList <T> reparseList = new ArrayList<T>();
		for(Hit<T> hit:hitList) {
			reparseList.add(hit.source());
		}
		return reparseList;
	}
}
