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
    
    //Internal State
    private Piece board[][] = new Piece[MAX_ROWS][MAX_COLUMNS] ;
    private int move = 1;
    //Which piece and computer are using
    private Piece human,computer;
    
    public void reset(){
        for (Piece[] row: board){
            for(Piece col:row){
                col = Piece.EMPTY;
            }
        }
        move = 1;
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

    private int evaluatePlayerChanges(Piece player){
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
                        int humanChances = evaluatePlayerChanges(human);
                        int computerChances = evaluatePlayerChanges(computer);
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

    
    private int findBestPlay() {
        int weights[] = { -1000, -1000, -1000, -1000, -1000, -1000,-1000, -1000, -1000};
        
        //Special cases
        if( (move == 4) && (board[1][1] == computer)){
            if ( ((board[0][0] == human) && (board[2][2] == human)) ||
                 ((board[0][2] == human) && (board[2][0] == human)) ){
                return 1;
            }
            
            if( ( (board[0][0] == human) || (board[0][1] == human) ) 
                    && (board[1][2] == human) ){
                return 2;
            }

            if( ( (board[0][1] == human) || (board[0][2] == human) ) 
                    && (board[1][0] == human) ){
                return 0;
            }

            if( ( (board[2][1] == human) || (board[2][2] == human) ) 
                    && (board[1][0] == human) ){
                return 6;
            }

            if( ( (board[2][0] == human) || (board[2][1] == human) ) 
                    && (board[1][2] == human) ){
                return 8;
            }
        }
        
        if( move == 1 ) return 0;
        
        if ( (move == 3) && (board[2][2] == Piece.EMPTY) ) return 8;
        
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
        
        return maxPos;
    }
    

}
