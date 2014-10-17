import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SocketListener {
	private ServerSocket serversocket=null;
	private Socket socket=null;
	private int PORT=1111;
	
	public static void main(String[] args) {
//		UserDb UsDb = new UserDb(null); //
		SocketListener sl = new SocketListener();
		sl.initializingSocket();
		sl.handlingSocketConnections();
	}
	
	private void initializingSocket(){//ok
		try{//ok
			System.out.println("initializingSocket()  - once");
			serversocket = new ServerSocket(PORT);
			System.out.println("The Local Server of the SmartHouse runs and is operative\n\n\n");
			}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("serversocket = new ServerSocket(PORT) - FAILED in CLASS SocketListener in method initilizingSocket()");
		}
	}
	
	private void handlingSocketConnections(){
		while(true){
			try {
	            socket = serversocket.accept();
	            LogIn login= new LogIn(socket,getIpAdress());
	            Thread client =new Thread(login,getIpAdress());
	            System.out.println("The user that has connected to the local server has IP number: "+getIpAdress());
	            System.out.println("handlingSocketConnections() - once");
	            client.start();
	        } catch (IOException ex) {
	        	ex.printStackTrace();
				System.out.println("serversocket = new ServerSocket(PORT) - FAILED in CLASS SocketListener in method initilizingSocket()");
	            Logger.getLogger(SocketListener.class.getName()).log(Level.SEVERE, null, ex);
	        }
		}
	}
	
	private String getIpAdress(){
    	String ipAdress=socket.getLocalSocketAddress().toString();
    	int start=0,end=0;
    	try {
			InetAddress ip=InetAddress.getLocalHost();
			for(int i=0;i<socket.getLocalSocketAddress().toString().length();i++){
				if(ipAdress.charAt(i)=='/'){
					start=i+1;
				}
				if(ipAdress.charAt(i)==':'){
					end=i;
				} 
			}
			socket.getLocalSocketAddress().toString();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
    	return ipAdress.substring(start, end);
    }
}