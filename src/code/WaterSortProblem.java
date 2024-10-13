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
            // Check if all elements in the row are the same
            String firstElement = row.get(0);
            for (String element : row) {
                if (!element.equals(firstElement)) {
                    return false; // Return false if any element is different
                }
            }
        }
        return true;
	}

	@Override
	int calculatePathCost(Node current , int currentCost) {
		int parentCost = 0;
		if(current.getParent() != null) {
			parentCost = current.getParent().getPathCost();
		}
		 	
		return parentCost + currentCost;
	}

}
