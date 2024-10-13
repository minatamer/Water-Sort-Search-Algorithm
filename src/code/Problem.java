package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;

public abstract class Problem {

    private Object initialState;
    private ArrayList<String> operators;
	private HashSet<ArrayList<ArrayList<String>> > stateSpace;

    public Problem(Object initialState) {
        this.initialState = initialState;
        this.operators = new ArrayList<>();
        this.stateSpace = new HashSet<>();
    }

    public Object getInitialState() {
        return initialState;
    }


    public ArrayList<String> getOperators() {
		return operators;
	}

	public void setOperators(ArrayList<String> operators) {
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

    abstract int calculatePathCost(Node current , int currentCost);

}
