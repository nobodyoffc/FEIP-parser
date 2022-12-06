package esClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.MgetRequest;
import co.elastic.clients.elasticsearch.core.MgetResponse;
import co.elastic.clients.elasticsearch.core.get.GetResult;
import co.elastic.clients.elasticsearch.core.mget.MultiGetResponseItem;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import fchBlock.ParseBlock;


public class IndicesOperators {
	
	static final Logger log = LoggerFactory.getLogger(IndicesOperators.class);
			
	
	/**
	 * Create new index
	 * @return 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */

	
	public static void createAllIndices(ElasticsearchClient esClient) throws ElasticsearchException, IOException {
			
		if(esClient==null) {
			System.out.println("Create a Java client for ES first.");
			return;
		}
		
		String blockJsonStr = "{\"mappings\":{\"properties\":{\"blockSize\":{\"type\":\"long\"},\"blockTime\":{\"type\":\"long\"},\"cdd\":{\"type\":\"long\"},\"diffTarget\":{\"type\":\"long\"},\"fee\":{\"type\":\"long\"},\"height\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"inValueT\":{\"type\":\"long\"},\"merkleRoot\":{\"type\":\"keyword\"},\"nonce\":{\"type\":\"long\"},\"outValueT\":{\"type\":\"long\"},\"preBlockId\":{\"type\":\"keyword\"},\"txCount\":{\"type\":\"long\"},\"version\":{\"type\":\"keyword\"}}}}";
		String blockHasJsonStr = "{\"mappings\":{\"properties\":{\"id\":{\"type\":\"keyword\"},\"txIds\":{\"type\":\"keyword\"}}}}";
		String txJsonStr = "{\"mappings\":{\"properties\":{\"blockId\":{\"type\":\"keyword\"},\"blockTime\":{\"type\":\"long\"},\"cdd\":{\"type\":\"long\"},\"coinbase\":{\"type\":\"text\"},\"fee\":{\"type\":\"long\"},\"height\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"inCount\":{\"type\":\"long\"},\"inValueT\":{\"type\":\"long\"},\"lockTime\":{\"type\":\"long\"},\"opReBrief\":{\"type\":\"text\"},\"outCount\":{\"type\":\"long\"},\"outValueT\":{\"type\":\"long\"},\"txIndex\":{\"type\":\"long\"},\"version\":{\"type\":\"long\"}}}}";
		String txHasJsonStr = "{\"mappings\":{\"properties\":{\"cdds\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"inAddrs\":{\"type\":\"wildcard\"},\"inIds\":{\"type\":\"keyword\"},\"inValues\":{\"type\":\"long\"},\"outAddrs\":{\"type\":\"wildcard\"},\"outIds\":{\"type\":\"keyword\"},\"outValues\":{\"type\":\"long\"}}}}";
		String utxoJsonStr = "{\"mappings\":{\"properties\":{\"addr\":{\"type\":\"wildcard\"},\"blockTime\":{\"type\":\"long\"},\"cd\":{\"type\":\"long\"},\"height\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"lockScript\":{\"type\":\"text\"},\"outIndex\":{\"type\":\"long\"},\"txId\":{\"type\":\"keyword\"},\"txIndex\":{\"type\":\"long\"},\"type\":{\"type\":\"keyword\"},\"value\":{\"type\":\"long\"}}}}";
		String stxoJsonStr = "{\"mappings\":{\"properties\":{\"addr\":{\"type\":\"wildcard\"},\"blockTime\":{\"type\":\"long\"},\"cdd\":{\"type\":\"long\"},\"height\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"lockScript\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":256}}},\"outIndex\":{\"type\":\"long\"},\"sequence\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":8}}},\"sigHash\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":20}}},\"spentHeight\":{\"type\":\"long\"},\"spentIndex\":{\"type\":\"long\"},\"spentTime\":{\"type\":\"long\"},\"spentTxId\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":64}}},\"txId\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":64}}},\"txIndex\":{\"type\":\"long\"},\"type\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":10}}},\"unlockScript\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":256}}},\"value\":{\"type\":\"long\"}}}}";
		String addressJsonStr = "{\"mappings\":{\"properties\":{\"balance\":{\"type\":\"long\"},\"birthHeight\":{\"type\":\"long\"},\"btcAddr\":{\"type\":\"wildcard\"},\"cd\":{\"type\":\"long\"},\"cdd\":{\"type\":\"long\"},\"dogeAddr\":{\"type\":\"wildcard\"},\"ethAddr\":{\"type\":\"wildcard\"},\"expend\":{\"type\":\"long\"},\"guide\":{\"type\":\"wildcard\"},\"id\":{\"type\":\"wildcard\"},\"income\":{\"type\":\"long\"},\"lastHeight\":{\"type\":\"long\"},\"ltcAddr\":{\"type\":\"wildcard\"},\"pubkey\":{\"type\":\"wildcard\"},\"trxAddr\":{\"type\":\"wildcard\"}}}}";
		String opreturnJsonStr = "{\"mappings\":{\"properties\":{\"cdd\":{\"type\":\"long\"},\"height\":{\"type\":\"long\"},\"id\":{\"type\":\"keyword\"},\"opReturn\":{\"type\":\"text\"},\"recipient\":{\"type\":\"wildcard\"},\"signer\":{\"type\":\"wildcard\"},\"txIndex\":{\"type\":\"long\"}}}}";

		

		InputStream blockJsonStrIs = new ByteArrayInputStream(blockJsonStr.getBytes());
		InputStream blockHasJsonStrIs = new ByteArrayInputStream(blockHasJsonStr.getBytes());
		InputStream txJsonStrIs = new ByteArrayInputStream(txJsonStr.getBytes());
		InputStream txHasJsonStrIs = new ByteArrayInputStream(txHasJsonStr.getBytes());
		InputStream utxoJsonStrIs = new ByteArrayInputStream(utxoJsonStr.getBytes());
		InputStream stxoJsonStrIs = new ByteArrayInputStream(stxoJsonStr.getBytes());
		InputStream addressJsonIs = new ByteArrayInputStream(addressJsonStr.getBytes());
		InputStream opreturnJsonStrIs = new ByteArrayInputStream(opreturnJsonStr.getBytes());

		
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(ParseBlock.BlockIndex).withJson(blockJsonStrIs));
			blockJsonStrIs.close();
			if(req.acknowledged()) {
			log.info("Index  block created.");
			}else {log.info("Index block creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(ParseBlock.BlockHasIndex).withJson(blockHasJsonStrIs));
			blockHasJsonStrIs.close();
			
			if(req.acknowledged()) {
			log.info("Index block_has created.");
			}else {log.info("Index block_has creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(ParseBlock.TxIndex).withJson(txJsonStrIs));
			txJsonStrIs.close();
			
			if(req.acknowledged()) {
			log.info("Index tx created.");
			}else {log.info("Index tx creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(ParseBlock.TxHasIndex).withJson(txHasJsonStrIs));
			txHasJsonStrIs.close();
			
			if(req.acknowledged()) {
			log.info("Index tx_has created.");
			}else {log.info("Index tx_has creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(ParseBlock.UtxoIndex).withJson(utxoJsonStrIs));
			utxoJsonStrIs.close();
			
			if(req.acknowledged()) {
			log.info("Index utxo created.");
			}else {log.info("Index utxo creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(ParseBlock.StxoIndex).withJson(stxoJsonStrIs));
			stxoJsonStrIs.close();
			
			if(req.acknowledged()) {
			log.info("Index stxo created.");
			}else {log.info("Index stxo creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(ParseBlock.AddressIndex).withJson(addressJsonIs));
			addressJsonIs.close();
			
			if(req.acknowledged()) {
			log.info("Index address created.");
			}else {log.info("Index address creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			CreateIndexResponse req = esClient.indices().create(c -> c.index(ParseBlock.OpReturnIndex).withJson(opreturnJsonStrIs));
			opreturnJsonStrIs.close();
			
			if(req.acknowledged()) {
			log.info("Index opreturn created.");
			}else {log.info("Index opreturn creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		return;
	}
	public static void deleteAllIndices(ElasticsearchClient esClient) throws ElasticsearchException, IOException {

		
		
		if(esClient==null) {
			System.out.println("Create a Java client for ES first.");
			return;
		}

		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(ParseBlock.BlockIndex));

			if(req.acknowledged()) {
			log.info("Index  block deleted.");
			}else {log.info("Index block deleting failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(ParseBlock.TxIndex));
			if(req.acknowledged()) {
			log.info("Index tx deleted.");
			}else {log.info("Index tx deleting failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(ParseBlock.UtxoIndex));
			if(req.acknowledged()) {
			log.info("Index utxo deleted.");
			}else {log.info("Index utxo deleting failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(ParseBlock.StxoIndex));
			if(req.acknowledged()) {
			log.info("Index stxo delted.");
			}else {log.info("Index stxo deleting failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(ParseBlock.AddressIndex));
			if(req.acknowledged()) {
			log.info("Index address deleted.");
			}else {log.info("Index address deleting failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(ParseBlock.BlockHasIndex));
			if(req.acknowledged()) {
			log.info("Index block_has deleted.");
			}else {log.info("Index block_has deleting failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(ParseBlock.TxHasIndex));
			if(req.acknowledged()) {
			log.info("Index tx_has deleted.");
			}else {log.info("Index tx_has deleting failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		try {
			DeleteIndexResponse req = esClient.indices().delete(c -> c.index(ParseBlock.OpReturnIndex));
			if(req.acknowledged()) {
			log.info("Index opreturn deleted.");
			}else {log.info("Index opreturn creating failed.");
			return;
			}
		}catch(ElasticsearchException e) {
			System.out.println("Sorry, "+e.error());
			return;
		}
		
		return;
	
	}
		
	public static <T> List<T> getMultiByIdList(List<String> idList, ElasticsearchClient esClient,String index, Class<T> classType) throws ElasticsearchException, IOException{

		MgetRequest.Builder mgetRequestBuilder = new MgetRequest.Builder();
		mgetRequestBuilder.index("test").ids(idList);
		MgetRequest mgetRequest = mgetRequestBuilder.build();
		MgetResponse<T> mgetResponse = esClient.mget(mgetRequest,classType);
		List<MultiGetResponseItem<T>> items = mgetResponse.docs();
		MultiGetResponseItem<T> multiGetResponseItem;
		GetResult<T> result;
		ListIterator<MultiGetResponseItem<T>> iter = items.listIterator();
		List<T> resultList = new ArrayList<T>();
		while(iter.hasNext()) {
			multiGetResponseItem = iter.next();
			result = multiGetResponseItem.result();
			resultList.add(result.source());
		}
		return resultList;
	}
		
}
