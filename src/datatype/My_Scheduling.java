package datatype;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class My_Scheduling {
    private PriorityQueue<Process_Data> set(){
        PriorityQueue<Process_Data> processes = new PriorityQueue<>((p1, p2) -> {
            if(p1.getBurstTime() > p2.getBurstTime())
                return 1;
            else if(p1.getResponseRatio() < p2.getResponseRatio())
                return -1;
            else{
                if(p1.getPriority() < p2.getPriority())
                    return 1;
                else if(p2.getPriority() > p2.getPriority())
                    return -1;
                else
                    return 0;
            }
        });

        return processes;
    }
}
