package lapera.world.cup.manager;

import lapera.world.cup.board.ScoreBoard;
import lapera.world.cup.board.WorldCupScoreBoard;
import lapera.world.cup.domain.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WorldCupScoreBoardManagerTest {
    private static ScoreBoardManager scoreBoardManager;
    
    @BeforeAll
    public static void createScoreBoardManager() {
        final ScoreBoard worldCupScoreBoard = new WorldCupScoreBoard();
        scoreBoardManager = new WorldCupScoreBoardManagerImpl(worldCupScoreBoard);
    }
    
    @AfterEach
    public void cleanInMemoryData() { 
        scoreBoardManager.getLiveGames().clear();
    }
    
    @Test
    public void shouldStartNewGame() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        
        //when
        scoreBoardManager.startGame(newGame);
        
        //then
        final List<Game> liveGames = scoreBoardManager.getLiveGames();
        final Game liveGame = liveGames.get(0);
        assertAll("Assert new Game started with initial scores",
                () -> assertEquals(1, liveGames.size()),
                () -> assertEquals(0, liveGame.getHomeScore()),
                () -> assertEquals(0, liveGame.getAwayScore()),
                () -> assertFalse(liveGame.isFinished()));
    }

    @Test
    public void shouldNotStartNewGameWhenHomeTeamMissing() {
        //given
        final Game firstGame = new Game(null, "Bianconeri", null, null, false);
        final Game secondGame = new Game("", "Bianconeri", null, null, false);

        //when
        scoreBoardManager.startGame(firstGame);
        scoreBoardManager.startGame(secondGame);

        //then
        assertEquals(0, scoreBoardManager.getLiveGames().size());
    }

    @Test
    public void shouldNotStartNewGameWhenAwayTeamMissing() {
        //given
        final Game firstGame = new Game("Rossoneri", null, null, null, false);
        final Game secondGame = new Game("Rossoneri", "", null, null, false);

        //when
        scoreBoardManager.startGame(firstGame);
        scoreBoardManager.startGame(secondGame);

        //then
        assertEquals(0, scoreBoardManager.getLiveGames().size());
    }

    @Test
    public void shouldNotStartNewGameWhenGameMarkedAsFinished() {
        //given
        final Game firstGame = new Game("Rossoneri", "Bianconeri", null, null, true);
        
        //when
        scoreBoardManager.startGame(firstGame);

        //then
        assertEquals(0, scoreBoardManager.getLiveGames().size());
    }


    @Test
    public void shouldUpdateScoreBoard() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", 3, 0, false);
        scoreBoardManager.startGame(newGame);

        //when
        scoreBoardManager.updateScore(updatedScore);

        //then
        final Game updatedGame = scoreBoardManager.getLiveGames().get(0);
        assertAll("Group assertions of Game scores",
                () -> assertEquals(3, updatedGame.getHomeScore()),
                () -> assertEquals(0, updatedGame.getAwayScore()));
    }

    @Test
    public void shouldNotUpdateScoreBoardWhenHomeScoreMissing() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", null, 2, false);
        scoreBoardManager.startGame(newGame);
        
        //when
        scoreBoardManager.updateScore(updatedScore);

        //then
        final Game updatedGame = scoreBoardManager.getLiveGames().get(0);
        assertAll("Group assertions of Game scores",
                () -> assertEquals(0, updatedGame.getHomeScore()),
                () -> assertEquals(0, updatedGame.getAwayScore()));
    }

    @Test
    public void shouldNotUpdateScoreBoardWhenAwayScoreMissing() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", 3, null, false);
        scoreBoardManager.startGame(newGame);
        
        //when
        scoreBoardManager.updateScore(updatedScore);

        //then
        final Game updatedGame = scoreBoardManager.getLiveGames().get(0);
        assertAll("Group assertions of Game scores",
                () -> assertEquals(0, updatedGame.getHomeScore()),
                () -> assertEquals(0, updatedGame.getAwayScore()));
    }

    @Test
    public void shouldNotUpdateScoreBoardWhenHomeScoreNegative() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", -2, 2, false);
        scoreBoardManager.startGame(newGame);

        //when
        scoreBoardManager.updateScore(updatedScore);

        //then
        final Game updatedGame = scoreBoardManager.getLiveGames().get(0);
        assertAll("Group assertions of Game scores",
                () -> assertEquals(0, updatedGame.getHomeScore()),
                () -> assertEquals(0, updatedGame.getAwayScore()));
    }

    @Test
    public void shouldNotUpdateScoreBoardWhenAwayScoreNegative() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", 3, -5, false);
        scoreBoardManager.startGame(newGame);

        //when
        scoreBoardManager.updateScore(updatedScore);

        //then
        final Game updatedGame = scoreBoardManager.getLiveGames().get(0);
        assertAll("Group assertions of Game scores",
                () -> assertEquals(0, updatedGame.getHomeScore()),
                () -> assertEquals(0, updatedGame.getAwayScore()));
    }
    
    @Test
    public void shouldNotUpdateScoreBoardWhenGameIsNotStarted() {
        //given
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", 3, 0, false);

        //when
        scoreBoardManager.updateScore(updatedScore);

        //then
        assertEquals(0, scoreBoardManager.getLiveGames().size());
    }

    @Test
    public void shouldRemoveFromLiveScoreBoardAfterUpdateWhenGameIsFinished() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", 7, 0, true);
        scoreBoardManager.startGame(newGame);
        
        //when
        scoreBoardManager.updateScore(updatedScore);
        
        //then
        assertEquals(0, scoreBoardManager.getLiveGames().size());
    }

    @Test
    public void shouldRemoveFromLiveScoreBoardAfterEndingGame() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        final Game endedGame = new Game("Rossoneri", "Bianconeri", 7, 0, true);
        scoreBoardManager.startGame(newGame);

        //when
        scoreBoardManager.endGame(endedGame);

        //then
        assertEquals(0, scoreBoardManager.getLiveGames().size());
    }

    @Test
    public void shouldReturnSortedLiveScoreBoard() {
        //given
        final Game firstGame = new Game("Rossoneri", "Bianconeri", null, null, false);
        final Game secondGame = new Game("ManU", "ManC", null, null, false);
        final Game thirdGame = new Game("FCB", "RCM", null, null, false);
        final Game firstGameUpdated = new Game("Rossoneri", "Bianconeri", 5, 1, false);
        final Game secondGameUpdated = new Game("FCB", "RCM", 6, 0, false);
        scoreBoardManager.startGame(firstGame);
        scoreBoardManager.startGame(secondGame);
        scoreBoardManager.startGame(thirdGame);

        //when
        scoreBoardManager.updateScore(firstGameUpdated);
        scoreBoardManager.updateScore(secondGameUpdated);

        //then
        final List<Game> liveGames = scoreBoardManager.getLiveGames();
        final Game firstLiveGame = liveGames.get(0);
        final Game secondLiveGame = liveGames.get(1);
        final Game thirdLiveGame = liveGames.get(2);

        assertAll("Assert order of live games",
                () -> assertEquals(3, liveGames.size()),
                () -> assertEquals("FCB", firstLiveGame.getHomeTeam()),
                () -> assertEquals(6, firstLiveGame.getTotalScore()),
                () -> assertEquals("Rossoneri", secondLiveGame.getHomeTeam()),
                () -> assertEquals(6, secondLiveGame.getTotalScore()),
                () -> assertEquals("ManU", thirdLiveGame.getHomeTeam()),
                () -> assertEquals(0, thirdGame.getTotalScore()));
    }
}
