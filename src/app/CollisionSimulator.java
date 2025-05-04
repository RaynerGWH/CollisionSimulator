package app;

import control.UserControlPanel;
import shapes.Shape;
import control.GameEngine;
import constants.Constants;
import javax.swing.*;
import java.awt.*;

/**
 * Main application class for the Collision Simulator.
 * Manages the UI and delegates game logic to the GameEngine.
 */
public class CollisionSimulator extends JFrame {
    
    private final GameEngine gameEngine;
    private SimulationPanel simulationPanel;
    private UserControlPanel controlPanel;
    
    public CollisionSimulator() {
        super("Collision Simulator");
        
        gameEngine = new GameEngine();
        initializeUI();
        
        // Set up the callback for UI updates
        gameEngine.setUpdateCallback(() -> simulationPanel.repaint());
    }
    
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setResizable(false);
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create and add simulation panel
        simulationPanel = new SimulationPanel();
        simulationPanel.setPreferredSize(new Dimension(Constants.SIMULATION_WIDTH, Constants.SIMULATION_HEIGHT));
        mainPanel.add(simulationPanel, BorderLayout.CENTER);
        
        // Create and add control panel
        controlPanel = new UserControlPanel(gameEngine);
        controlPanel.setPreferredSize(new Dimension(Constants.CONTROL_PANEL_WIDTH, Constants.SIMULATION_HEIGHT));
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        // Create and add control buttons panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }
    
    private JPanel createButtonPanel() {
        JPanel mainButtonPanel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel();
        JPanel speedPanel = new JPanel();
        
        // Control buttons
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");
        
        startButton.addActionListener(e -> gameEngine.start());
        stopButton.addActionListener(e -> gameEngine.stop());
        resetButton.addActionListener(e -> gameEngine.reset());
        
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(resetButton);
        
        // Speed control buttons
        speedPanel.add(new JLabel("Speed: "));
        ButtonGroup speedGroup = new ButtonGroup();
        
        JToggleButton speed1x = new JToggleButton("1x");
        JToggleButton speed2x = new JToggleButton("2x");
        JToggleButton speed3x = new JToggleButton("3x");
        
        speed1x.setSelected(true);  // Default speed
        
        speed1x.addActionListener(e -> gameEngine.setSpeedMultiplier(1.0));
        speed2x.addActionListener(e -> gameEngine.setSpeedMultiplier(2.0));
        speed3x.addActionListener(e -> gameEngine.setSpeedMultiplier(3.0));
        
        speedGroup.add(speed1x);
        speedGroup.add(speed2x);
        speedGroup.add(speed3x);
        
        speedPanel.add(speed1x);
        speedPanel.add(speed2x);
        speedPanel.add(speed3x);
        
        mainButtonPanel.add(controlPanel, BorderLayout.NORTH);
        mainButtonPanel.add(speedPanel, BorderLayout.SOUTH);
        
        return mainButtonPanel;
    }
    
    /**
     * Inner class for the simulation display panel.
     */
    private class SimulationPanel extends JPanel {
        
        public SimulationPanel() {
            setBackground(Color.WHITE);
            // Remove the black border from the panel itself
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Enable anti-aliasing for smoother shapes
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw the simulation boundary with a more visible style
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawRect(1, 1, Constants.SIMULATION_WIDTH - 3, Constants.SIMULATION_HEIGHT - 3);
            
            // Add corner markers for better visibility
            g2d.setColor(Color.GRAY);
            int markerSize = 10;
            // Top-left
            g2d.fillRect(0, 0, markerSize, 3);
            g2d.fillRect(0, 0, 3, markerSize);
            // Top-right
            g2d.fillRect(Constants.SIMULATION_WIDTH - markerSize, 0, markerSize, 3);
            g2d.fillRect(Constants.SIMULATION_WIDTH - 3, 0, 3, markerSize);
            // Bottom-left
            g2d.fillRect(0, Constants.SIMULATION_HEIGHT - 3, markerSize, 3);
            g2d.fillRect(0, Constants.SIMULATION_HEIGHT - markerSize, 3, markerSize);
            // Bottom-right
            g2d.fillRect(Constants.SIMULATION_WIDTH - markerSize, Constants.SIMULATION_HEIGHT - 3, markerSize, 3);
            g2d.fillRect(Constants.SIMULATION_WIDTH - 3, Constants.SIMULATION_HEIGHT - markerSize, 3, markerSize);
            
            // Draw all shapes
            for (Shape shape : gameEngine.getShapes()) {
                shape.draw(g2d);
            }
            
            // Draw info text with background for better readability
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillRect(5, 5, 150, 65);
            
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Shapes: " + gameEngine.getShapes().size(), 10, 20);
            g2d.drawString("Status: " + (gameEngine.isRunning() ? "Running" : "Stopped"), 10, 40);
            g2d.drawString("Speed: " + gameEngine.getSpeedMultiplier() + "x", 10, 60);
        }
    }
    
    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CollisionSimulator simulator = new CollisionSimulator();
            simulator.setVisible(true);
        });
    }
}