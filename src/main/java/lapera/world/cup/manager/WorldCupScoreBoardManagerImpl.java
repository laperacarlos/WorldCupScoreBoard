package lapera.world.cup.manager;

import lapera.world.cup.board.ScoreBoard;
import lapera.world.cup.domain.Game;

import java.util.Collections;
import java.util.List;

public class WorldCupScoreBoardManagerImpl implements ScoreBoardManager {
    private final ScoreBoard scoreBoard;

    public WorldCupScoreBoardManagerImpl(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    @Override
    public void startGame(Game game) {
    }

    @Override
    public void updateScore(Game game) {
        
    }
    @Override
    public void endGame(Game game) {

    }

    @Override
    public List<Game> getLiveGames() {
        return Collections.emptyList();
    }
}
