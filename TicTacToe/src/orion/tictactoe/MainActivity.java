package orion.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements OnClickListener
{
    GameEngine mEngine = GameEngine.getInstance();
    SquareView mBoard[][] = new SquareView[GameEngine.MAX_ROWS][GameEngine.MAX_COLUMNS];
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        //Set the default values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        setContentView(R.layout.main);
        ViewGroup grid = (ViewGroup) findViewById(R.id.grid);
        final int gridChildCount = grid.getChildCount();
        for(int i = 0; i < gridChildCount; i ++){
            View view  = grid.getChildAt(i);
            view.setOnClickListener(this);
            SquareView square = (SquareView) view;
            mBoard[square.getRow()][square.getColumn()] = square;
        }
        
        if( savedInstanceState == null){ //We starting a brand new activity
            //setup a new game
            newGame();
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main_action_bar, menu);
        return true;
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case R.id.newGameAction:
                newGame();
            return true;
        case R.id.shareAppAction:
            shareApp();
            return true;
        case R.id.settingsAction:
            Intent settingsIntent = new Intent(this, PreferencesActivity.class);
            startActivity(settingsIntent);
            return true;
        case R.id.aboutAction:
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GameEngine.getInstance().saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GameEngine.getInstance().restoreState(savedInstanceState);
        for(int row = 0; row < GameEngine.MAX_ROWS; row++){
            for(int col = 0; col < GameEngine.MAX_COLUMNS; col++){
                Piece currentPiece = GameEngine.getInstance().getBoard(row, col);
                mBoard[row][col].setState(GameEngine.getInstance().getBoard(row, col));
                mBoard[row][col].setClickable(currentPiece == Piece.EMPTY);
                
            }
        }
    }
    
    private void newGame(){
        //Retrieve user preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean computerPlayFirst = sharedPrefs.getBoolean(PreferencesActivity
                .COMPUTER_PLAY_FIRST_KEY, getResources()
                .getBoolean(R.bool.computer_play_first_default_setting));
        String humanPieceString = sharedPrefs.getString(PreferencesActivity
                .PLAYER_PIECE_KEY,getString(R.string.player_piece_default_setting));
        Piece humanPiece = humanPieceString.equals("cross")?Piece.CROSS:Piece.NOUGHT;
        Piece computerPiece = (humanPiece == Piece.CROSS)?Piece.NOUGHT:Piece.CROSS;
        
        //Restart Engine
        GameEngine engine = GameEngine.getInstance();
        engine.reset(computerPlayFirst);
        engine.setComputerPiece(computerPiece);
        engine.setHumanPiece(humanPiece);
        engine.start();
        
        //Restart UI
        for(int row = 0; row < GameEngine.MAX_ROWS; row++){
            for(int col = 0; col < GameEngine.MAX_COLUMNS; col++){
                mBoard[row][col].setState(Piece.EMPTY);
                mBoard[row][col].setClickable(true);
            }
        }
        if (computerPlayFirst){
            SquarePos pos = engine.doComputerPlay();
            mBoard[pos.getRow()][pos.getColumn()].setState(engine.getComputerPiece());
        }
    }
    
    public void shareApp(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text));
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}
