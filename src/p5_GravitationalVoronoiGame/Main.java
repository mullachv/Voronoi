package p5_GravitationalVoronoiGame;

import java.io.IOException;
import p5_GravitationalVoronoiGame.strategy.*;

public class Main {
	
	TCPClient tcpClient;
	String self = "mv_cly2";
	Board board;
	Strategy strategy;
	final String host = "localhost";
	int port = 1337;	
	
	//$java -jar client.jar -s3 myteamname
	public static void main(String[] args) throws Exception{
		Main m = new Main();
		m.tcpClient = new TCPClient();
		boolean isInitialized = false;
		
		if (args.length > 0 ) {
			if (args[0].substring(0, 2).equalsIgnoreCase("-s")) {
				String ar = args[0].substring(2, 3);
				if (ar.equalsIgnoreCase("R")) {
					m.strategy = new RandomStrategy();					
				} else if (ar.equalsIgnoreCase("O")) {
					m.strategy = new OnePixelOff();
				} else if (ar.equalsIgnoreCase("1")) {
					m.strategy = new OnePixelToMaxScoreStrategy();
				} else if (ar.equalsIgnoreCase("2")) {
					m.strategy = new OnePixelToMaxScoreStrategyV2();
				} else if (ar.equalsIgnoreCase("3")) {
					m.strategy = new OnePixelToMaxScoreStrategyV3();
				} else if (ar.equalsIgnoreCase("4")) {
					m.strategy = new OnePixelToMaxScoreStrategyV4();
				} else if (ar.equalsIgnoreCase("5")) {
					m.strategy = new OnePixelToMaxScoreStrategyV5();
				} else if (ar.equalsIgnoreCase("M")) {
					m.strategy = new MinMaxScoreStrategy();
				} else if (ar.equalsIgnoreCase("X")) {
					m.strategy = new RandomMaxPullLocationStrategy();
				} else if (ar.equalsIgnoreCase("L")) {
					m.strategy = new RandomMaxScoreLocationStrategy();
				}
			}
			if (args.length > 1) {
				m.self = args[1];
				
				if (args.length > 2) {
					if (args[2].substring(0, 2).equalsIgnoreCase("-m")) {
						String mx = args[2].substring(2);
						MoveTimer.initTimer(Integer.parseInt(mx));
						System.out.println("Max time: " + mx);
						isInitialized = true;
					}
					if (args.length > 3) {
						if (args[3].substring(0, 2).equalsIgnoreCase("-p")) {
							String iport = args[3].substring(2);
							m.port = Integer.parseInt(iport);
						}
					}
				}
			}
		}
		
		if(!isInitialized) {
			System.out.println("Not all initialization parameters supplied. Halting");
			System.exit(-1);
		}
		m.tcpClient.startTCP(m.host, m.port);
		m.board = new Board();

		// change to your strategy implementation here
		//m.strategy = new OnePixelToMaxScoreStrategyV3();
		
		m.run();
		
		m.tcpClient.closeTCP();
		
		m.board.printCurrScore();
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
				
				case "END": playing = false; break;
				
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
			System.out.println("Move i: " + i + "; line[i]: " + line[i] );
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
