package lapera.world.cup.manager;

import lapera.world.cup.board.ScoreBoard;
import lapera.world.cup.board.WorldCupScoreBoard;
import lapera.world.cup.domain.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13),  true);
        
        //when
        scoreBoardManager.processGame(newGame);
        
        //then
        final List<Game> liveGames = scoreBoardManager.getLiveGames();
        final Game liveGame = liveGames.get(0);
        assertAll("Assert new Game started with initial scores",
                () -> assertEquals(1, liveGames.size()),
                () -> assertEquals(0, liveGame.getHomeScore()),
                () -> assertEquals(0, liveGame.getAwayScore()),
                () -> assertTrue(liveGame.isLive()));
    }

    @Test
    public void shouldNotStartNewGameWhenHomeTeamMissing() {
        //given
        final Game firstGame = new Game(null, "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        final Game secondGame = new Game("", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 14), true);

        //when
        //then
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(firstGame)),
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(secondGame)),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().size()));
    }

    @Test
    public void shouldNotStartNewGameWhenAwayTeamMissing() {
        //given
        final Game firstGame = new Game("Rossoneri", null, null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        final Game secondGame = new Game("Rossoneri", "", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 14), true);

        //when
        //then
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(firstGame)),
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(secondGame)),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().size()));
    }

    @Test
    public void shouldNotStartNewGameWhenGameMarkedAsFinished() {
        //given
        final Game firstGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), false);
        
        //when
        //then
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(firstGame)),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().size()));
    }


    @Test
    public void shouldUpdateScoreBoard() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        scoreBoardManager.processGame(newGame);
        final Game updatedScore = updateGame(newGame, 3, 0, true);

        //when
        scoreBoardManager.processGame(updatedScore);

        //then
        final Game updatedGame = scoreBoardManager.getLiveGames().get(0);
        assertAll("Group assertions of Game scores",
                () -> assertEquals(3, updatedGame.getHomeScore()),
                () -> assertEquals(0, updatedGame.getAwayScore()));
    }

    @Test
    public void shouldNotUpdateScoreBoardWhenHomeScoreMissing() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        scoreBoardManager.processGame(newGame);
        final Game updatedScore = updateGame(newGame, null, 2, true);
        
        //when
        //then
        assertAll("Group assertions of Game scores",
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(updatedScore)),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().get(0).getHomeScore()),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().get(0).getAwayScore()));
    }

    @Test
    public void shouldNotUpdateScoreBoardWhenAwayScoreMissing() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        scoreBoardManager.processGame(newGame);
        final Game updatedScore = updateGame(newGame, 3, null, true);
        
        //when
        //then
        assertAll("Group assertions of Game scores",
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(updatedScore)),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().get(0).getHomeScore()),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().get(0).getAwayScore()));
    }

    @Test
    public void shouldNotUpdateScoreBoardWhenHomeScoreNegative() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        scoreBoardManager.processGame(newGame);
        final Game updatedScore = updateGame(newGame, -2, 2, true);

        //when
        //then
        assertAll("Group assertions of Game scores",
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(updatedScore)),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().get(0).getHomeScore()),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().get(0).getAwayScore()));
    }

    @Test
    public void shouldNotUpdateScoreBoardWhenAwayScoreNegative() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        scoreBoardManager.processGame(newGame);
        final Game updatedScore = updateGame(newGame, 3, -5, true);

        //when
        //then
        assertAll("Group assertions of Game scores",
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(updatedScore)),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().get(0).getHomeScore()),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().get(0).getAwayScore()));
    }
    
    @Test
    public void shouldNotUpdateScoreBoardWhenGameIsNotStarted() {
        //given
        final Game updatedScore = new Game("Rossoneri", "Bianconeri", 3, 0, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);

        //when
        //then
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(updatedScore)),
                () -> assertEquals(0, scoreBoardManager.getLiveGames().size()));
    }

    @Test
    public void shouldMoveToFinishedScoreBoardWithUpdatedScoreWhenGameIsFinished() {
        //given
        final Game newGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        scoreBoardManager.processGame(newGame);
        final Game updatedScore = updateGame(newGame, 7, 0, false);
        
        //when
        scoreBoardManager.processGame(updatedScore);
        
        //then
        final List<Game> finishedGames = scoreBoardManager.getFinishedGames();
        final Game finishedGame = finishedGames.get(0);
        
        assertAll(
                () -> assertEquals(0, scoreBoardManager.getLiveGames().size()),
                () -> assertEquals(1, finishedGames.size()),
                () -> assertEquals(7, finishedGame.getHomeScore()),
                () -> assertEquals(0, finishedGame.getAwayScore()));
    }

    @Test
    public void shouldNotEndGameWhenGameIsNotStarted() {
        //given
        final Game endedGame = new Game("Rossoneri", "Bianconeri", 7, 0, LocalDateTime.of(2024, 10,13, 13, 13, 13), false);

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> scoreBoardManager.processGame(endedGame));
    }

    @Test
    public void shouldReturnSortedLiveScoreBoard() {
        //given
        final Game firstGame = new Game("Rossoneri", "Bianconeri", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 13), true);
        scoreBoardManager.processGame(firstGame);
        final Game secondGame = new Game("ManU", "ManC", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 14), true);
        scoreBoardManager.processGame(secondGame);
        final Game thirdGame = new Game("FCB", "RCM", null, null, LocalDateTime.of(2024, 10,13, 13, 13, 15), true);
        scoreBoardManager.processGame(thirdGame);
        final Game firstGameUpdated = updateGame(firstGame, 5, 1, true);
        final Game thirdGameUpdated = updateGame(thirdGame, 6, 0, true);

        //when
        scoreBoardManager.processGame(firstGameUpdated);
        scoreBoardManager.processGame(thirdGameUpdated);

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
                () -> assertEquals(0, thirdLiveGame.getTotalScore()));
    }

    private Game updateGame(final Game game, final Integer homeScore, final Integer awayScore, final boolean isLive) {
        return new Game(game.getHomeTeam(), game.getAwayTeam(), homeScore, awayScore, game.getStartTime(), isLive);
    }
}
