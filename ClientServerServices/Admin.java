package ClientServerServices;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import ClientDatabaseServices.DbQueue;


public class Admin {
	DataInputStream serverInput;// PRELIMINARY PROTOCOL FUNCTION
	DataOutputStream serverOutput;// PRELIMINARY PROTOCOL FUNCTION
	DbQueue db_queue = null;
	private String user_ssn = null;

	private boolean userConnected = false;
	
	public void Admin(){
		this.userConnected = true;
		db_queue = new DbQueue("DEFUALT Admin");
	}
	
	public void taskHandler(ArrayList task) {
		System.out.println("taskHandler");
		if (task.get(0).equals("")) {// reacts on if-stmt
			//db_queue.adb.någonmetod();
			ArrayList send = new ArrayList();
			//sendStringOutputToUser(db_queue.adb.metod(parameter));// sends arrayList
		}
		if (task.get(0).equals("insertUser")) {// reacts on if-stmt
			//db_queue.adb.någonmetod();
			ArrayList send = new ArrayList();
			//sendStringOutputToUser(db_queue.adb.insertUser((String)task.get(1),(int)task.get(2),(boolean)task.get(3),(double)task.get(4))));// sends arrayList
		}
	}

	private String convertArrayListToString(ArrayList arrayList) {
		String stringList = "";
		int lastSeparator = -1;
		for (int i = 0; i < arrayList.size(); i++) {
			stringList = stringList + arrayList.get(i) + ":";
		}
		return stringList;
	}
}

