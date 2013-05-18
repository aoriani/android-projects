package orion.tictactoe;


import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;

/**
 * The class where resides the AI of the game
 * @author andre
 *
 */
public class GameEngine {
    
    private static final String LOG_TAG = "TictacToe/GameEngine";
    //Constants
    static final int MAX_ROWS = 3;
    static final int MAX_COLUMNS = 3;
    
    //State Keys
    private static final String STATE_BOARD = "GameEngine_STATE_BOARD";
    private static final String STATE_MOVE = "GameEngine_STATE_MOVE";
    private static final String STATE_HUMAN = "GameEngine_STATE_HUMAN";
    private static final String STATE_COMPUTER = "GameEngine_STATE_COMPUTER";
    private static final String STATE_STATE = "GameEngine_STATE_STATE";
    
    public static enum GameState{
        INIT,
        COMPUTER_TURN,
        HUMAN_TURN,
        COMPUTER_WINS,
        HUMAN_WINS,
        TIE;
    }
            
    
    //Internal State
    private Piece mBoard[][] = new Piece[MAX_ROWS][MAX_COLUMNS] ;
    private int mMove = 1;
    //Which piece and mComputer are using
    private Piece mHuman,mComputer;
    private boolean mComputerFirst;
    private GameState mState;
    
    //Singleton instance
    private static GameEngine sInstance = new GameEngine();
    
    GameEngine(){
        reset(true);
    }
    
    public static GameEngine getInstance(){
        return sInstance;
    }
    
    
    /**
     * Restart the game mEngine 
     * @param computerPlayFirst if the mComputer will play first in this new game
     */
    public void reset(boolean computerPlayFirst){
        for (Piece[] row: mBoard){
            for(int i = 0; i< MAX_COLUMNS; i++){//Can't use enhanced for to make the assignme
                row[i] = Piece.EMPTY;
            }
        }
        mMove = 1;
        mState = GameState.INIT;
        mComputerFirst = computerPlayFirst;
    }
    
    public Piece getBoard(int row, int col){
        if(row < 0 || row >= MAX_ROWS || col < 0 || col >= MAX_COLUMNS){
            throw new IllegalArgumentException(String
                    .format("Position out of the board: (%d,%d)", row, col));
        }
        return mBoard[row][col];
    }
    
    public GameState getState(){
        return mState;
    }
    
    public void setComputerPiece(Piece piece){
        if(piece == Piece.EMPTY) throw new IllegalArgumentException("Cannot set EMPTYh");
        mComputer = piece;
    }
    
    public Piece getComputerPiece(){
        return mComputer;
    }

    public void setHumanPiece(Piece piece){
        if(piece == Piece.EMPTY) throw new IllegalArgumentException("Cannot set EMPTY");
        mHuman = piece;
    }
    
    public Piece getHumanPiece(){
        return mHuman;
    }

    
    /**
     * Save the state to a bundle of an activity
     * @param bundle the bundle where the state shall be saved
     */
    public void saveState(Bundle bundle){
        bundle.putInt(STATE_MOVE, mMove);
        bundle.putString(STATE_COMPUTER, mComputer.name());
        bundle.putString(STATE_HUMAN, mHuman.name());
        bundle.putString(STATE_STATE, mState.name());
        ArrayList<String> flatBoard = new ArrayList<String>();
        for(int i = 0; i < MAX_ROWS; i++){
            for(int j = 0; j < MAX_COLUMNS; j++){
                flatBoard.add(mBoard[i][j].name());
            }
        }
        bundle.putStringArrayList(STATE_BOARD, flatBoard);
    }
    
    public void restoreState(Bundle bundle){
        mMove = bundle.getInt(STATE_MOVE);
        mComputer = Piece.valueOf(bundle.getString(STATE_COMPUTER));
        mHuman = Piece.valueOf(bundle.getString(STATE_HUMAN));
        mState = GameState.valueOf(bundle.getString(STATE_STATE));
        ArrayList<String> flatBoard = bundle.getStringArrayList(STATE_BOARD);
        int k = 0;
        for(int i = 0; i < MAX_ROWS; i++){
            for(int j = 0; j < MAX_COLUMNS; j++){
                mBoard[i][j] = Piece.valueOf(flatBoard.get(k++));
            }
        }
    }
    
    
    /**
     * Verify if we have winners
     * @return {@value Piece#NOUGHT} or {@value Piece#CROSS} if someone won or
     *  {@value Piece#EMPTY} if no one won
     */
    public Piece winner(){
       //Verify rows
       for (int row = 0; row < MAX_ROWS; row++){
           if ((mBoard[row][0] != Piece.EMPTY) && (mBoard[row][0] == mBoard[row][1])
                   && (mBoard[row][1] == mBoard[row][2])){
               return mBoard[row][0];
           }         
       }
       
       //Verify cols
       for(int col = 0;  col < MAX_COLUMNS; col++){
           if((mBoard[0][col] != Piece.EMPTY) && (mBoard[0][col] == mBoard[1][col])
                   && (mBoard[1][col] == mBoard[2][col])){
               return mBoard[0][col];
           }
       }
       
       // Verify diagonals
       if((mBoard[0][0] != Piece.EMPTY) && (mBoard[0][0] == mBoard[1][1]) && 
               (mBoard[1][1] == mBoard[2][2])){
           return mBoard[0][0];
       }
       
       if((mBoard[0][2] != Piece.EMPTY) && (mBoard[0][2] == mBoard[1][1]) && 
               (mBoard[1][1] == mBoard[2][0])){
           return mBoard[0][2];
       }
       
       return Piece.EMPTY; // No winners 
    }

