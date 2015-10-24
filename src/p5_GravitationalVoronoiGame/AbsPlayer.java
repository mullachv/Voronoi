package p5_GravitationalVoronoiGame;

import java.util.List;

public abstract class AbsPlayer {
	public static double MAX_DIST_SQ = 1000*1000 + 1000*1000;//greater 999^2 + 999^2 
	public static double MAX_PULL = 100000;//greater 
	public static int NW_X = 0;
	public static int NW_Y = 0;
	
	public static int NE_X = 999;
	public static int NE_Y = 0;
	
	public static int SW_X = 0;
	public static int SW_Y = 999;
	
	public static int SE_X = 999;
	public static int SE_Y = 999;
	
	int playerId;
	List<Move> moves;
	
	public double getPull(int x, int y, Player pl) {
		double acc = 0;
		
		System.out.println("moves size=" + moves.size());
		for (Move m: moves) {
			if (playerId != m.plId) {
				continue;
			}
			if (m.x == x && m.y == y) {
				return MAX_PULL;
			}
			acc += 1/getDistanceSq(x, y, m);
		}
		return acc;
	}
	
	public static double getPull(List<Move> moves, int x, int y, int playerId){
		double acc = 0;
		
		System.out.println("moves size=" + moves.size());
		for (Move m: moves) {
			if (playerId != m.plId) {
				continue;
			}
			if (m.x == x && m.y == y) {
				return MAX_PULL;
			}
			acc += 1/getDistanceSq(x, y, m);
		}
		return acc;
	}
	
	public static double getDistanceSq(int px, int py, Move mv) {
		return getDistanceSq(px, py, mv.x, mv.y);
	}
	
	private static double getDistanceSq(int px, int py, int mx, int my) {
		return (px-mx)*(px-mx) + (py-my)*(py-my);
	}
	
	public static double getNWDistanceSq(Move m) {
		return getDistanceSq(NW_X, NW_Y, m);
	}
	public static double getNEDistanceSq(Move m) {
		return getDistanceSq(NE_X, NE_Y, m);
	}
	public static double getSWDistanceSq(Move m) {
		return getDistanceSq(SW_X, SW_Y, m);
	}
	public static double getSEDistanceSq(Move m) {
		return getDistanceSq(SE_X, SE_Y, m);
	}
	
}
