package p5_GravitationalVoronoiGame;

import java.io.IOException;
import p5_GravitationalVoronoiGame.strategy.*;

public class Main {
	
	TCPClient tcpClient;
	String self = "mv_cly";
	Board board;
	Strategy strategy;
	final String host = "localhost";
	final int port = 1337;
	
	public static void main(String[] args) throws Exception{
		Main m = new Main();
		m.tcpClient = new TCPClient();
		m.tcpClient.startTCP(m.host, m.port);
		m.board = new Board();
		
		// change to your strategy implementation here
		m.strategy = new RandomStrategy();
		
		m.run();
	}
	
	private void run() throws IOException{
		boolean playing = true;
		
		while(playing){
			String input = tcpClient.read();
			System.out.println("Receive Input:" + input);
			String[] line = input.split("\n");
			int lastIdx = line.length-1;
		
			switch(line[lastIdx]){
				case "TEAM": tcpClient.write(getTeamName()); break;
			
				case "MOVE": tcpClient.write(getNextMove(line)); break;
				
				case "END": tcpClient.closeTCP();
							playing = false;
							break;
				
				case "RESTART": resetGame(); break;
			}
		}
	}
	
	private void resetGame(){
		board.reset();
	}
	
	private String getTeamName(){
		return self;
	}

	private String getNextMove(String[] line){
		if(line[0] == "RESTART"){
			//reset to initial state
			resetGame();
		}
		// store other players' move to my board
		for(int i=0; i<line.length-1; i++){
			String[] otherPLayersMove = line[i].split(" ");
			if(otherPLayersMove.length < 3){ continue; }
			String playerName = otherPLayersMove[0];
			int x = Integer.parseInt(otherPLayersMove[1]);
			int y = Integer.parseInt(otherPLayersMove[2]);
			board.addPrevMove(Move.createOthersMove(i+1, x, y));
		}
		// make next move and add myMove to my board
		Move nextMove = strategy.makeAMove(board);
		System.out.println("making move=" + nextMove.toString());
		board.addPrevMove(nextMove);
		return nextMove.toString();
	}
}
