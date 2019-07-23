/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BaseballElimination {

    private final Map<String, Integer> teams = new HashMap<>();
    private final Map<Integer, String> teamsById = new HashMap<>();
    private final Map<String, TeamEliminationStats> teamEliminationStats = new HashMap<>();

    private final int[] wins, loss, remaining;
    private final int[][] games;

    public BaseballElimination(String fileName) {
        In input = new In(fileName);
        int teamsCount = input.readInt();
        wins = new int[teamsCount];
        loss = new int[teamsCount];
        remaining = new int[teamsCount];
        games = new int[teamsCount][teamsCount];

        for (int i = 0; i < teamsCount; i++) {
            String teamName = input.readString();
            teams.put(teamName, i);
            teamsById.put(i, teamName);
            wins[i] = input.readInt();
            loss[i] = input.readInt();
            remaining[i] = input.readInt();
            games[i] = new int[teamsCount];
            for (int j = 0; j < teamsCount; j++) {
                games[i][j] = input.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return teams.size();
    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    public int wins(String teamName) {
        validateTeamName(teamName);
        return wins[teams.get(teamName)];
    }

    public int losses(String teamName) {
        validateTeamName(teamName);
        return loss[teams.get(teamName)];
    }

    public int remaining(String teamName) {
        validateTeamName(teamName);
        return remaining[teams.get(teamName)];
    }

    public int against(String t1, String t2) {
        validateTeamName(t1);
        validateTeamName(t2);
        return games[teams.get(t1)][teams.get(t2)];
    }

    public boolean isEliminated(String teamName) {
        validateTeamName(teamName);
        return teamEliminationStats
                .computeIfAbsent(teamName, t -> isTeamEliminated(t)).isEliminated;
    }

    public Iterable<String> certificateOfElimination(String teamName) {
        validateTeamName(teamName);
        return teamEliminationStats.computeIfAbsent(teamName, t -> isTeamEliminated(t)).certificateOfElimination;
    }

    private TeamEliminationStats isTeamEliminated(String teamName) {
        int maxTeamWins = remaining(teamName) + wins(teamName);
        for (String otherTeam : teams()) {
            if (!otherTeam.equals(teamName) && wins(otherTeam) > maxTeamWins) {
                return TeamEliminationStats.eliminationStats(Collections.singletonList(otherTeam));
            }
        }
        return isNonTriviallyEliminated(teamName);
    }

    // interesting way to count 2 dimensional arrays
    private int gamesBetweenTeamsCount() {
        int gamesBetweenTeamsCount = 0;
        for (int i = 0; i < numberOfTeams() - 1; i++) {
            for (int j = i + 1; j < numberOfTeams() - 1; j++) {
                gamesBetweenTeamsCount++;
            }
        }
        return gamesBetweenTeamsCount;
    }

    private TeamEliminationStats isNonTriviallyEliminated(String teamName) {
        validateTeamName(teamName);

        int teamId = teams.get(teamName);
        int checkedTeamMaxWins = wins[teamId] + remaining[teamId];
        int gamesBetweenTeamsCount = gamesBetweenTeamsCount();
        int vertexCount = 2 + numberOfTeams() - 1 + gamesBetweenTeamsCount;
        int sVertex = 0, tVertex = vertexCount - 1;
        int totalLeftGames = 0;
        FlowNetwork fn = new FlowNetwork(vertexCount);
        int gameVertexId = 1;
        Map<Integer, Integer> virtualTeamIndex = createVirtualTeamIndex(teamId, 1 + gamesBetweenTeamsCount);
        for (int i = 0; i < numberOfTeams(); i++) {
            for (int j = i + 1; j < numberOfTeams(); j++) {
                if (i == teamId || j == teamId) {
                    continue;
                }
                fn.addEdge(new FlowEdge(sVertex, gameVertexId, games[i][j]));
                totalLeftGames += games[i][j];
                fn.addEdge(new FlowEdge(gameVertexId, virtualTeamIndex.get(i), Double.MAX_VALUE));
                fn.addEdge(new FlowEdge(gameVertexId, virtualTeamIndex.get(j), Double.MAX_VALUE));
                gameVertexId++;
            }
        }

        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teamId) {
                continue;
            }
            // ???
            fn.addEdge(new FlowEdge(virtualTeamIndex.get(i), tVertex,
                                    checkedTeamMaxWins - wins[i] > 0 ?
                                    checkedTeamMaxWins - wins[i] : 0));
        }


        // ???
        FordFulkerson ff = new FordFulkerson(fn, sVertex, tVertex);
        if (totalLeftGames > ff.value()) {
            List<String> certificateOfElimination = new LinkedList<>();
            for (int i = 0; i < numberOfTeams(); i++) {
                if (i == teamId) {
                    continue;
                }
                if (ff.inCut(virtualTeamIndex.get(i))) {
                    certificateOfElimination.add(teamsById.get(i));
                }
            }
            return TeamEliminationStats.eliminationStats(certificateOfElimination);
        }
        return TeamEliminationStats.notEliminatedStats();
    }

    // what's the function of offset ???
    private Map<Integer, Integer> createVirtualTeamIndex(int teamId, int offset) {
        Map<Integer, Integer> virtualIndex = new HashMap<>();
        int vIndex = 0;
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teamId) {
                continue;
            }
            virtualIndex.put(i, vIndex + offset);
            vIndex++;
        }
        return virtualIndex;
    }

    private void validateTeamName(String teamName) {
        if (!teams.containsKey(teamName)) {
            throw new IllegalArgumentException();
        }
    }


    private static class TeamEliminationStats {
        public final boolean isEliminated;
        public final List<String> certificateOfElimination;

        TeamEliminationStats(boolean isEliminated, List<String> certificateOfElimination) {
            this.isEliminated = isEliminated;
            this.certificateOfElimination = certificateOfElimination;
        }

        public static TeamEliminationStats notEliminatedStats() {
            return new TeamEliminationStats(false, null);
        }

        public static TeamEliminationStats eliminationStats(List<String> certificateOfElimination) {
            return new TeamEliminationStats(true, certificateOfElimination);
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
