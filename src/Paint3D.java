import java.awt.Dimension;

public class Paint3D extends Paint{

    public Point3D references[];
    private static final int DISTANCE_X = 100;
    private static final int DISTANCE_Y = 100;

    public Paint3D(Dimension size) {
        super(size);

        Point3D reference = new Point3D(size.width / 2, size.height / 2, 0);
        this.references = new Point3D[]{reference};
    }

    public Paint3D(Dimension size, Point3D[] references) {
        super(size);
        this.references = references;
    }

    public void drawLine(Point3D p0, Point3D p1) {
        drawLine(p0, p1, 0);
    }

    public void drawLine(Point3D p0, Point3D p1, int referenceIndex) {
        int[] projectedP0 = project(p0);
        int[] projectedP1 = project(p1);

        // -100 para centrar el cubo
        drawLine(projectedP0[0] + 300, projectedP0[1] + 300, projectedP1[0] + 300, projectedP1[1] + 300);
    }

    private int[] project(Point3D point) {
        int x = point.x;
        int y = point.y;
        int z = point.z;

        // Proyección en perspectiva
        int px = (int) (x * DISTANCE_X / (z + DISTANCE_X));
        int py = (int) (y * DISTANCE_Y / (z + DISTANCE_Y));

        return new int[]{px, py};
    }
}
