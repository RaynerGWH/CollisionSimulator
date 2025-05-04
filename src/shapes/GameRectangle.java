package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

// GameRectangle object that acts as a rectangle in the simulator. Can't use rectangle as the class name as we're using the Rectangle class from java.awt.
public class GameRectangle implements Shape {
    
    private double x, y;        // Position (top-left corner)
    private double vx, vy;      // Velocity
    private double width, height; // Dimensions
    private double mass;        // Mass
    private Color color;        // Color
    
    public GameRectangle(double x, double y, double width, double height, double mass, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.color = color;
        this.vx = 0;
        this.vy = 0;
    }
    
    @Override
    public void move(double deltaTime) {
        x += vx * deltaTime;
        y += vy * deltaTime;
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
        
        // Draw border for better visibility
        g.setColor(Color.BLACK);
        g.drawRect((int)x, (int)y, (int)width, (int)height);
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }
    
    @Override
    public boolean collidedWith(Shape other) {
        if (other instanceof GameRectangle) {
            GameRectangle otherRect = (GameRectangle) other;
            return !(this.x + this.width < otherRect.x || 
                    otherRect.x + otherRect.width < this.x || 
                    this.y + this.height < otherRect.y || 
                    otherRect.y + otherRect.height < this.y);
        } else if (other instanceof Circle) {
            Circle circle = (Circle) other;
            // Find the closest point on the rectangle to the circle
            double closestX = Math.max(this.x, Math.min(circle.getX(), this.x + this.width));
            double closestY = Math.max(this.y, Math.min(circle.getY(), this.y + this.height));
            
            // Calculate the distance from the circle's center to this closest point
            double distanceX = circle.getX() - closestX;
            double distanceY = circle.getY() - closestY;
            
            // If the distance is less than the circle's radius, there's a collision
            double distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
            return distanceSquared < (circle.getRadius() * circle.getRadius());
        }
        return false;
    }
    
    @Override
    public void handleCollision(Shape other) {
        if (other instanceof GameRectangle) {
            GameRectangle otherRect = (GameRectangle) other;
            
            // Calculate center positions
            double centerX1 = this.x + this.width / 2;
            double centerY1 = this.y + this.height / 2;
            double centerX2 = otherRect.x + otherRect.width / 2;
            double centerY2 = otherRect.y + otherRect.height / 2;
            
            // Calculate overlap on each axis
            double overlapX = (this.width + otherRect.width) / 2 - Math.abs(centerX1 - centerX2);
            double overlapY = (this.height + otherRect.height) / 2 - Math.abs(centerY1 - centerY2);
            
            // Resolve collision on the axis with minimum overlap
            if (overlapX < overlapY) {
                // Collision on X axis
                double totalMass = this.mass + otherRect.mass;
                double v1 = this.vx;
                double v2 = otherRect.vx;
                
                // Calculate new velocities using conservation of momentum
                this.vx = ((this.mass - otherRect.mass) * v1 + 2 * otherRect.mass * v2) / totalMass;
                otherRect.vx = ((otherRect.mass - this.mass) * v2 + 2 * this.mass * v1) / totalMass;
                
                // Separate rectangles
                if (centerX1 < centerX2) {
                    this.x -= overlapX * (otherRect.mass / totalMass);
                    otherRect.x += overlapX * (this.mass / totalMass);
                } else {
                    this.x += overlapX * (otherRect.mass / totalMass);
                    otherRect.x -= overlapX * (this.mass / totalMass);
                }
            } else {
                // Collision on Y axis
                double totalMass = this.mass + otherRect.mass;
                double v1 = this.vy;
                double v2 = otherRect.vy;
                
                // Calculate new velocities using conservation of momentum
                this.vy = ((this.mass - otherRect.mass) * v1 + 2 * otherRect.mass * v2) / totalMass;
                otherRect.vy = ((otherRect.mass - this.mass) * v2 + 2 * this.mass * v1) / totalMass;
                
                // Separate rectangles
                if (centerY1 < centerY2) {
                    this.y -= overlapY * (otherRect.mass / totalMass);
                    otherRect.y += overlapY * (this.mass / totalMass);
                } else {
                    this.y += overlapY * (otherRect.mass / totalMass);
                    otherRect.y -= overlapY * (this.mass / totalMass);
                }
            }
        } else if (other instanceof Circle) {
            // Handle circle-rectangle collision
            other.handleCollision(this);
        }
    }
    
    @Override
    public void handleWallCollision(int width, int height) {
        // Left wall
        if (x < 0) {
            x = 0;
            vx = Math.abs(vx);
        }
        // Right wall
        if (x + this.width > width) {
            x = width - this.width;
            vx = -Math.abs(vx);
        }
        // Top wall
        if (y < 0) {
            y = 0;
            vy = Math.abs(vy);
        }
        // Bottom wall
        if (y + this.height > height) {
            y = height - this.height;
            vy = -Math.abs(vy);
        }
    }
    
    // Getters
    @Override
    public double getX() { return x; }
    
    @Override
    public double getY() { return y; }
    
    @Override
    public double getVelocityX() { return vx; }
    
    @Override
    public double getVelocityY() { return vy; }
    
    @Override
    public double getMass() { return mass; }
    
    public double getWidth() { return width; }
    
    public double getHeight() { return height; }
    
    // Setters
    @Override
    public void setVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }
}