package start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import esClient.StartClient;
import tools.ParseTools;

public class Start {
	public static long CddCheckHeight=2000000;
	public static long CddRequired=1;
	
	private static int MenuItemsNum =6;
	private static final Logger log = LoggerFactory.getLogger(Start.class);
	private static StartClient startClient = new StartClient();
	
	public static void main(String[] args)throws Exception{
		
		log.info("Start.");
		Scanner sc = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Configer configer = new Configer();
		
		boolean end = false;
		ElasticsearchClient esClient = null;
		System.out.println(" << FEIP parser >> pre version 2023.1.17\n");
		while(!end) {
			configer.initial();
			
			System.out.println(
					"	-----------------------------\n"
					+ "	Menu\n"
					+ "	-----------------------------\n"
					+"	1 Create a Java HTTP client\n"
					+"	2 Create a Java HTTPS client\n"
					+"	3 Start New Parse from file\n"
					+"	4 Restart from interruption\n"
					+"	5 Manual start from a height\n"
					+"	6 Reparse ID list\n"
					+"	0 Exit\n"
					+ "	-----------------------------"
					);	
			
			int choice = choose(sc);
			
			String path = configer.getPath();
			long bestHeight = 0;
			switch(choice) {
			case 1:
				if(configer.getIp()==null || configer.getPort() == 0 || configer.getPath()==null) {
					configer.configHttp(sc,br);
					configer.initial();
				}
				
				esClient = creatHttpClient(configer);
				
				if(esClient != null) {
					System.out.println("Client has been created: "+esClient.toString());
					log.info("Client has been created:{} ",esClient.toString());
				}else {
					System.out.println("\n******!Create ES client failed!******\n");
				}
				break;
			case 2:
				if(configer.getIp()==null || configer.getPort() == 0|| configer.getUsername()==null|| configer.getPath()==null) {
					configer.configHttps(sc,br);
					configer.initial();
				}
				
				esClient = creatHttpsClient(configer,sc);
				
				if(esClient != null) {
					System.out.println("Client has been created: "+esClient.toString());
					log.info("Client has been created:{} ",esClient.toString());
				}else {
					System.out.println("\n******!Create ES client failed!******\n");
				}
				break;
			case 3: 
				if(esClient==null) {
					System.out.println("Create a Java client for ES first.");
					break;
				}	
				System.out.println("Start from 0, all indices will be deleted. Do you want? y or n:");			
				String delete = sc.next();		
				if (delete.equals("y")) {					
					System.out.println("Do you sure? y or n:");				
					delete = sc.next();			
					if (delete.equals("y")) {	
		
						System.out.println("Deleting indices...");	
						Indices.deleteAllIndices(esClient);
						TimeUnit.SECONDS.sleep(3);
						
						System.out.println("Creating indices...");	
						Indices.createAllIndices(esClient);
						TimeUnit.SECONDS.sleep(2);
						
						end = startNewFromFile(esClient,path);
						
						break;
					}else break;
				}else break;
				
			case 4: 
				if(esClient==null) {
					System.out.println("Create a Java client for ES first.");
					break;
				}
				end = restartFromFile(esClient,path);
				break;
			case 5: 
				if(esClient==null) {
					System.out.println("Create a Java client for ES first.");
					break;
				}
				System.out.println("Input the height that parsing begin with: ");
				while(!sc.hasNextLong()){
					System.out.println("\nInput the number of the height:");
					sc.next();
				}
				bestHeight = sc.nextLong();
				end = manualRetartFromFile(esClient,path,bestHeight);
				break;
			case 6: 
				System.out.println("Input the name of ES index:");
				String index = sc.next();
				System.out.println("Input the ID list in compressed Json string:");
				String idListJsonStr = br.readLine();
				
				//try {
					Gson gson = new Gson();
					@SuppressWarnings("unchecked") List<String> idList = gson.fromJson(idListJsonStr, List.class);
				//}
				FileParser fileParser = new FileParser();			
				fileParser.reparseIdList(esClient, index,idList);
				break;
			case 0: 
				if(esClient!=null)startClient.shutdownClient();
				System.out.println("Exited, see you again.");
				end = true;
				break;
			default:
				break;
			}
		}
		startClient.shutdownClient();
		sc.close();
		br.close();
	}
	