    /**
     * Evaluate the likelyhood to win for the player give current board
     * @param player the player for each to evaluate
     * @return the number of plays the player can do in current mMove that will 
     * still allow the player to win 
     */
     int evaluatePlayerChances(Piece player){
        int playerChances = 0;
        
        //Verify rows
        for (int row = 0; row < MAX_ROWS; row++){
            if ( ((mBoard[row][0] == Piece.EMPTY) || (mBoard[row][0] == player)) &&
                    ((mBoard[row][1] == Piece.EMPTY) || (mBoard[row][1] == player))  &&
                    ((mBoard[row][2] == Piece.EMPTY) || (mBoard[row][2] == player)) ){
                playerChances++;
            }         
        }
        
        //Verify cols
        for(int col = 0;  col < MAX_COLUMNS; col++){
            if( ((mBoard[0][col] == Piece.EMPTY) || (mBoard[0][col] == player)) &&
                    ((mBoard[1][col] == Piece.EMPTY) || (mBoard[1][col] == player))  &&
                    ((mBoard[2][col] == Piece.EMPTY) || (mBoard[2][col] == player)) ){
                playerChances++;
            }
        }
        
        // Verify diagonals
        if( ((mBoard[0][0] == Piece.EMPTY) || (mBoard[0][0] == player)) &&
                ((mBoard[1][1] == Piece.EMPTY) || (mBoard[1][1] == player))  &&
                ((mBoard[2][2] == Piece.EMPTY) || (mBoard[2][2] == player)) ){
            playerChances++;
        }
        
        if( ((mBoard[0][0] == Piece.EMPTY) || (mBoard[0][0] == player)) &&
                ((mBoard[1][1] == Piece.EMPTY) || (mBoard[1][1] == player))  &&
                ((mBoard[2][2] == Piece.EMPTY) || (mBoard[2][2] == player)) ){
            playerChances++;
        }
        
        return playerChances;
    }
    
    /**
     * Given current mComputer simulated play, evaluate how much bad it is
     * based on the chances that human player has to succeed on the next play 
     * @return an weight to indicate how bad it can be the current choice for mComputer
     */
    int evaluateHowBadPlayCanBe(){
        int weights[] = {1000, 1000, 1000, 1000, 1000, 1000,1000, 1000, 1000};
        
        //Verify human choices
        for(int row = 0; row < MAX_ROWS ; row ++){
            for(int col = 0; col < MAX_COLUMNS; col++){
                if(mBoard[row][col] == Piece.EMPTY){
                    //Simulate human play in current square
                    mBoard[row][col] = mHuman;
                        //Evaluate players chances
                        int humanChances = evaluatePlayerChances(mHuman);
                        int computerChances = evaluatePlayerChances(mComputer);
                        //Calculate weights based on the changes of mComputer to succeed
                        weights[3*row + col] = computerChances - humanChances;
                        //But if this play makes human to win update weight to reflect that
                        if(winner() == mHuman) weights[3*row + col] = -100;
                    //Undo human simulated play
                    mBoard[row][col] = Piece.EMPTY;
                }
                
            }
        }
        
        //Return the lowest ( and thus the worst ) weight for current simulated plays
         int lowestWeight = weights[0];
         for(int weight: weights){
             if( weight < lowestWeight){
                 lowestWeight = weight;
             }
         }
         return lowestWeight;
    }

