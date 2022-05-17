package datatype;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class My_Scheduling {
    public static PriorityQueue<Process_Data> setPriority(){
        return new PriorityQueue<>((p1, p2) -> {
            if(p1.getRemainTime() > p2.getRemainTime())
                return 1;
            else if(p1.getRemainTime() < p2.getRemainTime())
                return -1;
            else{
                if(p1.getPriority() < p2.getPriority())
                    return 1;
                else if(p2.getPriority() > p2.getPriority())
                    return -1;
                else return 0;
            }
        });
    }
}
