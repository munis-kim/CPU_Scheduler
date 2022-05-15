import datatype.Gantt_data;
import datatype.Process_Data;
import algorithm.*;
import logic.Gantt_Chart;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class CPU_Scheduler extends JFrame {
    private final ArrayList<Process_Data> Arr_Process = new ArrayList<>();
    private ArrayList<Gantt_data> gantt = new ArrayList<>();
    private DefaultTableModel input_model, output_model, gantt_model;
    private JTable input_table;
    private JTable output_table;
    private JTable gantt_table;
    JTextField text_average = new JTextField();
    JTextField text_execution = new JTextField();
    JTextField text_turnaround = new JTextField();
    JTextField text_response = new JTextField();
    JTextField text_cpu_utilization = new JTextField();
    JTextField text_throughput = new JTextField();
    JTextField text_timeslice = new JTextField();

    private final String[] input_table_header = {"Process", "PID", "도착시간", "실행시간", "우선순위"};
    private final String[] output_table_header = {"Process", "PID", "Turnaround time", "Waiting time", "Response time"};
    private final String[] gantt_table_header = {"PID", "Start time", "End time"};
    private int process_count = 0;
    private int time_slice = 1;
    double average_waitingTime = 0;
    double total_executionTime = 0;
    double total_burstTime = 0;
    double average_turnaroundTime = 0;
    double average_responseTime = 0;
    double cpu_utilization = 0;
    double throughput_t = 0;
    Container contentPane = getContentPane();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CPU_Scheduler frame = new CPU_Scheduler();
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public CPU_Scheduler() {
        Container cp = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1150, 660);
        setTitle("Scheduling");
        cp.setLayout(null);
        input_model = new DefaultTableModel(input_table_header, 0);
        input_table = new JTable(input_model);
        input_table.setAutoCreateRowSorter(true);
        JScrollPane input_pane = new JScrollPane(input_table);
        input_pane.setBounds(30, 10, 550, 280);
        contentPane.add(input_pane);
        output_model = new DefaultTableModel(output_table_header, 0);
        output_table = new JTable(output_model);
        output_table.setAutoCreateRowSorter(true);
        JScrollPane output_pane = new JScrollPane(output_table);
        output_pane.setBounds(30, 300, 550, 280);
        contentPane.add(output_pane);
        gantt_model = new DefaultTableModel(gantt_table_header, 0);
        gantt_table = new JTable(gantt_model);
        gantt_table.setAutoCreateRowSorter(true);
        JScrollPane gantt_pane = new JScrollPane(gantt_table);
        gantt_pane.setBounds(820, 80, 300, 280);
        contentPane.add(gantt_pane);

        JLabel execution = new JLabel("Total ExecutionTime");
        JLabel average = new JLabel("Average WaitingTime");
        JLabel turnaround = new JLabel("Average TurnaroundTime");
        JLabel response = new JLabel("Average ResponseTime");
        JLabel cpu = new JLabel("CPU Utilization");
        JLabel throughput = new JLabel("throughput");
        JLabel timeslice = new JLabel("Time Slice");
        execution.setBounds(600, 80, 150, 20);
        average.setBounds(600, 130, 150, 20);
        turnaround.setBounds(600, 180, 150, 20);
        response.setBounds(600, 230, 150, 20);
        cpu.setBounds(600, 280, 150, 20);
        throughput.setBounds(600, 330, 150, 20);
        timeslice.setBounds(900, 10, 100, 30);
        contentPane.add(average);
        contentPane.add(execution);
        contentPane.add(timeslice);
        contentPane.add(turnaround);
        contentPane.add(response);
        contentPane.add(cpu);
        contentPane.add(throughput);

        text_execution.setBounds(750, 80, 50, 20);
        text_average.setBounds(750, 130, 50, 20);
        text_turnaround.setBounds(750, 180, 50, 20);
        text_response.setBounds(750, 230, 50, 20);
        text_timeslice.setBounds(980, 15, 50, 20);
        text_cpu_utilization.setBounds(750, 280, 50, 20);
        text_throughput.setBounds(750, 330, 50, 20);
        text_average.setEditable(false);
        text_execution.setEditable(false);
        text_turnaround.setEditable(false);
        text_response.setEditable(false);
        text_cpu_utilization.setEditable(false);
        text_throughput.setEditable(false);
        text_timeslice.setEditable(true);
        text_average.setBackground(Color.white);
        text_execution.setBackground(Color.white);
        text_turnaround.setBackground(Color.white);
        text_response.setBackground(Color.white);
        text_cpu_utilization.setBackground(Color.white);
        text_timeslice.setBackground(Color.white);
        text_throughput.setBackground(Color.white);
        text_average.setBorder(null);
        text_execution.setBorder(null);
        text_turnaround.setBorder(null);
        text_response.setBorder(null);
        text_cpu_utilization.setBorder(null);
        text_throughput.setBorder(null);
        text_timeslice.setBorder(null);
        contentPane.add(text_average);
        contentPane.add(text_execution);
        contentPane.add(text_timeslice);
        contentPane.add(text_response);
        contentPane.add(text_turnaround);
        contentPane.add(text_cpu_utilization);
        contentPane.add(text_throughput);

        JButton fileOpenBtn = new JButton("OpenFile");
        fileOpenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File_Input();
            }
        });
        fileOpenBtn.setBounds(12, 586, 97, 23);
        contentPane.add(fileOpenBtn);

        JComboBox chooseAlgorithm = new JComboBox();
        chooseAlgorithm.addItem("FCFS Scheduling");
        chooseAlgorithm.addItem("Shortest Job First Scheduling");
        chooseAlgorithm.addItem("Shortest Remaining Time First Scheduling");
        chooseAlgorithm.addItem("Priority_nonpreemptive Scheduling");
        chooseAlgorithm.addItem("Priority_preemptive Scheduling");
        chooseAlgorithm.addItem("Highest Response Ratio Next Scheduling");
        chooseAlgorithm.addItem("Round Robin Scheduling");
        chooseAlgorithm.addItem("My Scheduling");
        chooseAlgorithm.setBounds(600, 10, 280, 30);
        contentPane.add(chooseAlgorithm);

        JButton runBtn = new JButton("Run");
        runBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while (output_model.getRowCount() > 0) {
                    output_model.removeRow(0);
                }
                while (gantt_model.getRowCount() > 0) {
                    gantt_model.removeRow(0);
                }
                average_turnaroundTime = 0;
                average_responseTime = 0;
                gantt.clear();
                int scheduling_num = chooseAlgorithm.getSelectedIndex();
                if (scheduling_num == 6)
                    time_slice = Integer.parseInt(text_timeslice.getText());
                average_waitingTime = Scheduling.run(input_model, output_model, gantt, scheduling_num, time_slice);
                for(int i = 0; i < output_model.getRowCount(); ++i) {
                    average_responseTime += Double.parseDouble((String) output_model.getValueAt(i, 4));
                    average_turnaroundTime += Double.parseDouble((String) output_model.getValueAt(i, 2));
                }
                average_responseTime /= process_count;
                average_turnaroundTime /= process_count;
                Arr_Process.clear();
                Gantt_Chart draw_chart = new Gantt_Chart();
                total_executionTime = gantt.get(gantt.size() - 1).getStart_time();
                total_executionTime += gantt.get(gantt.size() - 1).getRun_time();
                draw_chart.setGantt(gantt, total_executionTime);
                draw_chart.setBounds(600, 380, 525, 200);
                contentPane.add(draw_chart);
                draw_chart.revalidate();
                draw_chart.repaint();
                repaint();

                total_burstTime = ((double) Math.round(total_burstTime * 10) / 10);
                average_waitingTime = ((double) Math.round(average_waitingTime * 100) / 100);
                average_turnaroundTime = ((double) Math.round(average_turnaroundTime * 100) / 100);
                average_responseTime = ((double) Math.round(average_responseTime * 100) / 100);
                cpu_utilization = ((double) Math.round(total_executionTime / total_burstTime) * 100) / 100;
                throughput_t = ((double) Math.round(process_count / total_executionTime * 10000) / 10000);
                text_average.setText(String.valueOf(average_waitingTime));
                text_execution.setText(String.valueOf(total_burstTime));
                text_turnaround.setText(String.valueOf(average_turnaroundTime));
                text_response.setText(String.valueOf(average_responseTime));
                text_cpu_utilization.setText(String.valueOf(cpu_utilization * 100));
                text_throughput.setText(String.valueOf(throughput_t));

                for (int i = 0; i < gantt.size(); ++i) {
                    Vector<String> row = new Vector<>();
                    Gantt_data pro_tmp = gantt.get(i);
                    row.add(Integer.toString(pro_tmp.getNum()));
                    row.add(Integer.toString(pro_tmp.getStart_time()));
                    row.add(Integer.toString(pro_tmp.getStart_time() + pro_tmp.getRun_time()));
                    gantt_model.addRow(row);
                }
            }
        });
        runBtn.setBounds(121, 586, 97, 23);
        contentPane.add(runBtn);
    }

    private void File_Input() {
        while (input_model.getRowCount() > 0) {
            input_model.removeRow(0);
        }
        text_average.setText(null);
        text_execution.setText(null);
        text_response.setText(null);
        text_turnaround.setText(null);
        total_executionTime = 0;
        total_burstTime = 0;
        process_count = 0;
        String filePath = "";
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt 파일", "txt");
        chooser.setCurrentDirectory(new File("./"));
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("열기");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int ret = chooser.showOpenDialog(null);

        if (ret == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().toString();
        }

        try {
            File file = new File(filePath);
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                process_count++;
                String str = scan.nextLine();
                String temp[] = str.split("\\s+");
                String name = temp[0];
                int pid = Integer.parseInt(temp[1]);
                int arriveTime = Integer.parseInt(temp[2]);
                int burstTime = Integer.parseInt(temp[3]);
                int priority = Integer.parseInt(temp[4]);
                total_burstTime += Double.parseDouble(temp[3]);
                Process_Data tmp = new Process_Data(name, pid, burstTime, arriveTime, priority);
                Arr_Process.add(tmp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < process_count; ++i) {
            Vector<String> row = new Vector<>();
            Process_Data pro_tmp = Arr_Process.get(i);
            row.add(pro_tmp.getName());
            row.add(Integer.toString(pro_tmp.getPID()));
            row.add(Integer.toString(pro_tmp.getArriveTime()));
            row.add(Integer.toString(pro_tmp.getBurstTime()));
            row.add(Integer.toString((pro_tmp.getPriority())));
            input_model.addRow(row);
        }
    }
}