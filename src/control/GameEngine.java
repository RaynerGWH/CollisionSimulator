package control;

import shapes.Shape;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import app.CollisionDetector;
import constants.Constants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEngine {

    private final List<Shape> shapes;
    private final CollisionDetector collisionDetector;
    private final Timer gameTimer;
    private boolean isRunning;
    private long lastUpdateTime;
    private Runnable updateCallback;
    private double speedMultiplier;

     public GameEngine() {
        this.shapes = new ArrayList<>();
        this.collisionDetector = new CollisionDetector();
        this.isRunning = false;
        this.lastUpdateTime = System.currentTimeMillis();
        
        // Initialise the game timer
        this.gameTimer = new Timer(Constants.FRAME_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    update();
                }
            }
        });
    }

    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }
    
    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
    
    public void setUpdateCallback(Runnable callback) {
        this.updateCallback = callback;
    }
    

    public void start() {
        if (!isRunning) {
            isRunning = true;
            lastUpdateTime = System.currentTimeMillis();
            gameTimer.start();
        }
    }
    
    public void stop() {
        isRunning = false;
        gameTimer.stop();
    }
    

    // Stops the program and clears all the shapes on the display.
    public void reset() {
        stop();
        shapes.clear();
        if (updateCallback != null) {
            updateCallback.run();
        }
    }
    
    // Adds a shape to simulator.
    public void addShape(Shape shape) {
        shapes.add(shape);
        if (updateCallback != null) {
            updateCallback.run();
        }
    }
    
    // Clears shapes on the display.
    public void clearShapes() {
        shapes.clear();
        if (updateCallback != null) {
            updateCallback.run();
        }
    }
    
    public List<Shape> getShapes() {
        return shapes;
    }
    
    // Checker to check if the game is still running.
    public boolean isRunning() {
        return isRunning;
    }
    
    // Updates the game on over time.
    private void update() {
        long currentTime = System.currentTimeMillis();
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // Convert to seconds
        deltaTime *= speedMultiplier;
        lastUpdateTime = currentTime;
        
        // Update physics
        for (Shape shape : shapes) {
            shape.move(deltaTime);
        }
        
        // Handle collisions
        collisionDetector.detectAndResolveCollisions(shapes);
        collisionDetector.checkWallCollisions(shapes, Constants.SIMULATION_WIDTH, 
                                            Constants.SIMULATION_HEIGHT);
        
        // Trigger UI update
        if (updateCallback != null) {
            updateCallback.run();
        }
    }

    
}
