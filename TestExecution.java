package CoreClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestExecution {
	public static String TestFolder;
	public static String RootFolder;
	public static String executionfolder;
	public static String TestResultUnsortedFile;
	static final int NumberOfTerminals = 10;
	public static String[][] TransactionbasedonTerminalID = new String[NumberOfTerminals][100]; 
	
	public static void CretareTestExecutionFolder(){
		RootFolder = Helper.GetConfigParameter("TestResultFolder");
		TestFolder =  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		Helper.CreateDirectory(RootFolder,TestFolder);
		executionfolder = RootFolder + TestFolder + "\\";
		
	}
	
	public static void SortTransactionsbasedonTerminalID(String TerminalID,String Transaction){
		
		int counter = 0;
		int transactionCounter = 0;
		boolean MatchedTerminalID = false;
		while(counter < NumberOfTerminals){
			if (TransactionbasedonTerminalID[counter][0] == null ){
				break;
			}else{
				if(TransactionbasedonTerminalID[counter][0].toString().equals(TerminalID)){
					MatchedTerminalID = true;
					transactionCounter = 1;
					while(TransactionbasedonTerminalID[counter][transactionCounter] != null){
						transactionCounter ++;
					}
					TransactionbasedonTerminalID[counter][transactionCounter] = Transaction;
					break;
				}
			}			
		counter ++;
		}
		
		if (MatchedTerminalID != true){
			TransactionbasedonTerminalID[counter][0] = TerminalID;
			TransactionbasedonTerminalID[counter][1] = Transaction;
		}
	}
	
	public static void CreateResultFiles(){
		int counter = 0;
		int transactionCounter = 0;
		
		while(TransactionbasedonTerminalID[counter][transactionCounter] != null){
			String Filename = TransactionbasedonTerminalID[counter][transactionCounter].toString() + "_" + new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime()) + ".txt";
			Helper.CreateFile(executionfolder, Filename);
			transactionCounter = 1;
			while(TransactionbasedonTerminalID[counter][transactionCounter] != null){
				Helper.WriteToTxtFile(TransactionbasedonTerminalID[counter][transactionCounter].toString(), executionfolder + Filename);
				transactionCounter ++;
			}
			transactionCounter = 0;
			counter ++;
		}
	}
	
}
