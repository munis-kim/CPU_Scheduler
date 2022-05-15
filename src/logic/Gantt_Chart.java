package logic;

import datatype.Gantt_data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Gantt_Chart extends JPanel {

    ArrayList<Gantt_data> gantt = new ArrayList<>();
    int total_executionTime;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        boolean first = true;
        int size = gantt.size();
        if (size == 0)
            return;
        int time = 0;
        int x = 500 / total_executionTime;
        int start_time = 0;
        for (int i = 0; i < size; ++i) {
            if (first) {
                start_time = gantt.get(i).getStart_time();
                first = false;
                g.drawString(String.valueOf(start_time), (start_time * x) - 1, 75);
            }
            time = gantt.get(i).getRun_time();
            start_time = gantt.get(i).getStart_time();
            String value = String.valueOf(start_time + time);
            int loc = x * time - 4;
            g.drawRect((start_time * x), 10, (x * time), 50);
            g.drawString(value, (start_time * x) + loc, 75);
            g.setColor(Color.red);
            g.drawString("p" + Integer.toString(gantt.get(i).getNum()), (start_time * x ) + loc/2 -2, 35);
            g.setColor(Color.black);
        }
    }

    public void setGantt(ArrayList<Gantt_data> gantt, double total_executionTime) {
        this.gantt = gantt;
        this.total_executionTime = (int) total_executionTime;
    }
}
