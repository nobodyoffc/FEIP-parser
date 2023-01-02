package personal;

import java.io.IOException;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.GetResponse;
import opReturn.Feip;
import opReturn.OpReturn;
import start.Indices;

public class PersonalParser {

	public boolean parseContacts(ElasticsearchClient esClient, OpReturn opre, Feip feip) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		
		Gson gson = new Gson();
		
		ContactsRaw contactsRaw = new ContactsRaw();
		
		try {
			contactsRaw = gson.fromJson(gson.toJson(feip.getData()),ContactsRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return isValid;
		}
		
		Contacts contacts = new Contacts();
		
		long height;
		switch(contactsRaw.getOp()) {
		
		case "add":
			contacts.setAddTxid(opre.getId());
			
			if(contactsRaw.getAlg()!=null) {
				contacts.setAlg(contactsRaw.getAlg());
			}
			contacts.setCiphertext(contactsRaw.getCiphertext());
			
			contacts.setOwner(opre.getSigner());
			contacts.setBirthTime(opre.getTime());
			contacts.setBirthHeight(opre.getHeight());
			contacts.setLastHeight(opre.getHeight());
			contacts.setActive(true);
			
			Contacts contacts1 = contacts;
			esClient.index(i->i.index(Indices.ContactsIndex).id(contacts1.getAddTxid()).document(contacts1));
			isValid = true;
			break;
		case "delete":
			if(contactsRaw.getAddTxid() ==null)return isValid;
			height = opre.getHeight();
			ContactsRaw contactsRaw1 = contactsRaw;
			
			GetResponse<Contacts> result = esClient.get(g->g.index(Indices.ContactsIndex).id(contactsRaw1.getAddTxid()), Contacts.class);
			
			if(!result.found())return isValid;

			contacts = result.source();
			
			if(!contacts.getOwner().equals(opre.getSigner()))return isValid;
			
			contacts.setActive(false);
			contacts.setLastHeight(height);
			
			Contacts contacts2 = contacts;
			esClient.index(i->i.index(Indices.ContactsIndex).id(contacts2.getAddTxid()).document(contacts2));
			
			isValid = true;
			break;
		case "recover":
			if(contactsRaw.getAddTxid() ==null)return isValid;
			height = opre.getHeight();
			
			ContactsRaw contactsRaw2 = contactsRaw;
			
			GetResponse<Contacts> result1 = esClient.get(g->g.index(Indices.ContactsIndex).id(contactsRaw2.getAddTxid()), Contacts.class);
			
			if(!result1.found())return isValid;

			contacts = result1.source();
			
			if(!contacts.getOwner().equals(opre.getSigner()))return isValid;
			
			contacts.setActive(true);
			contacts.setLastHeight(height);
			
			Contacts contacts3 = contacts;
			esClient.index(i->i.index(Indices.ContactsIndex).id(contacts3.getAddTxid()).document(contacts3));
			
			isValid = true;
			break;
		default:
			break;
		}
		return isValid;
	}

	public boolean parseMail(ElasticsearchClient esClient, OpReturn opre, Feip feip) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		
		Gson gson = new Gson();
		
		MailRaw mailRaw = new MailRaw();
		
		try {
			mailRaw = gson.fromJson(gson.toJson(feip.getData()),MailRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return isValid;
		}
		
		Mail mail = new Mail();
		
		long height;
		if(mailRaw.getOp()==null && mailRaw.getMsg()==null)return isValid;
		
		if(mailRaw.getMsg()!=null) {
			mail.setAlg(mailRaw.getAlg());
			mail.setCiphertextReci(mailRaw.getMsg());
			
			mail.setSender(opre.getSigner());
			mail.setRecipient(opre.getRecipient());
			mail.setBirthTime(opre.getTime());
			mail.setBirthHeight(opre.getHeight());
			mail.setLastHeight(opre.getHeight());
			mail.setActive(true);
			
			Mail mail1 = mail;
			esClient.index(i->i.index(Indices.MailIndex).id(mail1.getSendTxid()).document(mail1));
			
			isValid = true;
			return isValid;
		}
		if(mailRaw.getOp()!=null) {
			switch(mailRaw.getOp()) {
			case "send":
				mail.setSendTxid(opre.getId());
				
				if(mailRaw.getAlg()!=null) {
				mail.setAlg(mailRaw.getAlg());
				}
				
				if(mailRaw.getCiphertextSend()!=null) {
				mail.setCiphertextSend(mailRaw.getCiphertextSend());
				}
				
				if(mailRaw.getCiphertextReci()!=null) {
					mail.setCiphertextReci(mailRaw.getCiphertextReci());
				}else return isValid;
				
				if(mailRaw.getTextHash()!=null) {
					mail.setTextHash(mailRaw.getTextHash());
				}
				
				mail.setSender(opre.getSigner());
				mail.setRecipient(opre.getRecipient());
				mail.setBirthTime(opre.getTime());
				mail.setBirthHeight(opre.getHeight());
				mail.setLastHeight(opre.getHeight());
				mail.setActive(true);
				
				Mail mail0 = mail;
				esClient.index(i->i.index(Indices.MailIndex).id(mail0.getSendTxid()).document(mail0));
				isValid = true;
				break;
			case "delete":
				if(mailRaw.getSendTxid() ==null)return isValid;
				height = opre.getHeight();
				MailRaw mailRaw1 = mailRaw;
				
				GetResponse<Mail> result = esClient.get(g->g.index(Indices.MailIndex).id(mailRaw1.getSendTxid()), Mail.class);
				
				if(!result.found())return isValid;

				mail = result.source();
				
				if(!mail.getRecipient().equals(opre.getSigner()))return isValid;
				
				mail.setActive(false);
				mail.setLastHeight(height);
				
				Mail mail2 = mail;
				esClient.index(i->i.index(Indices.MailIndex).id(mail2.getSendTxid()).document(mail2));
				
				isValid = true;
				break;
			case "recover":
				if(mailRaw.getSendTxid() ==null)return isValid;
				height = opre.getHeight();
				MailRaw mailRaw2 = mailRaw;
				
				GetResponse<Mail> result1 = esClient.get(g->g.index(Indices.MailIndex).id(mailRaw2.getSendTxid()), Mail.class);
				
				if(!result1.found())return isValid;

				mail = result1.source();
				
				if(!mail.getRecipient().equals(opre.getSigner()))return isValid;
				
				mail.setActive(true);
				mail.setLastHeight(height);
				
				Mail mail3 = mail;
				esClient.index(i->i.index(Indices.MailIndex).id(mail3.getSendTxid()).document(mail3));
				
				isValid = true;
				break;
			default:
				break;
			}
		}
		return isValid;
	}

