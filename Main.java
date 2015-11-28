import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {

    private static int R;
    private static int N;
    private static char runMode;
    private static int[] philosopherWeight;

    private static void readFile(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            N = Integer.parseInt(bufferedReader.readLine());
            String line = bufferedReader.readLine();
            String[] tokens = line.split(" ");

            philosopherWeight = new int[N];
            for (int i = 0; i < tokens.length; i++)
                philosopherWeight[i] = Integer.parseInt(tokens[i]);

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runMode = args[2].charAt(0);
        readFile(args[0]);
        R = Integer.parseInt(args[1]);
        start();
    }

    private static int totalWeight() {
        int sum = 0;
        for (int i = 0; i < N; i++)
            sum += philosopherWeight[i];
        return sum;
    }

    public static void start() {
        Philosopher[] philosophers;

        Monitor monitor = new Monitor(N, R);

        philosophers = new Philosopher[N];

        int sum = totalWeight();

        for (int i = 0; i < N; i++) {
            if (runMode == 'U')
                philosophers[i] = new Philosopher(monitor, i, R, 1, N);
            else
                philosophers[i] =
                        new Philosopher(monitor, i, R, philosopherWeight[i], sum);
        }
        for (int i = 0; i < N; i++) {
            philosophers[i].start();
        }
        try {
            for (int i = 0; i < N; i++)
                philosophers[i].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
