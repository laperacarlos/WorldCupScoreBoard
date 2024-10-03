package lapera.world.cup.board;

import lapera.world.cup.domain.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WorldCupScoreBoardTest {
    private static ScoreBoard scoreBoard;

    @BeforeAll
    public static void createScoreBoard() {
        scoreBoard = new WorldCupScoreBoard();
    }

    @AfterEach
    public void cleanInMemoryData() {
        scoreBoard.getLiveGames().clear();
    }
    
    @Test
    void shouldAddNewGameToBoardAndReturnLiveScoreBoard() { 
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", 0, 0, LocalDateTime.of(2024, 10,13, 13, 13, 13),true);
        
        //when
        scoreBoard.addGame(newGame);
        
        //then
        assertEquals(1, scoreBoard.getLiveGames().size());
        
    }

    @Test
    void shouldUpdateGameScore() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", 0, 0, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        final Game updatedScore = updateGame(newGame,2, 0, true);
        scoreBoard.addGame(newGame);
        
        //when
        scoreBoard.updateGame(updatedScore);
        
        //then
        final Game updatedGame = scoreBoard.getLiveGames().get(0);
        
        assertAll("Group assertions of Game scores",
                () -> assertEquals(2, updatedGame.getHomeScore()),
                () -> assertEquals(0, updatedGame.getAwayScore()));
    }

    @Test
    void shouldEndGameAndRemoveFromBoard() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", 0, 0, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        final Game endedGame = updateGame(newGame, 7, 1, false);
        scoreBoard.addGame(newGame);
        
        //when
        scoreBoard.removeGame(endedGame);
        
        //then
        assertEquals(0, scoreBoard.getLiveGames().size());
    }
    
    private Game updateGame(final Game game, final Integer homeScore, final Integer awayScore, final boolean isLive) {
        return new Game(game.getHomeTeam(), game.getAwayTeam(), homeScore, awayScore, game.getStartTime(), isLive);
    }
}
