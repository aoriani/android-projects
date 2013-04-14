
package orion.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;

/** The view to show the squares in the tic tac toe game */
public class Square extends ImageView {

    
    private static final int MAX_LINES = 3;
    private static final int MAX_COLUMNS = 3; 

    /** The column */
    private int mX = 0;

    /** The line */
    private int mY = 0;

    /** The state of the Square - empty, cross or nought */
    private Piece mState = Piece.EMPTY;
    
    
    private static class SquareSavedState extends BaseSavedState{
         private int mState;
   
        public SquareSavedState(Parcelable superParcelable, Piece state) {
            super(superParcelable);
            mState = state.ordinal();
        }

        public SquareSavedState(Parcel parcel) {
            super(parcel);
            mState = parcel.readInt();
        }
        
        public Piece getState(){
            return Piece.values()[mState];
        }

        /* (non-Javadoc)
         * @see android.view.AbsSavedState#writeToParcel(android.os.Parcel, int)
         */
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mState);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SquareSavedState> CREATOR = new Parcelable.Creator<SquareSavedState>() {
            public SquareSavedState createFromParcel(Parcel in) {
                return new SquareSavedState(in);
            }

            public SquareSavedState[] newArray(int size) {
                return new SquareSavedState[size];
            }
        };

    }
    
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
            if(x >= 0 && x < MAX_LINES) mX = x;
            
            int y = a.getInt(R.styleable.Square_y, 0);
            if(y >= 0 &&  y < MAX_COLUMNS) mY = y;
            
            int state = a.getInt(R.styleable.Square_state, 0);
            mState = Piece.values()[state];
        } finally {
            a.recycle();
        }
    }

    /* (non-Javadoc)
     * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof SquareSavedState){
            SquareSavedState savedState = (SquareSavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());
            mState = savedState.getState();
        }else{
            super.onRestoreInstanceState(state);
        }
    }

    /* (non-Javadoc)
     * @see android.view.View#onSaveInstanceState()
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superParcelable = super.onSaveInstanceState();
        return new SquareSavedState(superParcelable, mState);
    }  
}
