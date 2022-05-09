package algorithm;

import datatype.*;
import logic.AddProcess;
import logic.Gantt_Chart;
import logic.ResultProcess;
import logic.Set_OutputTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Scheduling {
    public static double run(DefaultTableModel input_model, DefaultTableModel output_model, JPanel contentPane, double total_executionTime, int schedule_num, int time_slice) {
        PriorityQueue<Process_Data> processes = null;
        Queue<Process_Data> processes_RR = new LinkedList<>();
        ArrayList<Gantt_data> gantt = new ArrayList<>();
        ArrayList<Scheduling_Result> processes_result = new ArrayList<>();
        LinkedList<Integer> not_in_ready_queue = new LinkedList<>();
        int input_row_count = input_model.getRowCount();
        int startTime = 0;
        int count = input_row_count;
        double waitingTime = 0;
        Boolean preemptive = false;
        Boolean isRR = false;

        for (int i = 0; i < count; ++i) {
            //int temp = Integer.parseInt((String) input_model.getValueAt(i, 1));
            not_in_ready_queue.add(i);
        }

        switch (schedule_num) {
            case 0:
                processes = FCFS.setPriority();
                break;
            case 1:
                processes = SJF.setPriority();
                break;
            case 2:
                processes = SJF.setPriority();
                preemptive = true;
                break;
            case 3:
                processes = Priority.setPriority();
                break;
            case 4:
                processes = Priority.setPriority();
                preemptive = true;
                break;
            case 6:
                isRR = true;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + schedule_num);
        }

        if (isRR) {
            Process_Data temp = null;
            int timeSlice = time_slice;
            while (count > 0) {
                int k = 0;
                int size = not_in_ready_queue.size();
                for (int i = 0; i < size; ++i) {
                    int idx = not_in_ready_queue.get(i - k);
                    if (startTime >= Integer.parseInt((String) input_model.getValueAt(idx, 2))) {
                        Process_Data tmp = AddProcess.add(input_model, idx);
                        processes_RR.add(tmp);
                        int index = not_in_ready_queue.indexOf(idx);
                        not_in_ready_queue.remove(index);
                        k++;
                    }
                }
                if (temp != null && timeSlice == 0) {
                    processes_RR.add(temp);
                    timeSlice = time_slice;
                }
                if (processes_RR.size() > 0) {
                    Process_Data tmp = processes_RR.peek();
                    temp = tmp;
                    System.out.println(tmp.getPID() + " " + tmp.getRemainTime());
                    timeSlice--;
                    tmp.setRemainTime();
                    if (tmp.getStartTime() == -1) {
                        tmp.setStartTime(startTime);
                    }
                    Gantt_data tmp_pair = new Gantt_data(tmp.getPID(), startTime, 1);
                    gantt.add(tmp_pair);
                    if (timeSlice == 0) {
                        processes_RR.poll();
                    }
                    if (tmp.getRemainTime() == 0) {
                        Scheduling_Result tmp_result = ResultProcess.add(tmp, startTime + 1);
                        processes_result.add(tmp_result);
                        count--;
                        if(timeSlice != 0)
                            processes_RR.poll();
                        timeSlice = time_slice;
                        temp = null;
                    }
                }
                else {
                    temp = null;
                    timeSlice = time_slice;
                }
                startTime++;
            }
        } else {
            while (count > 0) {
                int k = 0;
                int size = not_in_ready_queue.size();

                for (int i = 0; i < size; ++i) {
                    int idx = not_in_ready_queue.get(i - k);
                    if (startTime >= Integer.parseInt((String) input_model.getValueAt(idx, 2))) {
                        Process_Data tmp = AddProcess.add(input_model, idx);
                        processes.add(tmp);
                        int index = not_in_ready_queue.indexOf(idx);
                        not_in_ready_queue.remove(index);
                        k++;
                    }
                }

                if (preemptive) {
                    if (processes.size() > 0) {
                        Process_Data tmp = processes.poll();
                        Gantt_data tmp_pair = new Gantt_data(tmp.getPID(), startTime, 1);
                        gantt.add(tmp_pair);
                        if (tmp.getStartTime() == -1) {
                            tmp.setStartTime(startTime);
                        }
                        tmp.setRemainTime();
                        int time = tmp.getRemainTime();
                        if (time == 0) {
                            Scheduling_Result tmp_result = ResultProcess.add(tmp, startTime + 1);
                            processes_result.add(tmp_result);
                            count--;
                        } else
                            processes.add(tmp);
                    }
                    startTime++;

                } else {
                    if (processes.size() > 0) {
                        int p_size = processes.size();
                        for (int j = 0; j < p_size; ++j) {
                            Process_Data tmp = processes.poll();
                            tmp.setStartTime(startTime);
                            Gantt_data tmp_pair = new Gantt_data(tmp.getPID(), startTime, tmp.getBurstTime());
                            gantt.add(tmp_pair);
                            startTime += tmp.getBurstTime();
                            Scheduling_Result tmp_result = ResultProcess.add(tmp, startTime);
                            processes_result.add(tmp_result);
                            count--;
                        }
                    } else {
                        startTime++;
                    }
                }
            }
        }

        Set_OutputTable setting = new Set_OutputTable();
        Gantt_Chart draw = new Gantt_Chart();
        waitingTime = setting.set(output_model, processes_result, input_row_count);
        draw.draw(contentPane, gantt, total_executionTime);
        return waitingTime;
    }
}
