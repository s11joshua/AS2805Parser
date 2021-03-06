package CoreClass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Helper {
	public static String GetConfigParameter(String ParameterKey){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("resources/Config.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			
		return prop.getProperty(ParameterKey);
	}
	
	public static void WriteToTxtFile(String Content, String PathforFileName){
		File file = new File(PathforFileName);
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(Content);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean CreateFile(String path, String Filename){
		try {
			Files.createFile(Paths.get(path + Filename));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean CreateDirectory(String RootFolder, String DirectoryName){
		try {
			Files.createDirectories(Paths.get(RootFolder + DirectoryName));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static void GetAllFileNamesFromDirectory(){
	File folder = new File(Helper.GetConfigParameter("WireSharkFileLocation"));
	File[] listOfFiles = folder.listFiles();
	int counter = 0; 
		for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	
		    	if (listOfFiles[i].getName().substring(listOfFiles[i].getName().length() - 3).equals("txt")){
		    		FileParsing.FileList[counter] = listOfFiles[i].getAbsolutePath();
		    		counter ++;
		    	}
		      }
		}		    
	}
	
	public static String FillAS2805Messages(String Message,int MessageLength,String FillValue, boolean prefix){
		String FillString = "";
		int counter = 0;
		int ExpectedStringLength = MessageLength;
		int ActualMessageLength = Message.length();
		while(counter <= (ExpectedStringLength-ActualMessageLength)){
			FillString = FillString + FillValue;
			counter ++;
		}
		if(prefix){
			return FillString + Message;
		}else{
			return Message + FillString;
		}
			
	}
	
	public static String ConvertAs$Amount(String Amount, int numberofdecimalplaces){
		String ReturnString = null;
		if (Long.parseLong(Amount) > 0 ){
			ReturnString = "$" + Long.parseLong(Amount.subSequence(0,(Amount.length() - numberofdecimalplaces)).toString());
			ReturnString = ReturnString + "." + Amount.substring(Amount.length() - numberofdecimalplaces).toString();
			return ReturnString;
		}else{
			ReturnString = "$0";
			return ReturnString;
		}
	}
	
	public static String ConverttoDecimal(String Amount, int numberofdecimalplaces){
		String ReturnString = null;
		
		if (Long.parseLong(Amount) > 0 ){
			ReturnString = Long.toString(Long.parseLong(Amount.subSequence(0,(Amount.length() - numberofdecimalplaces)).toString()));
			ReturnString = ReturnString + "." + Amount.substring(Amount.length() - numberofdecimalplaces).toString();
			return ReturnString;
		}else{
			ReturnString = "0";
			return ReturnString;
		}
	}
}
