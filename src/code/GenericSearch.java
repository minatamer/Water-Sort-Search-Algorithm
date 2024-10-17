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
    

	private Object applyOperator(Node current, Point operator) {
		    ArrayList<ArrayList<String>> newState = deepCopyState((ArrayList<ArrayList<String>>) current.getState());
		    ArrayList<String> sourceBottle = newState.get(operator.x);
		    ArrayList<String> sinkBottle = newState.get(operator.y);
		    int emptyLayersInSink = countEmptyLayers(sinkBottle);
		    int pourableLayersInSource = countPourableLayers(sourceBottle);
		    int layersToPour = Math.min(emptyLayersInSink, pourableLayersInSource);
		    
		    for (int i = 0; i < layersToPour; i++) {
		        String layer = removeTopLayer(sourceBottle);
		        addLayerToSink(sinkBottle, layer);
		    }

		    return newState;
		}
    
	private int countEmptyLayers(ArrayList<String> bottle) {
	    int count = 0;
	    for (String layer : bottle) {
	        if (layer.equals("e")) {
	            count++;
	        }
	    }
	    return count;
	}

	private int countPourableLayers(ArrayList<String> bottle) {
	    String topColor = getTopLayer(bottle);
	    int count = 0;
	    for (int i = 0; i < bottle.size(); i++) {
	        if (bottle.get(i).equals("e")) {
	            continue; 
	        }
	        if (!bottle.get(i).equals(topColor)) {
	            break; 
	        }
	        count++;
	    }
	    return count;
	}

	private String removeTopLayer(ArrayList<String> bottle) {
	    for (int i = 0; i < bottle.size(); i++) {
	        if (!bottle.get(i).equals("e")) {
	            String layer = bottle.get(i);
	            bottle.set(i, "e");
	            return layer;
	        }
	    }
	    return "e"; 
	}


	private void addLayerToSink(ArrayList<String> sinkBottle, String layer) {
	    for (int i = sinkBottle.size()-1 ; i >= 0; i--) {
	        if (sinkBottle.get(i).equals("e")) {
	            sinkBottle.set(i, layer);
	            return;
	        }
	    }
	}

    private String getTopLayer(ArrayList<String> bottle) {
        for (int i = 0; i < bottle.size(); i++) {
            if (!bottle.get(i).equals("e")) {
                return bottle.get(i);
            }
        }
        return "e"; 
    }

	private ArrayList<ArrayList<String>> deepCopyState(ArrayList<ArrayList<String>> state) {
	    ArrayList<ArrayList<String>> newState = new ArrayList<>();
	    for (ArrayList<String> bottle : state) {
	        newState.add(new ArrayList<>(bottle));
	    }
	    return newState;
	}
    private boolean isEmpty(ArrayList<String> bottle) {
    	if (bottle.get(bottle.size()-1).equals("e"))
    		return true;
    	else return false;
    }

    private boolean hasEmptyLayer(ArrayList<String> bottle) {
        for (String layer : bottle) {
            if (layer.equals("e")) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidMove(ArrayList<String> sourceBottle, ArrayList<String> sinkBottle) {
        String sourceTop = getTopLayer(sourceBottle);
        String sinkTop = getTopLayer(sinkBottle);
        return sinkTop.equals("e") || sourceTop.equals(sinkTop);
    }
    
    private ArrayList<Point> getOperators(Node current) {
        ArrayList<ArrayList<String>> state = (ArrayList<ArrayList<String>>) current.getState();
        int numberOfBottles = state.size();
        ArrayList<Point> operators = new ArrayList<>();

        for (int i = 0; i < numberOfBottles; i++) {
            ArrayList<String> sourceBottle = state.get(i);
            if (isEmpty(sourceBottle)) continue;

            for (int j = 0; j < numberOfBottles; j++) {
                if (i == j) continue;

                ArrayList<String> sinkBottle = state.get(j);

                if (hasEmptyLayer(sinkBottle) && isValidMove(sourceBottle, sinkBottle)) {
                    operators.add(new Point(i, j));
                }
            }
        }

        return operators; 
    }
    
    public static int heuristicMismatchBottles(ArrayList<ArrayList<String>> state) {
	    int nonUniformBottles = 0;

	    for (ArrayList<String> bottle : state) {
	        String topColor = bottle.get(0);
	        boolean isUniform = true;

	        for (String color : bottle) {
	            if (!color.equals(topColor)) {
	                isUniform = false;
	                break;
	            }
	        }

	        if (!isUniform) {
	            nonUniformBottles++;
	        }
	    }

	    return nonUniformBottles;
	}
	
	
    public static int heuristicMixedColors(ArrayList<ArrayList<String>> state) {
	    int totalDistinctColors = 0;
	    for (ArrayList<String> bottle : state) {
	        HashSet<String> uniqueColorsInBottle = new HashSet<>();
	        for (String color : bottle) {
	            if (!color.equals("e")) { 
	                uniqueColorsInBottle.add(color);
	            }
	        }
	        if (uniqueColorsInBottle.size() == 1) {
	        	totalDistinctColors += 0;
	        }
	        else {
	        	totalDistinctColors += uniqueColorsInBottle.size();
	        }
	        
	    }

	    return totalDistinctColors;
	}
    public ArrayList<Node> expand(String strategy, Node node) {
		ArrayList<Point> operators = getOperators(node);
		this.getProblem().getOperators().addAll(operators);
//		System.out.println(operators);
    	ArrayList<Node> children = new ArrayList<>();
        for (Point operator : operators) {
            ArrayList<ArrayList<String>> newState = (ArrayList<ArrayList<String>>) applyOperator(node, operator);
            if (newState != null) {
        		int heuristicValue = 0;
        		if (strategy.equals("GR1")) {
        			heuristicValue = heuristicMixedColors(newState);
        		}
        		if (strategy.equals("GR2")) {
        			heuristicValue = heuristicMismatchBottles(newState);
        		}
        		if (strategy.equals("AS1")) {
        			heuristicValue = heuristicMixedColors(newState);
        		}
        		if (strategy.equals("AS2")) {
        			heuristicValue = heuristicMismatchBottles(newState);
        		}
        		int cost = this.getProblem().calculatePathCost(node, operator);
                Node childNode = new Node(newState, node, operator, node.getDepth() + 1, cost , heuristicValue);
                children.add(childNode);
            }
        }
        return children;
    }
    
    public void enqueue(ArrayList<Node> expandedNodes , String strategy , int depth) {
            switch(strategy) {
            	case "BF":  
            		nodes.addAll(expandedNodes);
                     break;
            	case "DF":
            		nodes.addAll(0, expandedNodes);
                    break;
            	case "ID":
//            		nodes.addAll(0, expandedNodes);
            		 for (Node expandedNode : expandedNodes) {
            		        if (expandedNode.getDepth() <= depth) {
            		            nodes.add(0, expandedNode);
            		        }
            		    }
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
    


