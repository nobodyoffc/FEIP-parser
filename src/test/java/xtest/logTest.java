package xtest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;


public class logTest {

    public static void main(String[] args) throws ElasticsearchException, IOException {
        /*解析非classpath下的配置文件

        String log4jPath=System.getProperty("user.dir")+"\\src\\log\\logProperties\\log4j.properties";
        PropertyConfigurator.configure(log4jPath);
                 */
    	Logger log1 = LoggerFactory.getLogger(logTest.class);
    	
      //  org.apache.log4j.Logger log = LogManager.getLogger(logTest.class);
        
		////////////////////



		// createIndex(client,"test");

//        log.debug("调试");
//        log.info("信息");
//        log.warn("警告");
//        log.error("错误");
//        log.fatal("致命错误");
        
        log1.debug("调试");
        log1.info("信息");
        log1.warn("警告");
        log1.error("错误{}");

    }

}

