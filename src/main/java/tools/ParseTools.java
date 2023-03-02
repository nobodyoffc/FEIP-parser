package tools;

import com.google.gson.Gson;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ParseTools {
		
	public static void gsonPrint(Object ob) {
		Gson gson = new Gson();
		System.out.println("***********\n"+ob.getClass().toString()+": "+gson.toJson(ob)+"\n***********");
		return ;
	}

	public static String gsonStr(Object ob) {
		Gson gson = new Gson();
		return ob.getClass().toString()+": "+gson.toJson(ob);
	}

    public static String strToJson(String rawStr) {

		if(!rawStr.contains("{")) return null;

		int begin = rawStr.indexOf("{");

		String goodStr = rawStr.substring(begin);

		goodStr.replaceAll("\r|\n|\t", "");

		return goodStr;
	}
}
