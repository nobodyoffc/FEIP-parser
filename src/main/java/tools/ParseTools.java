package tools;

import java.io.IOException;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;

public class ParseTools {
		
	public static void gsonPrint(Object ob) {
		Gson gson = new Gson();
		System.out.println("***********\n"+ob.getClass().toString()+": "+gson.toJson(ob)+"\n***********");
		return ;
	}

	public static Block getBestBlock(ElasticsearchClient esClient) throws ElasticsearchException, IOException {
		SearchResponse<Block> result = esClient.search(s->s
				.index("block")
				.size(1)
				.sort(so->so.field(f->f.field("height").order(SortOrder.Desc)))
				, Block.class);
		return result.hits().hits().get(0).source();
	}
}
