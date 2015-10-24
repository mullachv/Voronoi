package p5_GravitationalVoronoiGame;

import java.io.*;
import java.net.*;

public class TCPClient {
	
	Socket clientSocket;
	DataOutputStream outToServer;
	//BufferedReader inFromServer;
	InputStream is;
	
	public void startTCP(String host, int port) throws UnknownHostException, IOException{
		clientSocket = new Socket(host, port);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		//inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		is = clientSocket.getInputStream();
	}
	
	public void closeTCP() throws IOException{
		clientSocket.close();
	}
	
	public String read() throws IOException{
		//String s = inFromServer.readLine();
		String s = readAll();
		return s;
	}
	
	public String readAll() throws IOException{
		byte[] b = new byte[1024];
		int msg = is.read(b);
		return new String(b, 0, msg);
	}
	
	public void write(String output) throws IOException{
		outToServer.writeBytes(output);
	}
}
