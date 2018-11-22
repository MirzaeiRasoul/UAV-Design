package Classes;

public final class Utils {

    private Utils() {
        throw new AssertionError();
    }

//    public static double calculateDistance(Circle source, Circle target) {
//        double distanceX = source.getCenterX() - target.getCenterX();
//        double distanceY = source.getCenterY() - target.getCenterY();
//        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
//        return distance;
//    }

    public static double calculateDistance(double sourceX, double sourceY, double targetX, double targetY) {
        double distanceX = targetX - sourceX;
        double distanceY = targetY - sourceY;
        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        return distance;
    }

}