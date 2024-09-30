package code;

import java.awt.Point;
import java.util.ArrayList;

public class WaterSortSearch extends GenericSearch{

	public WaterSortSearch(ArrayList<Node> nodes, ArrayList<Point> operators){
		super(nodes, operators);
		
	}

	@Override
	Boolean goalTest(Node current) {
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

	@Override
	Object applyOperator(Node current, Point operator) {
		
		return null;
	}

	@Override
	ArrayList<Point> getOperators(Node current) {
		
		return null;
	}

}
