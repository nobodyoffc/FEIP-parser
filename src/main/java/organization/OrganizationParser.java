package organization;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import esClient.EsTools;
import identity.Cid;
import opReturn.Feip;
import opReturn.OpReturn;
import start.Indices;
import start.Start;
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
			if(opre.getHeight()>Start.CddCheckHeight && opre.getCdd()<Start.CddRequired*100)return null;
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
			if(opre.getHeight()>Start.CddCheckHeight && opre.getCdd()<Start.CddRequired)return null;
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
				
				group.setCddToUpdate(groupHist.getCdd()+1);
				group.settCdd(group.gettCdd()+groupHist.getCdd());
				
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
			
			group.settCdd(group.gettCdd()+groupHist.getCdd());
			
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
			
			if(groupHist.getCdd() < group.getCddToUpdate())return isValid;
			
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
			
			Set<String> namerSet = new HashSet<String>();
			for(String namer: group.getNamers()) {
				namerSet.add(namer);
			}
			namerSet.add(groupHist.getSigner());
			String[] namers = namerSet.toArray(new String[namerSet.size()]);
			
			group.setNamers(namers);
			
			group.setLastTxid(groupHist.getId());
			group.setLastTime(groupHist.getTime());
			group.setLastHeight(groupHist.getHeight());
			
			group.settCdd(group.gettCdd()+groupHist.getCdd());
			
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
			
			group.settCdd(group.gettCdd()+groupHist.getCdd());
			
			organization.Group group4 = group;
			
			esClient.index(i->i.index(Indices.GroupIndex).id(groupHist.getGid()).document(group4));

			isValid = true;
			break;

		}
		return isValid;
	}

	public TeamHistory makeTeam(OpReturn opre, Feip feip) throws InterruptedException {
		// TODO Auto-generated method stub
		if(opre.getHeight()>Start.CddCheckHeight && opre.getCdd()<Start.CddRequired)return null;
		
		Gson gson = new Gson();
		
		TeamRaw teamRaw = new TeamRaw();
		
		try {
			teamRaw = gson.fromJson(gson.toJson(feip.getData()),TeamRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return null;
		}
		
		ParseTools.gsonPrint(teamRaw);
		
		TeamHistory teamHist = new TeamHistory();
		
		if(teamRaw.getOp()==null)return null;
		teamHist.setOp(teamRaw.getOp());

		switch(teamRaw.getOp()) {
		
		case "create":
			if(teamRaw.getStdName()==null)	return null;
			if(teamRaw.getTid()!=null) return null;
			if(opre.getHeight()>Start.CddCheckHeight && opre.getCdd()<Start.CddRequired*100)return null;
			teamHist.setId(opre.getId());
			teamHist.setTid(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());

			teamHist.setStdName(teamRaw.getStdName());
			if(teamRaw.getLocalNames()!=null)teamHist.setLocalNames(teamRaw.getLocalNames());
			if(teamRaw.getDesc()!=null)teamHist.setDesc(teamRaw.getDesc());
			if(teamRaw.getConsensusHash()!=null)teamHist.setConsensusHash(teamRaw.getConsensusHash());

			break;	
			
		case "disband":
			if(teamRaw.getTid()==null)return null;
			teamHist.setTid(teamRaw.getTid());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "transfer":
			if(teamRaw.getTid()==null)return null;
			if(teamRaw.getTransferee() ==null)return null;
			if(!teamRaw.getConfirm().equals("I transfer the team to the transferee."))return null;
			teamHist.setTid(teamRaw.getTid());
			teamHist.setTransferee(teamRaw.getTransferee());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "take over":
			if(teamRaw.getTid()==null)return null;
			if(!teamRaw.getConfirm().equals("I take over the team and agree with the team consensus."))return null;
			teamHist.setTid(teamRaw.getTid());
			if(teamRaw.getConsensusHash()!=null)teamHist.setConsensusHash(teamRaw.getConsensusHash());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
			
		case "update":
			if(teamRaw.getTid()==null)	return null;
			if(teamRaw.getStdName()==null)	return null;

			teamHist.setTid(teamRaw.getTid());
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());

			teamHist.setStdName(teamRaw.getStdName());
			if(teamRaw.getLocalNames()!=null)teamHist.setLocalNames(teamRaw.getLocalNames());
			if(teamRaw.getDesc()!=null)teamHist.setDesc(teamRaw.getDesc());
			if(teamRaw.getConsensusHash()!=null)teamHist.setConsensusHash(teamRaw.getConsensusHash());

			break;
		case "agree consensus":
			if(teamRaw.getTid()==null)return null;
			if(!teamRaw.getConfirm().equals("I agree with the new consensus."))return null;
			teamHist.setTid(teamRaw.getTid());
			if(teamRaw.getConsensusHash()!=null)
				teamHist.setConsensusHash(teamRaw.getConsensusHash());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "invite":
			if(teamRaw.getTid()==null)return null;
			if(teamRaw.getList()==null)return null;
			if(teamRaw.getList().length==0)return null;
			teamHist.setTid(teamRaw.getTid());
			teamHist.setList(teamRaw.getList());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "withdraw invitation":
			if(teamRaw.getTid()==null)return null;
			if(teamRaw.getList()==null)return null;
			if(teamRaw.getList().length==0)return null;
			teamHist.setTid(teamRaw.getTid());
			teamHist.setList(teamRaw.getList());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "join":
			if(teamRaw.getTid()==null)return null;
			if(!teamRaw.getConfirm().equals("I join the team and agree with the team consensus."))return null;
			teamHist.setTid(teamRaw.getTid());
			if(teamRaw.getConsensusHash()!=null)teamHist.setConsensusHash(teamRaw.getConsensusHash());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "leave":
			if(teamRaw.getTid()==null)return null;
			teamHist.setTid(teamRaw.getTid());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "dismiss":
			if(teamRaw.getTid()==null)return null;
			if(teamRaw.getList()==null)return null;
			if(teamRaw.getList().length==0)return null;
			teamHist.setTid(teamRaw.getTid());
			teamHist.setList(teamRaw.getList());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "appoint":
			if(teamRaw.getTid()==null)return null;
			if(teamRaw.getList()==null)return null;
			if(teamRaw.getList().length==0)return null;
			teamHist.setTid(teamRaw.getTid());
			teamHist.setList(teamRaw.getList());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "cancel appointment":
			if(teamRaw.getTid()==null)return null;
			if(teamRaw.getList()==null)return null;
			if(teamRaw.getList().length==0)return null;
			teamHist.setTid(teamRaw.getTid());
			teamHist.setList(teamRaw.getList());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		case "rate":
			if(teamRaw.getTid()==null)return null;
			if(teamRaw.getRate()<0 ||teamRaw.getRate()>5)return null;
			if(opre.getCdd()<Start.CddRequired)return null;
			teamHist.setTid(teamRaw.getTid());
			teamHist.setRate(teamRaw.getRate());
			teamHist.setCdd(opre.getCdd());
			
			teamHist.setId(opre.getId());
			teamHist.setHeight(opre.getHeight());
			teamHist.setIndex(opre.getTxIndex());
			teamHist.setTime(opre.getTime());
			teamHist.setSigner(opre.getSigner());
			break;
		default:
			return null;
		}
		return teamHist; 
	}

	public boolean parseTeam(ElasticsearchClient esClient, TeamHistory teamHist) throws ElasticsearchException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		Team team;
		boolean found = false;
		switch(teamHist.getOp()) {
		case "create":
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);		
			if(team==null) {
				team = new Team();
				team.setTid(teamHist.getId());
				team.setOwner(teamHist.getSigner());
				team.setStdName(teamHist.getStdName());
				if(teamHist.getLocalNames()!=null)team.setLocalNames(teamHist.getLocalNames());
				if(teamHist.getConsensusHash() !=null)team.setConsensusHash(teamHist.getConsensusHash());
				if(teamHist.getDesc() !=null)team.setDesc(teamHist.getDesc());

				String[] activeMembers = new String[1];
				activeMembers[0]=teamHist.getSigner();
				team.setActiveMembers(activeMembers);
				
				String[] administrators = new String[1];
				administrators[0]=teamHist.getSigner();
				team.setAdministrators(administrators);
				
				team.setBirthTime(teamHist.getTime());
				team.setBirthHeight(teamHist.getHeight());
				
				team.setLastTxid(teamHist.getId());
				team.setLastTime(teamHist.getTime());
				team.setLastHeight(teamHist.getHeight());
				
				team.setActive(true);
				
				Team team1=team;
				esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team1));
				isValid = true;
			}else {
				isValid = false;
			}
			break;
			
		case "disband":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.getOwner().equals(teamHist.getSigner())) {
				isValid = false;
				break;
			}
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			team.setLastTxid(teamHist.getId());
			team.setLastTime(teamHist.getTime());
			team.setLastHeight(teamHist.getHeight());
			team.setActive(false);
			
			Team team2 = team;
			esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team2));
			isValid = true;

			break;
			
		case "transfer":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(! team.getOwner().equals(teamHist.getSigner())) {
				Cid resultCid = EsTools.getById(esClient, Indices.CidIndex, teamHist.getSigner(), Cid.class);
				if(resultCid.getMaster()!=null) {
					if(! resultCid.getMaster().equals(teamHist.getSigner())) {
					isValid = false;
					break;
					}
				}else {
					isValid = false;
					break;
				}
			}
			
			if(teamHist.getTransferee().equals(team.getOwner())) {
				team.setTransferee(null);
			}else team.setTransferee(teamHist.getTransferee());
			
			team.setLastTxid(teamHist.getId());
			team.setLastTime(teamHist.getTime());
			team.setLastHeight(teamHist.getHeight());
					
			Team team3 = team;

			esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team3));
			isValid = true;

			break;
			
		case "take over":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(team.getTransferee()==null) {
				isValid = false;
				break;
			}
			
			if(teamHist.getConsensusHash()!=null) {
				if(!teamHist.getConsensusHash().equals(team.getConsensusHash())){
					isValid = false;
					break;
				}
			}
			
			String taker = teamHist.getSigner();
			
				if(team.getTransferee().equals(taker)) {
					
					team.setOwner(taker);
					
					Set<String> activeMemberSet = new HashSet<String>();
					for(String member:team.getActiveMembers()) {
						activeMemberSet.add(member);
					}
					activeMemberSet.add(taker);
					String[] activeMembers = activeMemberSet.toArray(new String[activeMemberSet.size()]);
					team.setActiveMembers(activeMembers);
					
					Set<String> administratorSet = new HashSet<String>();
					for(String member:team.getAdministrators()) {
						administratorSet.add(member);
					}
					administratorSet.add(taker);
					String[] administrators = administratorSet.toArray(new String[administratorSet.size()]);
					team.setAdministrators(administrators);
					
					team.setTransferee(null);
					
					Team team4 = team;

					esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team4));
					isValid = true;
					break;
			}
			break;
			
		case "update":	
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.getOwner().equals(teamHist.getSigner())) {
				isValid = false;
				break;
			}
			
			if(!team.isActive()) {
				isValid = false;
				break;
			}
				
			team.setStdName(teamHist.getStdName());
			if(teamHist.getLocalNames() !=null) team.setLocalNames(teamHist.getLocalNames());
			if(teamHist.getDesc() !=null) team.setDesc(teamHist.getDesc());
			
			if(teamHist.getConsensusHash() !=null) {
				if(team.getConsensusHash()!=null) {
					if(! team.getConsensusHash().equals(teamHist.getConsensusHash())) {
						team.setConsensusHash(teamHist.getConsensusHash());
						
						Set<String>memberSet = new HashSet<String>();
						for(String m:team.getActiveMembers()) {
							if(m.equals(team.getOwner()))continue;
							memberSet.add(m);
						}
						team.setNotAgreeMembers(memberSet.toArray(new String[memberSet.size()]));
					}
				}else {
					team.setConsensusHash(teamHist.getConsensusHash());
					
					Set<String>memberSet = new HashSet<String>();
					for(String m:team.getActiveMembers()) {
						if(m.equals(team.getOwner()))continue;
						memberSet.add(m);
					}
					team.setNotAgreeMembers(memberSet.toArray(new String[memberSet.size()]));
				}
			}
			
			
			if(team.getConsensusHash() !=null) {
				if(! team.getConsensusHash().equals(teamHist.getConsensusHash())) {
					team.setNotAgreeMembers(team.getActiveMembers());
				}
			}
			
			team.setLastTxid(teamHist.getId());
			team.setLastTime(teamHist.getTime());
			team.setLastHeight(teamHist.getHeight());
			
			Team team5 = team;
			
			esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team5));
			isValid = true;
			break;
			
		case "agree consensus":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(!teamHist.getConsensusHash().equals(team.getConsensusHash())){
				isValid = false;
				break;
			}
			
			found = false;
			String agreer = teamHist.getSigner();
			
			Set<String>notAgreeSet = new HashSet<String>();
			if(team.getNotAgreeMembers()!=null) {
				for(String member:team.getNotAgreeMembers()) {
					if(member.equals(agreer)) {
						found = true;
					}else{
						notAgreeSet.add(member);
					}
				}
			}
			if(found) {
				String[] notAgreeMembers = notAgreeSet.toArray(new String[notAgreeSet.size()]);
				
				if(notAgreeMembers.length==0) {
					team.setNotAgreeMembers(null);
				}else team.setNotAgreeMembers(notAgreeMembers);
				
				team.setLastTxid(teamHist.getId());
				team.setLastTime(teamHist.getTime());
				team.setLastHeight(teamHist.getHeight());
				
				Team team6 = team;

				esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team6));
				isValid = true;
				break;
			}else {
				isValid = false;
				break;
			}
			
		case "invite":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(team.getAdministrators()!=null) {
				for(String admin:team.getAdministrators()) {
					if(admin.equals(teamHist.getSigner())) {
						
						Set<String> inviteeSet = new HashSet<String>();
						if(team.getInvitees()!=null) {
							for(String invitee:team.getInvitees()) {
								inviteeSet.add(invitee);
							}
						}
						for(String invitee:teamHist.getList()) {
							if(invitee.equals(team.getOwner()))continue;
							inviteeSet.add(invitee);
						}
						String[] invitees = inviteeSet.toArray(new String[inviteeSet.size()]);
						team.setInvitees(invitees);
						team.setLastTxid(teamHist.getId());
						team.setLastTime(teamHist.getTime());
						team.setLastHeight(teamHist.getHeight());
						
						Team team7 = team;
	
						esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team7));
						isValid = true;
						break;
					}
				}
			}
			break;
			
		case "withdraw invitation":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(team.getAdministrators()!=null) {
				for(String admin:team.getAdministrators()) {
					if(admin.equals(teamHist.getSigner())) {
						
						Set<String> inviteeSet = new HashSet<String>();
						for(String invitee:team.getInvitees()) {
							inviteeSet.add(invitee);
						}
						for(String invitee:teamHist.getList()) {
							inviteeSet.remove(invitee);
						}
						String[] invitees = inviteeSet.toArray(new String[inviteeSet.size()]);
						team.setInvitees(invitees);
						team.setLastTxid(teamHist.getId());
						team.setLastTime(teamHist.getTime());
						team.setLastHeight(teamHist.getHeight());
						
						Team team7 = team;
	
						esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team7));
						isValid = true;
						break;
					}
				}
			}
			break;

		case "join":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(!teamHist.getConsensusHash().equals(team.getConsensusHash())){
				isValid = false;
				break;
			}
			if(team.getInvitees()!=null) {
				for(String invitee:team.getInvitees()) {
					if(invitee.equals(teamHist.getSigner())) {
						
						Set<String> activeMemberSet = new HashSet<String>();
						for(String activeMember:team.getActiveMembers()) {
							activeMemberSet.add(activeMember);
						}
						activeMemberSet.add(teamHist.getSigner());
						String[] activeMembers = activeMemberSet.toArray(new String[activeMemberSet.size()]);
						
						Set<String>leftMemberSet = new HashSet<String>();
						
						if(team.getLeftMembers()!=null) {
							for(String leftMember:team.getLeftMembers()) {
								if(!leftMember.equals(teamHist.getSigner())) {
									leftMemberSet.add(leftMember);
								}
							}
							String[] leftMembers = leftMemberSet.toArray(new String[leftMemberSet.size()]);
							team.setLeftMembers(leftMembers);
						}
						
						Set<String> inviteeSet = new HashSet<String>();
						for(String invite: team.getInvitees()) {
							if(!invite.equals(teamHist.getSigner())) inviteeSet.add(invite);
						}
						
						if(inviteeSet.size()==0) {
							team.setInvitees(null);
						}else {
							String[] invitees = inviteeSet.toArray(new String[inviteeSet.size()]);
							team.setInvitees(invitees);
						}
						team.setActiveMembers(activeMembers);
						team.setLastTxid(teamHist.getId());
						team.setLastTime(teamHist.getTime());
						team.setLastHeight(teamHist.getHeight());
						
						Team team7 = team;
	
						esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team7));
						isValid = true;
						break;
					}
				}
			}
			break;
			
		case "leave":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(team.getOwner().equals(teamHist.getSigner())) {
				isValid = false;
				break;
			}
			
			found = false;
			Set<String> activeMemberSet = new HashSet<String>();
			for(String activeMember:team.getActiveMembers()) {
				if(activeMember.equals(teamHist.getSigner())) {
					found = true;
				}else {
					activeMemberSet.add(activeMember);
				}
			}
			if(found) {
				String[] activeMembers = activeMemberSet.toArray(new String[activeMemberSet.size()]);
				team.setActiveMembers(activeMembers);
				
				Set<String> leaverSet = new HashSet<String>();
				if(team.getLeavers()!=null) {
					for(String leaver:team.getLeavers()) {
						leaverSet.add(leaver);
					}
					leaverSet.add(teamHist.getSigner());
					String[] leavers = leaverSet.toArray(new String[leaverSet.size()]);
					team.setLeavers(leavers);
				}
				if(team.getAdministrators()!=null) {
					Set<String> administratorSet = new HashSet<String>();
					for(String administrator:team.getAdministrators()) {
						if(!administrator.equals(teamHist.getSigner())) {
							administratorSet.add(administrator);
						}
					}
					String[] administrators = administratorSet.toArray(new String[administratorSet.size()]);
					team.setAdministrators(administrators);
				}
				team.setLastTxid(teamHist.getId());
				team.setLastTime(teamHist.getTime());
				team.setLastHeight(teamHist.getHeight());
				
				Team team7 = team;

				esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team7));
				isValid = true;
				break;
			}
			break;
			
		case "dismiss":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			for(String admin:team.getAdministrators()) {
				if(admin.equals(teamHist.getSigner())) {
					
					Set<String> activeMemberSet1 = new HashSet<String>();
					if(team.getActiveMembers()!=null) {
						for(String activeMember:team.getActiveMembers()) {
							activeMemberSet1.add(activeMember);
						}
					}
					
					Set<String>leftMemberSet = new HashSet<String>();
					if(team.getLeftMembers()!=null) {
						for(String leftMember:team.getLeftMembers()) {
							leftMemberSet.add(leftMember);
						}
					}
					
					Set<String>administratorSet = new HashSet<String>();
					if(team.getAdministrators()!=null) {
						for(String administrator:team.getAdministrators()) {
							administratorSet.add(administrator);
						}
					}
					
					Set<String>leaverSet = new HashSet<String>();
					if(team.getLeavers()!=null) {
						for(String leaver:team.getLeavers()) {
							leaverSet.add(leaver);
						}
					}
				
					for(String leaver:teamHist.getList()) {
						if(leaver.equals(team.getOwner()))continue;
						if(!activeMemberSet1.contains(leaver))continue;
						leftMemberSet.add(leaver);
						administratorSet.remove(leaver);
						activeMemberSet1.remove(leaver);
						leaverSet.remove(leaver);
					}
					
					String[] activeMembers = activeMemberSet1.toArray(new String[activeMemberSet1.size()]);
					team.setActiveMembers(activeMembers);
					
					if(leftMemberSet.size()==0) {
						team.setLeftMembers(null);
					}else {
						String[] leftMembers = leftMemberSet.toArray(new String[leftMemberSet.size()]);
						team.setLeftMembers(leftMembers);
					}
					
					if(leaverSet.size()==0) {
						team.setLeavers(null);
					}else {
						String[] leavers = leaverSet.toArray(new String[leaverSet.size()]);
						team.setLeavers(leavers);
					}
					if(administratorSet.size()==0) {
						team.setAdministrators(null);
					}else {
						String[] administrators = administratorSet.toArray(new String[administratorSet.size()]);
						team.setAdministrators(administrators);
					}
					
					team.setLastTxid(teamHist.getId());
					team.setLastTime(teamHist.getTime());
					team.setLastHeight(teamHist.getHeight());
					
					Team team7 = team;

					esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team7));
					isValid = true;
					break;
				}
			}
			break;
			
		case "appoint":
			
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(! team.getOwner().equals(teamHist.getSigner())) {
				isValid = false;
				break;
			}
			
			
			Set<String> activeMemberSet1 = new HashSet<String>();
			for(String member:team.getActiveMembers()) {
				activeMemberSet1.add(member);
			}
				
			Set<String> administratorSet = new HashSet<String>();
			if(team.getAdministrators()!=null) 
				for(String administrator:team.getAdministrators()) {
					administratorSet.add(administrator);
				}
			
			for(String member:teamHist.getList()) {
				if(member.equals(team.getOwner()))continue;
				if(activeMemberSet1.contains(member) ) {
					administratorSet.add(member);
				}
			}
			
			String[] administrators = administratorSet.toArray(new String[administratorSet.size()]);
			
			team.setAdministrators(administrators);
			team.setLastTxid(teamHist.getId());
			team.setLastTime(teamHist.getTime());
			team.setLastHeight(teamHist.getHeight());
			
			Team team7 = team;

			esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team7));
			isValid = true;
			break;
			
		case "cancel appointment":
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(! team.isActive()) {
				isValid = false;
				break;
			}
			
			if(! team.getOwner().equals(teamHist.getSigner())) {
				isValid = false;
				break;
			}
			
			Set<String> activeMemberSet2 = new HashSet<String>();
			for(String member:team.getActiveMembers()) {
				activeMemberSet2.add(member);
			}
					
			Set<String> administratorSet1 = new HashSet<String>();
			if(team.getAdministrators()!=null) 
				for(String administrator:team.getAdministrators()) {
					administratorSet1.add(administrator);
				}
			
			for(String member:teamHist.getList()) {
				if(member.equals(team.getOwner()))continue;
				administratorSet1.remove(member);
			}
			
			String[] administrators1 = administratorSet1.toArray(new String[administratorSet1.size()]);
			
			team.setAdministrators(administrators1);
			team.setLastTxid(teamHist.getId());
			team.setLastTime(teamHist.getTime());
			team.setLastHeight(teamHist.getHeight());
			
			Team team8 = team;

			esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team8));
			isValid = true;
			break;
			
		case "rate":
			team = EsTools.getById(esClient, Indices.TeamIndex, teamHist.getTid(), Team.class);
			
			if(team==null) {
				isValid = false;
				break;
			}
			
			if(team.getOwner().equals(teamHist.getSigner())) {
				isValid = false;
				break;
			}

			if(team.gettCdd()+teamHist.getCdd()==0) {
				team.settRate(0);
			}else {
				team.settRate(
						(team.gettRate()*team.gettCdd()+teamHist.getRate()*teamHist.getCdd())
						/(team.gettCdd()+teamHist.getCdd())
						);
			}
			team.settCdd(team.gettCdd()+teamHist.getCdd());
			
			team.setLastTxid(teamHist.getId());
			team.setLastTime(teamHist.getTime());
			team.setLastHeight(teamHist.getHeight());
			
			Team team9 = team;
			
			esClient.index(i->i.index(Indices.TeamIndex).id(teamHist.getTid()).document(team9));
			isValid = true;
			break;
		}
		return isValid;
	}

}
