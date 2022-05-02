package Algorithms;

public class Scheduling_Result {
    String name;
    int PID;
    int TurnaroundTime;
    int WaitingTime;
    int ResponseTime;

    public Scheduling_Result(String name, int PID, int TurnaroundTime, int WaitingTime, int ResponseTime){
        this.name = name;
        this.PID = PID;
        this.TurnaroundTime = TurnaroundTime;
        this.WaitingTime = WaitingTime;
        this.ResponseTime = ResponseTime;
    }
}