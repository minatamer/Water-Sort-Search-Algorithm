package code;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

public class WaterSortSearch extends GenericSearch{

	public WaterSortSearch(Problem problem){
		super(problem);
		
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
		
	public static String solve(String initialState,String strategy, boolean visualize) {
		
		ArrayList<ArrayList<String>> state = createState(initialState);
		int heuristicValue = 0;
		if (strategy.equals("GR1")) {
			heuristicValue = heuristicMixedColors(state);
		}
		if (strategy.equals("GR2")) {
			heuristicValue = heuristicMismatchBottles(state);
		}
		if (strategy.equals("AS1")) {
			heuristicValue = heuristicMixedColors(state);
		}
		if (strategy.equals("AS2")) {
			heuristicValue = heuristicMismatchBottles(state);
		}
		Node root = new Node(state, null, null, 0, 0, heuristicValue);
		Problem problem = new Problem(state);
		problem.getStateSpace().add(state);
		WaterSortSearch search = new WaterSortSearch(problem);
		search.getNodes().add(root);
		
		//Incase of ID 
		int depth = 1 ;
		
		while(true){
			if (search.getNodes().isEmpty() && !strategy.equals("ID")) {
				return "NOSOLUTION";
			}
			if (search.getNodes().isEmpty() && strategy.equals("ID")) {
				
				depth = depth + 1;
				search.getNodes().clear();
				search.getProblem().getStateSpace().clear();
				search.getNodes().add(root);
//				System.out.println(depth);
			}
			//First: dequeue and repeat loop
			Node current = search.getNodes().get(0);
			search.getNodes().remove(0);
			
			//Second: goal test
			boolean result = problem.isGoalState(current.getState());
//			System.out.println(result);
			
			if (result == true) {
//				System.out.println(current);
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
//				System.out.println(depth);
	    				search.enqueue(expandedNodes, "ID" , depth);
//	    				System.out.println(search.getNodes());
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
	    
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();

        long beforeUsedMemory = runtime.totalMemory() - runtime.freeMemory();

        String solution = WaterSortSearch.solve(grid6, "ID", false);
        long estimatedTime = System.nanoTime() - startTime;

        long afterUsedMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsedByTask = afterUsedMemory - beforeUsedMemory;

        System.out.println("Memory used by task: " + memoryUsedByTask / (1024 * 1024) + " MB");

        System.out.println("Time: " + estimatedTime / 1000000 + "ms");
        System.out.println("Solution: " + solution);
	  

		
	}



	

}
