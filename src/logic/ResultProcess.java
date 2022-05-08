package logic;

import datatype.Process_Data;
import datatype.Scheduling_Result;

public class ResultProcess {

    public static Scheduling_Result add(Process_Data tmp, int start_time){
        String name, pID, TurnaroundTime, WaitingTime, ResponseTime;
        name = tmp.getName();
        pID = Integer.toString(tmp.getPID());
        WaitingTime = Integer.toString(start_time - tmp.getArriveTime() - tmp.getBurstTime());
        TurnaroundTime = Integer.toString(tmp.getBurstTime() + Integer.parseInt(WaitingTime));
        ResponseTime = Integer.toString(tmp.getStartTime() - tmp.getArriveTime());
        Scheduling_Result tmp_result = new Scheduling_Result(name, pID, TurnaroundTime, WaitingTime, ResponseTime);
        return tmp_result;
    }
}
