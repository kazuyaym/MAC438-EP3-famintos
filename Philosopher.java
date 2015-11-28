import java.util.Random;

public class Philosopher extends Thread implements Comparable<Philosopher> {
    private double philosopherPriority;

    private Random timeThinking = new Random();

    private static int timeEating = 100;

    private Monitor monitor;

    private int id;

    private int weight;

    private static int weightSum;

    private int count;

    private int canEat;

    Philosopher(Monitor monitor, int id, int r, int weight, int totalWeight) {
        this.id = id;
        this.monitor = monitor;
        this.weight = weight;
        philosopherPriority = 1.0/weight;
        count = 0;
        // arredonda para cima
        canEat = (int) Math.ceil((double)(weight*r)/totalWeight);
        weightSum = totalWeight;
    }

    public double getPhilosopherPriority() {
        return philosopherPriority;
    }

    public int compareTo(Philosopher p) {
        Double aux = new Double(getPhilosopherPriority());
        return aux.compareTo(p.getPhilosopherPriority());
    }

    public int getID() {
        return this.id;
    }

    public int getWeight() {
        return this.weight;
    }

    public void printStartEat() {
        System.out.println("Filosofo " + id + " obteve os garfos." + "\n");
        System.out.flush();
    }

    public boolean wantsToEat() {
        Random rand = new Random();
        if (count <= canEat) {
            if (weight > 1)
                return (rand.nextFloat() < (1.0 * getWeight() / weightSum));
            else return true;
        }
        else return false;
    }

    public void run() {
        while (monitor.hasPortion()) {
            try {
                think();
                if (wantsToEat()) {
                    if (monitor.pickUpForks()) {
                        eat();
                        System.out.println("Filosofo " + id + " terminou de comer." + "\n");
                        System.out.flush();
                    }
                    monitor.putDownForks();
                }
            } catch (InterruptedException e) {
                System.out.println("Filosofo " + id + " foi interrompido.\n");
            }
        }
        monitor.leavingDinner();
        System.out.println("Filosofo " + id + " comeu " + count + " vezes.\n");
    }

    private void eat() throws InterruptedException {
        count++;
        Thread.sleep(timeEating);
    }

    private void think() throws InterruptedException {
        Thread.sleep(timeThinking.nextInt(id*200+100));
    }
}
