package orion.tictactoe;

/**
 * The class where resides the AI of the game
 * @author andre
 *
 */
public class GameEngine {
    
    //Constants
    static final int MAX_ROWS = 3;
    static final int MAX_COLUMNS = 3;
    
    public static enum GameState{
        INIT,
        COMPUTER_TURN,
        HUMAN_TURN,
        COMPUTER_WINS,
        HUMAN_WINS,
        TIE;
    }
            
    
    //Internal State
    private Piece board[][] = new Piece[MAX_ROWS][MAX_COLUMNS] ;
    private int move = 1;
    //Which piece and computer are using
    private Piece human,computer;
    private boolean computerFirst;
    private GameState state;
    
    public GameEngine(){
        reset(true);
    }
    
    
    /**
     * Restart the game engine 
     * @param computerPlayFirst if the computer will play first in this new game
     */
    public void reset(boolean computerPlayFirst){
        for (Piece[] row: board){
            for(Piece col:row){
                col = Piece.EMPTY;
            }
        }
        move = 1;
        state = GameState.INIT;
        computerFirst = computerPlayFirst;
    }
    
    public Piece getBoard(int row, int col){
        if(row < 0 || row >= MAX_ROWS || col < 0 || col >= MAX_COLUMNS){
            throw new IllegalArgumentException(String
                    .format("Position out of the board: (%d,%d)", row, col));
        }
        return board[row][col];
    }
    
    public GameState getState(){
        return state;
    }
    
    public void setComputerPiece(Piece piece){
        if(piece == Piece.EMPTY) throw new IllegalArgumentException("Cannot set EMPTYh");
        computer = piece;
    }

    public void setHumanPiece(Piece piece){
        if(piece == Piece.EMPTY) throw new IllegalArgumentException("Cannot set EMPTYh");
        human = piece;
    }

    /**
     * Verify if we have winners
     * @return {@value Piece#NOUGHT} or {@value Piece#CROSS} if someone won or
     *  {@value Piece#EMPTY} if no one won
     */
    public Piece winner(){
       //Verify rows
       for (int row = 0; row < MAX_ROWS; row++){
           if ((board[row][0] != Piece.EMPTY) && (board[row][0] == board[row][1])
                   && (board[row][1] == board[row][2])){
               return board[row][0];
           }         
       }
       
       //Verify cols
       for(int col = 0;  col < MAX_COLUMNS; col++){
           if((board[0][col] != Piece.EMPTY) && (board[0][col] == board[1][col])
                   && (board[1][col] == board[2][col])){
               return board[0][col];
           }
       }
       
       // Verify diagonals
       if((board[0][0] != Piece.EMPTY) && (board[0][0] == board[1][1]) && 
               (board[1][1] == board[2][2])){
           return board[0][0];
       }
       
       if((board[0][2] != Piece.EMPTY) && (board[0][2] == board[1][1]) && 
               (board[1][1] == board[2][0])){
           return board[0][0];
       }
       
       return Piece.EMPTY; // No winners 
    }

