public class Lesson2 {
    public static void main(String[] params) {
        double sum = 1000;
        double percent = 2;

        int periods = 0;
        while (periods < 6) {
//            sum = sum * (1 + percent / 100);
            sum = calculateSumWithPercent(sum, percent);
            periods = periods + 1;
            System.out.println(sum);
        }
    }

    public static double calculateSumWithPercent(double sum, double percent) {
        return sum * (1 + percent / 100);
    }
}