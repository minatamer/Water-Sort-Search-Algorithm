package code;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

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
	   
	   private int calculateCumulativeCost(Node current, Point operator) {
		   ArrayList<ArrayList<String>> newState = deepCopyState((ArrayList<ArrayList<String>>) current.getState());

		    ArrayList<String> sourceBottle = newState.get(operator.x);
		    ArrayList<String> sinkBottle = newState.get(operator.y);

		    // Calculate the number of empty layers in the sink
		    int emptyLayersInSink = countEmptyLayers(sinkBottle);

		    // Calculate the number of pourable layers from the source (same color on top)
		    int pourableLayersInSource = countPourableLayers(sourceBottle);


		    // Determine the number of layers to pour
		    int layersToPour = Math.min(emptyLayersInSink, pourableLayersInSource);
		   
		    int cost = layersToPour;
		    
//		    int cumulativeCost = this.getProblem().calculatePathCost(current, cost); 
		    
	
		    int cumulativeCost = current.getPathCost() + cost;
		    
		    return cumulativeCost;
	   }
	   
	   @Override
	   Object applyOperator(Node current, Point operator) {
		    // Clone the current state to avoid modifying the original state directly
		    ArrayList<ArrayList<String>> newState = deepCopyState((ArrayList<ArrayList<String>>) current.getState());

		    ArrayList<String> sourceBottle = newState.get(operator.x);
		    ArrayList<String> sinkBottle = newState.get(operator.y);

		    // Calculate the number of empty layers in the sink
		    int emptyLayersInSink = countEmptyLayers(sinkBottle);

		    // Calculate the number of pourable layers from the source (same color on top)
		    int pourableLayersInSource = countPourableLayers(sourceBottle);


		    // Determine the number of layers to pour
		    int layersToPour = Math.min(emptyLayersInSink, pourableLayersInSource);
		   
		    int cost = layersToPour;
		    
		    int cumulativeCost = this.getProblem().calculatePathCost(current, cost); 
		    
		 
		    

		    // Pour the layers from source to sink
		    for (int i = 0; i < layersToPour; i++) {
		        // Find the topmost pourable layer from the source
		        String layer = removeTopLayer(sourceBottle);
		        // Add the layer to the sink
		        addLayerToSink(sinkBottle, layer);
		    }

		    return newState;
		}

		// Helper method to count the number of empty layers in a bottle
		private int countEmptyLayers(ArrayList<String> bottle) {
		    int count = 0;
		    for (String layer : bottle) {
		        if (layer.equals("e")) {
		            count++;
		        }
		    }
		    return count;
		}

		// Helper method to count the number of pourable layers (same color) from the top of the source bottle
		private int countPourableLayers(ArrayList<String> bottle) {
		    String topColor = getTopLayer(bottle);
		    int count = 0;
		    for (int i = 0; i < bottle.size(); i++) {
		        if (bottle.get(i).equals("e")) {
		            continue; // Skip empty layers
		        }
		        if (!bottle.get(i).equals(topColor)) {
		            break; // Stop if we find a different color
		        }
		        count++;
		    }
		    return count;
		}

		// Helper method to remove the topmost pourable layer from a bottle
		private String removeTopLayer(ArrayList<String> bottle) {
		    for (int i = 0; i < bottle.size(); i++) {
		        if (!bottle.get(i).equals("e")) {
		            String layer = bottle.get(i);
		            bottle.set(i, "e"); // Set the removed layer to empty ("e")
		            return layer;
		        }
		    }
		    return "e"; // Return "e" if nothing to pour
		}

		// Helper method to add a layer to the top of the sink bottle
		private void addLayerToSink(ArrayList<String> sinkBottle, String layer) {
		    for (int i = sinkBottle.size()-1 ; i >= 0; i--) {
		        if (sinkBottle.get(i).equals("e")) {
		            sinkBottle.set(i, layer);
		            return;
		        }
		    }
		}

		// Helper method to deep copy the state (to avoid modifying the original state)
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
	        return "e"; // Return "e" if the bottle is empty
	    }

		@Override		
	    ArrayList<Node> expand(String strategy, Node node) {
			ArrayList<Point> operators = getOperators(node);
//			System.out.println(operators);
	    	ArrayList<Node> children = new ArrayList<>();
	        for (Point operator : operators) {
	            ArrayList<ArrayList<String>> newState = (ArrayList<ArrayList<String>>) applyOperator(node, operator);
	            if (newState != null) {
	        		int heuristicValue = 0;
	        		if (strategy.equals("GR1")) {
	        			heuristicValue = heuristicNonUniformBottles(newState);
	        		}
	        		if (strategy.equals("GR2")) {
	        			heuristicValue = heuristicEmptyLayers(newState);
	        		}
	        		if (strategy.equals("AS1")) {
	        			heuristicValue = heuristicNonUniformBottles(newState);
	        		}
	        		if (strategy.equals("AS2")) {
	        			heuristicValue = heuristicEmptyLayers(newState);
	        		}
	        		int cost = calculateCumulativeCost(node, operator);
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
		
		public static int heuristicEmptyLayers(ArrayList<ArrayList<String>> state) {
		    int emptyLayers = 0;

		    for (ArrayList<String> bottle : state) {
		        // Count the empty layers in each bottle
		        for (String layer : bottle) {
		            if (layer.equals("e")) {
		                emptyLayers++;
		            }
		        }
		    }

		    // The more empty layers, the easier the rearranging, so we use this as our heuristic
		    return emptyLayers;
		}
		
		public static int heuristicNonUniformBottles(ArrayList<ArrayList<String>> state) {
		    int nonUniformBottles = 0;

		    for (ArrayList<String> bottle : state) {
		        // Check if the bottle is empty or contains only one color
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




	
	public static String solve(String initialState,String strategy, boolean visualize) {
		
		ArrayList<ArrayList<String>> state = createState(initialState);
		int heuristicValue = 0;
		if (strategy.equals("GR1")) {
			heuristicValue = heuristicNonUniformBottles(state);
		}
		if (strategy.equals("GR2")) {
			heuristicValue = heuristicEmptyLayers(state);
		}
		if (strategy.equals("AS1")) {
			heuristicValue = heuristicNonUniformBottles(state);
		}
		if (strategy.equals("AS2")) {
			heuristicValue = heuristicEmptyLayers(state);
		}
		Node root = new Node(state, null, null, 0, 0, heuristicValue);
		WaterSortProblem problem = new WaterSortProblem(state);
		problem.getStateSpace().add(state);
		WaterSortSearch search = new WaterSortSearch(problem);
		search.getNodes().add(root);
		
		while(true){
			if (search.getNodes().isEmpty()) return "NOSOLUTION;";
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
				return resultString;
			}
			

			
			//Third: expand 
			ArrayList<Node> expandedNodes = search.expand(strategy, current);
			int numExpanded = search.getNumOfExpandedNodes() + 1;
			search.setNumOfExpandedNodes(numExpanded);
//			System.out.println("expanded nodes:" + expandedNodes);
			
			//Fourth: Enqueue according to strategy 
			if(strategy.equals("ID")) {
	    		for (int i = 0; i<20 ; i++) {
	    			for(int j = 0 ; j<i ; j++) {
	    				Iterator<Node> iterator = expandedNodes.iterator();
	    				while (iterator.hasNext()) {
	    				    Node node = iterator.next();
	    				    if (problem.getStateSpace().contains(node.getState())) {
	    				        iterator.remove(); // Use the iterator's remove method
	    				    } else {
	    				        problem.getStateSpace().add((ArrayList<ArrayList<String>>) node.getState());
	    				    }
	    				}
	    				search.enqueue(expandedNodes, "ID");
	    			}
	    			
	    		}
			}
			else {
				Iterator<Node> iterator = expandedNodes.iterator();
				while (iterator.hasNext()) {
				    Node node = iterator.next();
				    if (problem.getStateSpace().contains(node.getState())) {
				        iterator.remove(); // Use the iterator's remove method
				    } else {
				        problem.getStateSpace().add((ArrayList<ArrayList<String>>) node.getState());
				    }
				}
				search.enqueue(expandedNodes, strategy);

			}
			

		}
		
		 
	}
	
	
	public static void main (String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		System.out.print("Enter initial state: ");
//		String initialState = scanner.next();
		
		//Simple example 1 to see if it works
	    String ex1 = "3;" +
	            "4;" +
	            "b,b,y,y;" + "b,b,y,y;" +"e,e,e,e;";
	    
		//Simple example 2 to see if it works
	    String ex2 = "4;" +
	            "4;" +
	            "b,b,y,y;" + "b,b,y,y;" +"e,e,e,e;" +"e,e,e,e;";

//
//	    String grid0 = "5;" +
//	            "4;" +
//	            "b,y,r,b;" + "b,y,r,r;" +
//	            "y,r,b,y;" + "e,e,e,e;" + "e,e,e,e;";
		
	    
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
		

	    String solution = WaterSortSearch.solve(grid1, "BF", false);
	    System.out.print(solution);
	  

		
	}



	

}
