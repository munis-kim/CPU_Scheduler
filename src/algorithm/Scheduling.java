package algorithm;

import datatype.Gantt_data;
import datatype.Process_Data;
import datatype.Scheduling_Result;
import logic.AddProcess;
import logic.Gantt_Chart;
import logic.ResultProcess;
import logic.Set_OutputTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Scheduling {
    public static double run(DefaultTableModel input_model, DefaultTableModel output_model, JPanel contentPane, double total_executionTime, int schedule_num){
        PriorityQueue<Process_Data> processes;
        ArrayList<Gantt_data> gantt = new ArrayList<>();
        ArrayList<Scheduling_Result> processes_result = new ArrayList<>();
        LinkedList<Integer> not_in_ready_queue = new LinkedList<>();
        int input_row_count = input_model.getRowCount();
        int startTime = 0;
        int count = input_row_count;
        double waitingTime = 0;

        for(int i = 0; i < count; ++i){
            int temp = Integer.parseInt((String)input_model.getValueAt(i, 1));
            not_in_ready_queue.add(temp);
        }

        switch(schedule_num){
            case 0:
                processes = FCFS.setPriority();
                break;
            case 1:
                processes = SJF.setPriority();
                break;
            case 3:
                processes = Priority_nonpreemptive.setPriority();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + schedule_num);
        }

        while(count > 0){
            int k = 0;
            int size = not_in_ready_queue.size();
            for (int i = 0; i < size; ++i) {
                if(startTime >= Integer.parseInt((String)input_model.getValueAt(i, 2))) {
                    int value = not_in_ready_queue.get(i - k++);
                    Process_Data tmp = AddProcess.add(input_model, value - 1);
                    processes.add(tmp);
                    int index = not_in_ready_queue.indexOf(value);
                    not_in_ready_queue.remove(index);
                }
            }

            if(processes.size() > 0) {
                Process_Data temp = processes.peek();
                int p_size = processes.size();
                for(int j = 0; j < p_size; ++j){
                    Process_Data tmp = processes.poll();
                    Scheduling_Result tmp_result = ResultProcess.add(tmp, gantt, startTime);
                    processes_result.add(tmp_result);
                    startTime += tmp.getBurstTime();
                    count--;
                }
            }
            else{
                startTime++;
            }
        }

        Set_OutputTable setting = new Set_OutputTable();
        Gantt_Chart draw = new Gantt_Chart();
        waitingTime = setting.set(output_model, processes_result, input_row_count);
        draw.draw(contentPane, gantt, total_executionTime);
        return waitingTime;
    }
}