	private static int choose(Scanner sc) throws IOException {
		System.out.println("\nInput the number you want to do:\n");
		int choice = 0;
		while(true) {
			while(!sc.hasNextInt()){
				System.out.println("\nInput one of the integers shown above.");
				sc.next();
			}
			choice = sc.nextInt();
		if(choice <= MenuItemsNum && choice>=0)break;
		System.out.println("\nInput one of the integers shown above.");
		}
		return choice;
	}
	
	private static ElasticsearchClient creatHttpClient(start.Configer configer) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		System.out.println("creatHttpClient");

		ElasticsearchClient esClient = null;
		try {
			esClient = startClient.getClientHttp(configer);
			System.out.println(esClient.info());
		} catch (ElasticsearchException | IOException e) {
			// TODO Auto-generated catch block
			log.info("Create esClient wrong",e);
			return null;
		}
			
		return esClient;
	}

	private static ElasticsearchClient creatHttpsClient(Configer configer,Scanner sc)  {
		// TODO Auto-generated method stub
		System.out.println("creatHttpsClient.");

		ElasticsearchClient esClient = null;
		try {
			esClient = startClient.getClientHttps(configer, sc);
			System.out.println(esClient.info());
		} catch (KeyManagementException | ElasticsearchException | NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			log.info("Create esClient wrong",e);
			return null;
		}
		
		return esClient;
	}

	private static boolean startNewFromFile(ElasticsearchClient esClient, String path) throws Exception {
		
//		if(esClient==null) {
//			System.out.println("Create a Java client for ES first.");
//			return false;
//		}
		
		System.out.println("startNewFromFile.");
		
		FileParser fileParser = new FileParser();
		
		fileParser.setPath(path);
		fileParser.setFileName("opreturn0.byte");
		fileParser.setPointer(0);
		fileParser.setLastHeight(0);
		fileParser.setLastIndex(0);
		
		boolean isRollback = false;
		boolean error = fileParser.parseFile(esClient,isRollback);
		return error;
		// TODO Auto-generated method stub
	}

	private static boolean restartFromFile(ElasticsearchClient esClient, String path) throws Exception {
		
		SearchResponse<ParseMark> result = esClient.search(s->s
				.index(Indices.ParseMark)
				.size(1)
				.sort(s1->s1
						.field(f->f
								.field("lastIndex").order(SortOrder.Desc)
								.field("lastHeight").order(SortOrder.Desc)
								)
						)
				, ParseMark.class);
		

		
		ParseMark parseMark = result.hits().hits().get(0).source();

		ParseTools.gsonPrint(parseMark);
		
		FileParser fileParser = new FileParser();
		
		fileParser.setPath(path);
		fileParser.setFileName(parseMark.getFileName());
		fileParser.setPointer(parseMark.getPointer());
		fileParser.setLength(parseMark.getLength());
		fileParser.setLastHeight(parseMark.getLastHeight());
		fileParser.setLastIndex(parseMark.getLastIndex());
		fileParser.setLastId(parseMark.getLastId());
		
		boolean isRollback = false;
		boolean error = fileParser.parseFile(esClient,isRollback);
		
		System.out.println("restartFromFile.");
		return error;
	}

	private static boolean manualRetartFromFile(ElasticsearchClient esClient, String path, long height) throws Exception {
		
		SearchResponse<ParseMark> result = esClient.search(s->s
				.index(Indices.ParseMark)
				.query(q->q.range(r->r.field("lastHeight").lte(JsonData.of(height))))
				.size(1)
				.sort(s1->s1
						.field(f->f
								.field("lastIndex").order(SortOrder.Desc)
								.field("lastHeight").order(SortOrder.Desc)))
				, ParseMark.class);
		
		if(result.hits().total().value()==0) {
			boolean error = restartFromFile(esClient,path);
			return error;
		}
		
		ParseMark parseMark = result.hits().hits().get(0).source();
		
		FileParser fileParser = new FileParser();
		
		fileParser.setPath(path);
		fileParser.setFileName(parseMark.getFileName());
		fileParser.setPointer(parseMark.getPointer());
		fileParser.setLength(parseMark.getLength());
		fileParser.setLastHeight(parseMark.getLastHeight());
		fileParser.setLastIndex(parseMark.getLastIndex());
		fileParser.setLastId(parseMark.getLastId());
		
		boolean isRollback = true;
		
		boolean error = fileParser.parseFile(esClient,isRollback);
		
		System.out.println("manualRetartFromFile");
		return error;
		// TODO Auto-generated method stub
		
	}
	
	
}

