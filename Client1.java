package day12assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client1 {
	Socket socket;
	BufferedReader in, keyIn;
	PrintStream printStream;
	
	public Client1() {
		try {
			socket=new Socket("localhost", 2000);
			System.out.println("Client1 ready..");
			
			
			keyIn=new BufferedReader(new InputStreamReader(System.in));				//To enter your input
			printStream=new PrintStream(socket.getOutputStream(), true);
			
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));	//To get server input
			ExecutorService es=Executors.newSingleThreadExecutor();
			
			System.out.println("Enter your name: ");
			String name=keyIn.readLine();
			printStream.println(name);
			
			while(true) {
				es.execute(new Runnable() {
					@Override
					public void run() {
						System.out.println("Client1 - Enter your message: ");
						String keyInMessage = null;
						try {
							keyInMessage = keyIn.readLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
						printStream.println(keyInMessage);
					}
				});
				
				
				String msg=in.readLine();
				System.out.print("Message from Server: ");
				System.out.println(msg);
				System.out.println();
			}
			
		}catch(Exception e) {}
	}
	
	public static void main(String[] args) {
		new Client1();
	}
}