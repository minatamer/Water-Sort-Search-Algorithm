package code;

import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Point;

public abstract class GenericSearch {

	private ArrayList<Node> nodes;
	private ArrayList<Point> operators;
	private Problem problem;
	
	public GenericSearch(Problem problem) {
		this.nodes = new ArrayList<>();
		this.operators = new ArrayList<>();
		this.problem = problem;
		
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

	public ArrayList<Point> getOperators() {
		return operators;
	}

	public void setOperators(ArrayList<Point> operators) {
		this.operators = operators;
	}



    abstract boolean goalTest (Node current);
    
    abstract Object applyOperator (Node current, Point operator);
    
    abstract Object getOperators(Node current);
	
	abstract ArrayList<Node> expand(Node node);
    
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
            	case "GR1":break;
            	case "GR2":break;
            	case "AS1":break;
            	case "AS2":break;
            }
       
        }
	}
    


