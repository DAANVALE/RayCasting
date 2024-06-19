import java.awt.Dimension;

public class Paint3D extends Paint{

    public Point3D references[];
    private static final int DISTANCE = 150;

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
        drawLine(projectedP0[0], projectedP0[1], projectedP1[0], projectedP1[1]);
    }

    private int[] project(Point3D point) {
        int x = point.x;
        int y = point.y;
        int z = point.z;

        // Proyección en perspectiva
        int px = (int) (x * DISTANCE / (z + DISTANCE));
        int py = (int) (y * DISTANCE / (z + DISTANCE));

        return new int[]{px, py};
    }

    private int[] proyectParalelism(Point3D point){

        if(references.length == 0){
            System.out.println("No references found");
            return project(point);
        }

        int x = point.x;
        int y = point.y;
        int z = point.z;

        // Proyección en perspectiva
        int px = (int) (x * references[0].x / (z + references[0].x));
        int py = (int) (y * references[0].y / (z + references[0].y));

        return new int[]{px, py};
    }
}
