package p5_GravitationalVoronoiGame;

public class MoveTimer {
	long thinkTime = 0;
	long previousTime;
	long currentTime;
	long allowedTimePerMove;//milliseconds per move
	long MaxMoves;
	long mxTHINKTIME = 2 * 60 * 1000; //2minutes
	boolean firstCall = true;
	static MoveTimer mt = null;
	int printModulo = 250;
	long callCount = 0;
	long edgeWindow = 1000;
	
	public static MoveTimer initTimer(int max) {
		if (mt == null) {
			mt = new MoveTimer(max);
		}
		return mt;
	}

	public static MoveTimer getTimer() {
		return mt;
	}
	
	private MoveTimer(int max) {
		MaxMoves = max;
		previousTime = System.currentTimeMillis();
		allowedTimePerMove = mxTHINKTIME / MaxMoves ; //- edgeWindow;
	}
	
	public void beginTimer() {
		previousTime = System.currentTimeMillis();		
	}
	
	public boolean isTimeExceeded() {
//		if (firstCall) {
//			firstCall = false;
//			beginTimer();
//			return false;
//		}
		callCount++;
		currentTime = System.currentTimeMillis();
//		updateThinkTime();
		if (currentTime >= previousTime + allowedTimePerMove) {
//			previousTime = currentTime;
			System.out.println("TimeExceeded: " + allowedTimePerMove/1000);
			return true;
		}
//		if (callCount % printModulo == 0) {
//			System.out.println("not Exceeded allowed time: p:" + previousTime + " --> " + currentTime + 
//					"; allowed: " + allowedTimePerMove);
//		}
		return false;
	}
	
//	public void updateThinkTime() {
//		thinkTime += currentTime - previousTime;
//	}
}
