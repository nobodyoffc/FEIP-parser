package xtest;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.cat.HealthResponse;
import co.elastic.clients.elasticsearch.cat.health.HealthRecord;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import esClient.StartClient;


public class log {

    public static void main(String[] args) throws ElasticsearchException, IOException {
        /*解析非classpath下的配置文件

        String log4jPath=System.getProperty("user.dir")+"\\src\\log\\logProperties\\log4j.properties";
        PropertyConfigurator.configure(log4jPath);
                 */
    	Logger log1 = LoggerFactory.getLogger(log.class);
    	
      //  org.apache.log4j.Logger log = LogManager.getLogger(logTest.class);
        
		////////////////////
		StartClient sc = new StartClient();
		sc.setParams("192.168.31.193", 9200);

		ElasticsearchClient esClient = sc.getClientHttp();
		// createIndex(client,"test");
		HealthResponse ch = esClient.cat().health();
		List<HealthRecord> vb = ch.valueBody();
		System.out.println("ES Client was created. The cluster is: " + vb.get(0).cluster());
		////////////////////
		
		SearchResponse<Block> re = esClient.search(s->s
				.index("block")
				.query(q->q
						.range(r->r.field("heigh").lt(JsonData.of(10)))), Block.class);
		
		List<Hit<Block>> hits = re.hits().hits();
		for(Hit<Block> hit:hits) {
			System.out.println("blockHeight: "+hit.source().getHeight());
		}
//        log.debug("调试");
//        log.info("信息");
//        log.warn("警告");
//        log.error("错误");
//        log.fatal("致命错误");
        
        log1.debug("调试");
        log1.info("信息");
        log1.warn("警告");
        log1.error("错误{}",vb.toArray());

    }

}

