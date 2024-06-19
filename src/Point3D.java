class Point3D {
    int x, y, z;

    Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point3D calculateCenter(Point3D[] points) {
        double sumX = 0, sumY = 0, sumZ = 0;
        for (Point3D point : points) {
            sumX += point.x;
            sumY += point.y;
            sumZ += point.z;
        }

        int count = points.length;
        return new Point3D(
                (int)(sumX / count),
                (int)(sumY / count),
                (int)(sumZ / count));
    }

    Point3D rotateAroundCenter(Point3D center, double thetaX, double thetaY) {
        // Translate point to origin
        double translatedX = this.x - center.x;
        double translatedY = this.y - center.y;
        double translatedZ = this.z - center.z;

        // Rotation around Y-axis (horizontal rotation)
        double cosThetaY = Math.cos(thetaY);
        double sinThetaY = Math.sin(thetaY);
        double xRotY = translatedX * cosThetaY - translatedZ * sinThetaY;
        double zRotY = translatedX * sinThetaY + translatedZ * cosThetaY;

        // Rotation around X-axis (vertical rotation)
        double cosThetaX = Math.cos(thetaX);
        double sinThetaX = Math.sin(thetaX);
        double yRotX = translatedY * cosThetaX - zRotY * sinThetaX;
        double zRotX = translatedY * sinThetaX + zRotY * cosThetaX;

        // Translate point back
        double finalX = xRotY + center.x;
        double finalY = yRotX + center.y;
        double finalZ = zRotX + center.z;

        return new Point3D((int)finalX, (int)finalY, (int)finalZ);
    }
}
