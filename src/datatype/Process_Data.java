package datatype;

public class Process_Data {
    String name;
    int PID;
    int BurstTime;
    int ArriveTime;
    int Priority;

    public Process_Data(String name, int PID, int BurstTime, int ArriveTime, int Priority){
        this.name = name;
        this.PID= PID;
        this.BurstTime = BurstTime;
        this.ArriveTime = ArriveTime;
        this.Priority = Priority;
    }

    public String getName(){
        return name;
    }

    public int getPID(){
        return PID;
    }

    public int getBurstTime(){
        return BurstTime;
    }

    public int getArriveTime(){
        return ArriveTime;
    }

    public int getPriority(){
        return Priority;
    }

}
