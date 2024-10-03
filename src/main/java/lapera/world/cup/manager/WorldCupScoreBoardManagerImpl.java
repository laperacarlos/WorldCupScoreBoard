package lapera.world.cup.manager;

import lapera.world.cup.board.ScoreBoard;
import lapera.world.cup.domain.Game;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class WorldCupScoreBoardManagerImpl implements ScoreBoardManager {
    private final ScoreBoard scoreBoard;

    public WorldCupScoreBoardManagerImpl(final ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    @Override
    public void processGame(final Game game) {
        initialValidation(game);

        if (game.isLive()) {
            if (scoreBoard.getLiveGames().contains(game)) {
                updateScore(game);
            } else {
                startGame(game);
            }
        } else {
            endGame(game);
        }
    }

    @Override
    public List<Game> getLiveGames() {
        return sortGames(scoreBoard.getLiveGames());
    }
    
    @Override
    public List<Game> getFinishedGames() {
        return sortGames(scoreBoard.getFinishedGames());
    }

    private void startGame(final Game game) {
        validateStartGame(game);
        setInitialScore(game);
        
        log.info("Starting game {} : {}", game.getHomeTeam(), game.getAwayTeam());
        scoreBoard.addGame(game);
    }

    private void updateScore(final Game game) {
        validateUpdateGame(game);
        
        log.info("Score update for game {} {} : {} {}", game.getHomeTeam(), game.getHomeScore(), game.getAwayTeam(), game.getAwayScore());
        scoreBoard.updateGame(game);
    }

    private void endGame(final Game game) {
        validateEndGame(game);

        log.info("Game ended with score {} {} : {} {}", game.getHomeTeam(), game.getHomeScore(), game.getAwayTeam(), game.getAwayScore());
        scoreBoard.removeGame(game);
    }
    
    private List<Game> sortGames(final List<Game> games) {
        games.sort(
                Comparator.comparing(Game::getTotalScore)
                        .thenComparing(Game::getStartTime)
                        .reversed());
        return games;
    }

    private void initialValidation(final Game game) {
        validateTeamName(game.getHomeTeam());
        validateTeamName(game.getAwayTeam());
    }

    private void validateTeamName(final String teamName) {
        Optional.ofNullable(teamName).ifPresentOrElse(this::validateIfTeamNameNotEmpty, () -> {
            throw new IllegalArgumentException("Invalid game. Home or Away team name is null!");
        });
    }
    
    private void validateIfTeamNameNotEmpty(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Invalid game. Home or Away team name is empty!");
        }
    }

    private void validateStartGame(Game game) {
        int homeScore = Optional.ofNullable(game.getHomeScore()).orElse(0);
        int awayScore = Optional.ofNullable(game.getAwayScore()).orElse(0);
        
        if (homeScore + awayScore != 0) {
            throw new IllegalArgumentException("Invalid game. Game can't start with score other than 0:0");
        }
    }
    
    private void validateUpdateGame(final Game game) {
        validateScore(game.getHomeScore());
        validateScore(game.getAwayScore());
    }
    
    private void validateScore(final Integer score) {
        Optional.ofNullable(score).ifPresentOrElse(this::validateIfScoreNotNegative, () -> {
            throw new IllegalArgumentException("Invalid score. Home or Away score is null!");
        });
    }
    
    private void validateIfScoreNotNegative(final int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Invalid game. Home or Away score is negative!");
        }
    }

    private void validateEndGame(final Game game) {
        validateIfGameStarted(game);
        validateScore(game.getHomeScore());
        validateScore(game.getAwayScore());
    }

    private void validateIfGameStarted(final Game game) {
        if (!scoreBoard.getLiveGames().contains(game)) {
            throw new IllegalArgumentException("Invalid game. Game not started can't be ended!");
        }
    }

    private void setInitialScore(final Game game) {
        game.setHomeScore(0);
        game.setAwayScore(0);
    }
}
