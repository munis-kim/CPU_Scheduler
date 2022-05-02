public class Scheduling_Result {
    String name, PID, TurnaroundTime, WaitingTime, ResponseTime;

    public Scheduling_Result(String name, String PID, String TurnaroundTime, String WaitingTime, String ResponseTime){
        this.name = name;
        this.PID = PID;
        this.TurnaroundTime = TurnaroundTime;
        this.WaitingTime = WaitingTime;
        this.ResponseTime = ResponseTime;
    }
}