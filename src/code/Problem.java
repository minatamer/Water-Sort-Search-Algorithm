package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Problem {

    private Object initialState;
    private ArrayList<String> operators;
    private ArrayList<String> stateSpace; 

    public Problem(Object initialState) {
        this.initialState = initialState;
        this.operators = new ArrayList<>();
        this.stateSpace = new ArrayList<>();
    }

    public Object getInitialState() {
        return initialState;
    }


    abstract boolean isGoalState(Object state);

    abstract int getPathCost(Node current , int currentCost);

}
