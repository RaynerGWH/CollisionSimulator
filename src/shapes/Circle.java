package shapes;

import constants.Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Circle implements Shape {

    private double x, y; // Position
    private double vx, vy; // Velocity
    private double radius; // Radius
    private double mass; // Mass
    private Color color; // Color

    public Circle(double x, double y, double radius, double mass, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
        this.vx = 0;
        this.vy = 0;
    }

    public void move(double deltaTime) {
        x += vx * deltaTime;
        y += vy * deltaTime;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        int diameter = (int) (radius * 2);
        g.fillOval((int) (x - radius), (int) (y - radius), diameter, diameter);

        // Draw border for better visibility
        g.setColor(Color.BLACK);
        g.drawOval((int) (x - radius), (int) (y - radius), diameter, diameter);
    }

    public Rectangle getBounds() {
        int diameter = (int) (radius * 2);
        return new Rectangle((int) (x - radius), (int) (y - radius), diameter, diameter);
    }

    public boolean collidedWith(Shape other) {
        if (other instanceof Circle) {
            Circle otherCircle = (Circle) other;
            double dx = this.x - otherCircle.x;
            double dy = this.y - otherCircle.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            return distance < (this.radius + otherCircle.radius);
        } else if (other instanceof GameRectangle) {
            return other.collidedWith(this); // Let GameRectangle handle collision detection, reuses logic made in
                                             // GameRectangle class.
        }
        return false;
    }

    @Override
    public void handleCollision(Shape other) {
        if (other instanceof Circle) {
            Circle otherCircle = (Circle) other;

            // Calculate collision normal
            double dx = otherCircle.x - this.x;
            double dy = otherCircle.y - this.y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance == 0) {
                return; // Avoid division by zero
            }

            dx /= distance;
            dy /= distance;

            // Relative velocity
            double dvx = otherCircle.vx - this.vx;
            double dvy = otherCircle.vy - this.vy;

            // Relative velocity in collision normal direction
            double dvn = dvx * dx + dvy * dy;

            // Do not resolve if velocities are separating
            if (dvn > 0)
                return;

            // Collision impulse
            double impulse = (2 * dvn * Constants.RESTITUTION) / (this.mass + otherCircle.mass);

            // Update velocities
            this.vx += impulse * otherCircle.mass * dx;
            this.vy += impulse * otherCircle.mass * dy;
            otherCircle.vx -= impulse * this.mass * dx;
            otherCircle.vy -= impulse * this.mass * dy;

            // Separate overlapping circles
            double overlap = (this.radius + otherCircle.radius) - distance;
            if (overlap > 0) {
                double totalMass = this.mass + otherCircle.mass;
                double moveRatio1 = otherCircle.mass / totalMass;
                double moveRatio2 = this.mass / totalMass;

                this.x -= dx * overlap * moveRatio1;
                this.y -= dy * overlap * moveRatio1;
                otherCircle.x += dx * overlap * moveRatio2;
                otherCircle.y += dy * overlap * moveRatio2;
            }
        } else if (other instanceof GameRectangle) {
            GameRectangle rect = (GameRectangle) other;

            // Find the closest point on the rectangle to the circle's center
            double closestX = Math.max(rect.getX(), Math.min(this.x, rect.getX() + rect.getWidth()));
            double closestY = Math.max(rect.getY(), Math.min(this.y, rect.getY() + rect.getHeight()));

            // Calculate the distance from the circle's center to this closest point
            double dx = this.x - closestX;
            double dy = this.y - closestY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Check if we're actually colliding
            if (distance >= this.radius) {
                return;
            }

            // If distance is 0, the circle center is inside the rectangle
            if (distance < 0.0001) {
                // Push the circle out to the nearest edge
                double distLeft = this.x - rect.getX();
                double distRight = (rect.getX() + rect.getWidth()) - this.x;
                double distTop = this.y - rect.getY();
                double distBottom = (rect.getY() + rect.getHeight()) - this.y;

                double minDist = Math.min(Math.min(distLeft, distRight), Math.min(distTop, distBottom));

                if (minDist == distLeft) {
                    this.x = rect.getX() - this.radius;
                    this.vx = -Math.abs(this.vx);
                    dx = -1;
                    dy = 0;
                } else if (minDist == distRight) {
                    this.x = rect.getX() + rect.getWidth() + this.radius;
                    this.vx = Math.abs(this.vx);
                    dx = 1;
                    dy = 0;
                } else if (minDist == distTop) {
                    this.y = rect.getY() - this.radius;
                    this.vy = -Math.abs(this.vy);
                    dx = 0;
                    dy = -1;
                } else {
                    this.y = rect.getY() + rect.getHeight() + this.radius;
                    this.vy = Math.abs(this.vy);
                    dx = 0;
                    dy = 1;
                }
            } else {
                // Normalise the direction
                dx /= distance;
                dy /= distance;
            }

            // Relative velocity
            double rvx = this.vx - rect.getVelocityX();
            double rvy = this.vy - rect.getVelocityY();

            // Relative velocity in collision normal direction
            double speed = rvx * dx + rvy * dy;

            // Do not resolve if velocities are separating
            if (speed > 0)
                return;

            // Collision impulse
            double impulse = 2 * speed / (this.mass + rect.getMass());

            // Update velocities
            this.vx -= impulse * rect.getMass() * dx;
            this.vy -= impulse * rect.getMass() * dy;
            rect.setVelocity(
                    rect.getVelocityX() + impulse * this.mass * dx,
                    rect.getVelocityY() + impulse * this.mass * dy);

            // Separate if overlapping
            if (distance > 0.0001) {
                double overlap = this.radius - distance;
                if (overlap > 0) {
                    // Move the circle away from the rectangle
                    this.x += dx * overlap;
                    this.y += dy * overlap;
                }
            }
        }
    }

    @Override
    public void handleWallCollision(int width, int height) {
        // Left wall
        if (x - radius < 0) {
            x = radius;
            vx = Math.abs(vx);
        }
        // Right wall
        if (x + radius > width) {
            x = width - radius;
            vx = -Math.abs(vx);
        }
        // Top wall
        if (y - radius < 0) {
            y = radius;
            vy = Math.abs(vy);
        }
        // Bottom wall
        if (y + radius > height) {
            y = height - radius;
            vy = -Math.abs(vy);
        }
    }

    // Getters
    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getVelocityX() {
        return vx;
    }

    @Override
    public double getVelocityY() {
        return vy;
    }

    @Override
    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    // Setters
    @Override
    public void setVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }
}