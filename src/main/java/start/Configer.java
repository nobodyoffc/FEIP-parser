package start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;

public class Configer {
	private String esIp;
	private int esPort;
	private String esUsername;
	private String opReturnJsonPath;
	private String tomcatStartCommand;
	private int redisPort;
	private String redisHost;
	
	public void initial() throws IOException {

		Configer configer = new Configer();
		
		Gson gson = new Gson();
		File configFile = new File("config.json");
		if(configFile.exists()) {
			FileInputStream fis = new FileInputStream(configFile);
			byte[] configJsonBytes = new byte[fis.available()];
			fis.read(configJsonBytes);
			
			String configJson = new String(configJsonBytes);
			configer = gson.fromJson(configJson, Configer.class);
			
			if(configer==null) {
				fis.close();
				return;
			}
			
			esIp = configer.getEsIp();
			esPort = configer.getEsPort();
			esUsername = configer.getEsUsername();
			opReturnJsonPath = configer.getOpReturnJsonPath();
			tomcatStartCommand = configer.getTomcatStartCommand();
			redisPort = configer.getRedisPort();
			redisHost = configer.getRedisHost();
			
			System.out.println("Initialized:");
			tools.ParseTools.gsonPrint(configer);
			fis.close();
		}
		return;
	}

	public Configer configEs(Scanner sc, BufferedReader br) throws IOException {
		System.out.println("Config ES...");
		Gson gson = new Gson();
		File configFile = new File("config.json");
		
		FileOutputStream fos = new FileOutputStream(configFile);
		
		System.out.println("Input the IP of ES server:");

		while(true) {
			setEsIp(br.readLine());
			if (getEsIp().matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))"))break;
			System.out.println("It must be a IPaddress, like \"100.102.102.10\". Input again.");
		}
		
		System.out.println("Input the port of ES server:");
		
		while(true){
			if(!sc.hasNextInt()) {
				System.out.println("It must be a port. It's a integer between 0 and 655350. Input again.\"");
				setEsPort(sc.nextInt());
			}
			else {
				setEsPort(sc.nextInt());
				if( getEsPort()>0 && getEsPort()<65535)break;
				System.out.println("It has to be between 0 and 655350. Input again.");
				setEsPort(sc.nextInt());
			}
		}
		

		System.out.println("Input the username of ES, or press enter if it's a http service:");
		String input = br.readLine();
		if("".equals(input)) {
			System.out.println("\nElasticSerch HTTP client configed.");	

		}else {
			setEsUsername(input);
			System.out.println("\nElasticSerch HTTPS client configed.");
		}

		System.out.println("Initialized:");
		tools.ParseTools.gsonPrint(this);
		
		fos.write(gson.toJson(this).getBytes());
		fos.flush();
		fos.close();
		
		return this;
	}
	
	public void configOpReturnJsonPath(BufferedReader br) throws IOException {
		
		Configer configer = new Configer();
		
		Gson gson = new Gson();
		File configFile = new File("config.json");
		System.out.println("Opened config.json in: "+configFile.getAbsolutePath());
		
		FileOutputStream fos = new FileOutputStream(configFile);
		
		File file;
		String path;
		
		while(true) {
			System.out.println("Input the path of the directory opreturn*.byte file located in:");
			path = br.readLine();
			configer.setOpReturnJsonPath(path);
	        file = new File(configer.getOpReturnJsonPath());
	        if (!file.exists()) {
	        	System.out.println("\nThe path does not exist.");
	        }else break;
		}
		
		fos.write(gson.toJson(configer).getBytes());
		fos.flush();
		fos.close();
		
		System.out.println("\nThe op_return json file path is configed.");	
	}

	public void configTomcatStartCommand(BufferedReader br) throws IOException {
		
		Gson gson = new Gson();
		File configFile = new File("config.json");
		System.out.println("Opened config.json in: "+configFile.getAbsolutePath());
		
		FileOutputStream fos = new FileOutputStream(configFile);
		
		String comm;

		System.out.println("Input the whole command for startting tomcat server:");
		comm = br.readLine();
		setTomcatStartCommand(comm);

		fos.write(gson.toJson(this).getBytes());
		fos.flush();
		fos.close();
		
		System.out.println("\nThe tomcat server command is configed.");	
	}

	public void configRedis(Scanner sc, BufferedReader br) throws IOException {
		
		Gson gson = new Gson();
		File configFile = new File("config.json");
		
		System.out.println("Opened config.json in: "+configFile.getAbsolutePath());
		
		FileOutputStream fos = new FileOutputStream(configFile);
		
		System.out.println("Input the IP of Redis server:");

		while(true) {
			setRedisHost(br.readLine());
			if (getRedisHost().matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))"))break;
			System.out.println("It must be a IPaddress, like \"100.102.102.10\". Input again.");
		}
		
		System.out.println("Input the port of Redis server:");
		
		while(true){
			if(!sc.hasNextInt()) {
				System.out.println("It must be a port. It's a integer between 0 and 655350. Input again.\"");
				setRedisPort(sc.nextInt());
			}
			else {
				setRedisPort(sc.nextInt());
				if( getRedisPort()>0 && getRedisPort()<65535)break;
				System.out.println("It has to be between 0 and 655350. Input again.");
				setRedisPort(sc.nextInt());
			}
		}
		
		fos.write(gson.toJson(this).getBytes());
		fos.flush();
		fos.close();
		
		System.out.println("\nThe host and port of redis are configed.");	
	}

	
	public String getEsIp() {
		return esIp;
	}

	public void setEsIp(String esIp) {
		this.esIp = esIp;
	}

	public int getEsPort() {
		return esPort;
	}

	public void setEsPort(int esPort) {
		this.esPort = esPort;
	}

	public String getEsUsername() {
		return esUsername;
	}

	public void setEsUsername(String esUsername) {
		this.esUsername = esUsername;
	}

	public String getOpReturnJsonPath() {
		return opReturnJsonPath;
	}

	public void setOpReturnJsonPath(String opReturnJsonPath) {
		this.opReturnJsonPath = opReturnJsonPath;
	}

	public String getTomcatStartCommand() {
		return tomcatStartCommand;
	}

	public void setTomcatStartCommand(String tomcatStartCommand) {
		this.tomcatStartCommand = tomcatStartCommand;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}
}
