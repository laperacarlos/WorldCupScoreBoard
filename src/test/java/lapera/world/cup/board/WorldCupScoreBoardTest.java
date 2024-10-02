package lapera.world.cup.board;

import lapera.world.cup.domain.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WorldCupScoreBoardTest {
    private static ScoreBoard scoreBoard;

    @BeforeAll
    public static void createScoreBoard() {
        scoreBoard = new WorldCupScoreBoard();
    }
    
    @Test
    void shouldAddNewGameToBoardAndReturnLiveScoreBoard() { 
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", 0, 0, false);
        
        //when
        scoreBoard.addGame(newGame);
        
        //then
        assertEquals(1, scoreBoard.getLiveScoreBoard().size());
        
    }

    @Test
    void shouldUpdateGameScore() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", 0, 0, false);
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", 2, 0, false);
        scoreBoard.addGame(newGame);
        
        //when
        scoreBoard.updateGame(updatedScore);
        
        //then
        final Game updatedGame = scoreBoard.getLiveScoreBoard().get(0);
        
        assertAll("Group assertions of Game scores",
                () -> assertEquals(2, updatedGame.getHomeScore()),
                () -> assertEquals(0, updatedGame.getAwayScore()));
    }

    @Test
    void shouldEndGameAndRemoveFromBoard() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", 0, 0, false);
        final Game endedGame = new Game("Rossoneri", "Bianconeri", 7, 0, true);
        scoreBoard.addGame(newGame);
        
        //when
        scoreBoard.removeGame(endedGame);
        
        //then
        assertEquals(0, scoreBoard.getLiveScoreBoard().size());
    }
}
