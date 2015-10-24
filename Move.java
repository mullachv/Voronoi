package p5_GravitationalVoronoiGame;

public class Move {
	String playerName;
	int x;
	int y;
	
	private Move(String playerName, int x, int y){
		this.playerName = playerName;
		this.x = x;
		this.y = y;
	}
	
	public static Move createMyMove(int x, int y){
		return new Move("me", x, y);
	}
	
	public static Move createOthersMove(String playerName, int x, int y){
		return new Move(playerName, x, y);
	}
	
	public String toString(){
		return x + " " + y;
	}
	
	public String toFullString(){
		return playerName + " " + x + " " + y;
	}
}
