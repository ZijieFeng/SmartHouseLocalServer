import java.util.ArrayList;

import Queue.WriteQueue;


public class AurdinoRealTimeInputQueueListener implements Runnable {
	WriteQueue wq = new WriteQueue(0);
	
	
	
	
	public void run(){
		/*
		if(admin==null && user!=null){
			userRealTimeListenerArduino("make listener here");
		}else{
			adminRealTimeListenerArduino();
		}
		*/
	}
	/*

	private void userRealTimeListenerArduino(String arduinoInput){
		while(user.userConnected==true){
		if(arduinoInput.equals("all_chk!")){
			user.taskHandler(convertStringToArrayList("checkAllDevices:"));
		}
		else if(arduinoInput.equals("sa_on!")){
			user.taskHandler(convertStringToArrayList("toggleDevice:3:true:"));
		}
		else if(arduinoInput.equals("sa_off!")){
			user.taskHandler(convertStringToArrayList("toggleDevice:3:false:"));
		}
		else if(arduinoInput.equals("li_on!")){
			user.taskHandler(convertStringToArrayList("toggleDevice:16:true:"));
		}
		else if(arduinoInput.equals("li_off!")){
			user.taskHandler(convertStringToArrayList("toggleDevice:16:false:"));
		}
		else if(arduinoInput.equals("fa_on!")){
			
		}
		else if(arduinoInput.equals("fa_off!")){
			
		}
		else if(arduinoInput.equals("wl_off!")){
			
		}
		else if(arduinoInput.equals("wl_off!")){
			
		}
	}
	if(user.userConnected==false){
		Thread.currentThread().stop();
	}//more to come
	
	}

private void adminRealTimeListenerArduino(){
	//make listener here
	
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
}*/
	
}
