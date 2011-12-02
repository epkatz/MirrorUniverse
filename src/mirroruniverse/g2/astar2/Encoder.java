package mirroruniverse.g2.astar2;

public class Encoder {
	public static int encode(int x1, int y1, int x2, int y2) {
		int code = 0;
		
		code |= y2;
		code <<= 8;
		
		code |= x2;
		code <<= 8;
		
		code |= y1;
		code <<= 8;
		
		code |= x1;
		
		return code;
	}
	
	public static int[] decode(int code) {
		int[] result = new int[4];
		int mask = 255;
		for (int i = 0; i != 4; ++i) {
			result[i] = code & mask;
			code >>= 8;
		}
		
		return result;
	}
	
//	public static void main( String[] args ) {
//		// test
//		int code = encode(200,200,200,200);
//		int[] result = decode(code);
//		System.out.println(code);
//		for (int i = 0; i != result.length; ++i)
//			System.out.println(result[i]);
//	}
}
