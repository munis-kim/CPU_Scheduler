package datatype;

import datatype.*;
import java.util.*;

public class FCFS {

    public static PriorityQueue<Process_Data> setPriority(){
        PriorityQueue<Process_Data> processes = new PriorityQueue<>(new Comparator<Process_Data>() {
            @Override
            public int compare(Process_Data p1, Process_Data p2) {
                if(p1.getArriveTime() > p2.getArriveTime())
                    return 1;
                else if(p1.getArriveTime() < p2.getArriveTime())
                    return -1;
                else
                    return 0;
            }
        });
        return processes;
    }
}
