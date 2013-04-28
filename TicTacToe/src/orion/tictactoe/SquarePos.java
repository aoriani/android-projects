package orion.tictactoe;

/**
 *  Utility class to convert from unidimensional to bidimensional and 
 *  to represent a point
 */
public class SquarePos {
    int mRow,mCol;
    
    SquarePos(int row, int col){
        mRow = row; mCol = col;
    }
    
    public int getRow(){return mRow;}
    
    public int getColumn(){return mCol;}
    
    static SquarePos fromUnidimensional(int i){
        int row = i / GameEngine.MAX_ROWS;
        int col = i % GameEngine.MAX_ROWS;
        return new SquarePos(row,col);
    }
}
