public class EEA {

    public static long ggT(long num1, long num2) {
        long x0 = 1l, x1 = 0l, y0 = 0l, y1 = 1l, q, r;
        long a = num1 > num2 ? num1 : num2;
        long b = a == num1 ? num2 : num1;
        long initialA = a, initialB = b;
        do  {
            q =  a / b;
            r = a % b;
            long x1Temp = x1;
            long y1Temp = y1;
            x1 = x0 - q * x1;
            y1 = y0 - q * y1;
            x0 = x1Temp;
            y0 = y1Temp;
            a = b;
            b = r;
        }
        while(r != 0l && b > 0l);
        return x0 * initialA + y0 * initialB;
    }

}
