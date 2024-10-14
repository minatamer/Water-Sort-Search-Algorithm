package code;

import java.util.ArrayList;
import java.util.HashSet;
import java.awt.Point;

public abstract class Problem {

    private Object initialState;
    private HashSet<Point> operators;
	private HashSet<ArrayList<ArrayList<String>> > stateSpace;

    public Problem(Object initialState) {
        this.initialState = initialState;
        this.operators = new HashSet<>();
        this.stateSpace = new HashSet<>();
    }

    public Object getInitialState() {
        return initialState;
    }


    public HashSet<Point> getOperators() {
		return operators;
	}

	public void setOperators(HashSet<Point> operators) {
		this.operators = operators;
	}

	public HashSet<ArrayList<ArrayList<String>>> getStateSpace() {
		return stateSpace;
	}

	public void setStateSpace(HashSet<ArrayList<ArrayList<String>>> stateSpace) {
		this.stateSpace = stateSpace;
	}

	public void setInitialState(Object initialState) {
		this.initialState = initialState;
	}

	abstract boolean isGoalState(Object state);

    abstract int calculatePathCost(Node current , Point operator);

}
