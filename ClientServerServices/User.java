package ClientServerServices;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ClientDatabaseServices.DbQueue;

public class User{
	private DataInputStream serverInput;// PRELIMINARY PROTOCOL FUNCTION
	private DataOutputStream serverOutput;// PRELIMINARY PROTOCOL FUNCTION
	private DbQueue db_queue = null;
	private String user_ssn;

	boolean userConnected = false;

	public User() {//
		this.userConnected = true;
		db_queue = new DbQueue("user");
		
		// maybe send all important info to user like, name, ssn,permissions etc
	}// test
	
	public String taskHandler(ArrayList task) {
		System.out.println("taskHandler");
		for(int i=0;i<task.size();i++){
			System.out.println("task.get(i) == "+task.get(i));
		}
		if (task.get(0).equals("toggleDevice") &&  task.size()==3) {// reacts on if-stmt
			System.out.println("inne i toggleDevice");
			return convertArrayListToString(db_queue.udb.toggleDevice(Integer.parseInt(task.get(1).toString()),Integer.parseInt(task.get(2).toString()) ));		
		}else if(task.get(0).equals("checkDevice") && task.isEmpty()==false && task.size()==2){	
			String print=convertArrayListToString(db_queue.udb.checkDevice(Integer.parseInt(task.get(1).toString())));
			System.out.println("checkDevice("+Integer.parseInt(task.get(1).toString())+") == "+print);
			return print;		
		}else if(task.get(0).equals("testDevice") && task.isEmpty()==false && task.size()==2){
			//return convertArrayListToString(db_queue.udb.testDevice(Integer.parseInt(task.get(1).toString())));		
		}else if(task.get(0).equals("checkAllDevices") && task.isEmpty()==false && task.size()==1){
			return convertArrayListToString(db_queue.udb.checkAllDevices());
		}//else
		return "Incorrect Command!:";	
	}
	
	public String convertArrayListToString(ArrayList arrayList) {
		String stringList = "";
		int lastSeparator = -1;
		for (int i = 0; i < arrayList.size(); i++) {
			stringList = stringList + arrayList.get(i) + "_";
		}
		return stringList;
	}
}
