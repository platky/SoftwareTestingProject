import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class CallGraph {
	// Assume no duplicate function name
	// i.e. if call graph contained functions with the same name but different params, this blows up
	Map<String, Node> functionsList = new HashMap<String, Node>();
	
	Map<String, Integer> supportList_indiv = new HashMap<String, Integer>();
	Map<Set<String>, Integer> supportList_pair = new HashMap<Set<String>, Integer>();
	
	private int T_SUPPORT;
	private double T_CONFIDENCE;
	

	public CallGraph() {
		
	}

	public void setParams(int T_SUPPORT, double T_CONFIDENCE) {
		this.T_SUPPORT = T_SUPPORT;
		this.T_CONFIDENCE = T_CONFIDENCE;
	}

	public void addNode(Node node) {
		functionsList.put(node.getName(), node);		
		
		ListIterator<String> childrenIterator = node.getChildren().listIterator(0);
		
		// update support values for individual calls
		while (childrenIterator.hasNext()) {
			String child = childrenIterator.next();
			Integer count = supportList_indiv.get(child);
			if (count != null) {
				count += 1;
			} else {
				count = 1;
			}
			supportList_indiv.put(child, count);
			
			// update support values for pair calls
			// TODO: O(n^2). bad. do we need to do this..?
			
			// get the iterator pointing right after the index the outer iterator is pointing at
			// this prevents going through the list more than necessarily
			ListIterator<String> innerIterator = node.getChildren().listIterator(childrenIterator.nextIndex());
			while (innerIterator.hasNext()) {
				String child2 = innerIterator.next();
				Set<String> pair = new HashSet<String>();	// we use hashset as unordered pair
				pair.add(child);
				pair.add(child2);
				// TODO: check if this part works properly
				
				Integer pairCount = supportList_pair.get(pair);
				if (pairCount != null) {
					pairCount += 1;
				} else {
					pairCount = 1;
				}
				supportList_pair.put(pair, pairCount);
			}
		}
		
	}

	public void evaluate() {
		// main bug detecting code
	}

	
	
}
