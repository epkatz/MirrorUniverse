package mirroruniverse.g2;

public class Config {
	public static boolean DEBUG = false;
	
	public static final int NEW_SPACE_THRESHOLD = 100;
	public static final int CHECKING_INTERVAL = 40;
	
	public static int DEFAULT_QUEUE_SIZE = 1024;
	
	public static final int GUARANTEED_SIZE = 150;
	public static final int MAX_SIZE = GUARANTEED_SIZE * 2 + 1;
}
