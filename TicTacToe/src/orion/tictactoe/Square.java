
package orion.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

/** The view to show the squares in the tic tac toe game */
public class Square extends ImageView {

    
    private static final int MAX_LINES = 3;
    private static final int MAX_COLUMN = 3; 

    /** The column */
    private int mX = 0;

    /** The line */
    private int mY = 0;

    /** The state of the Square - empty, cross or nought */
    private Piece mState = Piece.EMPTY;
    
    public Square(Context context) {
        super(context);
    }

    public Square(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Square(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Square);
        try {
            int x = a.getInt(R.styleable.Square_x, 0);
            if(x >= 0 && x < 3) mX = x;
            
            int y = a.getInt(R.styleable.Square_y, 0);
            if(y >= 0 &&  y < 3) mY = y;
            
            int state = a.getInt(R.styleable.Square_state, 0);
            mState = Piece.values()[state];
        } finally {
            a.recycle();
        }
    }

}
