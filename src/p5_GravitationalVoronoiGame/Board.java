package p5_GravitationalVoronoiGame;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	List<Move> previousMoves;
	boolean[][] hasStone;
	final int size = 1000;
	
	public Board(){
		previousMoves = new ArrayList<Move>();
		hasStone = new boolean[size][size];
	}
	
	public void addPrevMove(Move m){
		previousMoves.add(m);
		hasStone[m.x][m.y] = true;
	}
	
	public List<Move> getPrevMoves(){
		return previousMoves;
	}
	
	public void reset(){
		previousMoves.clear();
		hasStone = new boolean[size][size];
	}
	
	public int getBoardSize(){
		return size;
	}
	
	public boolean isEmptyAt(int x, int y){
		return !hasStone[x][y];
	}
	
	public void printAllPrevMovs(){
		for(Move m: previousMoves){
			System.out.println(m.toFullString());
		}
	}
}
