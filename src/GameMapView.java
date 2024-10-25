
public class GameMapView extends Window {

    GameMapLogic gameMapLogic;

    public GameMapView(int fieldSize) {
        super(fieldSize);
        gameMapLogic = new GameMapLogic(fieldSize);
    }

    @Override
    protected boolean handleCellClick(Coord pressedButton) {
        return gameMapLogic.invertCell(pressedButton);
    }

    @Override
    protected void advanceGameLogic() {
        gameMapLogic.tick();
    }

    @Override
    protected boolean isCellAlive(int x, int y) {
        return gameMapLogic.getCellState(x, y);
    }

    @Override
    protected void resetLogic() {
        gameMapLogic = new GameMapLogic(fieldSize);
    }
}
