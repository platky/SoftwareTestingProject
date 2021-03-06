import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

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
		
	
		for (Map.Entry<Set<String>, Integer> pairs: supportList_pair.entrySet()){
			//check T_SUPPORT first
			if(pairs.getValue()>=this.T_SUPPORT){
				//check pair against BOTH individual values
				Iterator<String> it = pairs.getKey().iterator();
				String n1 = it.next();
				int v1 = supportList_indiv.get(n1);
				String n2 = it.next();
				int v2 = supportList_indiv.get(n2);
			
				//check first value
				double conf1=((double)pairs.getValue()*100)/ (double)v1;
				if(conf1>=this.T_CONFIDENCE){
					//likely bug, run the location finder
					bugLocator(n1, n1, n2, pairs.getValue(), conf1);
				}
				
				//check second value
				double conf2=((double)pairs.getValue()*100)/ (double)v2;
				if(conf2>=this.T_CONFIDENCE){
					//likely bug, run the location finder
					bugLocator(n2, n1, n2, pairs.getValue(), conf2);
				}
			}
		}
		
	}
	
	public void bugLocator(String fn, String pair1, String pair2, int sup, double conf){
		for (Map.Entry<String, Node> fList: functionsList.entrySet()){
			boolean inPair = fList.getValue().containsPair(pair1, pair2);
			if(!inPair){
				if(fList.getValue().containsChild(fn)){
					//likely bug here, post it!
					DecimalFormat df = new DecimalFormat("0.00"); 
					//output of pair needs to be alphabetical it seems
					if(pair2.compareTo(pair1)<0){
						String tmp = pair1;
						pair1=pair2;
						pair2=tmp;
					}
					System.out.println("bug: "+fn+" in "+fList.getKey()+", pair: ("+pair1+", "+pair2+"), support: "+
					sup + ", confidence: "+df.format(conf)+"%");
				}
			}
		}
		
	}
	
	public void interProcedural(){
		supportList_indiv.clear();
		supportList_pair.clear();
		//simply expand scope of all functions 1 deep before running evaluate
		//store orig set
		Map<String, Node> saveList = new HashMap<String, Node>(functionsList);
		functionsList.clear();
		for (Map.Entry<String, Node> fList: saveList.entrySet()){
			//grab children and iterate through
			Node newNode = fList.getValue();
			List<String> children = newNode.getChildren();
			for(int i=0; i<children.size(); i++){
				//retrieve this child node then grab their children!
				List<String> innerChildren = saveList.get(children.get(i)).getChildren();
				//add all these children to the original functions children
				for(int j=0; j<innerChildren.size(); j++){
					if(!newNode.containsChild(innerChildren.get(j))){
						newNode.addChild(innerChildren.get(j));
						
					}
				}
				//now remove this child
				newNode.removeChild(children.get(i));
			}
			//for simplicity just re-add the node back to overwrite old one
			//this allows for recounting of pairs and fn appearances
			this.addNode(newNode);
			
		}
	}
	

	
	
}
