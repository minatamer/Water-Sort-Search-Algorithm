package code;

import java.awt.Point;

public class Node {
    private Object state;
    private Node parent;
    private Point operator;
    private int depth;
    private int pathCost;

    public Node(Object state, Node parent, Point operator, int depth, int pathCost) {
        this.state = state;
        this.parent = parent;
        this.operator = operator;
        this.depth = depth;
        this.pathCost = pathCost;
    }

    public Object getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public Point getOperator() {
        return operator;
    }

    public int getDepth() {
        return depth;
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setOperator(Point operator) {
        this.operator = operator;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    @Override
    public String toString() {
        return "Node [state=" + state + ", operator=" + operator + 
               ", depth=" + depth + ", pathCost=" + pathCost + "]";
    }
}
