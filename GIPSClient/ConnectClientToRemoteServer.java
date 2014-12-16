package GIPSClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import DDNS.DDNS;
import Queue.ACQueue;
import Client.Client;
import Security.GenerateSessionKey;
import Server.Server;

public class ConnectClientToRemoteServer {
	//GenerateSessionKey secKey = new GenerateSessionKey();
	InetSocketAddress addr = new InetSocketAddress("smarthousehkr.ddns.net",8080);
	Client cl;
	ACQueue ac;
	private SSLSocket socket;
	private Server send;
	private DDNS ddns = new DDNS();
	//*****************************************************************************************
	
	public ConnectClientToRemoteServer(int remotePort,int lsPort){
		String ip="127.0.0.1";
		String myIP="127.0.0.1";
		try{
			ip=ddns.reciveIpFromDDNS();
			System.out.println("ip == "+ip);
			String[] commandsHandler = {"_","no_command","error","exit"};
			cl=new Client(ip,remotePort,"C:/Users/James/Desktop/SmarthouseDatabase/SmartHouseRemoteServerOld/bin/keystore.jks","password",commandsHandler);
			ac = cl.getClientQueue();
			Thread clientThread = new Thread(cl, "ClientThread");
			clientThread.start();
			System.out.println("\n\nNow we try to connect to RS\n\n");
			myIP=ddns.getMyCurrentIp();   
			//String key=secKey.
			sendCommand("SendingIpAndPort_"+myIP+"_"+lsPort+"_");
			System.out.println("sendCommand("+myIP+"_"+lsPort+"_);");
			String compare=reciveCommand();
			System.out.println("reciveCommand() == "+compare);
			//Thread.currentThread().sleep(1000);
			if(compare.equals("SendingIpAndPort_Connection Accomplished_")){
				closeConnection();
				System.out.println("Connection to Remote Server Established");
			}
			
		}catch(Exception ex){
			System.out.println("My Ip is: 127.0.0.1 -- (No internetConnection)\n\n");
		}
		 
	}
	
	private void sendCommand(String message) {
		cl.setCommand(message);
		System.out.println("cl.setCommand("+message+");");	
	}
	
	private String reciveCommand() {
		String reply = cl.getReply();
		System.out.println(reply);
		return reply;
		
		
	}
	
	private ArrayList convertStringToArrayList(String stringList) {
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
	
	private String convertArrayListToString(ArrayList arrayList) {
		String stringList = "";
		int lastSeparator = -1;
		for (int i = 0; i < arrayList.size(); i++) {
			stringList = stringList + arrayList.get(i) + "_";
		}
		return stringList;
	}
	
	private void closeConnection(){
		try{
			System.out.println("cl.quitCommunication();");
			//cl.quitCommunication();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
}
