package algorithm;

import datatype.*;
import logic.AddProcess;
import logic.ResultProcess;
import logic.Set_OutputTable;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Scheduling {
    private PriorityQueue<Process_Data> processes = null;
    private PriorityQueue<Process_Data>[] my_processes = new PriorityQueue[4];
    private Queue<Process_Data> processes_RR = new LinkedList<>();
    private ArrayList<Gantt_data> gantt = new ArrayList<>();
    private ArrayList<Scheduling_Result> processes_result = new ArrayList<>();
    private PriorityQueue<Process_Data> not_in_ready_queue = Not_Ready_Queue.setPriority();
    private int count;
    private int startTime = 0;
    private double waitingTime = 0;
    private Boolean preemptive = false;
    private Boolean isRR = false;
    private Boolean isHRRN = false;
    private Boolean isMine = false;
    private int gantt_num = -1;
    private int gantt_idx = -1;

    public double run(DefaultTableModel input_model, DefaultTableModel output_model, ArrayList<Gantt_data> gantt_original, int schedule_num, int time_slice) {
        gantt_num = -1;
        gantt_idx = -1;
        startTime = 0;
        waitingTime = 0;
        int input_row_count = input_model.getRowCount();
        count = input_row_count;

        for (int i = 0; i < count; ++i) {
            not_in_ready_queue.add(AddProcess.add(input_model, i));
        }

        switch (schedule_num) {
            case 0 -> processes = FCFS.setPriority();
            case 1 -> processes = SJF.setPriority();
            case 2 -> {
                processes = SJF.setPriority();
                preemptive = true;
            }
            case 3 -> processes = Priority.setPriority();
            case 4 -> {
                processes = Priority.setPriority();
                preemptive = true;
            }
            case 5 -> {
                processes = HRRN.setPriority();
                isHRRN = true;
            }
            case 6 -> isRR = true;
            case 7 -> {
                for (int i = 0; i < 4; ++i) {
                    my_processes[i] = My_Scheduling.setPriority();
                }
                isMine = true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + schedule_num);
        }

        if (isRR) {
            Process_Data temp = null;
            int timeSlice = time_slice;
            while (count > 0) {
                while (not_in_ready_queue.size() > 0 && not_in_ready_queue.peek().getArriveTime() <= startTime) {
                    Process_Data tmp = not_in_ready_queue.poll();
                    processes_RR.add(tmp);
                }

                if (temp != null && timeSlice == 0) {
                    processes_RR.add(temp);
                    timeSlice = time_slice;
                }
                if (processes_RR.size() > 0) {
                    Process_Data tmp = processes_RR.peek();
                    temp = tmp;
                    timeSlice--;
                    tmp.setRemainTime();
                    if (tmp.getStartTime() == -1) {
                        tmp.setStartTime(startTime);
                    }
                    if (gantt_num != tmp.getPID()) {
                        gantt_num = tmp.getPID();
                        Gantt_data tmp_pair = new Gantt_data(tmp.getPID(), startTime, 1);
                        gantt.add(tmp_pair);
                        gantt_idx++;
                    } else {
                        gantt.get(gantt_idx).setRun_time();
                    }
                    if (timeSlice == 0) {
                        processes_RR.poll();
                    }
                    if (tmp.getRemainTime() == 0) {
                        Scheduling_Result tmp_result = ResultProcess.add(tmp, startTime + 1);
                        processes_result.add(tmp_result);
                        count--;
                        if (timeSlice != 0)
                            processes_RR.poll();
                        timeSlice = time_slice;
                        temp = null;
                    }
                } else {
                    temp = null;
                    timeSlice = time_slice;
                }
                startTime++;
            }
        } else if (isMine) {
            int[] myScheduleTime = {8, 4, 2, 1};
            int[] remainTime = {7, 3, 1, 0};
            int cycleTime = 0;
            for (int j : myScheduleTime) cycleTime += j;
            int remainCycleTime = cycleTime;
            int inQueueProcess = 0;
            while (count > 0) {
                while (not_in_ready_queue.size() > 0 && not_in_ready_queue.peek().getArriveTime() <= startTime) {
                    Process_Data tmp = not_in_ready_queue.poll();
                    int priority = tmp.getPriority() / 4;
                    my_processes[3 - priority].add(tmp);
                }

                if (remainCycleTime > 0) {
                    boolean progress = false;
                    if (remainCycleTime > remainTime[0] && my_processes[0].size() != 0) {
                        Process_Data tmp = my_processes[0].poll();
                        preemtive_run(tmp);
                        progress = true;
                    } else if (remainCycleTime > remainTime[1] && my_processes[1].size() != 0) {
                        Process_Data tmp = my_processes[1].poll();
                        preemtive_run(tmp);
                        progress = true;
                    } else if (remainCycleTime > remainTime[2] && my_processes[2].size() != 0) {
                        Process_Data tmp = my_processes[2].poll();
                        preemtive_run(tmp);
                        progress = true;
                    } else if (my_processes[3].size() != 0) {
                        Process_Data tmp = my_processes[3].poll();
                        preemtive_run(tmp);
                        progress = true;
                    }
                    inQueueProcess = 0;
                    for (PriorityQueue<Process_Data> i : my_processes) {
                        inQueueProcess += i.size();
                    }
                    if (progress) {
                        startTime++;
                        remainCycleTime--;
                    } else if (inQueueProcess > 0) {
                        remainCycleTime = cycleTime;
                    } else startTime++;
                } else remainCycleTime = cycleTime;
            }
        } else {
            while (count > 0) {
                while (not_in_ready_queue.size() > 0 && not_in_ready_queue.peek().getArriveTime() <= startTime) {
                    Process_Data tmp = not_in_ready_queue.poll();
                    int arrive = tmp.getArriveTime();
                    tmp.setWaitingTime(startTime - arrive);
                    processes.add(tmp);
                }

                if (preemptive) {
                    if (processes.size() > 0) {
                        Process_Data tmp = processes.poll();
                        preemtive_run(tmp);
                    }
                    startTime++;
                } else if (isHRRN) {
                    if (processes.size() > 0) {
                        Queue<Process_Data> tmp_ = new LinkedList<>();
                        Process_Data tmp = processes.poll();
                        tmp.setStartTime(startTime);
                        Gantt_data tmp_pair = new Gantt_data(tmp.getPID(), startTime, tmp.getBurstTime());
                        gantt.add(tmp_pair);
                        int time = tmp.getBurstTime();
                        startTime += time;
                        Scheduling_Result tmp_result = ResultProcess.add(tmp, startTime);
                        processes_result.add(tmp_result);
                        count--;
                        int size_ = processes.size();
                        for (int i = 0; i < size_; ++i) {
                            Process_Data tmp_1 = processes.poll();
                            tmp_1.setWaitingTime(time);
                            tmp_.add(tmp_1);
                        }
                        for (int i = 0; i < size_; ++i) {
                            tmp = tmp_.poll();
                            processes.add(tmp);
                        }
                    } else {
                        startTime++;
                    }
                } else {
                    if (processes.size() > 0) {
                        Process_Data tmp = processes.poll();
                        tmp.setStartTime(startTime);
                        Gantt_data tmp_pair = new Gantt_data(tmp.getPID(), startTime, tmp.getBurstTime());
                        gantt.add(tmp_pair);
                        startTime += tmp.getBurstTime();
                        Scheduling_Result tmp_result = ResultProcess.add(tmp, startTime);
                        processes_result.add(tmp_result);
                        count--;
                    } else {
                        startTime++;
                    }
                }
            }
        }

        Set_OutputTable setting = new Set_OutputTable();
        waitingTime = setting.set(output_model, processes_result, input_row_count);
        gantt_original.addAll(gantt);
        return waitingTime;
    }

    private void preemtive_run(Process_Data tmp) {
        if (gantt_num != tmp.getPID()) {
            gantt_num = tmp.getPID();
            Gantt_data tmp_pair = new Gantt_data(tmp.getPID(), startTime, 1);
            gantt.add(tmp_pair);
            gantt_idx++;
        } else {
            gantt.get(gantt_idx).setRun_time();
        }
        if (tmp.getStartTime() == -1)
            tmp.setStartTime(startTime);
        tmp.setRemainTime();
        int time = tmp.getRemainTime();
        if (time == 0) {
            Scheduling_Result tmp_result = ResultProcess.add(tmp, startTime + 1);
            processes_result.add(tmp_result);
            count--;
        } else {
            if (isMine) {
                int priority = tmp.getPriority() / 4;
                my_processes[3 - priority].add(tmp);
            } else processes.add(tmp);
        }
    }
}
