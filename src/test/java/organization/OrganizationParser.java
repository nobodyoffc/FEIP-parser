package organization;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import esClient.EsTools;
import opReturn.Feip;
import opReturn.OpReturn;
import start.Indices;
import tools.ParseTools;

public class OrganizationParser {

	public GroupHistory makeGroup(OpReturn opre, Feip feip) {
		// TODO Auto-generated method stub
Gson gson = new Gson();
		
		GroupRaw groupRaw = new GroupRaw();
		
		try {
			groupRaw = gson.fromJson(gson.toJson(feip.getData()),GroupRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return null;
		}
		
		ParseTools.gsonPrint(groupRaw);
		
		GroupHistory groupHist = new GroupHistory();
		
		if(groupRaw.getOp()==null)return null;
		groupHist.setOp(groupRaw.getOp());

		switch(groupRaw.getOp()) {
		
		case "create":
			if(groupRaw.getName()==null)return null;
			if(groupRaw.getGid()!=null)return null;
			if(groupHist.getHeight()>1550000 && groupHist.getCdd()<100)return null;
			groupHist.setId(opre.getId());
			groupHist.setGid(opre.getId());
			groupHist.setHeight(opre.getHeight());
			groupHist.setIndex(opre.getTxIndex());
			groupHist.setTime(opre.getTime());
			groupHist.setSigner(opre.getSigner());
			groupHist.setCdd(opre.getCdd());

			groupHist.setName(groupRaw.getName());
			if(groupRaw.getDesc()!=null)groupHist.setDesc(groupRaw.getDesc());

			break;	
			
		case "update":
			if(groupRaw.getGid()==null)	return null;
			if(groupRaw.getName()==null)	return null;
			groupHist.setGid(groupRaw.getGid());
			groupHist.setId(opre.getId());
			groupHist.setHeight(opre.getHeight());
			groupHist.setIndex(opre.getTxIndex());
			groupHist.setTime(opre.getTime());
			groupHist.setSigner(opre.getSigner());
			groupHist.setCdd(opre.getCdd());

			groupHist.setName(groupRaw.getName());
			if(groupRaw.getDesc()!=null)groupHist.setDesc(groupRaw.getDesc());

			break;	
			
		case "join":
			if(groupRaw.getGid()==null)return null;
			if(groupHist.getHeight()>1550000 && groupHist.getCdd()<1)return null;
			groupHist.setGid(groupRaw.getGid());
			
			groupHist.setId(opre.getId());
			groupHist.setHeight(opre.getHeight());
			groupHist.setIndex(opre.getTxIndex());
			groupHist.setTime(opre.getTime());
			groupHist.setSigner(opre.getSigner());
			groupHist.setCdd(opre.getCdd());
			break;
		case "leave":
			if(groupRaw.getGid()==null)return null;
			groupHist.setGid(groupRaw.getGid());
			
			groupHist.setId(opre.getId());
			groupHist.setHeight(opre.getHeight());
			groupHist.setIndex(opre.getTxIndex());
			groupHist.setTime(opre.getTime());
			groupHist.setSigner(opre.getSigner());
			
			break;
		default:
			return null;
		}
		return groupHist; 
	}

	public boolean parseGroup(ElasticsearchClient esClient, GroupHistory groupHist) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		Group group;

		switch(groupHist.getOp()) {
		case "create":
			group = EsTools.getById(esClient, Indices.GroupIndex, groupHist.getGid(), Group.class);		
			if(group==null) {
				group = new Group();
				group.setGid(groupHist.getId());
				group.setName(groupHist.getName());
				group.setDesc(groupHist.getDesc());
				
				String[] namers = new String[1];
				String[] activeMembers = new String[1];
				
				namers[0]=groupHist.getSigner();
				activeMembers[0]=groupHist.getSigner();
				
				group.setNamers(namers);
				group.setActiveMembers(activeMembers);
				
				group.setBirthTime(groupHist.getTime());
				group.setBirthHeight(groupHist.getHeight());
				
				group.setLastTxid(groupHist.getId());
				group.setLastTime(groupHist.getTime());
				group.setLastHeight(groupHist.getHeight());
				
				Group group1=group;
				esClient.index(i->i.index(Indices.GroupIndex).id(groupHist.getGid()).document(group1));
				isValid = true;
			}else {
				isValid = false;
			}
			
			break;
			
		case "join":
			
			group = EsTools.getById(esClient, Indices.GroupIndex, groupHist.getGid(), Group.class);
			
			if(group==null) {
				isValid = false;
				break;
			}
			String [] activeMembers = new String[group.getActiveMembers().length+1];
			
			Set<String>memberSet = new HashSet<String>();
			
			for(String member:group.getActiveMembers()) {
				memberSet.add(member);
			}
			memberSet.add(groupHist.getSigner());
			activeMembers = memberSet.toArray(new String[memberSet.size()]);
			
			group.setActiveMembers(activeMembers);
			
			group.setLastTxid(groupHist.getId());
			group.setLastTime(groupHist.getTime());
			group.setLastHeight(groupHist.getHeight());
			
			organization.Group group2 = group;
			
			esClient.index(i->i.index(Indices.GroupIndex).id(groupHist.getGid()).document(group2));

			isValid = true;
			break;
			
		case "update":	
			
			group = EsTools.getById(esClient, Indices.GroupIndex, groupHist.getGid(), Group.class);
			
			if(group==null) {
				isValid = false;
				break;
			}
			
			if(groupHist.getCdd() < group.getRequiredCdd())return isValid;
			
			boolean found =false;
			for(String member:group.getActiveMembers()) {
				if(member.equals(groupHist.getSigner())) {
					found=true;
					break;
				}
			}
			if(!found)return isValid;
				
			group.setName(groupHist.getName());
			group.setDesc(groupHist.getDesc());
			
			String[] namers = new String[group.getNamers().length+1];
			namers[group.getNamers().length]=groupHist.getSigner();
			
			group.setNamers(namers);
			
			group.setLastTxid(groupHist.getId());
			group.setLastTime(groupHist.getTime());
			group.setLastHeight(groupHist.getHeight());
			
			Group group3 = group;
			
			esClient.index(i->i.index(Indices.GroupIndex).id(groupHist.getGid()).document(group3));
			isValid = true;
			break;
			
		case "leave":
			group = EsTools.getById(esClient, Indices.GroupIndex, groupHist.getGid(), Group.class);
			
			if(group==null) {
				isValid = false;
				break;
			}
			String [] activeMembers1 = new String[group.getActiveMembers().length+1];
			
			Set<String>memberSet1 = new HashSet<String>();
			
			boolean found1 =false;
			for(String member:group.getActiveMembers()) {
				if(!member.equals(groupHist.getSigner())) {
					memberSet1.add(member);
				}else found1=true;
			}
			if(!found1)return isValid;
			
			activeMembers1 = memberSet1.toArray(new String[memberSet1.size()]);
			
			group.setActiveMembers(activeMembers1);
			
			group.setLastTxid(groupHist.getId());
			group.setLastTime(groupHist.getTime());
			group.setLastHeight(groupHist.getHeight());
			
			organization.Group group4 = group;
			
			esClient.index(i->i.index(Indices.GroupIndex).id(groupHist.getGid()).document(group4));

			isValid = true;
			break;

		}
		return isValid;
	}

}
