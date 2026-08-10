package MazeSolver.gui; 


public class Node {

    private int row, col;

    public Node(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    @Override
    public boolean equals(Object o) {
        Node n = (Node)o;
        return n.row == row && n.col == col;
    }
}