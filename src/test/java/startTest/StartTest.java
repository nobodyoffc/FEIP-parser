package startTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import esClient.StartClient;
import parser.FileParser;
import start.Configer;


public class StartTest {
	
	private static int MenuItemsNum =5;
	private static final Logger log = LoggerFactory.getLogger(StartTest.class);
	private static StartClient startClient = new StartClient();
	
	public static void main(String[] args)throws Exception{
		
		log.info("Start.");
		Scanner sc = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Configer configer = new Configer();
		
		boolean end = false;
		ElasticsearchClient esClient = null;
		while(!end) {
			configer.initial();
			
			System.out.println(
					" << Freecash for Freedom >> FreeProtocols test v1 2022.12.16\n\n"	
					+"	1 Create a Java HTTP client\n"
					+"	2 Create a Java HTTPS client\n"
					+"	3 Start New Parse from file\n"
					+"	4 Restart from interruption\n"
					+"	5 Manual start from a height\n"
					+"	6 Config\n"
					+"	0 exit"
					);	
			
			int choice = choose(sc);
			
			String path = configer.getPath();
			long bestHeight = 0;
			
			switch(choice) {
			case 1:
				if(configer.getIp()==null || configer.getPort() == 0 || configer.getPath()==null) 
					configer.configHttp(sc,br);
				
				esClient = creatHttpClient(configer);
				
				if(esClient != null) {
					System.out.println("Client has been created: "+esClient.toString());
					log.info("Client has been created:{} ",esClient.toString());
				}else {
					System.out.println("\n******!Config the ip , port and username, and input the correct password!******\n");
					configer.configHttps(sc,br);
				}
				break;
			case 2:
				if(configer.getIp()==null || configer.getPort() == 0|| configer.getUsername()==null|| configer.getPath()==null) 
					configer.configHttps(sc,br);
				
				esClient = creatHttpsClient(configer,sc);
				
				if(esClient != null) {
					System.out.println("Client has been created: "+esClient.toString());
					log.info("Client has been created:{} ",esClient.toString());
				}else {
					System.out.println("\n******!Config the ip , port and username, and input the correct password!******\n");
					configer.configHttps(sc,br);
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
		
						Indices.deleteAllIndices(esClient);
						TimeUnit.SECONDS.sleep(2);
						
						Indices.createAllIndices(esClient);
						
						end = startNewFromFile(esClient,path);
						
						break;
					}else break;
				}else break;
				
			case 4: 
				
				end = restartFromFile(esClient,path,bestHeight);
				break;
			case 5: 
				end = manualRetartFromFile(esClient,path,bestHeight);
				break;
			case 0: 
				if(esClient!=null)startClient.shutdownClient();
				System.out.println("Exited, see you again.");
				end = true;
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
		
		boolean error = fileParser.parseFile(esClient);
		return error;
		// TODO Auto-generated method stub
	}

	private static boolean restartFromFile(ElasticsearchClient esClient, String path, long bestHeight) {
		System.out.println("restartFromFile.");
		return true;
		// TODO Auto-generated method stub
		
	}

	private static boolean manualRetartFromFile(ElasticsearchClient esClient, String path, long bestHeight) {
		System.out.println("manualRetartFromFile");
		return true;
		// TODO Auto-generated method stub
		
	}
	
}
