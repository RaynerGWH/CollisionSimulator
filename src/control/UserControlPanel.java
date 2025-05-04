package control;

import constants.Constants;
import shapes.Shape;
import shapes.Circle;
import shapes.GameRectangle;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

// This class focuses on the UI (JPanel) 
public class UserControlPanel extends JPanel {
    
    private final GameEngine gameEngine;
    private final Random random = new Random();
    
    private JComboBox<String> shapeTypeCombo;
    private JSpinner massSpinner;
    private JSpinner velocityXSpinner;
    private JSpinner velocityYSpinner;
    private JSpinner sizeSpinner;
    
    public UserControlPanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        // Shape type selector
        shapeTypeCombo = new JComboBox<>(Constants.SHAPE_TYPES);
        
        // Spinners for shape properties with tooltips
        massSpinner = createSpinner(1.0, Constants.MIN_MASS, Constants.MAX_MASS, Constants.MASS_STEP);
        velocityXSpinner = createSpinner(0.0, Constants.MIN_VELOCITY, Constants.MAX_VELOCITY, Constants.VELOCITY_STEP);
        velocityYSpinner = createSpinner(0.0, Constants.MIN_VELOCITY, Constants.MAX_VELOCITY, Constants.VELOCITY_STEP);
        sizeSpinner = createSpinner(30.0, Constants.MIN_SIZE, Constants.MAX_SIZE, Constants.SIZE_STEP);
        
        // Add tooltips to spinners
        massSpinner.setToolTipText(String.format("Range: %.1f to %.1f", Constants.MIN_MASS, Constants.MAX_MASS));
        velocityXSpinner.setToolTipText(String.format("Range: %.0f to %.0f", Constants.MIN_VELOCITY, Constants.MAX_VELOCITY));
        velocityYSpinner.setToolTipText(String.format("Range: %.0f to %.0f", Constants.MIN_VELOCITY, Constants.MAX_VELOCITY));
        sizeSpinner.setToolTipText(String.format("Range: %.0f to %.0f", Constants.MIN_SIZE, Constants.MAX_SIZE));
    }
    
    private JSpinner createSpinner(double value, double min, double max, double step) {
        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, step);
        JSpinner spinner = new JSpinner(model);
        
        // Format the spinner to show appropriate decimal places
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner);
        if (step < 1) {
            editor.getFormat().setMinimumFractionDigits(1);
            editor.getFormat().setMaximumFractionDigits(1);
        } else {
            editor.getFormat().setMinimumFractionDigits(0);
            editor.getFormat().setMaximumFractionDigits(0);
        }
        spinner.setEditor(editor);
        
        return spinner;
    }
    
    private void layoutComponents() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Title
        JLabel titleLabel = new JLabel("Shape Properties");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        add(titleLabel, gbc);
        
        // Reset insets and gridwidth
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 1;
        
        // Shape Type
        addLabelComponentAndRange("Shape Type:", shapeTypeCombo, "", gbc, row++);
        
        // Mass
        addLabelComponentAndRange("Mass:", massSpinner, 
            String.format("(%.1f - %.1f)", Constants.MIN_MASS, Constants.MAX_MASS), gbc, row++);
        
        // Velocity X
        addLabelComponentAndRange("Velocity X:", velocityXSpinner, 
            String.format("(%.0f - %.0f)", Constants.MIN_VELOCITY, Constants.MAX_VELOCITY), gbc, row++);
        
        // Velocity Y
        addLabelComponentAndRange("Velocity Y:", velocityYSpinner, 
            String.format("(%.0f - %.0f)", Constants.MIN_VELOCITY, Constants.MAX_VELOCITY), gbc, row++);
        
        // Size
        addLabelComponentAndRange("Size:", sizeSpinner, 
            String.format("(%.0f - %.0f)", Constants.MIN_SIZE, Constants.MAX_SIZE), gbc, row++);
        
        // Add separator
        JSeparator separator = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(15, 0, 15, 0);
        add(separator, gbc);
        
        // Reset insets
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Buttons
        JButton addShapeButton = new JButton("Add Shape");
        addShapeButton.setBackground(new Color(70, 130, 180));
        addShapeButton.setForeground(Color.WHITE);
        addShapeButton.setFocusPainted(false);
        addShapeButton.addActionListener(e -> addShape());
        
        JButton randomButton = new JButton("Add Random Shape");
        randomButton.setBackground(new Color(60, 179, 113));
        randomButton.setForeground(Color.WHITE);
        randomButton.setFocusPainted(false);
        randomButton.addActionListener(e -> addRandomShape());
        
        JButton clearButton = new JButton("Clear All");
        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> gameEngine.clearShapes());
        
        // Add buttons with full width
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(5, 0, 5, 0);
        add(addShapeButton, gbc);
        
        gbc.gridy = row++;
        add(randomButton, gbc);
        
        gbc.gridy = row++;
        add(clearButton, gbc);
    }
    
    private void addLabelComponentAndRange(String labelText, JComponent component, String rangeText,
                                          GridBagConstraints gbc, int row) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        JLabel label = new JLabel(labelText);
        add(label, gbc);
        
        // Component
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(component, gbc);
        
        // Range label
        if (!rangeText.isEmpty()) {
            gbc.gridx = 2;
            gbc.weightx = 0.0;
            JLabel rangeLabel = new JLabel(rangeText);
            rangeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            rangeLabel.setForeground(Color.GRAY);
            add(rangeLabel, gbc);
        }
    }
    
    private void addShape() {
        try {
            String shapeType = (String) shapeTypeCombo.getSelectedItem();
            double mass = (Double) massSpinner.getValue();
            double velocityX = (Double) velocityXSpinner.getValue();
            double velocityY = (Double) velocityYSpinner.getValue();
            double size = (Double) sizeSpinner.getValue();
            
            // Random position within the spawn area
            double x = random.nextDouble() * Constants.SPAWN_WIDTH + Constants.SPAWN_PADDING;
            double y = random.nextDouble() * Constants.SPAWN_HEIGHT + Constants.SPAWN_PADDING;
            
            // Random colour
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            
            Shape shape;
            if ("Circle".equals(shapeType)) {
                shape = new Circle(x, y, size / 2, mass, color);
            } else {
                shape = new GameRectangle(x, y, size, size, mass, color);
            }
            
            shape.setVelocity(velocityX, velocityY);
            gameEngine.addShape(shape);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error adding shape: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // This method helps to add a random shape into the simulator.
    private void addRandomShape() {
        // Randomise all values
        shapeTypeCombo.setSelectedIndex(random.nextInt(Constants.SHAPE_TYPES.length));
        
        // Generate random values within the allowed ranges.
        massSpinner.setValue(Constants.MIN_MASS + random.nextDouble() * (Constants.MAX_MASS - Constants.MIN_MASS));
        velocityXSpinner.setValue(Constants.MIN_VELOCITY + random.nextDouble() * (Constants.MAX_VELOCITY - Constants.MIN_VELOCITY));
        velocityYSpinner.setValue(Constants.MIN_VELOCITY + random.nextDouble() * (Constants.MAX_VELOCITY - Constants.MIN_VELOCITY));
        sizeSpinner.setValue(Constants.MIN_SIZE + random.nextDouble() * (Constants.MAX_SIZE - Constants.MIN_SIZE));
        
        addShape();
    }
}