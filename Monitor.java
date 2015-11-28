import java.util.concurrent.Semaphore;
import java.util.PriorityQueue;
import java.util.Iterator;

class Monitor {
    Semaphore monitor_mutex = new Semaphore(1, true);

    private enum State {THINKING, HUNGRY, EATING};
    private State[] philosopherState;
    private static int nPortions;
    private PriorityQueue cv;

    // signal(cv)
    protected void signal(PriorityQueue cv) {
        if (!cv.isEmpty()) {
            Philosopher current = (Philosopher) cv.remove();
        }
    }

    // wait(cv)
    protected void wait(PriorityQueue cv) {
        Philosopher current = (Philosopher) Thread.currentThread();
        cv.add(current);
        monitor_mutex.release();
        try {
            monitor_mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // signal_all(cv)
    protected void signal_all(PriorityQueue cv) {
        Iterator queueIterator = cv.iterator();
        while (queueIterator.hasNext()) {
            signal(cv);
        }
    }

    // empty(cv)
    protected boolean empty(PriorityQueue cv) {
        return cv.isEmpty();
    }

    // P_monitor() - usado para travar o monitor
    protected void P_monitor() {
        try {
            monitor_mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // V_monitor() - libera o monitor
    protected void V_monitor() {
        monitor_mutex.release();
    }

    public Monitor(int N, int R) {
        cv = new PriorityQueue();
        nPortions = R;
        philosopherState = new State[N];
        for (int i = 0; i < N; i++)
            philosopherState[i] = State.THINKING;
    }

    public boolean pickUpForks() {
        int id;
        id = ((Philosopher) Thread.currentThread()).getID();
        P_monitor();
        if(nPortions > 0) {
            philosopherState[id] = State.HUNGRY;
            while (adjacentPhilosophersIsEating()) {
                wait(cv);
                if(nPortions <= 0) return false;
            }
            philosopherState[id] = State.EATING;
            nPortions--;
            ((Philosopher) Thread.currentThread()).printStartEat();
            return true;
        }
        else return false;
    }

    public void putDownForks() {
        int id;
        id = ((Philosopher) Thread.currentThread()).getID();
        philosopherState[id] = State.THINKING;
        signal(cv);
        V_monitor();
    }

    private boolean adjacentPhilosophersIsEating() {
        int id;
        id = ((Philosopher) Thread.currentThread()).getID();
        if (philosopherState[(id+1)%philosopherState.length] == State.EATING)
            return true;
        if (philosopherState[(id+philosopherState.length-1)%philosopherState.length] == State.EATING)
            return true;
        return false;
    }

    public boolean hasPortion() {
        return (nPortions > 0);
    }

    public void leavingDinner() {
        P_monitor();
        signal_all(cv);
        V_monitor();
    }
}

