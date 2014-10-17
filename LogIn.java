import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.mysql.fabric.xmlrpc.Client;

public class LogIn implements Runnable {
	DataInputStream serverInput;//PRELIMINARY PROTOCOL FUNCTION
    DataOutputStream serverOutput;//PRELIMINARY PROTOCOL FUNCTION
    private boolean userLogin =  false;
    private String userID = "";    
    private String userType = "";    
    private Socket socket;
    private String client_ip=null;
    private boolean clinetIsConnected=false;
	public LogIn(Socket socket,String ip){
		this.socket=socket;
		this.client_ip=ip;
		this.clinetIsConnected=true;
	}
	

	public void run() {//ok
		while(clinetIsConnected==true){
			try{
				loginHandler(convertStringToArrayList(getStringInputFromUser()));
			}catch(Exception ex){
				ex.printStackTrace();
				clinetIsConnected=false;
				System.out.println("\n\nClient with the ip number: "+client_ip+" has disconnected from the server\n\n");
				 Thread.currentThread().stop();
			}
		}		
	}    
	
	
	
	private void loginHandler(ArrayList usernamePassword){
			try{
				System.out.println("loginHandler");
				System.out.println("usernamePassword.size() == "+usernamePassword.size());
				for(int i=0;i<usernamePassword.size();i++){
					System.out.println("\n\nusernamePassword.get("+i+")) == "+usernamePassword.get(i)+"\n\n");
				}
				if(usernamePassword.isEmpty()==false && authenticationIsCorrect(usernamePassword.get(0).toString(),usernamePassword.get(1).toString())==true){
					User user=new User(socket,"9310101337");
			        Thread login =new Thread(user,"ssn");
			        login.start();
			        System.out.println("Has been sent away to User");
			        Thread.currentThread().stop();//ok
				}else{
					ArrayList result = new ArrayList();
					result.add("Wrong password or username");
					sendStringOutputToUser("ok");;
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
	}
	
	private boolean authenticationIsCorrect(String username, String password){
		System.out.println("userName: " + username);
		System.out.println("password: " + password);
		if(username.equals("username") && password.equals("password")){
			System.out.println("authenticationIsCorrect("+username+", "+password+") == return true;");
			return true;
		}else{
			System.out.println("authenticationIsCorrect("+username+", "+password+") == return false;");
			return false;
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