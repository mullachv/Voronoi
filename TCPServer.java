package p5_GravitationalVoronoiGame;

import java.io.*;
import java.net.*;

// This Class is just for sucket testing
class TCPServer
{
   public static void main(String argv[]) throws Exception
      {
         ServerSocket welcomeSocket = new ServerSocket(8001);

         while(true)
         {
             System.out.println("server[1]");
            Socket connectionSocket = welcomeSocket.accept();
            
            System.out.println("server[0]");
            BufferedReader inFromClient =
               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            
            System.out.println("server[2]");
            
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            //clientSentence = inFromClient.readLine();
            //System.out.println("Received: " + clientSentence);
            //capitalizedSentence = clientSentence.toUpperCase() + '\n';
            
            System.out.println("server[3]");
            
            outToClient.writeBytes("TEAM");
            
            System.out.println("server[4]");
         }
      }
}
