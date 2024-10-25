import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Window {
    private static final int DIMENSION_SIZE = 28;
    private static final int GRID_GAP = 2;

    private final JFrame frame;
    private final JPanel gameGrid;
    protected final JButton[][] buttonsGrid;
    protected final int fieldSize;

    private final Timer gameTimer;
    private boolean isRunning = false;
    private JButton startStopButton;
    private JSlider speedSlider;

    public Window(int fieldSize) {
        this.fieldSize = fieldSize;
        this.buttonsGrid = new JButton[fieldSize][fieldSize];
        this.frame = new JFrame("Game of Life");
        this.gameGrid = new JPanel(new GridLayout(fieldSize, fieldSize, GRID_GAP, GRID_GAP));

        gameTimer = new Timer(100, e -> advanceGame());
        setupUI();
    }

    private void setupUI() {
        frame.setLayout(new BorderLayout());
        setupGrid();
        addControlButtons();
        frame.pack();
        frame.setVisible(true);
    }

    private void setupGrid() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                JButton btn = createGridButton();
                buttonsGrid[i][j] = btn;
                gameGrid.add(btn);
            }
        }
        frame.add(gameGrid, BorderLayout.CENTER);
    }

    private JButton createGridButton() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(DIMENSION_SIZE, DIMENSION_SIZE));
        btn.setBackground(Color.lightGray);
        btn.addActionListener(this::handleCellClick);
        return btn;
    }

    private void addControlButtons() {
        JPanel controls = new JPanel();

        controls.add(createButton("Next Generation", e -> advanceGame()));
        startStopButton = createButton("Start", e -> toggleGameRunning());
        controls.add(startStopButton);
        controls.add(createButton("Reset", e -> resetGame()));

        controls.add(new JLabel("Delay:"));
        controls.add(setupSpeedSlider());

        frame.add(controls, BorderLayout.SOUTH);
    }

    private JButton createButton(String label, ActionListener action) {
        JButton button = new JButton(label);
        button.addActionListener(action);
        return button;
    }

    private JSlider setupSpeedSlider() {
        speedSlider = new JSlider(JSlider.HORIZONTAL, 25, 1000, 100);
        speedSlider.setMajorTickSpacing(150);
        speedSlider.setMinorTickSpacing(30);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(e -> setTimerSpeed());

        return speedSlider;
    }

    protected abstract boolean handleCellClick(Coord pressedButton);

    private void handleCellClick(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        Coord pressedButton = getButtonPosition(button);

        if (pressedButton != null) {
            boolean isAlive = handleCellClick(pressedButton);
            updateButtonColor(button, isAlive);
        }
    }

    private void advanceGame() {
        advanceGameLogic();
        updateGridView();
    }

    protected abstract void advanceGameLogic();

    private void updateGridView() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                updateButtonColor(buttonsGrid[i][j], isCellAlive(i, j));
            }
        }
    }

    protected abstract boolean isCellAlive(int x, int y);

    protected void updateButtonColor(JButton button, boolean isAlive) {
        button.setBackground(isAlive ? Color.ORANGE : Color.lightGray);
    }

    private void toggleGameRunning() {
        if (isRunning) {
            gameTimer.stop();
            startStopButton.setText("Start");
        } else {
            gameTimer.start();
            startStopButton.setText("Stop");
        }
        isRunning = !isRunning;
    }

    private void resetGame() {
        gameTimer.stop();
        startStopButton.setText("Start");
        isRunning = false;
        resetLogic();
        updateGridView();
    }

    protected abstract void resetLogic();

    private void setTimerSpeed() {
        int delay = speedSlider.getValue();
        gameTimer.setDelay(delay);
    }

    protected Coord getButtonPosition(JButton button) {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (buttonsGrid[i][j] == button) return new Coord(i, j);
            }
        }
        return null;
    }
}
