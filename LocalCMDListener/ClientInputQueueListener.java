package LocalCMDListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

import Client.Client;
import ClientServerServices.Admin;
import ClientServerServices.LogIn;
import ClientServerServices.User;
import Queue.ACQueue;
import Queue.Item;
import Server.Server;
import Queue.WriteQueue;

	
public class ClientInputQueueListener implements Runnable {
	private WriteQueue wq;
	private Item its;
	private Server server;
	private LogIn login;
	private User user;
	private Admin admin;
	private Random rand=new Random();
	private ACQueue ac = new ACQueue();
	private static AtomicBoolean updated = new AtomicBoolean(true);
	private HashMap<Integer,Item> commandQueue;
	public ClientInputQueueListener(int port){//@authors: created by Dino
		this.its = new Item();
		this.server= new Server(port, 0, "C:/Users/James/Desktop/SmarthouseDatabase/SmartHouseRemoteServerOld/bin/keystore.jks", "password", "pwnage12",null,"exit");/*/home/v4h4/git/SmartHouseLocalServer/keystore.jks"*/
		
		Thread connectionListener = new Thread(server,"Server");
		
		connectionListener.start();
		this.wq = server.getTheQueue();
		this.user = new User();
 		this.admin = new Admin();
 		this.login = new LogIn();
		commandQueue = wq.getMap();
		System.out.println("Object av protokollet är skapade");
	}
	
	
	
	public void run() {
		commandsReceiver();
	}
	
	

	
	private boolean isMalawareCode(String command){//@authors: created by Dino
		String allowed="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_!1234567890#";
		int counter=0;
		for(int i=0;i<command.length();i++){
			for(int q=0;q<allowed.length();q++){
				if(command.charAt(i)==allowed.charAt(q)){
					counter++;
				}
			}
		}
		if(counter==command.length() && command.contains("!")){
			return false;
		}
		return true;
	}
	
	private synchronized void setNewUpdates(){//@authors: created by Dino
		wq.setHasAddedCommands(true);
		System.out.println("UPDATED	 -- commandQueue=wq.getMap();");
		for(int i=0;i<commandQueue.size();i++){
			((Item) commandQueue.get(i)).setPriority(rand.nextInt(5));
		}
	}
	 
	private synchronized void checkForUpdates(){//@authors: created by Dino
		if(wq.getHasAddedCommands()==true){//stop executing and update hasmap and sort it
			
			wq.setHasAddedCommands(false);
			System.out.println("UPDATED	 -- commandQueue=wq.getMap();");
			Item item;
			for(int i=0;i<commandQueue.size();i++){
				item = commandQueue.get(i);
				item.setPriority(rand.nextInt(11));
				item.setUserPrio(0);
			}
		}
	}
	
	private synchronized void authenticationForUnauthorizedClients(int i){//@authors: created by Dino
		Item item = commandQueue.get(i);
		System.out.println("item.getUser() == "+item.getUser());
		System.out.println("item.isAnswered() == "+item.isAnswered());
		if(item.isAnswered()==false && item.getUser()==null && convertStringToArrayList(item.getCmd()).get(0).equals("login")){
			System.out.println("START - whole login process: "+0);
			long runningTime=System.currentTimeMillis();
			ArrayList authentication=login.getSSN_WhenClientHasSuccessfullyLoggedIn(item.getCmd());
			if(authentication.get(0).equals("FAILD")){
				item.setPriority(0);		
				item.setReply("error_You have FAILD the authentication");
			}else{
				System.out.println("authentication.get(0) == "+authentication.get(0));
				System.out.println("authentication.get(1) == "+authentication.get(1));
				item.setUser(authentication.get(0).toString());//doesn't work to send cmd, and to fail to login
				item.setPriority((int)authentication.get(1));		
				System.out.println("((Item) wq.getMap().get("+i+")).getUser() == "+item.getUser());
				System.out.println("((Item) wq.getMap().get("+i+")).getPriority() == "+item.getPriority());	
				item.setReply("login_You have successfully logged sozos in as "+authentication.get(0).toString());
			
			}
			runningTime = System.currentTimeMillis()-runningTime;
			System.out.println("END - whole login process: "+runningTime);
			runningTime=0;
			//checkForUpdates();
		}		
	}

	
	private synchronized boolean handleAuthorizedUsers(int i){//@authors: created by Dino
		Item item = commandQueue.get(i);
		System.out.println("\n\nwq.getMap().get(i)).getUserPrio() == "+item.getUserPrio());
		System.out.println("wq.getMap().get(i)).getUser() == "+item.getUser());
		System.out.println("wq.getMap().get(i)).isAnswered() == "+item.isAnswered()+"\n\n");
		if(item.getUserPrio()==0 && item.getUser()!=null && item.isAnswered()==false ){//user
			System.out.println("START - whole user process: "+0);
			long runningTime=System.currentTimeMillis();		
			System.out.println("3.1");
			String command=item.getCmd();
			String userResult=user.taskHandler(convertStringToArrayList(command));
			if(userResult.equals("FAILD")){
				item.setReply("error_Something FAILD in the taskhandler");
			}else{
				item.setReply(convertStringToArrayList(command).get(0)+"_"+userResult);
			}
			runningTime = System.currentTimeMillis()-runningTime;
			System.out.println("END - whole user process: "+runningTime);
			runningTime=0;
			//checkForUpdates();
		}
		return true;
	}
	
	private synchronized void authorizedAdmins(int i){//@authors: created by Dino
		Item item =commandQueue.get(i);
		if(item.getUserPrio()==1 && item.getUser()!=null && item.isAnswered()==false && wq.getHasAddedCommands()==false){//admin
			System.out.println("3.3");
			System.out.println("\n\nINNE I admin-if satsen\n\n");
			admin.taskHandler(convertStringToArrayList(item.getCmd()));
		}
	}
	
	public synchronized void commandsReceiver(){//@authors: created by Dino,Zijie,Tarik 
		try{
			int prio=10;
			while(true){
				checkForUpdates();
				for(int i = 0; i < commandQueue.size(); i++) {
					Item item;
					if((item= commandQueue.get(i)).isAnswered() == false) {
						System.out.println("wq.getMap().size() == "+commandQueue.size());
						checkForUpdates();
						System.out.println("prio == "+prio);
						if(item.getPriority() == prio){
							authenticationForUnauthorizedClients(i);
							handleAuthorizedUsers(i);
							authorizedAdmins(i);
						}
						prio--;
						if(prio==-1){
							prio=10;
						}
					} 
				}	 
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private boolean sessionKeyIsValid(String SessionKey){
		return true;
	}
	
	
	private ArrayList convertStringToArrayList(String stringList) {//@authors: created by Dino
		ArrayList taskList = new ArrayList();
		int lastSeparator = -1;
		for (int i = 0; i < stringList.length(); i++) {
			if (stringList.charAt(i) == '_') {
				taskList.add(stringList.subSequence(lastSeparator + 1, i));
				lastSeparator = i;
			}
		}
		return taskList;
	}
}
