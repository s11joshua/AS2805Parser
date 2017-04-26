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
	
	public static void Translate(){
		int counter = 0;
		int transactionCounter = 0;
		
		while(TransactionbasedonTerminalID[counter][transactionCounter] != null){
			
			transactionCounter = 1;
			while(TransactionbasedonTerminalID[counter][transactionCounter] != null){
				String AS2805ParsedMessage = TransactionbasedonTerminalID[counter][transactionCounter];
				String lines[] = AS2805ParsedMessage.split("\\r?\\n\\n");
				String ReturnString = "";
				for (String s: lines) {
					if(s.toString().substring(0, 2).equals("4:")){
						ReturnString = ReturnString + "4: " + AS2805Parser.Feild04(AS2805ParsedMessage) + "\n";						
					}else if(s.toString().substring(0, 3).equals("22:")){
						ReturnString = ReturnString + s.toString() + "\n" + AS2805Parser.Feild22(AS2805ParsedMessage) + "\n";
					}else if(s.toString().substring(0, 3).equals("25:")){
						ReturnString = ReturnString + s.toString() + "\n" + AS2805Parser.Feild25(AS2805ParsedMessage) + "\n";
					}else if(s.toString().substring(0, 3).equals("32:")){
						ReturnString = ReturnString + s.toString() + "\n" + AS2805Parser.Feild32(AS2805ParsedMessage) + "\n";
					}else if(s.toString().substring(0, 3).equals("39:")){
						ReturnString = ReturnString + s.toString() + "\n" + AS2805Parser.Feild39(AS2805ParsedMessage) + "\n";
					}else if(s.toString().substring(0, 3).equals("41:")){
						ReturnString = ReturnString + s.toString() + "\n" + AS2805Parser.Feild41(AS2805ParsedMessage) + "\n";
					}else if(s.toString().substring(0, 3).equals("62:")){
						ReturnString = ReturnString + s.toString() + "\n" + AS2805Parser.Feild62(AS2805ParsedMessage) + "\n";
					}else if(s.toString().substring(0, 3).equals("63:")){
						ReturnString = ReturnString + s.toString() + "\n" + AS2805Parser.Feild63(AS2805ParsedMessage) + "\n";
					}
					else{
						ReturnString = ReturnString + s.toString() + "\n"+ "\n";
					}
					 
				}
				TransactionbasedonTerminalID[counter][transactionCounter] = ReturnString;
				/*AS2805Parser.Feild04(AS2805ParsedMessage);
				AS2805Parser.Feild22(AS2805ParsedMessage);
				AS2805Parser.Feild25(AS2805ParsedMessage);
				AS2805Parser.Feild32(AS2805ParsedMessage);
				AS2805Parser.Feild39(AS2805ParsedMessage);
				AS2805Parser.Feild41(AS2805ParsedMessage);
				//AS2805Parser.Feild42(AS2805ParsedMessage);
				AS2805Parser.Feild62(AS2805ParsedMessage);
				AS2805Parser.Feild63(AS2805ParsedMessage);*/
				//Helper.WriteToTxtFile(TransactionbasedonTerminalID[counter][transactionCounter].toString(), executionfolder + Filename);
				System.out.println(TransactionbasedonTerminalID[counter][transactionCounter]);
				transactionCounter ++;
			}
			transactionCounter = 0;
			counter ++;
		}
	}
	
}