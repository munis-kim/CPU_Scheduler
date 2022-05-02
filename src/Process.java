public class Process {
    String name;
    int PID;
    int BurstTime;
    int ArriveTime;
    int Priority;

    Process(String name, int PID, int BurstTime, int ArriveTime, int Priority){
        this.name = name;
        this.PID= PID;
        this.BurstTime = BurstTime;
        this.ArriveTime = ArriveTime;
        this.Priority = Priority;
    }
}
