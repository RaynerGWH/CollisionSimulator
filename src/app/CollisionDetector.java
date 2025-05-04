package app;

import shapes.Shape;
import java.util.List;

/**
 * Handles collision detection and response between shapes.
 * Uses momentum conservation for realistic physics simulation.
 */
public class CollisionDetector {
    
    /**
     * Checks and resolves collisions between all shapes.
     * @param shapes List of shapes to check for collisions
     */
    public void detectAndResolveCollisions(List<Shape> shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            for (int j = i + 1; j < shapes.size(); j++) {
                Shape shape1 = shapes.get(i);
                Shape shape2 = shapes.get(j);
                
                if (shape1.collidedWith(shape2)) {
                    shape1.handleCollision(shape2);
                }
            }
        }
    }
    
    /**
     * Checks and resolves collisions with walls.
     * @param shapes List of shapes to check for wall collisions
     * @param width Width of the container
     * @param height Height of the container
     */
    public void checkWallCollisions(List<Shape> shapes, int width, int height) {
        for (Shape shape : shapes) {
            shape.handleWallCollision(width, height);
        }
    }
}