package shapes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public interface Shape {
    
    void move(double time);

    void draw(Graphics2D g);

    boolean collidedWith(Shape otherShape);

    void handleCollision(Shape otherShape);

    void handleWallCollision(int width, int height);

    // Gets the bounding rectangle for any shape for collision detection. Works for any shape because any shape can fit within a rectangular box.
    public abstract Rectangle getBounds();

    // Getters for physics properties
    double getX();

    double getY();

    double getVelocityX();

    double getVelocityY();

    double getMass();
    
    // Setters for physics properties
    void setVelocity(double vx, double vy);
}
