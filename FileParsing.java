package CoreClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;


public class FileParsing {

	static String[][] TransactionRawArray = new String[1000][20];
	static String[][] TrimmedTransactionArray = new String[1000][20];
	static String[] Transactions = new String[1000];
	static String RawFile = null;
	public static String[] FileList = new String[100];
	public static String ErrorList = null;
	
	public static void main(String[] args) {
		
		try {
						
			int counter = 0;
			Helper.GetAllFileNamesFromDirectory();
			while(FileList[counter] != null){
				ReadWireSharkRawFile(FileList[counter].toString());
				counter ++;
			}
			ParseWireSharkFile();
			TestExecution.CretareTestExecutionFolder();
			int TransactionCounter = 1;
			while(Transactions[TransactionCounter] != null){
				String ParsedMessage = AS2805Parser.ParseAS2805Message(Transactions[TransactionCounter].toString());
				if(ParsedMessage != null){
					TestExecution.SortTransactionsbasedonTerminalID(AS2805Parser.ReturnElemnetValue("41",ParsedMessage),ParsedMessage);
				}
				TransactionCounter ++;
			}			
			TestExecution.CreateResultFiles();
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
		
	public static void ReadWireSharkRawFile(String FileName) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(FileName));
		
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    RawFile = RawFile + sb.toString();
		    
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    br.close();
		    
		}
				
	}
	
	public static void ParseWireSharkFile(){
		BufferedReader bufReader = new BufferedReader(new StringReader(RawFile));
		String ParseString = Helper.GetConfigParameter("WireSharkParseString");
		String line=null;
		int TransactionArrayCounter = 1;
		try {
			while( (line=bufReader.readLine()) != null ){
				if(line.length() >= 6 && line.subSequence(0, 6).toString().equals(ParseString)){
					
					int Counter = 1;
					do{
						TransactionRawArray[TransactionArrayCounter][Counter] = line;
						TrimmedTransactionArray[TransactionArrayCounter][Counter] = (line.subSequence(6, 55).toString()).replaceAll(" ", "");
						if (Counter == 1){
							Transactions[TransactionArrayCounter] = (line.subSequence(6, 55).toString()).replaceAll(" ", "");
						}else{
							Transactions[TransactionArrayCounter] = Transactions[TransactionArrayCounter] + (line.subSequence(6, 55).toString()).replaceAll(" ", "");
						}
						
						Counter ++;	
					}while((line=bufReader.readLine()) != null && line.length() > 1 && line.subSequence(0, 1).toString().equals("0"));
					TransactionArrayCounter ++;
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
