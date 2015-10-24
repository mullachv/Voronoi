package p5_GravitationalVoronoiGame;

public class Move {
	public int plId;
	//String playerName;
	public int x;
	public int y;
	
	public Move(int id, int x, int y){
		this.plId = id;
		this.x = x;
		this.y = y;
	}
	
	public static Move createMyMove(int x, int y){
		return new Move(0, x, y);
	}
	
	public static Move createOthersMove(int pId, int x, int y){
		return new Move(pId, x, y);
	}
	
	public String toString(){
		return x + " " + y;
	}
	
	public String toFullString(){
		return plId + " " + x + " " + y;
	}
}
