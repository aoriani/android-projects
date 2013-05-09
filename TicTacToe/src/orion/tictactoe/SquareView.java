
package orion.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;

/** The view to show the squares in the tic tac toe game */
public class SquareView extends ImageView {

    /** The row */
    private int mRow = 0;

    /** The column */
    private int mColumn = 0;

    /** The state of the SquareView - empty, cross or nought */
    private Piece mState = Piece.EMPTY;
    
    /** Class to save the SquareView view's state */
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
    
    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareView);
        try {
            int x = a.getInt(R.styleable.SquareView_row, 0);
            if(x >= 0 && x < GameEngine.MAX_ROWS) mRow = x;
            
            int y = a.getInt(R.styleable.SquareView_column, 0);
            if(y >= 0 &&  y < GameEngine.MAX_COLUMNS) mColumn = y;
            
            int state = a.getInt(R.styleable.SquareView_state, 0);
            setState(Piece.values()[state]);
        } finally {
            a.recycle();
        }
    }
    
    public int getRow(){
        return mRow;
    }
    
    public int getColumn(){
        return mColumn;
    }
    
    public void setState(Piece state){
        mState = state;
        this.setImageDrawable(mState.getMedia(getContext()));
    }
    
    public Piece getState(){
        return mState;
    }
    
    /* (non-Javadoc)
     * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof SquareSavedState){
            SquareSavedState savedState = (SquareSavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());
            setState(savedState.getState());
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
