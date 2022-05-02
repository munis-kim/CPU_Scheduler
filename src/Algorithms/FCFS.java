package Algorithms;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

class Pair {
    int num;
    int time;

    public Pair(int num, int time){
        this.num = num;
        this.time = time;
    }

    public int getNum(){
        return num;
    }

    public int getTime(){
        return time;
    }
}

public class FCFS {
    private ArrayList<Scheduling_Result> Result_Process= new ArrayList<>();

    public void Scheduling(DefaultTableModel input_model, DefaultTableModel output_model){
        ArrayList<Process> processes = new ArrayList<>();
        ArrayList<Pair> gantt = new ArrayList<>();
        int input_row_count = input_model.getRowCount();
        for(int i = 0; i < input_row_count; ++i){

        }
    }
}
