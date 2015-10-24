package p5_GravitationalVoronoiGame.strategy;

import java.util.*;
import p5_GravitationalVoronoiGame.*;

public class RandomStrategy implements Strategy {
	Random r = new Random();
	
	@Override
	public Move makeAMove(Board board){
		Move nextMove = null;
		while(nextMove == null){
			int x = r.nextInt(board.getBoardSize());
			int y = r.nextInt(board.getBoardSize());
			Move tmpMove = Move.createMyMove(x, y);
			if(board.isEmptyAt(x, y)){
				nextMove = tmpMove;
			}
		}
		return nextMove;
	}
}
