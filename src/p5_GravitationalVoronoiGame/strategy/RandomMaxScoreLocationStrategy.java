package p5_GravitationalVoronoiGame.strategy;

import java.util.*;
import p5_GravitationalVoronoiGame.*;

public class RandomMaxScoreLocationStrategy implements Strategy {
	Random r = new Random();
	
	@Override
	public Move makeAMove(Board board){
		int poolNo = 1000;
		int maxPull = 0;
		Move bestMove = null;
		List<Move> moves = new ArrayList<Move>(board.getPrevMoves());
		
		for(int i=0; i<poolNo; i++){
			Move nextMove = null;
			while(nextMove == null){
				int x = r.nextInt(board.getBoardSize());
				int y = r.nextInt(board.getBoardSize());
				if(board.isEmptyAt(x, y)){
					nextMove = Move.createMyMove(x, y);
					moves.add(nextMove);
					if(AbsPlayer.getPull(board.getPrevMoves(), x, y, 0) > maxPull){
						bestMove = nextMove;
					}
				}
			}
		}
		
		return bestMove;
	}
}