    /**
     * Evaluate the likelyhood to win for the player give current board
     * @param player the player for each to evaluate
     * @return the number of plays the player can do in current move that will 
     * still allow the player to win 
     */
    private int evaluatePlayerChances(Piece player){
        int playerChances = 0;
        
        //Verify rows
        for (int row = 0; row < MAX_ROWS; row++){
            if ( ((board[row][0] == Piece.EMPTY) || (board[row][0] == player)) &&
                    ((board[row][1] == Piece.EMPTY) || (board[row][1] == player))  &&
                    ((board[row][2] == Piece.EMPTY) || (board[row][2] == player)) ){
                playerChances++;
            }         
        }
        
        //Verify cols
        for(int col = 0;  col < MAX_COLUMNS; col++){
            if( ((board[0][col] == Piece.EMPTY) || (board[0][col] == player)) &&
                    ((board[1][col] == Piece.EMPTY) || (board[1][col] == player))  &&
                    ((board[2][col] == Piece.EMPTY) || (board[2][col] == player)) ){
                playerChances++;
            }
        }
        
        // Verify diagonals
        if( ((board[0][0] == Piece.EMPTY) || (board[0][0] == player)) &&
                ((board[1][1] == Piece.EMPTY) || (board[1][1] == player))  &&
                ((board[2][2] == Piece.EMPTY) || (board[2][2] == player)) ){
            playerChances++;
        }
        
        if( ((board[0][0] == Piece.EMPTY) || (board[0][0] == player)) &&
                ((board[1][1] == Piece.EMPTY) || (board[1][1] == player))  &&
                ((board[2][2] == Piece.EMPTY) || (board[2][2] == player)) ){
            playerChances++;
        }
        
        return playerChances;
    }
    
    
    /**
     * Given current computer simulated play, evaluate how much bad it is
     * based on the chances that human player has to succeed on the next play 
     * @return an weight to indicate how bad it can be the current choice for computer
     */
    private int evaluateHowBadPlayCanBe(){
        int weights[] = {1000, 1000, 1000, 1000, 1000, 1000,1000, 1000, 1000};
        
        //Verify human choices
        for(int row = 0; row < MAX_ROWS ; row ++){
            for(int col = 0; col < MAX_COLUMNS; col++){
                if(board[row][col] == Piece.EMPTY){
                    //Simulate human play in current square
                    board[row][col] = human;
                        //Evaluate players chances
                        int humanChances = evaluatePlayerChances(human);
                        int computerChances = evaluatePlayerChances(computer);
                        //Calculate weights based on the changes of computer to succeed
                        weights[3*row + col] = computerChances - humanChances;
                        //But if this play makes human to win update weight to reflect that
                        if(winner() == human) weights[3*row + col] = -100;
                    //Undo human simulated play
                    board[row][col] = Piece.EMPTY;
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

    
    private SquarePos findBestPlay() {
        int weights[] = { -1000, -1000, -1000, -1000, -1000, -1000,-1000, -1000, -1000};
        
        //Special cases
        if( (move == 4) && (board[1][1] == computer)){
            if ( ((board[0][0] == human) && (board[2][2] == human)) ||
                 ((board[0][2] == human) && (board[2][0] == human)) ){
                return SquarePos.fromUnidimensional(1);
            }
            
            if( ( (board[0][0] == human) || (board[0][1] == human) ) 
                    && (board[1][2] == human) ){
                return SquarePos.fromUnidimensional(2);
            }

            if( ( (board[0][1] == human) || (board[0][2] == human) ) 
                    && (board[1][0] == human) ){
                return SquarePos.fromUnidimensional(0);
            }

            if( ( (board[2][1] == human) || (board[2][2] == human) ) 
                    && (board[1][0] == human) ){
                return SquarePos.fromUnidimensional(6);
            }

            if( ( (board[2][0] == human) || (board[2][1] == human) ) 
                    && (board[1][2] == human) ){
                return SquarePos.fromUnidimensional(8);
            }
        }
        
        if( move == 1 ) return SquarePos.fromUnidimensional(0);
        
        if ( (move == 3) && (board[2][2] == Piece.EMPTY) ) return SquarePos.fromUnidimensional(8);
        
        //Heuristic - MinMax
        for(int row = 0; row < MAX_ROWS; row++){
            for(int col = 0; col < MAX_COLUMNS; col++){
                if(board[row][col] == Piece.EMPTY){
                    //Simulate computer play
                    board[row][col] = computer;
                        weights[3*row + col] = evaluateHowBadPlayCanBe();
                        //Highlight position that generate win
                        if(winner() == computer) weights[3*row + col] = 10000;
                    //Undo computer simulated play
                    board[row][col] = Piece.EMPTY;
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
    
    
    public void start(){
        if(state != GameState.INIT){
            throw new IllegalStateException("To start you must be on INIT state to start");
        }
        //Validate
        if( computer != human ){
            throw new IllegalStateException("Computer and human shall use different pieces");
        }
        state = computerFirst? GameState.COMPUTER_TURN:GameState.HUMAN_TURN; 
    }
    
    public void doHumanPlay(SquarePos pos){
        //TODO: Implement
    }

    public void doComputerPlay(){
        //TODO: Implement
    }

}
