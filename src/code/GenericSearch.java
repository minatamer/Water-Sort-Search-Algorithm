package code;

import java.util.ArrayList;
import java.awt.Point;

public abstract class GenericSearch {

	private ArrayList<Node> nodes = new ArrayList<>();
	private ArrayList<Point> operators = new ArrayList<>();
	
	public GenericSearch(ArrayList<Node> nodes, ArrayList<Point> operators) {
		this.nodes = nodes;
		this.operators = operators;
	}

    public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Point> getOperators() {
		return operators;
	}

	public void setOperators(ArrayList<Point> operators) {
		this.operators = operators;
	}



    abstract Boolean goalTest (Node current);
    
    abstract Object applyOperator (Node current, Point operator);
    
	abstract ArrayList<Point> getOperators (Node current);
    
    private ArrayList<Node> expand(Node node, ArrayList<Point> operators) {
    	ArrayList<Node> children = new ArrayList<>();
        for (Point operator : operators) {
            Object newState = applyOperator(node, operator);
            if (newState != null) {
                Node childNode = new Node(newState, node, operator, node.getDepth() + 1, node.getPathCost() + 1);
                children.add(childNode);
            }
        }
        return children;
    }
    
	public Node generalSearch(Node root, String searchAlgorithm) {
        
		nodes.add(root);
        while (true) {
            if (nodes.isEmpty()) {
                return null; 
            }
            
            Node node = nodes.remove(0);
            
            if (goalTest(node)) {
                return node;
            }
            ArrayList<Node> children = expand(node, operators);
            
            //Switch on searchAlgorithm 
            switch(searchAlgorithm) {
            	case "BF":  
            		 for (Node child : children) {
                         nodes.add(child); 
                     }
                     break;
            	case "DF":
           		 for (Node child : children) {
                     nodes.add(0, child);
                 }
                 break;
            	case "ID":break;
            	case "UC":break;
            	case "GR1":break;
            	case "GR2":break;
            	case "AS1":break;
            	case "AS2":break;
            }
       
        }
	}
    

}
