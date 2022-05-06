package logic;

import datatype.Process_Data;

import javax.swing.table.DefaultTableModel;

public class AddProcess {
    public static Process_Data add(DefaultTableModel input_model, int i){
        String name;
        int pID, arriveTime, burstTime, priority;
        name = (String) input_model.getValueAt(i, 0);
        pID = Integer.parseInt((String) input_model.getValueAt(i, 1));
        arriveTime = Integer.parseInt((String)input_model.getValueAt(i, 2));
        burstTime = Integer.parseInt((String)input_model.getValueAt(i, 3));
        priority = Integer.parseInt((String)input_model.getValueAt(i, 4));
        Process_Data ret = new Process_Data(name, pID, burstTime, arriveTime, priority);
        return ret;
    }
}
