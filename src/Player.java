import java.awt.event.KeyEvent;

public class Player {

    public static int position_x = 25, position_y = 25;
    public static Point3D point = new Point3D(position_x, 200, position_y);
    public static int angle = 240;

    public static double[][] arrow = {
            {4, 0}, // Punta
            {4, 4}, // Centro
            {0, 6}, // Cola izquierda
            {8, 6}  // Cola derecha
    };

    public static double[][] getDinamicArrow;

    public static double[][] dinamicArrow(){
        double radians = Math.toRadians(angle);

        // Calcular el centro original de la flecha
        double[] originalCenter = arrow[1];

        // Crear la matriz de transformación
        double[][] transformMatrix = {
                {Math.cos(radians), -Math.sin(radians)},
                {Math.sin(radians), Math.cos(radians)}
        };

        double[][] newArrow = new double[arrow.length][2];
        for (int i = 0; i < arrow.length; i++) {
            double[] point = arrow[i];
            double[] rotatedPoint = new double[2];

            // Trasladar el punto al origen
            double translatedX = point[0] - originalCenter[0];
            double translatedY = point[1] - originalCenter[1];

            // Rotar el punto
            rotatedPoint[0] = translatedX * transformMatrix[0][0] + translatedY * transformMatrix[0][1];
            rotatedPoint[1] = translatedX * transformMatrix[1][0] + translatedY * transformMatrix[1][1];

            // Trasladar el punto rotado al nuevo centro
            rotatedPoint[0] += position_x;
            rotatedPoint[1] += position_y;

            newArrow[i] = rotatedPoint;
        }

        getDinamicArrow = newArrow;
        return newArrow;
    }

    public static void movePlayer(int keyCode){

        int fixAngle = angle + 30;

        if (keyCode == KeyEvent.VK_W) { // Mover hacia adelante
            position_x += 5 * Math.cos(Math.toRadians(fixAngle));
            position_y += 5 * Math.sin(Math.toRadians(fixAngle));
        } else if (keyCode == KeyEvent.VK_S) { // Mover hacia atrás
            position_x -= 5 * Math.cos(Math.toRadians(fixAngle));
            position_y -= 5 * Math.sin(Math.toRadians(fixAngle));
        } else if (keyCode == KeyEvent.VK_D) { // Mover a la derecha
            position_x += 5 * Math.cos(Math.toRadians(fixAngle + 90));
            position_y += 5 * Math.sin(Math.toRadians(fixAngle + 90));
        } else if (keyCode == KeyEvent.VK_A) { // Mover a la izquierda
            position_x += 5 * Math.cos(Math.toRadians(fixAngle - 90));
            position_y += 5 * Math.sin(Math.toRadians(fixAngle - 90));
        } else if (keyCode == KeyEvent.VK_E) { // Rotar a la derecha
            angle += 20;
            angle = angle % 360;
        } else if (keyCode == KeyEvent.VK_Q) { // Rotar a la izquierda
            angle -= 20;
            angle = angle % 360;
        }else{
            return;
        }

        if(position_x < 20){
            position_x = 20;
        }else if (position_x > 220){
            position_x = 220;
        }

        if(position_y < 20){
            position_y = 20;
        }
        else if (position_y > 220){
            position_y = 220;
        }
    }
}