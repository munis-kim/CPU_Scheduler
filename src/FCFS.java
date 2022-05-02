import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;

public class FCFS {
    private ArrayList<Scheduling_Result> Result_Process = new ArrayList<>();

    public double Scheduling(DefaultTableModel input_model, DefaultTableModel output_model, JPanel contentPane, double total_executionTime) {
        ArrayList<Process> processes = new ArrayList<>();
        ArrayList<Gantt_data> gantt = new ArrayList<>();
        ArrayList<Scheduling_Result> processes_result = new ArrayList<>();
        int input_row_count = input_model.getRowCount();
        int start_time = 0;
        for (int i = 0; i < input_row_count; ++i) {
            String name;
            int pID, arriveTime, burstTime, priority;
            name = (String) input_model.getValueAt(i, 0);
            pID = Integer.parseInt((String) input_model.getValueAt(i, 1));
            arriveTime = Integer.parseInt((String)input_model.getValueAt(i, 2));
            burstTime = Integer.parseInt((String)input_model.getValueAt(i, 3));
            priority = Integer.parseInt((String)input_model.getValueAt(i, 4));
            Process tmp = new Process(name, pID, burstTime, arriveTime, priority);
            processes.add(tmp);
        }
        Collections.sort(processes, (p1, p2) -> {
            if(p1.ArriveTime > p2.ArriveTime)
                return 1;
            else if(p1.ArriveTime < p2.ArriveTime)
                return -1;
            else
                return 0;
        });

        Process temp = processes.get(0);
        start_time = temp.ArriveTime;
        for(int i = 0; i < input_row_count; ++i){
            Process tmp = processes.get(i);
            String name, pID, TurnaroundTime, WaitingTime, ResponseTime;
            name = tmp.name;
            pID = Integer.toString(tmp.pID);
            TurnaroundTime = Integer.toString(start_time + tmp.BurstTime - tmp.ArriveTime);
            WaitingTime = Integer.toString(start_time - tmp.ArriveTime);
            ResponseTime = Integer.toString(start_time - tmp.ArriveTime);
            Gantt_data tmp_pair = new Gantt_data(tmp.pID, start_time, tmp.BurstTime);
            gantt.add(tmp_pair);
            Scheduling_Result tmp_result = new Scheduling_Result(name, pID, TurnaroundTime, WaitingTime, ResponseTime);
            processes_result.add(tmp_result);
            start_time += tmp.BurstTime;
        }

        Set_OutputTable setting = new Set_OutputTable();
        Gantt_Chart draw = new Gantt_Chart();
        double waitingTime = setting.set(output_model, processes_result, input_row_count);
        draw.draw(contentPane, gantt, total_executionTime);
        return waitingTime;
    }
}
