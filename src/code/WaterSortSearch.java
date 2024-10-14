package code;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

public class WaterSortSearch extends GenericSearch{

	public WaterSortSearch(Problem problem){
		super(problem);
		
	}

	@Override
	boolean goalTest(Node current) {
		String[][] state = (String[][]) current.getState();
		for(int i=0;i< state.length ;i++) {
			String color = state[i][0];
			for(int j=0;j< state[i].length ;j++) {
				if(state[i][j] != color) {
					return false;
				}
			}
		}
		return true;
	}

	
	
	
	   public static ArrayList<ArrayList<String>> createState(String input) {
	        ArrayList<ArrayList<String>> result = new ArrayList<>();

	        String[] parts = input.split(";");
	        for (int i = 2; i < parts.length; i++) {
	            String[] row = parts[i].split(",");
	            ArrayList<String> rowList = new ArrayList<>();
	            for (String element : row) {
	                rowList.add(element);
	            }
	            if (!rowList.isEmpty()) {
	                result.add(rowList);
	            }
	        }

	        return result;
	    }
	   
//	   private int calculateCumulativeCost(Node current, Point operator) {
//		   ArrayList<ArrayList<String>> newState = deepCopyState((ArrayList<ArrayList<String>>) current.getState());
//		    ArrayList<String> sourceBottle = newState.get(operator.x);
//		    ArrayList<String> sinkBottle = newState.get(operator.y);
//		    int emptyLayersInSink = countEmptyLayers(sinkBottle);
//		    int pourableLayersInSource = countPourableLayers(sourceBottle);
//		    int layersToPour = Math.min(emptyLayersInSink, pourableLayersInSource);
//		    int cost = layersToPour;
//		    int cumulativeCost = current.getPathCost() + cost;
//		    return cumulativeCost;
//	   }
	   
