package constants;

public class Constants {

    // Window dimensions
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 600;
    
    // Simulation area dimensions
    public static final int SIMULATION_WIDTH = 600;
    public static final int SIMULATION_HEIGHT = 400;
    
    // Control panel dimensions
    public static final int CONTROL_PANEL_WIDTH = 300;

    // Physics constants
    public static final double RESTITUTION = 1.0; // Elastic collision
    public static final int TARGET_FPS = 60;
    public static final int FRAME_DELAY = 1000 / TARGET_FPS;

    // Shape property limits
    public static final double MIN_MASS = 0.1;
    public static final double MAX_MASS = 10.0;
    public static final double MASS_STEP = 0.1;
    
    public static final double MIN_VELOCITY = -500.0;
    public static final double MAX_VELOCITY = 500.0;
    public static final double VELOCITY_STEP = 10.0;

    public static final double MIN_SIZE = 10.0;
    public static final double MAX_SIZE = 100.0;
    public static final double SIZE_STEP = 5.0;

    // Shape spawn area (with padding from edges)
    public static final int SPAWN_PADDING = 50;
    public static final int SPAWN_WIDTH = SIMULATION_WIDTH - (2 * SPAWN_PADDING);
    public static final int SPAWN_HEIGHT = SIMULATION_HEIGHT - (2 * SPAWN_PADDING);
    
    // UI constants
    public static final int UI_PADDING = 5;
    
    // Shape types. To be updated if any new shapes are updated that implement the Shapes inferace.
    public static final String[] SHAPE_TYPES = {"Circle", "Rectangle"};
}
