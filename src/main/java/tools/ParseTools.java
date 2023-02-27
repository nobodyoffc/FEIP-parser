package tools;

import com.google.gson.Gson;

public class ParseTools {
		
	public static void gsonPrint(Object ob) {
		Gson gson = new Gson();
		System.out.println("***********\n"+ob.getClass().toString()+": "+gson.toJson(ob)+"\n***********");
		return ;
	}

    public static String strToJson(String rawStr) {

		if(!rawStr.contains("{")) return null;

		int begin = rawStr.indexOf("{");

		String goodStr = rawStr.substring(begin);

		goodStr.replaceAll("\r|\n|\t", "");

		return goodStr;
	}
}
