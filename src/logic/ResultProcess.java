package logic;

import datatype.Gantt_data;
import datatype.Process_Data;
import datatype.Scheduling_Result;

import java.util.ArrayList;

public class ResultProcess {

    public static Scheduling_Result add(Process_Data tmp, ArrayList<Gantt_data> gantt, int start_time){
        String name, pID, TurnaroundTime, WaitingTime, ResponseTime;
        name = tmp.getName();
        pID = Integer.toString(tmp.getPID());
        TurnaroundTime = Integer.toString(start_time + tmp.getBurstTime() - tmp.getArriveTime());
        WaitingTime = Integer.toString(start_time - tmp.getArriveTime());
        ResponseTime = Integer.toString(start_time - tmp.getArriveTime());
        Gantt_data tmp_pair = new Gantt_data(tmp.getPID(), start_time, tmp.getBurstTime());
        gantt.add(tmp_pair);
        Scheduling_Result tmp_result = new Scheduling_Result(name, pID, TurnaroundTime, WaitingTime, ResponseTime);
        return tmp_result;
    }
}
