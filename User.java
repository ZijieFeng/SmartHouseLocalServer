import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class User implements Runnable {
	private DataInputStream serverInput;// PRELIMINARY PROTOCOL FUNCTION
	private DataOutputStream serverOutput;// PRELIMINARY PROTOCOL FUNCTION
	private DbQueue db_queue = null;
	private String user_ssn = null;

	boolean userConnected = false;
	private Socket socket = null;

	public User(Socket socket, String user_ssn) {//
		this.socket = socket;
		this.userConnected = true;
		this.user_ssn = user_ssn;
		db_queue = new DbQueue("User", this.user_ssn);
		RealTimeListenerArdunio rt = new RealTimeListenerArdunio(this,null);//user,admin
	    Thread realTime =new Thread(rt,"userRealTimeListenerThread");
	    realTime.start();
		
		// maybe send all important info to user like, name, ssn,permissions etc
	}// test

	public void run() {
		while (userConnected == true) {
			try {
				System.out.println("är på väg in i taskhandler");
				taskHandler(convertStringToArrayList(getStringInputFromUser()));
			} catch (Exception ex) {
				ex.printStackTrace();
				userConnected = false;
			}
		}
	}
	
	
	public void taskHandler(ArrayList task) {
		System.out.println("taskHandler");
		if (task.get(0).equals("toggleDevice") && task.isEmpty()==false && task.size()==3) {// reacts on if-stmt
			//
			sendStringOutputToUser(convertArrayListToString(db_queue.udb.toggleDevice(Integer.parseInt(task.get(1).toString()), Boolean.parseBoolean(task.get(2).toString()))));		
		}else if(task.get(0).equals("checkDevice") && task.isEmpty()==false && task.size()==2){
			String print=convertArrayListToString(db_queue.udb.checkDevice(Integer.parseInt(task.get(1).toString())));
			System.out.println("checkDevice("+Integer.parseInt(task.get(1).toString())+") == "+print);
			sendStringOutputToUser(print);		
		}else if(task.get(0).equals("testDevice") && task.isEmpty()==false && task.size()==2){
			//sendStringOutputToUser(convertArrayListToString(db_queue.udb.testDevice(Integer.parseInt(task.get(1).toString()))));		
		}else if(task.get(0).equals("checkAllDevices") && task.isEmpty()==false && task.size()==1){
			sendStringOutputToUser(convertArrayListToString(db_queue.udb.checkAllDevices()));
		}else{
			sendStringOutputToUser("TotallyWrong:");
		}	
	
		System.out.println("\n\ntask.size() == "+task.size()+"\n\n");
		for(int i=0;i<task.size();i++){
			System.out.println("task.get(i) == "+task.get(i));
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
			System.out
					.println("\n\nERROR - Class: User(Local Server)\nMethod: private String getStringInputFromUser()");
		}
		return input;
	}

	private void sendStringOutputToUser(String output) {
		System.out.println(Thread.currentThread().getName()
				+ " �r inne i sendOutputToUser(String output) ");
		try {
			serverOutput = new DataOutputStream(socket.getOutputStream());
			serverOutput.writeUTF(output);
			serverOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.out
					.println("\n\nERROR - Class: User(Local Server)\nMethod: private String sendStringOutputToUser(String output)");
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
