package datatype;

public class Gantt_data {
    int pID;
    int start_time;
    int run_time;

    public Gantt_data(int pID, int start_time, int run_time) {
        this.pID = pID;
        this.start_time = start_time;
        this.run_time = run_time;
    }

    public int getNum() {
        return pID;
    }

    public int getStart_time() {
        return start_time;
    }

    public int getRun_time() {
        return run_time;
    }

    public void setRun_time() {
        run_time++;
    }
}
