package hw4;

import static java.lang.Math.sqrt;

public class Main
{
    public static double triangleArea(int a, int b, int c) {
        if (a <= 0 || b <= 0 || c <= 0) {
            throw new IllegalArgumentException("Стороны должны быть положительными");
        }

        if (a >= b + c || b >= a + c || c >= a + b) {
            throw new IllegalArgumentException("Не выполняется неравенство треугольника или он вырожденный");
        }

        double p = (a + b + c) / 2.;

        return sqrt(p * (p - a) * (p - b) * (p - c));
    }

    public static void main( String[] args ) {
        System.out.println(triangleArea(3, 4, 5));
    }
}
