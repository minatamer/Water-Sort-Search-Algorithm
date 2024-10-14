package code;
import java.awt.Point;
import java.util.ArrayList;

public class WaterSortProblem extends Problem {
	
	private ArrayList<ArrayList<String>> initialState;

    public WaterSortProblem(ArrayList<ArrayList<String>> initialState) {
        super(initialState);  
        this.initialState = initialState;
    }


	@Override
	boolean isGoalState(Object state) {
		ArrayList<ArrayList<String>> arrayState = (ArrayList<ArrayList<String>> ) state;
        for (ArrayList<String> row : arrayState) {
            String firstElement = row.get(0);
            for (String element : row) {
                if (!element.equals(firstElement)) {
                    return false; 
                }
            }
        }
        return true;
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
	
    private String getTopLayer(ArrayList<String> bottle) {
        for (int i = 0; i < bottle.size(); i++) {
            if (!bottle.get(i).equals("e")) {
                return bottle.get(i);
            }
        }
        return "e"; 
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
	
	private ArrayList<ArrayList<String>> deepCopyState(ArrayList<ArrayList<String>> state) {
	    ArrayList<ArrayList<String>> newState = new ArrayList<>();
	    for (ArrayList<String> bottle : state) {
	        newState.add(new ArrayList<>(bottle));
	    }
	    return newState;
	}
	
	@Override
	int calculatePathCost(Node current, Point operator) {
		   ArrayList<ArrayList<String>> newState = deepCopyState((ArrayList<ArrayList<String>>) current.getState());
		    ArrayList<String> sourceBottle = newState.get(operator.x);
		    ArrayList<String> sinkBottle = newState.get(operator.y);
		    int emptyLayersInSink = countEmptyLayers(sinkBottle);
		    int pourableLayersInSource = countPourableLayers(sourceBottle);
		    int layersToPour = Math.min(emptyLayersInSink, pourableLayersInSource);
		    int cost = layersToPour;
		    int cumulativeCost = current.getPathCost() + cost;
		    return cumulativeCost;
	   }

}
