import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	private String fileName;
	
	
	public Parser(String fileName) {
		this.fileName = fileName;
	}
	
	// parses the input call graph data and form into call graph model
	public CallGraph parse() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = br.readLine();
		
		CallGraph cg = new CallGraph();
		Node node = null;
		
		Boolean inNode = false;

		while (line != null) {
			// TODO: Should this be reworked into a rigorous regex matching?
			if (line.startsWith("Call graph node for function")) {
				//System.out.println("starter");
				node = new Node(retrieveFunctionName(line));
				inNode=true;
			} else if (line.startsWith("  CS<0x")) {
				//System.out.println("address add");
				//can be "calls function" or "calls node"
				if(inNode){
					if(!line.contains("calls external node")){
						assert(node != null);
						String child = retrieveFunctionName(line);
						if (!node.containsChild(child)) {
							node.addChild(retrieveFunctionName(line));	// add child (avoid duplicate)
						}
					}
				}
			} else {
				inNode=false;
				// No more information regarding the current working node
				// add it to the call graph model.
				if (node != null) {
				//System.out.println("null function");
					cg.addNode(node);
					node = null;
				}
			}
			// Ignore external nodes and null functions
			
			
			line = br.readLine();
		}
		br.close();
		
		return cg;
	}
	
	// returns the function name surrounded by quotation marks (note: not apostrophes)
	public String retrieveFunctionName(String line) {
		int firstIndex = line.indexOf('\'');
		int lastIndex = line.lastIndexOf('\'');
		System.out.println("starts at "+firstIndex+" ends at "+lastIndex);
		
		return line.substring(firstIndex + 1, lastIndex);
	}
	
}

