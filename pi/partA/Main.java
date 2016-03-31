import java.io.IOException;
import java.security.InvalidParameterException;

public class Main {
	private static int T_SUPPORT = 3;
	private static double T_CONFIDENCE = 65;
	
	public static void main(String[] args) throws IOException {
		// Only allowed arg lengths are 1 or 3
		// (1) ./pipair <bitcode file>
		// (3) ./pipair <bitcode file> <T_SUPPORT> <T_CONFIDENCE>
		if (args.length == 3) {
			T_SUPPORT = Integer.parseInt(args[1]);
			T_CONFIDENCE = Integer.parseInt(args[2]);
		} else if (args.length != 1) {
			throw new InvalidParameterException("Wrong number of arguments");
		}
		
		Parser parser = new Parser(args[0]);
		CallGraph cg = parser.parse();
		cg.setParams(T_SUPPORT, T_CONFIDENCE);

		cg.evaluate();
		
	}
}
