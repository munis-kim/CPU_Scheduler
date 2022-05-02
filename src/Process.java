public class Process {
    String name;
    int pID;
    int BurstTime;
    int ArriveTime;
    int Priority;

    public Process(String name, int pID, int BurstTime, int ArriveTime, int Priority){
        this.name = name;
        this.pID= pID;
        this.BurstTime = BurstTime;
        this.ArriveTime = ArriveTime;
        this.Priority = Priority;
    }
}