	public boolean parseSafe(ElasticsearchClient esClient, OpReturn opre, Feip feip) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		
		Gson gson = new Gson();
		
		SafeRaw safeRaw = new SafeRaw();
		
		try {
			safeRaw = gson.fromJson(gson.toJson(feip.getData()),SafeRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return isValid;
		}
		
		Safe safe = new Safe();
		
		long height;
		switch(safeRaw.getOp()) {
		
		case "add":
			safe.setAddTxid(opre.getId());
			
			if(safeRaw.getAlg()!=null) {
				safe.setAlg(safeRaw.getAlg());
			}
			
			if(safeRaw.getCiphertext()!=null) {
				safe.setCiphertext(safeRaw.getCiphertext());
			}else if(safeRaw.getMsg()!=null) {
				safe.setCiphertext(safeRaw.getMsg());
			}else return isValid;
			
			safe.setOwner(opre.getSigner());
			safe.setBirthTime(opre.getTime());
			safe.setBirthHeight(opre.getHeight());
			safe.setLastHeight(opre.getHeight());
			safe.setActive(true);
			
			Safe safe0 = safe;
			
			esClient.index(i->i.index(Indices.SafeIndex).id(safe0.getAddTxid()).document(safe0));
			isValid = true;
			break;
			
		case "delete":
			if(safeRaw.getAddTxid() ==null)return isValid;
			height = opre.getHeight();
			SafeRaw safeRaw1 = safeRaw;
			
			GetResponse<Safe> result = esClient.get(g->g.index(Indices.SafeIndex).id(safeRaw1.getAddTxid()), Safe.class);
			
			if(!result.found())return isValid;

			safe = result.source();
			
			if(!safe.getOwner().equals(opre.getSigner()))return isValid;
			
			safe.setActive(false);
			safe.setLastHeight(height);
			
			Safe safe2 = safe;
			esClient.index(i->i.index(Indices.SafeIndex).id(safe2.getAddTxid()).document(safe2));
			
			isValid = true;
			break;
		case "recover":
			if(safeRaw.getAddTxid() ==null)return isValid;
			height = opre.getHeight();
			SafeRaw safeRaw2 = safeRaw;
			
			GetResponse<Safe> result1 = esClient.get(g->g.index(Indices.SafeIndex).id(safeRaw2.getAddTxid()), Safe.class);
			
			if(!result1.found())return isValid;

			safe = result1.source();
			
			if(!safe.getOwner().equals(opre.getSigner()))return isValid;
			
			safe.setActive(true);
			safe.setLastHeight(height);
			
			Safe safe3 = safe;
			esClient.index(i->i.index(Indices.SafeIndex).id(safe3.getAddTxid()).document(safe3));
			
			isValid = true;
			break;
		default:
			break;
		}
		return isValid;
	}

	public boolean parseStatement(ElasticsearchClient esClient, OpReturn opre, Feip feip) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		boolean isValid = false;
		
		Gson gson = new Gson();
		
		StatementRaw statementRaw = new StatementRaw();
		
		try {
			statementRaw = gson.fromJson(gson.toJson(feip.getData()),StatementRaw.class);
		}catch(com.google.gson.JsonSyntaxException e) {
			return isValid;
		}
		
		Statement statement = new Statement();

		statement.setStid(opre.getId());
		
		if(statementRaw.getTitle()==null && statementRaw.getContent()==null)return isValid;
		
		if(statementRaw.getTitle()!=null) {
			statement.setTitle(statementRaw.getTitle());
		}
		
		if(statementRaw.getContent()!=null) {
			statement.setContent(statementRaw.getContent());
		}
		
		statement.setOwner(opre.getSigner());
		statement.setBirthTime(opre.getTime());
		statement.setBirthHeight(opre.getHeight());
		statement.setLastHeight(opre.getHeight());
		statement.setActive(true);
		
		esClient.index(i->i.index(Indices.StatementIndex).id(statement.getStid()).document(statement));
		isValid = true;

		return isValid;
	}
}
