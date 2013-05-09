package orion.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.util.Log;

public class Main extends Activity implements OnClickListener
{
    private static final String LOG_TAG = "TicTacToe/MainActivity";
    
    GameEngine mEngine = GameEngine.getInstance();
    SquareView mBoard[][] = new SquareView[GameEngine.MAX_ROWS][GameEngine.MAX_COLUMNS];
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewGroup grid = (ViewGroup) findViewById(R.id.grid);
        final int gridChildCount = grid.getChildCount();
        for(int i = 0; i < gridChildCount; i ++){
            View view  = grid.getChildAt(i);
            view.setOnClickListener(this);
            SquareView square = (SquareView) view;
            mBoard[square.getRow()][square.getColumn()] = square;
        }
        mEngine.reset(false);
        mEngine.setHumanPiece(Piece.CROSS);
        mEngine.setComputerPiece(Piece.NOUGHT);
        mEngine.start();
        
    }

    @Override
    public void onClick(View clickedView) {
        if(clickedView instanceof SquareView){
            SquareView square = (SquareView) clickedView;
            if(mEngine.getState() == GameEngine.GameState.HUMAN_TURN){
                mEngine.doHumanPlay(new SquarePos(square.getRow(),square.getColumn()));
                square.setClickable(false);
                square.setState(mEngine.getHumanPiece());
            }
            if(mEngine.getState() == GameEngine.GameState.COMPUTER_TURN){
                SquarePos computerPlay = mEngine.doComputerPlay();
                mBoard[computerPlay.getRow()][computerPlay.getColumn()]
                        .setState(mEngine.getComputerPiece());
                mBoard[computerPlay.getRow()][computerPlay.getColumn()].setClickable(false);
            }
            switch(mEngine.getState()){
                case COMPUTER_WINS:
                    Toast.makeText(this, "Computer won", Toast.LENGTH_LONG).show();
                    break;
                case HUMAN_WINS:
                    Toast.makeText(this, "Human won", Toast.LENGTH_LONG).show();
                    break;
                case TIE:
                    Toast.makeText(this, "Tie", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
        
    }
}
