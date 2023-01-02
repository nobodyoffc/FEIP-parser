package personal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.json.JsonData;
import start.Indices;

public class PersonalRollbacker {

	public void rollback(ElasticsearchClient esClient, long lastHeight) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		List<String> indexList = new ArrayList<String>();
		indexList.add(Indices.ContactsIndex);
		indexList.add(Indices.MailIndex);
		indexList.add(Indices.SafeIndex);
		indexList.add(Indices.StatementIndex);
		esClient.deleteByQuery(d->d.index(indexList).query(q->q.range(r->r.field("birthHeight").gt(JsonData.of(lastHeight)))));
	}
}
