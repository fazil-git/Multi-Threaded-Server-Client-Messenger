package day12assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//prototype design pattern
public class Server implements Cloneable{
	static ServerSocket serverSocket;
	Socket socket;
	static PrintStream printStream;
	BufferedReader in;
	static BufferedReader keyIn;
	static Server obj;
	static List<Server> objList;
	static Map<String, Socket> map;
	
	public Server() {
		try {
			serverSocket=new ServerSocket(2000);
			System.out.println("Server ready for client..");

		}catch(Exception e) {}
	}
	
	static Server getServer() throws CloneNotSupportedException {
		if(obj==null) obj=new Server();
		return (Server) obj;
	}
	
	public void getConnection(Socket socket) throws IOException {
		in=new BufferedReader(new InputStreamReader(socket.getInputStream()));	//To get client input
		String name=in.readLine();
		map.put(name, socket);

		while(true) {
			String msg=in.readLine();
			System.out.println("Message from client ("+name+"-"+socket.getPort()+") : "+msg);
			System.out.println();
		}
	}
	
	public void serverPublish() throws IOException {
		keyIn=new BufferedReader(new InputStreamReader(System.in));				//To enter your input
		
		while(true) {
			System.out.println("Server- Enter your input: ");
			String keyInMessage=keyIn.readLine();
			
			String[] name=keyInMessage.split(" ");
			if(map.containsKey(name[0])) {
				socket=map.get(name[0]);
			
			printStream=new PrintStream(socket.getOutputStream(), true);
			printStream.println(keyInMessage.substring(name[0].length()-1));
			}
			else {
				System.out.println("Enter the client name please!! ");
				System.out.println("Enter the name leave a <space> and then enter your message..");
			}
		}
	}
	
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		ExecutorService es=Executors.newFixedThreadPool(4);	
		map=new HashMap<String, Socket>();
		Server objX=Server.getServer();
		
		
		es.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Server obj1=(Server) obj.clone();
					obj1.socket=serverSocket.accept();
					System.out.println("Client connected in port number: "+obj1.socket.getPort());
					obj1.getConnection(obj1.socket);
				} catch (CloneNotSupportedException | IOException e) {
					e.printStackTrace();
				}
			}
		}); 
		
		es.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Server obj2=(Server) obj.clone();
					obj2.socket=serverSocket.accept();
					System.out.println("Client connected in port number: "+obj2.socket.getPort());
					obj2.getConnection(obj2.socket);
				} catch (CloneNotSupportedException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		objX.serverPublish();
	}
}