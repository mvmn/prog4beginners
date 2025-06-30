public class Lesson3 {
    public static void main(String[] params) {
        double sum = 1000;
        double percent = 2;

        double[] sums;
        sums = new double[7];
        sums[0] = 1000;

        int periods = 0;
        while (periods < 6) {
            sum = sum * (1 + percent / 100);
            sums[periods + 1] = sum;
            periods = periods + 1;
        }

        printArray(sums);
        for (int i = 0; i < 6; i++) {
            sums[6 - i] = sums[6 - i] - sums[5 - i];
        }
        sums[0] = 0;

//        double[] x;
//        x = null;
//        multiply(sums, 10);
//        int xlen = x.length;
        printArray(sums);
    }

    public static void printArray(double[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            System.out.print("; ");
        }
        System.out.println();
    }

    public static void multiply(double[] array, double multilier) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i] * multilier;
        }
    }
}