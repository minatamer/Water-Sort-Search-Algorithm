package code;

import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Point;
import java.util.HashSet;

public abstract class GenericSearch {

	private ArrayList<Node> nodes;
	private Problem problem;
	private int numOfExpandedNodes;

	public GenericSearch(Problem problem) {
		this.nodes = new ArrayList<>();
		this.problem = problem;
		this.numOfExpandedNodes = 0;
		
	}

    public int getNumOfExpandedNodes() {
		return numOfExpandedNodes;
	}

	public void setNumOfExpandedNodes(int numOfExpandedNodes) {
		this.numOfExpandedNodes = numOfExpandedNodes;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

    abstract boolean goalTest (Node current);
    
    abstract Object applyOperator (Node current, Point operator);
    
    abstract Object getOperators(Node current);
	
	abstract ArrayList<Node> expand(String strategy , Node node);
    
	public void enqueue(ArrayList<Node> expandedNodes , String strategy) {
            switch(strategy) {
            	case "BF":  
            		nodes.addAll(expandedNodes);
                     break;
            	case "DF":
            		nodes.addAll(0, expandedNodes);
                    break;
            	case "ID":
            		nodes.addAll(0, expandedNodes);
            		break;
            	case "UC":
            		nodes.addAll(expandedNodes);
            		nodes.sort(Comparator.comparingInt(Node::getPathCost));
            		break;
            	case "GR1":
            		nodes.addAll(expandedNodes);
            		nodes.sort(Comparator.comparingInt(Node::getHeuristicValue));
            		break;
            	case "GR2":
            		nodes.addAll(expandedNodes);
            		nodes.sort(Comparator.comparingInt(Node::getHeuristicValue));
            		break;
            	case "AS1":
            	    nodes.addAll(expandedNodes);
            	    nodes.sort(Comparator.comparingInt(node -> node.getHeuristicValue() + node.getPathCost()));
            	    break;
            	case "AS2":
            	    nodes.addAll(expandedNodes);
            	    nodes.sort(Comparator.comparingInt(node -> node.getHeuristicValue() + node.getPathCost()));
            	    break;
            }
       
        }
	}
    


