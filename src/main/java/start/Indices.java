package start;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;

public class Indices {
	
	static final Logger log = LoggerFactory.getLogger(Indices.class);
	
	public static final String ParseMark = "parse_mark";
	
	public static final String CidIndex = "cid";
	public static final String CidHistIndex = "cid_history";
	public static final String RepuHistIndex = "reputation_history";

	public static final String FreeProtocolIndex = "protocol";
	public static final String FreeProtocolHistIndex = "protocol_history";
	
	public static final String CodeIndex = "code";
	public static final String CodeHistIndex = "code_history";

	public static final String ServiceIndex = "service";
	public static final String ServiceHistIndex = "service_history";

	public static final String AppIndex = "app";
	public static final String AppHistIndex = "app_history";

	public static final String ContactsIndex = "contacts";

	public static final String MailIndex = "mail";

	public static final String SafeIndex = "safe";
	
	public static final String StatementIndex = "statement";

	public static final String GroupIndex = "group";
	public static final String TeamIndex = "team";
	
	public static final String GroupHistIndex = "group_history";
	public static final String TeamHistIndex = "team_history";

	public static void createAllIndices(ElasticsearchClient esClient) throws ElasticsearchException, IOException {
			
		if(esClient==null) {
			System.out.println("Create a Java client for ES first.");
			return;
		}

		String cidJsonStr = "{\"mappings\":{\"properties\":{\"cid\":{\"type\":\"wildcard\"},\"height\":{\"type\":\"long\"},\"homepage\":{\"type\":\"text\"},\"hot\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"master\":{\"type\":\"wildcard\"},\"noticeFee\":{\"type\":\"long\"},\"reputation\":{\"type\":\"long\"},\"usedCids\":{\"type\":\"wildcard\"}}}}";
		String cidHistJsonStr = "{\"mappings\":{\"properties\":{\"data_name\":{\"type\":\"wildcard\"},\"data_op\":{\"type\":\"wildcard\"},\"height\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"index\":{\"type\":\"short\"},\"noticeFee\":{\"type\":\"long\"},\"signer\":{\"type\":\"wildcard\"},\"sn\":{\"type\":\"short\"},\"time\":{\"type\":\"long\"},\"ver\":{\"type\":\"short\"}}}}";
		String repuHistJsonStr = "{\"mappings\":{\"properties\":{\"cause\":{\"type\":\"text\"},\"height\":{\"type\":\"long\"},\"hot\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"index\":{\"type\":\"short\"},\"ratee\":{\"type\":\"wildcard\"},\"rater\":{\"type\":\"wildcard\"},\"reputation\":{\"type\":\"long\"},\"time\":{\"type\":\"long\"}}}}";
		String parseMarkJsonStr = "{\"mappings\":{\"properties\":{\"fileName\":{\"type\":\"wildcard\"},\"lastHeight\":{\"type\":\"long\"},\"lastId\":{\"type\":\"keyword\"},\"lastIndex\":{\"type\":\"long\"},\"length\":{\"type\":\"short\"},\"pointer\":{\"type\":\"long\"}}}}";		

		String protocolJsonStr = "{\"mappings\":{\"properties\":{\"pid\":{\"type\":\"keyword\"},\"type\":{\"type\":\"wildcard\"},\"sn\":{\"type\":\"wildcard\"},\"ver\":{\"type\":\"wildcard\"},\"name\":{\"type\":\"wildcard\"},\"lang\":{\"type\":\"wildcard\"},\"desc\":{\"type\":\"text\"},\"authors\":{\"type\":\"wildcard\"},\"prePid\":{\"type\":\"keyword\"},\"fileUrls\":{\"type\":\"text\"},\"title\":{\"type\":\"wildcard\"},\"owner\":{\"type\":\"wildcard\"},\"birthTxid\":{\"type\":\"keyword\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastTxid\":{\"type\":\"keyword\"},\"lastTime\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"tCdd\":{\"type\":\"long\"},\"tRate\":{\"type\":\"float\"},\"status\":{\"type\":\"boolean\"}}}}";
		String protocolHistJsonStr = "{\"mappings\":{\"properties\":{\"id\":{\"type\":\"keyword\"},\"height\":{\"type\":\"long\"},\"index\":{\"type\":\"short\"},\"time\":{\"type\":\"long\"},\"type\":{\"type\":\"wildcard\"},\"sn\":{\"type\":\"wildcard\"},\"ver\":{\"type\":\"wildcard\"},\"name\":{\"type\":\"wildcard\"},\"desc\":{\"type\":\"text\"},\"authors\":{\"type\":\"wildcard\"},\"lang\":{\"type\":\"wildcard\"},\"prePid\":{\"type\":\"keyword\"},\"fileUrls\":{\"type\":\"text\"},\"signer\":{\"type\":\"wildcard\"},\"pid\":{\"type\":\"keyword\"},\"op\":{\"type\":\"keyword\"},\"rate\":{\"type\":\"short\"},\"cdd\":{\"type\":\"long\"}}}}";

		
		String codeJsonStr = "{\"mappings\":{\"properties\":{\"coid\":{\"type\":\"keyword\"},\"name\":{\"type\":\"wildcard\"},\"version\":{\"type\":\"wildcard\"},\"hash\":{\"type\":\"keyword\"},\"desc\":{\"type\":\"text\"},\"langs\":{\"type\":\"wildcard\"},\"urls\":{\"type\":\"text\"},\"protocols\":{\"type\":\"keyword\"},\"pubKeyAdmin\":{\"type\":\"keyword\"},\"owner\":{\"type\":\"wildcard\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastTxid\":{\"type\":\"keyword\"},\"lastTime\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"tCdd\":{\"type\":\"long\"},\"tRate\":{\"type\":\"float\"},\"active\":{\"type\":\"boolean\"}}}}";
		String codeHistJsonStr = "{\"mappings\":{\"properties\":{\"id\":{\"type\":\"keyword\"},\"height\":{\"type\":\"long\"},\"index\":{\"type\":\"short\"},\"time\":{\"type\":\"long\"},\"signer\":{\"type\":\"wildcard\"},\"coid\":{\"type\":\"keyword\"},\"op\":{\"type\":\"keyword\"},\"name\":{\"type\":\"wildcard\"},\"version\":{\"type\":\"wildcard\"},\"hash\":{\"type\":\"keyword\"},\"desc\":{\"type\":\"text\"},\"langs\":{\"type\":\"wildcard\"},\"urls\":{\"type\":\"text\"},\"protocols\":{\"type\":\"keyword\"},\"pubKeyAdmin\":{\"type\":\"keyword\"},\"rate\":{\"type\":\"short\"},\"cdd\":{\"type\":\"long\"}}}}";
		
		String serviceJsonStr = "{\"mappings\":{\"properties\":{\"sid\":{\"type\":\"keyword\"},\"stdName\":{\"type\":\"wildcard\"},\"localNames\":{\"type\":\"wildcard\"},\"desc\":{\"type\":\"text\"},\"types\":{\"type\":\"wildcard\"},\"urls\":{\"type\":\"text\"},\"pubKeyAdmin\":{\"type\":\"keyword\"},\"protocols\":{\"type\":\"keyword\"},\"params\":{\"type\":\"object\"},\"owner\":{\"type\":\"wildcard\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastTxid\":{\"type\":\"keyword\"},\"lastTime\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"tCdd\":{\"type\":\"long\"},\"tRate\":{\"type\":\"float\"},\"active\":{\"type\":\"boolean\"}}}}";
		String serviceHistJsonStr = "{\"mappings\":{\"properties\":{\"id\":{\"type\":\"keyword\"},\"height\":{\"type\":\"long\"},\"index\":{\"type\":\"short\"},\"time\":{\"type\":\"long\"},\"signer\":{\"type\":\"wildcard\"},\"stdName\":{\"type\":\"wildcard\"},\"localNames\":{\"type\":\"wildcard\"},\"desc\":{\"type\":\"text\"},\"types\":{\"type\":\"wildcard\"},\"urls\":{\"type\":\"text\"},\"pubKeyAdmin\":{\"type\":\"keyword\"},\"protocols\":{\"type\":\"keyword\"},\"params\":{\"type\":\"object\"},\"sid\":{\"type\":\"keyword\"},\"op\":{\"type\":\"keyword\"},\"rate\":{\"type\":\"short\"},\"cdd\":{\"type\":\"long\"}}}}";

		String appJsonStr = "{\"mappings\":{\"properties\":{\"aid\":{\"type\":\"keyword\"},\"stdName\":{\"type\":\"wildcard\"},\"localNames\":{\"type\":\"wildcard\"},\"types\":{\"type\":\"wildcard\"},\"desc\":{\"type\":\"text\"},\"urls\":{\"type\":\"text\"},\"pubKeyAdmin\":{\"type\":\"keyword\"},\"protocols\":{\"type\":\"keyword\"},\"services\":{\"type\":\"keyword\"},\"owner\":{\"type\":\"wildcard\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastTxid\":{\"type\":\"keyword\"},\"lastTime\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"tCdd\":{\"type\":\"long\"},\"tRate\":{\"type\":\"float\"},\"active\":{\"type\":\"boolean\"}}}}";
		String appHistJsonStr = "{\"mappings\":{\"properties\":{\"id\":{\"type\":\"keyword\"},\"height\":{\"type\":\"long\"},\"index\":{\"type\":\"short\"},\"time\":{\"type\":\"long\"},\"signer\":{\"type\":\"wildcard\"},\"stdName\":{\"type\":\"wildcard\"},\"localNames\":{\"type\":\"wildcard\"},\"desc\":{\"type\":\"text\"},\"types\":{\"type\":\"wildcard\"},\"urls\":{\"type\":\"text\"},\"pubKeyAdmin\":{\"type\":\"keyword\"},\"protocols\":{\"type\":\"keyword\"},\"services\":{\"type\":\"keyword\"},\"aid\":{\"type\":\"keyword\"},\"op\":{\"type\":\"keyword\"},\"rate\":{\"type\":\"short\"},\"cdd\":{\"type\":\"long\"}}}}";		
		
		String contactsJsonStr = "{\"mappings\":{\"properties\":{\"addTxid\":{\"type\":\"keyword\"},\"alg\":{\"type\":\"wildcard\"},\"ciphertext\":{\"type\":\"keyword\"},\"owner\":{\"type\":\"wildcard\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"active\":{\"type\":\"boolean\"}}}}";
		String mailJsonStr = "{\"mappings\":{\"properties\":{\"sendTxid\":{\"type\":\"keyword\"},\"alg\":{\"type\":\"wildcard\"},\"ciphertextSend\":{\"type\":\"keyword\"},\"ciphertextReci\":{\"type\":\"keyword\"},\"textHash\":{\"type\":\"keyword\"},\"owner\":{\"type\":\"wildcard\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"active\":{\"type\":\"boolean\"}}}}";
		String safeJsonStr = "{\"mappings\":{\"properties\":{\"addTxid\":{\"type\":\"keyword\"},\"alg\":{\"type\":\"wildcard\"},\"ciphertext\":{\"type\":\"keyword\"},\"owner\":{\"type\":\"wildcard\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"active\":{\"type\":\"boolean\"}}}}";		
		String statementJsonStr = "{\"mappings\":{\"properties\":{\"stid\":{\"type\":\"keyword\"},\"title\":{\"type\":\"text\"},\"content\":{\"type\":\"text\"},\"owner\":{\"type\":\"wildcard\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"active\":{\"type\":\"boolean\"}}}}";
		
		String groupJsonStr = "{\"mappings\":{\"properties\":{\"gid\":{\"type\":\"keyword\"},\"name\":{\"type\":\"wildcard\"},\"desc\":{\"type\":\"text\"},\"namers\":{\"type\":\"wildcard\"},\"activeMembers\":{\"type\":\"wildcard\"},\"leftMembers\":{\"type\":\"wildcard\"},\"birthTime\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"lastTxid\":{\"type\":\"keyword\"},\"lastTime\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"tCdd\":{\"type\":\"long\"}}}}";
		String groupHistJsonStr = "{\"mappings\":{\"properties\":{\"id\":{\"type\":\"keyword\"},\"height\":{\"type\":\"long\"},\"index\":{\"type\":\"short\"},\"time\":{\"type\":\"long\"},\"signer\":{\"type\":\"wildcard\"},\"gid\":{\"type\":\"keyword\"},\"op\":{\"type\":\"keyword\"},\"name\":{\"type\":\"wildcard\"},\"desc\":{\"type\":\"text\"},\"cdd\":{\"type\":\"long\"}}}}";		
		
		
		InputStream cidJsonStrIs = new ByteArrayInputStream(cidJsonStr.getBytes());
		InputStream cidHistJsonStrIs = new ByteArrayInputStream(cidHistJsonStr.getBytes());
		InputStream repuHistJsonStrIs = new ByteArrayInputStream(repuHistJsonStr.getBytes());
		InputStream parseMarkJsonStrIs = new ByteArrayInputStream(parseMarkJsonStr.getBytes());

		
		InputStream protocolJsonStrIs = new ByteArrayInputStream(protocolJsonStr.getBytes());
		InputStream appJsonStrIs = new ByteArrayInputStream(appJsonStr.getBytes());
		InputStream serviceJsonStrIs = new ByteArrayInputStream(serviceJsonStr.getBytes());
		InputStream codeJsonStrIs = new ByteArrayInputStream(codeJsonStr.getBytes());
		
		InputStream protocolHistJsonStrIs = new ByteArrayInputStream(protocolHistJsonStr.getBytes());
		InputStream serviceHistJsonStrIs = new ByteArrayInputStream(serviceHistJsonStr.getBytes());
		InputStream appHistJsonStrIs = new ByteArrayInputStream(appHistJsonStr.getBytes());
		InputStream codeHistJsonStrIs = new ByteArrayInputStream(codeHistJsonStr.getBytes());
		
		InputStream contactsJsonStrIs = new ByteArrayInputStream(contactsJsonStr.getBytes());
		InputStream mailJsonStrIs = new ByteArrayInputStream(mailJsonStr.getBytes());
		InputStream safeJsonStrIs = new ByteArrayInputStream(safeJsonStr.getBytes());
		InputStream statementJsonStrIs = new ByteArrayInputStream(statementJsonStr.getBytes());
		
		InputStream groupJsonStrIs = new ByteArrayInputStream(groupJsonStr.getBytes());
		InputStream groupHistJsonStrIs = new ByteArrayInputStream(groupHistJsonStr.getBytes());
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.GroupHistIndex).withJson(groupHistJsonStrIs));
			groupHistJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index group_history created.");
			}else {
				log.info("Index group_history creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index group_history creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.GroupIndex).withJson(groupJsonStrIs));
			groupJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index group created.");
			}else {
				log.info("Index group creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index group creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.StatementIndex).withJson(statementJsonStrIs));
			statementJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index statement created.");
			}else {
				log.info("Index statement creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index statement creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.SafeIndex).withJson(safeJsonStrIs));
			safeJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index  safe created.");
			}else {
				log.info("Index safe creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index safe creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.MailIndex).withJson(mailJsonStrIs));
			mailJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index  mail created.");
			}else {
				log.info("Index mail creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index mail creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.ContactsIndex).withJson(contactsJsonStrIs));
			contactsJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index  contacts created.");
			}else {
				log.info("Index contacts creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index contacts creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.CodeIndex).withJson(codeJsonStrIs));
			codeJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index  code created.");
			}else {
				log.info("Index code creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index code creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.CodeHistIndex).withJson(codeHistJsonStrIs));
			codeHistJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index code_history created.");
			}else {
				log.info("Index code_history creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index code_history creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.AppHistIndex).withJson(appHistJsonStrIs));
			appHistJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index app_history created.");
			}else {
				log.info("Index app_history creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index app_history creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.ServiceHistIndex).withJson(serviceHistJsonStrIs));
			serviceHistJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index service_history created.");
			}else {
				log.info("Index service_history creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index service_history creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.FreeProtocolHistIndex).withJson(protocolHistJsonStrIs));
			protocolHistJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index protocol_history created.");
			}else {
				log.info("Index protocol_history creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index protocol_history creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.ServiceIndex).withJson(serviceJsonStrIs));
			serviceJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index service created.");
			}else {
				log.info("Index service creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index service creating failed.",e);
			return;
		}

		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.AppIndex).withJson(appJsonStrIs));
			appJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index  app created.");
			}else {
				log.info("Index app creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index app creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.FreeProtocolIndex).withJson(protocolJsonStrIs));
			protocolJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index  protocol created.");
			}else {
				log.info("Index protocol creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index protocol creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.ParseMark).withJson(parseMarkJsonStrIs));
			parseMarkJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index  parse_mark created.");
			}else {
				log.info("Index parse_mark creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index parse_mark creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.CidIndex).withJson(cidJsonStrIs));
			cidJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index  cid created.");
			}else {
				log.info("Index cid creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index cid creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.CidHistIndex).withJson(cidHistJsonStrIs));
			cidHistJsonStrIs.close();
			if(req.acknowledged()) {
			log.info("Index  cid_history created.");
			}else {
				log.info("Index cid_history creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index cid_history creating failed.",e);
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(Indices.RepuHistIndex).withJson(repuHistJsonStrIs));
			repuHistJsonStrIs.close();
			if(req.acknowledged()) {
				log.info("Index reputation_history created.");
			}else {
				log.info("Index reputation_history creating failed.");
				return;
			}
		}catch(ElasticsearchException e) {
			log.info("Index reputation_history creating failed.",e);
			return;
		}
		return;
	}
	
	public static void deleteAllIndices(ElasticsearchClient esClient) throws  IOException {

		if(esClient==null) {
			System.out.println("Create a Java client for ES first.");
			return;
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.GroupIndex));

			if(req.acknowledged()) {
			log.info("Index group deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index group deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.GroupHistIndex));

			if(req.acknowledged()) {
			log.info("Index group_history deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index group_history deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.StatementIndex));

			if(req.acknowledged()) {
			log.info("Index statement deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index statement deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.SafeIndex));

			if(req.acknowledged()) {
			log.info("Index safe deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index safe deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.MailIndex));

			if(req.acknowledged()) {
			log.info("Index mail deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index mail deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.ContactsIndex));

			if(req.acknowledged()) {
			log.info("Index contacts deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index contacts deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.CodeIndex));

			if(req.acknowledged()) {
			log.info("Index code deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index code deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.CodeHistIndex));

			if(req.acknowledged()) {
			log.info("Index code_history deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index code_history deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.AppHistIndex));

			if(req.acknowledged()) {
			log.info("Index app_history deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index app_history deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.AppIndex));

			if(req.acknowledged()) {
			log.info("Index app deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index app deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.ServiceHistIndex));

			if(req.acknowledged()) {
			log.info("Index service_history deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index service_history deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.ServiceIndex));

			if(req.acknowledged()) {
			log.info("Index service deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index service deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.FreeProtocolHistIndex));

			if(req.acknowledged()) {
			log.info("Index protocol_history deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index protocol_history deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.FreeProtocolIndex));

			if(req.acknowledged()) {
			log.info("Index protocol deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index protocol deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.ParseMark));

			if(req.acknowledged()) {
			log.info("Index  parse_mark deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index block_mark deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.CidIndex));

			if(req.acknowledged()) {
			log.info("Index  block_Mark deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index block_mark deleting failed.",e);
		}

		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.CidHistIndex));

			if(req.acknowledged()) {
			log.info("Index  block deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index block deleting failed.",e);
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(Indices.RepuHistIndex));
			if(req.acknowledged()) {
			log.info("Index tx deleted.");
			}
		}catch(ElasticsearchException e) {
			log.info("Index tx deleting failed.",e);
		}

		return;
	}	
}
