package CoreClass;

import java.math.BigInteger;


public class AS2805Parser {
	                                                                                                  
	public static String AS2805FieldLength[] = {"0","0","LLvar","6","12","12","12","10","0","8","8", 
												"6","6","4","4","4","4","0","4","3","3",
												"3","4","0","0","2","0","0","8","0","0",
												"0","LLvar","11","0","LLvar","0","24","12","4","3",
												"16","30","40","0","76","0","0","LLLvar","3","3",
												"3","16","0","120","0","0","12","12","12","0",
												"LLLvar","LLLvar","LLLvar","16","0","0","0","0","0","0",
												"0","0","0","0","0","0","0","0","0","0",
												"0","0","0","0","0","0","0","0","0","42",
												"0","0","0","0","0","0","0","0","0","0",
												"0","0","0","0","0","0","0","0","0","0",
												"0","0","0","0","0","0","0","0","0","0",
												"0","0","0","0","0","0","0","16","0","0",												
												};
	
	public static String ParseAS2805Message(String AS2805Message){
				
		/*int TransactionCounter = 1;
		while(FileParsing.Transactions[TransactionCounter] != null){
			String Transaction = FileParsing.Transactions[TransactionCounter];*/
			
			String Transaction = AS2805Message;
			String MessageString = null;
			String TrimTransaction = null;
			String MessageType = null;
			//String TerminalID = null;
			
			MessageString = "Msg type: " + Transaction.substring(47, 50) + "\n" + "\n";
			
			if (Transaction.substring(47, 50).toString().equals("110") || Transaction.substring(47, 50).toString().equals("130")
				|| Transaction.substring(47, 50).toString().equals("210") || Transaction.substring(47, 50).toString().equals("230")	
				|| Transaction.substring(47, 50).toString().equals("430")){
				if(Transaction.substring(47, 50).toString().equals("430")){
					MessageType = " Reversal Response";
				}else{
					MessageType = "Response";
				}
			}else{
				if(Transaction.substring(47, 50).toString().equals("420")){
					MessageType = "Reversal Request";
				}else{
					MessageType = "Request";
				}
				
			}
			
			String BinaryBitmap = new BigInteger(Transaction.substring(50, 66).toUpperCase().toString(), 16).toString(2);
			
			if (BinaryBitmap.length() == 62){
				BinaryBitmap = "00" + BinaryBitmap;
			}else if(BinaryBitmap.length() == 63){
				BinaryBitmap = "0" + BinaryBitmap;
			}
						
			if (BinaryBitmap.substring(0, 1).equals("1")){
				MessageString = MessageString + "Bitmap: " + Transaction.substring(50, 82).toUpperCase() + "\n" + "\n";
				BinaryBitmap = new BigInteger(Transaction.substring(50, 82).toUpperCase().toString(), 16).toString(2);
				TrimTransaction = Transaction.subSequence(82, Transaction.length()).toString();
			}else{
				MessageString = MessageString + "Bitmap: " + Transaction.substring(50, 66).toUpperCase() + "\n" + "\n";
				TrimTransaction = Transaction.subSequence(66, Transaction.length()).toString();
			}
			
			int counter = 0;
			String TransactionType = null;
			for (char BitmapBit : BinaryBitmap.toCharArray()){
				counter ++;
		       	
				if (counter == 3){	
					if(TrimTransaction.subSequence(0,2).toString().equals("38")){
						TransactionType = "Pre-Authorization";
					}else if(TrimTransaction.subSequence(0,2).toString().equals("20")){
						TransactionType = "Refund";
					}else if(TrimTransaction.subSequence(0,2).toString().equals("00")){
						TransactionType = "Purchase";
					}
				}
				
		        if (BitmapBit == '1' && counter >=2){
		        	MessageString = MessageString + counter + ": ";
		        	String BitLength = AS2805FieldLength[counter].toString();
		        	if(BitLength.equals("LLLvar")){
		        		int LengthofDEField = 0;
		        		String FirstValue = Integer.toString((Integer.parseInt((String) TrimTransaction.subSequence(0, 2)) - 30));
		        		String SecondValue = Integer.toString((Integer.parseInt((String) TrimTransaction.subSequence(2, 4)) - 30));
		        		String ThirdValue = Integer.toString((Integer.parseInt((String) TrimTransaction.subSequence(4, 6)) - 30));
		        		LengthofDEField = Integer.parseInt((FirstValue + SecondValue + ThirdValue))*2;
		        		TrimTransaction = TrimTransaction.substring(6).toString();
		        		MessageString = MessageString + TrimTransaction.subSequence(0,LengthofDEField).toString().toUpperCase();
		        		TrimTransaction = TrimTransaction.substring(LengthofDEField).toString();
		        	}else if(BitLength.equals("LLvar")){
		        		int LengthofDEField = Integer.parseInt((String) TrimTransaction.subSequence(0, 2));
		        		if (LengthofDEField % 2 != 0){
		        			LengthofDEField = LengthofDEField + 1;
		        		}	        		
		        		TrimTransaction = TrimTransaction.substring(2).toString();
		        		MessageString = MessageString + TrimTransaction.subSequence(0,LengthofDEField).toString().toUpperCase();
		        		TrimTransaction = TrimTransaction.substring(LengthofDEField).toString();
		        	}else{
		        		int LengthofDEField = Integer.parseInt(AS2805FieldLength[counter].toString());
		        		/*if(counter == 41){
		        			TerminalID = TrimTransaction.subSequence(0,LengthofDEField).toString().toUpperCase();
		        		}*/
		        		MessageString = MessageString + TrimTransaction.subSequence(0,LengthofDEField).toString().toUpperCase();
		        		TrimTransaction = TrimTransaction.substring(LengthofDEField).toString();
		        	}
		        	MessageString = MessageString + "\n" + "\n";
		        }
		    }
			MessageString = "Transaction Details: " + TransactionType + " " + MessageType + "\n" + MessageString;
			return MessageString;
			//System.out.println("Transaction Details: " + TransactionType + " " + MessageType + "\n");
			//System.out.println(Transaction + "\n");
			//System.out.println(MessageString);
			//System.out.println(TerminalID);
			//TestExecution.SortTransactionsbasedonTerminalID(TerminalID,MessageString);
			//Helper.WriteToTxtFile(MessageString, TestExecution.executionfolder + TestExecution.TestResultUnsortedFile);
			
			//TransactionCounter ++;
		//}
	}
	
	public static String ReturnElemnetValue(String DataElementNumber,String AS2805ParsedMessage){
		String lines[] = AS2805ParsedMessage.split("\\r?\\n\\n");
		String ReturnString = null;
		int counter = 0;
		while(lines[counter].toString().length() >= 1){
			if(lines[counter].toString().substring(0, DataElementNumber.toString().length()).equals(DataElementNumber)){
				String[] Temp = lines[counter].toString().replaceAll(" ", "").split(":");
				ReturnString = Temp[1];
				break;
			}
			counter ++;
		}
		
		return ReturnString;
	}
}
