package datatype;

public class Process_Data {
    String name;
    int PID;
    int BurstTime;
    int ArriveTime;
    int Priority;
    int RemainTime;
    int StartTime = -1;
    int WaitingTime = 0;

    public Process_Data(String name, int PID, int BurstTime, int ArriveTime, int Priority){
        this.name = name;
        this.PID= PID;
        this.BurstTime = BurstTime;
        this.ArriveTime = ArriveTime;
        this.Priority = Priority;
        this.RemainTime = BurstTime;
    }

    public String getName(){
        return name;
    }

    public int getPID(){ return PID; }

    public int getBurstTime(){
        return BurstTime;
    }

    public int getArriveTime(){
        return ArriveTime;
    }

    public int getPriority(){
        return Priority;
    }

    public int getRemainTime(){ return RemainTime; }

    public int getStartTime(){ return StartTime; }

    public void setStartTime(int time){ StartTime = time; }

    public void setRemainTime(){
        RemainTime--;
    }

    public void setWaitingTime(int time) { this.WaitingTime += time; }

    public double getResponseRatio(){
        double ret = (double)(WaitingTime + BurstTime) / BurstTime;
        return ret;
    }
}