	   @Override
	   Object applyOperator(Node current, Point operator) {
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


		private ArrayList<ArrayList<String>> deepCopyState(ArrayList<ArrayList<String>> state) {
		    ArrayList<ArrayList<String>> newState = new ArrayList<>();
		    for (ArrayList<String> bottle : state) {
		        newState.add(new ArrayList<>(bottle));
		    }
		    return newState;
		}

		@Override
		
	    ArrayList<Point> getOperators(Node current) {
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

	    private String getTopLayer(ArrayList<String> bottle) {
	        for (int i = 0; i < bottle.size(); i++) {
	            if (!bottle.get(i).equals("e")) {
	                return bottle.get(i);
	            }
	        }
	        return "e"; 
	    }

		@Override		
	    ArrayList<Node> expand(String strategy, Node node) {
			ArrayList<Point> operators = getOperators(node);
			this.getProblem().getOperators().addAll(operators);
//			System.out.println(operators);
	    	ArrayList<Node> children = new ArrayList<>();
	        for (Point operator : operators) {
	            ArrayList<ArrayList<String>> newState = (ArrayList<ArrayList<String>>) applyOperator(node, operator);
	            if (newState != null) {
	        		int heuristicValue = 0;
	        		if (strategy.equals("GR1")) {
	        			heuristicValue = heuristicMixedColors(newState);
	        		}
	        		if (strategy.equals("GR2")) {
	        			heuristicValue = heuristicEmptyLayers(newState);
	        		}
	        		if (strategy.equals("AS1")) {
	        			heuristicValue = heuristicMixedColors(newState);
	        		}
	        		if (strategy.equals("AS2")) {
	        			heuristicValue = heuristicEmptyLayers(newState);
	        		}
	        		int cost = this.getProblem().calculatePathCost(node, operator);
	                Node childNode = new Node(newState, node, operator, node.getDepth() + 1, cost , heuristicValue);
	                children.add(childNode);
	            }
	        }
	        return children;
	    }
		
		
		public static String getSolutionPath(Node solutionNode , int numOfExpandedNodes) {
		    StringBuilder solutionPath = new StringBuilder();
		    Node currentNode = solutionNode;
		    while (currentNode.getParent() != null) {
		        Point operator = currentNode.getOperator(); 
		        if (operator != null) {
		            solutionPath.insert(0, "pour_" + operator.x + "_" + operator.y + ",");
		        }
		        currentNode = currentNode.getParent();
		    }
		    if (solutionPath.length() > 0) { //remove comma at the end
		        solutionPath.setLength(solutionPath.length() - 1);
		    }

		    return solutionPath.toString() + ";" + solutionNode.getPathCost() + ";" + numOfExpandedNodes ; 
		}
		
		public String visualize(Node solutionNode ){
		    StringBuilder solutionPath = new StringBuilder();
		    Node currentNode = solutionNode;
		    
		    while (currentNode.getParent() != null) {
		    	solutionPath.insert(0, "Current State: " + currentNode.getState());
		        Point operator = currentNode.getOperator(); 
		        if (operator != null) {
		            solutionPath.insert(0, "pour_" + operator.x + "_" + operator.y + ",");
		        }
		        solutionPath.insert(0, "\n");
		        currentNode = currentNode.getParent();
		    }
		    if (solutionPath.length() > 0) { //remove comma at the end
		        solutionPath.setLength(solutionPath.length() - 1);
		    }
		    solutionPath.insert(0, "Initial State: " + this.getProblem().getInitialState() );

		    return solutionPath.toString();
		}
		
		public static int heuristicEmptyLayers(ArrayList<ArrayList<String>> state) {
		    int emptyLayers = 0;

		    for (ArrayList<String> bottle : state) {
		        for (String layer : bottle) {
		            if (layer.equals("e")) {
		                emptyLayers++;
		            }
		        }
		    }

		    return emptyLayers;
		}
		
//		public static int heuristicNonUniformBottles(ArrayList<ArrayList<String>> state) {
//		    int nonUniformBottles = 0;
//
//		    for (ArrayList<String> bottle : state) {
//		        String topColor = bottle.get(0);
//		        boolean isUniform = true;
//
//		        for (String color : bottle) {
//		            if (!color.equals(topColor)) {
//		                isUniform = false;
//		                break;
//		            }
//		        }
//
//		        if (!isUniform) {
//		            nonUniformBottles++;
//		        }
//		    }
//
//		    return nonUniformBottles;
//		}
		
		
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




	
	public static String solve(String initialState,String strategy, boolean visualize) {
		
		ArrayList<ArrayList<String>> state = createState(initialState);
		int heuristicValue = 0;
		if (strategy.equals("GR1")) {
			heuristicValue = heuristicMixedColors(state);
		}
		if (strategy.equals("GR2")) {
			heuristicValue = heuristicEmptyLayers(state);
		}
		if (strategy.equals("AS1")) {
			heuristicValue = heuristicMixedColors(state);
		}
		if (strategy.equals("AS2")) {
			heuristicValue = heuristicEmptyLayers(state);
		}
		Node root = new Node(state, null, null, 0, 0, heuristicValue);
		WaterSortProblem problem = new WaterSortProblem(state);
		problem.getStateSpace().add(state);
		WaterSortSearch search = new WaterSortSearch(problem);
		search.getNodes().add(root);
		
		//Incase of ID 
		int depth = 1 ;
		
		while(true){
			if (search.getNodes().isEmpty() && !strategy.equals("ID")) {
				return "NOSOLUTION";
			}
			else {
				depth++;
			}
			//First: dequeue and repeat loop
			Node current = search.getNodes().get(0);
			search.getNodes().remove(0);
//			System.out.println(current);
			
			//Second: goal test
			boolean result = problem.isGoalState(current.getState());
//			System.out.println(result);
			
			if (result == true) {
				System.out.println(current);
				String resultString = getSolutionPath(current , search.getNumOfExpandedNodes());
				if (visualize == true) {
					String visualizeString = search.visualize(current);
					System.out.println(visualizeString);
				}
				return resultString;
			}
			

			
			//Third: expand 
			ArrayList<Node> expandedNodes = search.expand(strategy, current);
			int numExpanded = search.getNumOfExpandedNodes() + 1;
			search.setNumOfExpandedNodes(numExpanded);
//			System.out.println("expanded nodes:" + expandedNodes);
			
			//Fourth: Enqueue according to strategy 
//			if(strategy.equals("ID")) {
//	    		for (int i = 0; i<20 ; i++) {
//	    			for(int j = 0 ; j<i ; j++) {
//	    				Iterator<Node> iterator = expandedNodes.iterator();
//	    				while (iterator.hasNext()) {
//	    				    Node node = iterator.next();
//	    				    if (problem.getStateSpace().contains(node.getState())) {
//	    				        iterator.remove(); 
//	    				    } else {
//	    				        problem.getStateSpace().add((ArrayList<ArrayList<String>>) node.getState());
//	    				    }
//	    				}
//	    				search.enqueue(expandedNodes, "ID");
//	    			}
//	    			
//	    		}
//			}
			if(strategy.equals("ID")) {
	    				Iterator<Node> iterator = expandedNodes.iterator();
	    				while (iterator.hasNext()) {
	    				    Node node = iterator.next();
	    				    if (problem.getStateSpace().contains(node.getState())) {
	    				        iterator.remove(); 
	    				    } else {
	    				        problem.getStateSpace().add((ArrayList<ArrayList<String>>) node.getState());
	    				    }
	    				}
	    				search.enqueue(expandedNodes, "ID" , depth);
			}
			else {
				Iterator<Node> iterator = expandedNodes.iterator();
				while (iterator.hasNext()) {
				    Node node = iterator.next();
				    if (problem.getStateSpace().contains(node.getState())) {
				        iterator.remove(); 
				    } else {
				        problem.getStateSpace().add((ArrayList<ArrayList<String>>) node.getState());
				    }
				}
				search.enqueue(expandedNodes, strategy , 0);

			}
			

		}
		
		 
	}
	
	
	public static void main (String[] args) {
		//Simple example 1 to see if it works
	    String ex1 = "3;" +
	            "4;" +
	            "b,b,y,y;" + "b,b,y,y;" +"e,e,e,e;";
	    
		//Simple example 2 to see if it works
	    String ex2 = "4;" +
	            "4;" +
	            "b,b,y,y;" + "b,b,y,y;" +"e,e,e,e;" +"e,e,e,e;";
			    
	    String grid0 = "3;" +
	            "4;" +
	            "r,y,r,y;" +
	            "y,r,y,r;" +
	            "e,e,e,e;";
	    
	    String grid3 = "6;" +
	            "4;" +
	            "g,g,g,r;" +
	            "g,y,r,o;" +
	            "o,r,o,y;" +
	            "y,o,y,b;" +
	            "r,b,b,b;" +
	            "e,e,e,e;";
	    
	    String grid1 = "5;" +
	            "4;" +
	            "b,y,r,b;" +
	            "b,y,r,r;" +
	            "y,r,b,y;" +
	            "e,e,e,e;" +
	            "e,e,e,e;";
	    
	    String grid2 = "5;" +
	            "4;" +
	            "b,r,o,b;" +
	            "b,r,o,o;" +
	            "r,o,b,r;" +
	            "e,e,e,e;" +
	            "e,e,e,e;";
	    
        String grid6 = "6;5;" + "r,g,y,r,b;" + "b,g,r,b,g;" + "g,y,g,y,b;" + "y,y,r,b,r;" + "e,e,e,e,e;"
                + "e,e,e,e,e;";
		

//	    String solution = WaterSortSearch.solve(grid1, "BF", false);
//	    System.out.print(solution);
	    
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();

        long beforeUsedMemory = runtime.totalMemory() - runtime.freeMemory();

        String solution = WaterSortSearch.solve(grid6, "GR1", false);
        long estimatedTime = System.nanoTime() - startTime;

        long afterUsedMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsedByTask = afterUsedMemory - beforeUsedMemory;

        System.out.println("Memory used by task: " + memoryUsedByTask / (1024 * 1024) + " MB");

        System.out.println("Time: " + estimatedTime / 1000000 + "ms");
        System.out.println("Solution: " + solution);
	  

		
	}



	

}
