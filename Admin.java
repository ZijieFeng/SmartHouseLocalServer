import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;


public class Admin implements Runnable {
	DataInputStream serverInput;// PRELIMINARY PROTOCOL FUNCTION
	DataOutputStream serverOutput;// PRELIMINARY PROTOCOL FUNCTION
	DbQueue db_queue = null;
	private String user_ssn = null;

	private boolean userConnected = false;
	private Socket socket = null;
	
	public void Admin(){
		this.socket = socket;
		this.userConnected = true;
		this.user_ssn = user_ssn;
		db_queue = new DbQueue("Admin", this.user_ssn);
	}
	
	public void run() {// As long as user is connected the taskhandler will wait
		// for instructions from user to calculate and retrun
		// answer
		while (userConnected == true) {
			try {
				taskHandler(convertStringToArrayList(getStringInputFromUser()));
			} catch (Exception ex) {
				ex.printStackTrace();
				userConnected = false;
			}
		}
	}

	private void taskHandler(ArrayList task) {
		System.out.println("taskHandler");
		if (task.get(0).equals("")) {// reacts on if-stmt
			//db_queue.adb.någonmetod();
			ArrayList send = new ArrayList();
			//sendStringOutputToUser(db_queue.adb.metod(parameter));// sends arrayList
		}
	}

	private String getStringInputFromUser() {
		System.out.println(Thread.currentThread().getName()
				+ " �r inne i getInputFromUser() ");
		String input = "";
		try {
			serverInput = new DataInputStream(socket.getInputStream());
			input = serverInput.readUTF();
			return input;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("\n\nERROR - Class: User(Local Server)\nMethod: private String getStringInputFromUser()");
		}
		return input;
	}

	private void sendStringOutputToUser(String output) {
		System.out.println(Thread.currentThread().getName()+ " �r inne i sendOutputToUser(String output) ");
		try {
			serverOutput = new DataOutputStream(socket.getOutputStream());
			serverOutput.writeUTF(output);
			serverOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("\n\nERROR - Class: User(Local Server)\nMethod: private String sendStringOutputToUser(String output)");
		}
	}

	private ArrayList convertStringToArrayList(String stringList) {
		ArrayList taskList = new ArrayList();
		int lastSeparator = -1;
		for (int i = 0; i < stringList.length(); i++) {
			if (stringList.charAt(i) == ':') {
				taskList.add(stringList.subSequence(lastSeparator + 1, i));
				lastSeparator = i;
			}
		}
		return taskList;
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

