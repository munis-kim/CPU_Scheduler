package datatype;

import java.util.Comparator;
import java.util.PriorityQueue;

public class HRRN {

    public static PriorityQueue<Process_Data> setPriority(){
        PriorityQueue<Process_Data> processes = new PriorityQueue<>(new Comparator<Process_Data>() {
            @Override
            public int compare(Process_Data p1, Process_Data p2) {
                if(p1.getResponseRatio() > p2.getResponseRatio())
                    return 1;
                else if(p1.getResponseRatio() < p2.getResponseRatio())
                    return -1;
                else{
                    if(p1.getPriority() > p2.getPriority())
                        return 1;
                    else if(p2.getPriority() < p2.getPriority())
                        return -1;
                    else
                        return 0;
                }
            }
        });
        return processes;
    }
}
