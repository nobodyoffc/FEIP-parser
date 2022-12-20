package startTest;

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
	

	public static final String CidIndex = "cid";
	public static final String CidHistIndex = "cid_history";
	public static final String RepuHistIndex = "reputation_history";

			
	public static void createAllIndices(ElasticsearchClient esClient) throws ElasticsearchException, IOException {
			
		if(esClient==null) {
			System.out.println("Create a Java client for ES first.");
			return;
		}

		String cidMappingJsonStr = "{\"mappings\":{\"properties\":{\"cid\":{\"type\":\"wildcard\"},\"height\":{\"type\":\"long\"},\"homepage\":{\"type\":\"text\"},\"hot\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"master\":{\"type\":\"wildcard\"},\"noticeFee\":{\"type\":\"long\"},\"reputation\":{\"type\":\"long\"},\"usedCids\":{\"type\":\"wildcard\"}}}}";
		String cidHistMappingJsonStr = "{\"mappings\":{\"properties\":{\"data_name\":{\"type\":\"wildcard\"},\"data_op\":{\"type\":\"wildcard\"},\"height\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"index\":{\"type\":\"short\"},\"noticeFee\":{\"type\":\"long\"},\"signer\":{\"type\":\"wildcard\"},\"sn\":{\"type\":\"short\"},\"time\":{\"type\":\"long\"},\"ver\":{\"type\":\"short\"}}}}";
		String repuHistJsonStr = "{\"mappings\":{\"properties\":{\"height\":{\"type\":\"long\"},\"hot\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"index\":{\"type\":\"short\"},\"ratee\":{\"type\":\"wildcard\"},\"rater\":{\"type\":\"wildcard\"},\"reputation\":{\"type\":\"long\"},\"time\":{\"type\":\"long\"}}}}";		
		InputStream cidJsonStrIs = new ByteArrayInputStream(cidMappingJsonStr.getBytes());
		InputStream cidHistJsonStrIs = new ByteArrayInputStream(cidHistMappingJsonStr.getBytes());
		InputStream repuHistJsonStrIs = new ByteArrayInputStream(repuHistJsonStr.getBytes());


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
