import GIPSClient.ConnectClientToRemoteServer;
import LocalCMDListener.ClientInputQueueListener;



public class StartLocalServer {

	
	public static void main(String[] args) {
		try{
			int port=7238;//this localserver has port 7238
			int remoteServerPort=5906;
			ConnectClientToRemoteServer clientToRemote = new ConnectClientToRemoteServer(remoteServerPort,port);
			ClientInputQueueListener ls = new ClientInputQueueListener(port);//port
			Thread localServer = new Thread(ls,"Local Sever");
			System.out.println("ciq.commandsReceiver();");
			localServer.start();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		
		//RemoteServerListener rs= new RemoteServerListener(1236);
		//Thread remoteServer = new Thread(rs,"Remote Server");
		//remoteServer.start();
		//ciq.commandsReceiver();
	}

}
