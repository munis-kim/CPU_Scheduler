package logic;

import datatype.Scheduling_Result;
import datatype.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Vector;


public class Set_OutputTable {
    public double set(DefaultTableModel output_model, ArrayList<Scheduling_Result> process_result, int len){
        double waitingTime = 0;
        for(int i = 0; i < len; ++i){
            Vector<String> row = new Vector<>();
            Scheduling_Result tmp = process_result.get(i);
            waitingTime += Double.parseDouble(tmp.getWaitingTime());
            row.add(tmp.getName());
            row.add(tmp.getPID());
            row.add(tmp.getTurnaroundTime());
            row.add(tmp.getWaitingTime());
            row.add(tmp.getResponseTime());
            output_model.addRow(row);
        }
        return waitingTime / len;
    }
}
