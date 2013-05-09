package orion.tictactoe;

/**
 *  Utility class to convert from unidimensional to bidimensional and 
 *  to represent a point
 */
public class SquarePos {
    private int mRow,mCol;
    
    SquarePos(int row, int col){
        mRow = row; mCol = col;
    }
    
    @Override
    public String toString() {
        return "SquarePos [" + mRow + ", " + mCol + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mCol;
        result = prime * result + mRow;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SquarePos other = (SquarePos) obj;
        if (mCol != other.mCol) return false;
        if (mRow != other.mRow) return false;
        return true;
    }

    public int getRow(){return mRow;}
    
    public int getColumn(){return mCol;}
    
    static SquarePos fromUnidimensional(int i){
        int row = i / GameEngine.MAX_ROWS;
        int col = i % GameEngine.MAX_ROWS;
        return new SquarePos(row,col);
    }
}
