package hw4;

public class Edge<N extends Comparable<N>, L extends Comparable<L>> implements Comparable<Edge<N,L>> {
	private N parentNode;
	private N childNode;
	private L label;
	
	public Edge(N parentNode, N childNode, L label) {
		this.parentNode = parentNode;
		this.childNode = childNode;
		this.label = label;
		checkRep();
	}
	
	public N getParent() {
		return parentNode;
	}
	
	public N getChild() {
		return childNode;
	}
	
	public L getLabel() {
		return label;
	}
	
	@Override
    public int compareTo(Edge<N,L> edge2) {
        if (childNode.equals(edge2.getChild()) && parentNode.equals(edge2.getParent())) {
            return label.compareTo(edge2.label);
        }
        else if (parentNode.equals(edge2.getParent()) ) {
            return childNode.compareTo(edge2.getChild());    
        }
        else {
            return parentNode.compareTo(edge2.getParent());
        }
    }
	
	private void checkRep() throws RuntimeException {
		if(parentNode == null){
			throw new RuntimeException("No parentNode");
		}
		if(childNode == null){
			throw new RuntimeException("No childNode");
		}
		if(label == null){
			throw new RuntimeException("No label");
		}
	}
	
}
