package esClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.MgetRequest;
import co.elastic.clients.elasticsearch.core.MgetResponse;
import co.elastic.clients.elasticsearch.core.BulkRequest.Builder;
import co.elastic.clients.elasticsearch.core.mget.MultiGetResponseItem;

public class EsTools {
	public static final int MAX_SINGLE_REQUEST_COUNT = 5000;
	final static Logger log = LoggerFactory.getLogger(EsTools.class);
	
	private EsTools() {};
	
	public static <T> List<T> getMultiByIdList(
			List<String> idList, 
			ElasticsearchClient esClient,
			String index, 
			Class<T> classType
			) throws Exception{
		
		List<T> resultList = new ArrayList<T>();
		boolean error = false;

		if(idList.size()>MAX_SINGLE_REQUEST_COUNT) {

			Iterator<String> iter = idList.iterator();
			for(int i=0; i< idList.size()/MAX_SINGLE_REQUEST_COUNT+1 ;i++){
				
				MgetRequest.Builder mgetRequestBuilder = new MgetRequest.Builder();
				ArrayList<String> idSubList = new ArrayList<String>();
				
				for(int j = 0; j<idList.size()-i*MAX_SINGLE_REQUEST_COUNT && j< MAX_SINGLE_REQUEST_COUNT;j++) {
					idSubList.add(iter.next());
				}
				
				mgetRequestBuilder
					.index(index)
					.ids(idSubList);
				
				MgetRequest mgetRequest = mgetRequestBuilder.build();
				MgetResponse<T> mgetResponse = null;
					

				Exception e1 = new IOException();
			    for(int k=0;k<3;k++){
					try {
						mgetResponse = esClient.mget(mgetRequest,classType);
					} catch (ElasticsearchException | IOException e) {
						e1 = e;
						error=true;
						TimeUnit.SECONDS.sleep(5);
						continue;
					}
					break;
				}
				if (error ==  true) {
					e1.printStackTrace();
					throw e1;
				}
				
				

				List<MultiGetResponseItem<T>> items = mgetResponse.docs();
				
				List<T> resultSubList = new ArrayList<T>();
				ListIterator<MultiGetResponseItem<T>> iter1 = items.listIterator();
				while(iter1.hasNext()) resultSubList.add(iter1.next().result().source());
				
				resultList.addAll(resultSubList);
			}
			return resultList;
		}else {
			MgetRequest.Builder mgetRequestBuilder = new MgetRequest.Builder();
				mgetRequestBuilder
					.index(index)
					.ids(idList);
				MgetRequest mgetRequest = mgetRequestBuilder.build();
				MgetResponse<T> mgetResponse = null;
				
				
				error = false;
				Exception e1 = new IOException();
			    for(int i=0;i<3;i++){
					try {
						mgetResponse = esClient.mget(mgetRequest,classType);
					} catch (ElasticsearchException | IOException e) {
						e1 = e;
						error=true;
						TimeUnit.SECONDS.sleep(5);
						continue;
					}
					break;
				}
				if (error ==  true) {
					e1.printStackTrace();
					throw e1;
				}
				
				List<MultiGetResponseItem<T>> items = mgetResponse.docs();
				
				ListIterator<MultiGetResponseItem<T>> iter1 = items.listIterator();
				while(iter1.hasNext()) resultList.add(iter1.next().result().source());
				
				return resultList;
			}	
		}
			
	public static <T> void bulkWriteList(ElasticsearchClient esClient
			,String indexT, ArrayList<T> tList
			, ArrayList<String> idList
			,  Class<T> classT) throws Exception {
		
		if(tList.isEmpty()) return;
		if(tList.isEmpty())return;
		
		Iterator<T> iter = tList.iterator();	
		Iterator<String> iterId = idList.iterator();
		for(int i=0;i<tList.size()/MAX_SINGLE_REQUEST_COUNT+1;i++) {
			
			BulkRequest.Builder br = new BulkRequest.Builder();
			
			for(int j = 0; j< MAX_SINGLE_REQUEST_COUNT && i*MAX_SINGLE_REQUEST_COUNT+j<tList.size();j++) {
				T t = iter.next();
				String tid = iterId.next();
				br.operations(op->op.index(in->in
						.index(indexT)
						.id(tid)
						.document(t)));
			}
			bulkWithBuilder(esClient,br);
		}
	}
	
	public static void bulkDeleteList(ElasticsearchClient esClient,String index, ArrayList<String> idList) throws Exception
			 {
		if(idList==null) return;
		if(idList.isEmpty())return;
		
		Iterator<String> iterId = idList.iterator();
		for(int i=0;i<(Math.ceil(idList.size()/MAX_SINGLE_REQUEST_COUNT));i++) {
			
			BulkRequest.Builder br = new BulkRequest.Builder();
			
			for(int j = 0; j< MAX_SINGLE_REQUEST_COUNT && i*MAX_SINGLE_REQUEST_COUNT+j<idList.size();j++) {
				String tid = iterId.next();
				br.operations(op->op.delete(in->in
						.index(index)
						.id(tid)));
			}
			br.timeout(t->t.time("600s"));			
			esClient.bulk(br.build());
		}
	}
	
	public static void bulkWithBuilder(ElasticsearchClient esClient, Builder br) throws Exception {
		
		br.timeout(t->t.time("600s"));			
		esClient.bulk(br.build());
		
	}
}
