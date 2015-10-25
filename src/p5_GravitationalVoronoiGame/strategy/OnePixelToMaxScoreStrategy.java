package p5_GravitationalVoronoiGame.strategy;

import p5_GravitationalVoronoiGame.AbsPlayer;
import p5_GravitationalVoronoiGame.Board;
import p5_GravitationalVoronoiGame.Move;
import p5_GravitationalVoronoiGame.Strategy;

public class OnePixelToMaxScoreStrategy implements Strategy{

	int[][] directions = {{-1, 0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1}, {0, 1}, {-1, 1}};
	
	@Override
	public Move makeAMove(Board board) {
		if(board.getPrevMoves().size() == 0){
			return Move.createMyMove(board.getBoardSize()/2, board.getBoardSize()/2);
		}
		
		Move prevMove = board.getPrevMoves().get(board.getPrevMoves().size() - 1);
		Move bestMove = null;
		int maxScore = 0;
		
		for(int i=0; i<directions.length; i++){
			int nextX = prevMove.x + directions[i][0];
			int nextY = prevMove.y + directions[i][1];
			if(isValidMove(board, nextX, nextY)){
				Move thisMove = Move.createMyMove(nextX, nextY);
				int thisScore = board.testMyScoreWithThisMove(thisMove);
				if(thisScore > maxScore){
					maxScore = thisScore;
					bestMove = thisMove;
				}
			}
		}
		return bestMove;
	}
	
	private boolean isValidMove(Board board, int x, int y){
		if(x < 0 || y < 0 || x >= board.getBoardSize() || y >= board.getBoardSize() || !board.isEmptyAt(x, y)){
			return false;
		}
		
		return true;
	}

}
