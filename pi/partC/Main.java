import java.io.IOException;
import java.security.InvalidParameterException;

public class Main {
	private static int T_SUPPORT = 3;
	private static double T_CONFIDENCE = 65;
	private static int C_SWITCH =0;
	
	public static void main(String[] args) throws IOException {
		// Only allowed arg lengths are 1 or 3 (or 4 partC)
		// (1) ./pipair <bitcode file>
		// (3) ./pipair <bitcode file> <T_SUPPORT> <T_CONFIDENCE>
		if (args.length == 3) {
			T_SUPPORT = Integer.parseInt(args[1]);
			T_CONFIDENCE = Integer.parseInt(args[2]);
		} else if (args.length ==4){
			//for part C
			T_SUPPORT = Integer.parseInt(args[1]);
			T_CONFIDENCE = Integer.parseInt(args[2]);
			C_SWITCH =Integer.parseInt(args[3]); 
		} else if (args.length != 1) {
			throw new InvalidParameterException("Wrong number of arguments");
		}
		
		
		Parser parser = new Parser(args[0]);
		CallGraph cg = parser.parse();
		cg.setParams(T_SUPPORT, T_CONFIDENCE);

		if(C_SWITCH>=1){
			for(int i=0; i<C_SWITCH; i++){
				cg.interProcedural();
			}
		}
		cg.evaluate();
		
	}
}
