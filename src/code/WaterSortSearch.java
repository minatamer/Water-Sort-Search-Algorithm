package code;

import java.awt.Point;
import java.util.ArrayList;
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
		    
		    int cost = this.getProblem().getPathCost(current, layersToPour);
		    current.setPathCost(cost);

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
	    ArrayList<Node> expand(Node node) {
			ArrayList<Point> operators = getOperators(node);
	    	ArrayList<Node> children = new ArrayList<>();
	        for (Point operator : operators) {
	            ArrayList<ArrayList<String>> newState = (ArrayList<ArrayList<String>>) applyOperator(node, operator);
	            if (newState != null) {
	                Node childNode = new Node(newState, node, operator, node.getDepth() + 1, node.getPathCost() + 1);
	                children.add(childNode);
	            }
	        }
	        return children;
	    }
		

	
	public static String solve(String initialState,String strategy, boolean visualize) {
		
		//		Problem problem = new Problem();
		ArrayList<ArrayList<String>> state = createState(initialState);
//	       for (ArrayList<String> row : state) {
//	            System.out.println(row);
//	        }
		
		Node root = new Node(state, null, null, 0, 0);
		WaterSortProblem problem = new WaterSortProblem(state);
		WaterSortSearch search = new WaterSortSearch(problem);
		search.getNodes().add(root);
		
		while(true){
			//First: dequeue and repeat loop
			Node current = search.getNodes().get(0);
			
			//Second: goal test
			boolean result = problem.isGoalState(current.getState());
			
			if (result == true) return "backtrack";
			
			if (search.getNodes().isEmpty()) return "failure";
			
			//Third: expand 
			ArrayList<Node> expandedNodes = search.expand(current);
			
			//Fourth: Enqueue according to strategy 
			if(strategy.equals("ID")) {
	    		for (int i = 0; i<20 ; i++) {
	    			for(int j = 0 ; j<i ; j++) {
	    				search.enqueue(expandedNodes, "ID");
	    			}
	    			
	    		}
			}
			else {
				search.enqueue(expandedNodes, strategy);
			}
			

		}
		
		 
	}
	
	
	public static void main (String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		System.out.print("Enter initial state: ");
//		String initialState = scanner.next();
	    String grid0 = "5;" +
	            "4;" +
	            "b,y,r,b;" + "b,y,r,r;" +
	            "y,r,b,y;" + "e,e,e,e;" + "e,e,e,e;";
	    String solution = WaterSortSearch.solve(grid0, "BF", false);
	    

		
	}



	

}
