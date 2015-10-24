package p5_GravitationalVoronoiGame.strategy;

import p5_GravitationalVoronoiGame.AbsPlayer;
import p5_GravitationalVoronoiGame.Board;
import p5_GravitationalVoronoiGame.Move;
import p5_GravitationalVoronoiGame.Strategy;

public class OnePixelOff implements Strategy{

	@Override
	public Move makeAMove(Board board) {
		// TODO Auto-generated method stub
		//Lisboard.getPrevMoves();
		Move prevMove = board.getPrevMoves().get(board.getPrevMoves().size() - 1);
		Move nextMove = new Move(0, prevMove.x, prevMove.y);
		do {
			if (AbsPlayer.getNEDistanceSq(prevMove) > AbsPlayer.getSWDistanceSq(prevMove)) {
				nextMove.x++;
				nextMove.y--;
			} else {
				nextMove.x--;
				nextMove.y++;			
			}
			
			if (AbsPlayer.getNWDistanceSq(prevMove) > AbsPlayer.getSEDistanceSq(prevMove)) {
				nextMove.x--;
				nextMove.y--;
			} else {
				nextMove.x++;
				nextMove.y++;			
			}
			
		} while (!board.isEmptyAt(nextMove.x, nextMove.y));
		return nextMove;
	}
	

}
