
public class GameMapLogic {
    private static final int BORDER = 10;

    private boolean[][] gameMap;

    public GameMapLogic(int fieldSize) {
        gameMap = new boolean[fieldSize + BORDER * 2][fieldSize + BORDER * 2];
    }

    public boolean invertCell(Coord coord) {
        coord.x += BORDER;
        coord.y += BORDER;

        gameMap[coord.x][coord.y] = !gameMap[coord.x][coord.y];
        return gameMap[coord.x][coord.y];
    }

    public void tick() {
        boolean[][] nextState = new boolean[gameMap.length][gameMap[0].length];

        for (int x = 0; x < gameMap.length; ++x) {
              for (int y = 0; y < gameMap.length; ++y) {
                    nextState[x][y] = applyGoLRules(x, y);
              }
        }

        gameMap = nextState;
    }

    public boolean applyGoLRules(int x, int y) {
        int aliveNeighbours = 0;

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) continue; // Skip the current cell

                int neighbourX = x + i;
                int neighbourY = y + j;

                if (neighbourX >= 0 && neighbourX < gameMap.length &&
                        neighbourY >= 0 && neighbourY < gameMap.length) {
                    aliveNeighbours += gameMap[neighbourX][neighbourY] ? 1 : 0;
                }
            }
        }

        boolean currentCellState = gameMap[x][y];

        if (currentCellState && (aliveNeighbours < 2 || aliveNeighbours > 3)) {
            return false;
        } else if (!currentCellState && aliveNeighbours == 3) {
            return true;
        } else {
            return currentCellState;
        }
    }

    public boolean getCellState(int i, int j) {
        return gameMap[i + BORDER][j + BORDER];
    }

}
