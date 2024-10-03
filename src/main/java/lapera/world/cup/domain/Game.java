package lapera.world.cup.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"homeScore", "awayScore", "isLive"})
public class Game {
    private String homeTeam;
    private String awayTeam;
    private Integer homeScore;
    private Integer awayScore;
    private LocalDateTime startTime;
    private boolean isLive;

    public Game(final String homeTeam, final String awayTeam, final Integer homeScore, final Integer awayScore, final LocalDateTime startTime, final boolean isLive) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.startTime = startTime;
        this.isLive = isLive;
    }
    
    public Integer getTotalScore() {
        return homeScore + awayScore;
    }
}
    
