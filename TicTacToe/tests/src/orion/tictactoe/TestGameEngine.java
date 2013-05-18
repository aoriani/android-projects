package orion.tictactoe;

import android.os.Bundle;
import orion.tictactoe.GameEngine.GameState;
import junit.framework.TestCase;

public class TestGameEngine extends TestCase {
    
    private GameEngine engine;
    
    @Override
    protected void setUp() throws Exception {
        engine = new GameEngine();
    }

    public void testWinnerboard() {
        Piece board[][] ={
                {Piece.CROSS,Piece.EMPTY,Piece.CROSS},
                {Piece.EMPTY, Piece.NOUGHT, Piece.NOUGHT},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        assertTrue("No winners expected",engine.winner() == Piece.EMPTY);
    }

    public void testWinnerFirstRow() {
        Piece board[][] ={
                {Piece.CROSS,Piece.CROSS,Piece.CROSS},
                {Piece.EMPTY, Piece.NOUGHT, Piece.NOUGHT},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        assertTrue("Cross victory expected",engine.winner() == Piece.CROSS);
    }
    
    public void testWinnerSecondRow() {
        Piece board[][] ={
                {Piece.CROSS,Piece.EMPTY,Piece.CROSS},
                {Piece.NOUGHT, Piece.NOUGHT, Piece.NOUGHT},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        assertTrue("Nought Expected to win",engine.winner() == Piece.NOUGHT);
    }
    
    public void testWinnerThirdRow() {
        Piece board[][] ={
                {Piece.CROSS,Piece.EMPTY,Piece.CROSS},
                {Piece.EMPTY, Piece.NOUGHT, Piece.NOUGHT},
                {Piece.CROSS, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        assertTrue("Cross expected to win",engine.winner() == Piece.CROSS);
    }
    
    public void testWinnerFirstColumn() {
        Piece board[][] ={
                {Piece.NOUGHT,Piece.EMPTY,Piece.CROSS},
                {Piece.NOUGHT, Piece.NOUGHT, Piece.NOUGHT},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        assertTrue("No winners expected",engine.winner() == Piece.NOUGHT);
    }
    
    public void testWinnerSecondColumn() {
        Piece board[][] ={
                {Piece.CROSS,Piece.CROSS,Piece.CROSS},
                {Piece.EMPTY, Piece.CROSS, Piece.NOUGHT},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        assertTrue("Cross expected to win",engine.winner() == Piece.CROSS);
    }
    
    public void testWinnerThirdColumn() {
        Piece board[][] ={
                {Piece.CROSS,Piece.EMPTY,Piece.CROSS},
                {Piece.EMPTY, Piece.NOUGHT, Piece.CROSS},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        GameEngine engine = new GameEngine();
        engine.setBoard(board);
        assertTrue("Expected CROSS to win",engine.winner() == Piece.CROSS);
    }
    
    public void testWinnerNWSEDiagonal() {
        Piece board[][] ={
                {Piece.CROSS,Piece.EMPTY,Piece.CROSS},
                {Piece.EMPTY, Piece.CROSS, Piece.NOUGHT},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        assertTrue("Expectec Cross to win",engine.winner() == Piece.CROSS);
    }
    
    public void testWinnerNESWDiagonal() {
        Piece board[][] ={
                {Piece.CROSS,Piece.EMPTY,Piece.NOUGHT},
                {Piece.EMPTY, Piece.NOUGHT, Piece.NOUGHT},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        assertTrue("Expectec NOUGHT to win",engine.winner() == Piece.NOUGHT);
    }
    
    public void testTieCondition(){
        engine.reset(false);
        engine.setComputerPiece(Piece.CROSS);
        engine.setHumanPiece(Piece.NOUGHT);
        
        assertEquals("Initial state must be INIT", GameState.INIT, engine.getState());
        engine.start();
        assertEquals("After start state must be HUMAN_TURN", GameState.HUMAN_TURN, engine.getState());
        //Lets move to the end
        engine.setMove(10);
        //Move to next state
        engine.nextState();
        assertEquals("In the 10th move with no winners expected state is  TIE",
                GameState.TIE, engine.getState());
    }
    
    public void testComputerWinsCondition(){
        engine.reset(false);
        engine.setComputerPiece(Piece.CROSS);
        engine.setHumanPiece(Piece.NOUGHT);
        engine.start();
        assertEquals("We should be on move 1",1, engine.getMove());
        engine.doHumanPlay(new SquarePos(1,1));
        assertEquals("We should be on move 2",2, engine.getMove());
        assertEquals("It must be computer's turn", GameState.COMPUTER_TURN, engine.getState());
        Piece board[][] ={
                {Piece.EMPTY,Piece.EMPTY,Piece.CROSS},
                {Piece.NOUGHT, Piece.NOUGHT, Piece.CROSS},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        engine.nextState();
        assertEquals("After the plays, computer must wins", GameState.COMPUTER_WINS,engine.getState());
    }
    
    public void testHumanWinsCondition(){
        engine.reset(false);
        engine.setComputerPiece(Piece.CROSS);
        engine.setHumanPiece(Piece.NOUGHT);
        engine.start();
        Piece board[][] ={
                {Piece.NOUGHT,Piece.EMPTY,Piece.CROSS},
                {Piece.NOUGHT, Piece.NOUGHT, Piece.NOUGHT},
                {Piece.NOUGHT, Piece.CROSS, Piece.CROSS}
        };
        engine.setBoard(board);
        engine.nextState();
        assertEquals("After the plays, human must wins", GameState.HUMAN_WINS,engine.getState());
    }
    
    public void testEvaluatePlayerChancesEmptyBoard(){
        assertEquals("On empty board we have chances on all rows, columns and diagonals",
                8, engine.evaluatePlayerChances(Piece.CROSS));
    }
    
    public void testEvaluatePlayerChancesEnemyOnAllBoard(){
        Piece board[][] ={
                {Piece.NOUGHT,Piece.CROSS,Piece.NOUGHT},
                {Piece.CROSS, Piece.EMPTY, Piece.EMPTY,},
                {Piece.NOUGHT, Piece.EMPTY,Piece.NOUGHT,}
        };
        engine.setBoard(board);
        assertEquals("With the other player with pieces on all corners there is only the central row and col",
                2, engine.evaluatePlayerChances(Piece.CROSS));
    }
    
    public void testevaluateHowBadPlayCanHumanHasWinChance(){
        //If with the current computer play 
        // human can win the next play, then qualify this play as very bad
        engine.setComputerPiece(Piece.CROSS);
        engine.setHumanPiece(Piece.NOUGHT);
        Piece board[][] ={
                {Piece.NOUGHT,Piece.EMPTY,Piece.NOUGHT},
                {Piece.CROSS, Piece.EMPTY, Piece.CROSS,},
                {Piece.CROSS, Piece.EMPTY,Piece.CROSS,}
        };
        engine.setBoard(board);
        assertEquals("Human can win in the next play, this must be considered bad play",
                -100, engine.evaluateHowBadPlayCanBe());
    }
    
    public void testevaluateHowBadPlayCanHumanHasBetterChances(){
        //Human will not win but it will have better chances to win
        // than computer
        engine.setComputerPiece(Piece.CROSS);
        engine.setHumanPiece(Piece.NOUGHT);
        Piece board[][] ={
                {Piece.EMPTY,Piece.EMPTY,Piece.EMPTY},
                {Piece.CROSS, Piece.NOUGHT, Piece.CROSS,},
                {Piece.EMPTY, Piece.EMPTY,Piece.EMPTY,}
        };
        engine.setBoard(board);
        assertTrue("Human must still have better chances", engine.evaluateHowBadPlayCanBe() < 0 );
    }
    
    public void testevaluateHowBadPlayCanComputerHasBetterChances(){
        //Human will not win but it will have better chances to win
        // than computer
        engine.setComputerPiece(Piece.NOUGHT);
        engine.setHumanPiece(Piece.CROSS);
        Piece board[][] ={
                {Piece.NOUGHT,Piece.EMPTY,Piece.NOUGHT},
                {Piece.CROSS, Piece.NOUGHT, Piece.CROSS,},
                {Piece.EMPTY, Piece.EMPTY,Piece.EMPTY,}
        };
        engine.setBoard(board);
        assertTrue("Computer must  have better chances", engine.evaluateHowBadPlayCanBe() > 0 );
    }
    
    public void testHeuristicChoosesWinningSpot(){
        engine.setComputerPiece(Piece.NOUGHT);
        engine.setHumanPiece(Piece.CROSS);
        engine.setMove(5); //Avoid special cases;
        
        Piece board[][] ={
                {Piece.NOUGHT,Piece.EMPTY,Piece.NOUGHT},
                {Piece.CROSS, Piece.EMPTY, Piece.CROSS,},
                {Piece.CROSS, Piece.EMPTY,Piece.EMPTY,}
        };
        engine.setBoard(board);
        assertEquals("Computer has to choose the winning spot (0,1)", 
                new SquarePos(0,1),engine.findBestPlay());
    }
    
    public void testHeuristicChoosesPreventsHumanVictory(){
        engine.setComputerPiece(Piece.NOUGHT);
        engine.setHumanPiece(Piece.CROSS);
        engine.setMove(4); //Avoid special cases;
        
        Piece board[][] ={
                {Piece.NOUGHT,Piece.EMPTY,Piece.EMPTY},
                {Piece.CROSS, Piece.EMPTY, Piece.NOUGHT,},
                {Piece.CROSS, Piece.EMPTY,Piece.CROSS,}
        };
        engine.setBoard(board);
        assertEquals("Computer has to choose to block human victory (2,1)", 
                new SquarePos(2,1),engine.findBestPlay());
    }
    
    /**
     * Test if the saving and restoring of the board is working properly
     */
    public void testSaveRestore(){
        engine.setComputerPiece(Piece.NOUGHT);
        engine.setHumanPiece(Piece.CROSS);
        engine.setMove(4);
        engine.start();
        
        Piece board[][] ={
                {Piece.NOUGHT,Piece.EMPTY,Piece.EMPTY},
                {Piece.CROSS, Piece.EMPTY, Piece.NOUGHT,},
                {Piece.CROSS, Piece.EMPTY,Piece.CROSS,}
        };
        
        engine.setBoard(board);
        Bundle bundle = new Bundle();
        engine.saveState(bundle);
        
        GameEngine restoredEngine = new GameEngine();
        restoredEngine.restoreState(bundle);
        assertEquals(engine.getMove(),restoredEngine.getMove());
        assertEquals(engine.getState(), restoredEngine.getState());
        assertEquals(engine.getComputerPiece(),restoredEngine.getComputerPiece());
        assertEquals(engine.getHumanPiece(), restoredEngine.getHumanPiece());
        for(int row = 0; row < GameEngine.MAX_ROWS; row++){
            for(int col = 0; col < GameEngine.MAX_COLUMNS; col++){
                assertEquals(String.format("Asserting for [%d,%d]",row,col), 
                        engine.getBoard(row, col), restoredEngine.getBoard(row, col));
            }
        }
        
    }
}
