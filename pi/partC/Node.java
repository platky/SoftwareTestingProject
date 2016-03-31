import java.util.ArrayList;
import java.util.List;

public class Node {
	private String name;
	private List<String> children;

	public Node(String name) {
		this.name = name;
		this.setChildren(new ArrayList<String>());
	}
	
	public void addChild(String childName) {
		getChildren().add(childName);
	}
	
	public void removeChild(String childName){
		getChildren().remove(childName);
	}
		
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
		
	public boolean containsChild(String childName) {
		return getChildren().contains(childName);
	}
	
	public boolean containsPair(String child1, String child2){
		if(getChildren().contains(child1)&& getChildren().contains(child2)){
			return true;
		}
		return false;
	}

	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}
}
