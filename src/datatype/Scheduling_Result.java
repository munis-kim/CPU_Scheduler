package datatype;

public class Scheduling_Result {
    String name, PID, TurnaroundTime, WaitingTime, ResponseTime;

    public Scheduling_Result(String name, String PID, String TurnaroundTime, String WaitingTime, String ResponseTime){
        this.name = name;
        this.PID = PID;
        this.TurnaroundTime = TurnaroundTime;
        this.WaitingTime = WaitingTime;
        this.ResponseTime = ResponseTime;
    }

    public String getName(){
        return name;
    }

    public String getPID(){
        return PID;
    }

    public String getTurnaroundTime(){
        return TurnaroundTime;
    }

    public String getWaitingTime(){
        return WaitingTime;
    }

    public String getResponseTime(){
        return ResponseTime;
    }
}