    /**
     * find the best play for the mComputer
     * @return The position that mComputer must play
     */
    SquarePos findBestPlay() {
        int weights[] = { -1000, -1000, -1000, -1000, -1000, -1000,-1000, -1000, -1000};
        
        //Special cases
        
        if( mMove == 1 ){
            return SquarePos.fromUnidimensional(0);
        }
        
        if ( (mMove == 3) && (mBoard[2][2] == Piece.EMPTY) ) {
            return SquarePos.fromUnidimensional(8);
        }
        
        if( (mMove == 4) && (mBoard[1][1] == mComputer)){
            if ( ((mBoard[0][0] == mHuman) && (mBoard[2][2] == mHuman)) ||
                 ((mBoard[0][2] == mHuman) && (mBoard[2][0] == mHuman)) ){
                return SquarePos.fromUnidimensional(1);
            }
            
            if( ( (mBoard[0][0] == mHuman) || (mBoard[0][1] == mHuman) ) 
                    && (mBoard[1][2] == mHuman) ){
                return SquarePos.fromUnidimensional(2);
            }

            if( ( (mBoard[0][1] == mHuman) || (mBoard[0][2] == mHuman) ) 
                    && (mBoard[1][0] == mHuman) ){
                return SquarePos.fromUnidimensional(0);
            }

            if( ( (mBoard[2][1] == mHuman) || (mBoard[2][2] == mHuman) ) 
                    && (mBoard[1][0] == mHuman) ){
                return SquarePos.fromUnidimensional(6);
            }

            if( ( (mBoard[2][0] == mHuman) || (mBoard[2][1] == mHuman) ) 
                    && (mBoard[1][2] == mHuman) ){
                return SquarePos.fromUnidimensional(8);
            }
        }
        
        
        //Heuristic - MinMax
        for(int row = 0; row < MAX_ROWS; row++){
            for(int col = 0; col < MAX_COLUMNS; col++){
                if(mBoard[row][col] == Piece.EMPTY){
                    //Simulate mComputer play
                    mBoard[row][col] = mComputer;
                        weights[3*row + col] = evaluateHowBadPlayCanBe();
                        //Highlight position that generate win
                        if(winner() == mComputer) weights[3*row + col] = 10000;
                    //Undo mComputer simulated play
                    mBoard[row][col] = Piece.EMPTY;
                }
            }
        }
        
        //Find the position with the biggest potential
        int maxPos = 0;
        for (int i = 0; i < (MAX_ROWS*MAX_COLUMNS); i++){
            if(weights[i] > weights[maxPos]){
                maxPos = i;
            }
        }
        
        return SquarePos.fromUnidimensional(maxPos);
    }
    
    /**
     * Move the mEngine to a playable mState after validating players
     */
    public void start(){
        if(mState != GameState.INIT){
            throw new IllegalStateException("To start you must be on INIT mState to start");
        }
        //Validate
        if( (mComputer == mHuman) || (mComputer == null) || (mHuman == null) ){
            throw new IllegalStateException("Computer and human shall use different"
                                        +" pieces and they must be not null");
        }
        mState = mComputerFirst? GameState.COMPUTER_TURN:GameState.HUMAN_TURN; 
    }
    
    /**
     * Ensure the player can do the wanted mMove
     * @param pos The position the playes wants to use
     * @throws IllegalArgumentException if the position is already taken
     */
    private void validatePlay(SquarePos pos) throws IllegalArgumentException{
        if( mBoard[pos.getRow()][pos.getColumn()]!= Piece.EMPTY){
            throw new IllegalArgumentException("Cannot play in occupied place");
        }
    }

    /**
     * Calculate the next mState after a play
     */
    void nextState(){
        switch(mState){
            case COMPUTER_TURN:
            case HUMAN_TURN:
                Piece currWinner = winner();
                switch(currWinner){
                    case CROSS:
                    case NOUGHT: //We have a winner
                        mState = (mComputer == currWinner)?GameState.COMPUTER_WINS:GameState.HUMAN_WINS;
                        return;
                    case EMPTY://We have a next mMove or a tie if the board has already been full filled
                        if (mMove == 10){
                            mState = GameState.TIE;
                        }else{
                            mState = (mState == GameState.COMPUTER_TURN)?GameState.HUMAN_TURN:GameState.COMPUTER_TURN;
                        }
                        return;
                }
                break;
            default:
                Log.d(LOG_TAG,"There is no next mState for "+ mState.toString());
        }
        
    }
    
    /**
     * Does the play for a human player
     * @param pos The position the human player wants to play
     * @throws IllegalStateException if it is not the human's turn
     */
    public void doHumanPlay(SquarePos pos){
        if(mState != GameState.HUMAN_TURN) {
            throw new IllegalStateException("Not computer's turn - current state is " + mState);
        }
        validatePlay(pos);
        mBoard[pos.getRow()][pos.getColumn()] = mHuman;
        mMove++;
        nextState();
    }
    
    /**
     * Does the mComputer play.
     * @throws IllegalStateException if it is not the mComputer's turn
     * @return the chosen position by computer to play
     */
    public SquarePos doComputerPlay(){
        if(mState != GameState.COMPUTER_TURN) {
            throw new IllegalStateException("Not computer's turn - current state is " + mState);
        }
        SquarePos pos = findBestPlay();
        validatePlay(pos);
        mBoard[pos.getRow()][pos.getColumn()] = mComputer;
        mMove++;
        nextState();
        return pos;
    }
    
    
    //Backdoor for testing
    void setBoard(Piece board[][]){
        assert (board.length == MAX_ROWS) && (board[0].length == MAX_COLUMNS);
        mBoard = board;
    }
    
    int getMove(){
        return mMove;
    }
    
    void setMove(int move){
        mMove = move;
    }

}
