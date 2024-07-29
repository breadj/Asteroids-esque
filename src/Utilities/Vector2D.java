package Utilities;

import java.util.Vector;

public class Vector2D {
    public static final Vector2D ZERO = new Vector2D(0);
    public double x, y;

    // CONSTRUCTORS

    public Vector2D() {
        x = y = 0;
    }

    public Vector2D(double z) {
        x = y = z;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vector2D v) {
            return x == v.x && y == v.y;
        } else
            return false;
    }

    @Override
    public String toString() {
        return "(" + x + ',' + y + ')';
    }

    public double mag() {
        return Math.hypot(x, y);
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public double angleTo(Vector2D other) {
        double angle = this.angle() - other.angle();
        if (angle < -Math.PI) return angle + 2 * Math.PI;
        if (angle > Math.PI) return angle - 2 * Math.PI;
        return angle;
    }

    public void add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void addScaled(Vector2D v, double fac) {
        this.x += fac * v.x;
        this.y += fac * v.y;
    }

    public void subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public void mult(double fac) {
        this.x *= fac;
        this.y *= fac;
    }

    public void rotate(double angle) {
        double newX = this.x * Math.cos(angle) - this.y * Math.sin(angle);
        this.y = this.x * Math.sin(angle) + this.y * Math.cos(angle);
        this.x = newX;
    }

    public double dot(Vector2D v) {
        return this.x * v.x + this.y * v.y;
    }

    public double dist(Vector2D v) {
        return Math.hypot(this.x - v.x, this.y - v.y);
    }

    public void normalise() {
        if (mag() != 0) {
            double m = mag();
            this.x /= m;
            this.y /= m;
        }
    }

    public void wrap(double w, double h) {
        if (x > w) x -= w;
        else if (x < 0) x+= w;
        if (y > h) y -= h;
        else if (y < 0) y += h;
    }


    // STATIC METHODS

    public static Vector2D polar(double angle, double mag) {
        return new Vector2D(mag * Math.cos(angle), mag * Math.sin(angle));
    }

    public static double mag(Vector2D v) {
        return Math.hypot(v.x, v.y);
    }

    public static double angle(Vector2D v) {
        return Math.atan2(v.y, v.x);
    }

    public static double angleTo(Vector2D a, Vector2D b) {
        double angle = a.angle() - b.angle();
        if (angle < -Math.PI) return angle + 2 * Math.PI;
        if (angle > Math.PI) return angle - 2 * Math.PI;
        return angle;
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D add(Vector2D v, double x, double y) {
        return new Vector2D(v.x + x, v.y + y);
    }

    public static Vector2D addScaled(Vector2D a, Vector2D b, double scale) {
        return new Vector2D(a.x + (b.x * scale), a.y + (b.y * scale));
    }

    public static Vector2D subtract(Vector2D a, Vector2D b) {
        return new Vector2D(a.x - b.x, a.y - b.y);
    }

    public static Vector2D subtract(Vector2D v, double x, double y) {
        return new Vector2D(v.x - x, v.y - y);
    }

    public static Vector2D mult(Vector2D v, double scalar) {
        return new Vector2D(v.x * scalar, v.y * scalar);
    }

    public static Vector2D rotate(Vector2D v, double angle) {
        double newX = v.x * Math.cos(angle) - v.y * Math.sin(angle);
        double newY = v.x * Math.sin(angle) + v.y * Math.cos(angle);
        return new Vector2D(newX, newY);
    }

    public static double dot(Vector2D a, Vector2D b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double dist(Vector2D a, Vector2D b) {
        return Math.hypot(a.x - b.x, a.y - b.y);
    }

    public static Vector2D normalise(Vector2D v) {
        double mag = v.mag();
        if (mag != 0)
            return new Vector2D(v.x / mag, v.y / mag);
        return new Vector2D();

    }

    public static Vector2D wrap(Vector2D v, double w, double h) {
        double newX = v.x;
        double newY = v.y;
        if (newX > w) newX -= w;
        else if (newX < 0) newX += w;
        if (newY > h) newY -= h;
        else if (newY < 0) newY += h;
        return new Vector2D(newX, newY);
    }
}
