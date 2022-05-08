import datatype.Process_Data;
import algorithm.*;
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
    private DefaultTableModel input_model, output_model;
    private JTable input_table;
    private JTable output_table;
    JTextField text_average = new JTextField();
    JTextField text_execution = new JTextField();
    private String input_table_header[] = {"Process", "PID", "도착시간", "실행시간", "우선순위"};
    private String output_table_header[] = {"Process", "PID", "Turnaround time", "Waiting time", "Response time"};
    private int process_count = 0;
    private int time_slice = 1;
    double average_waitingTime = 0;
    double total_executionTime = 0;
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
        JPanel chart = new JPanel();
        chart.setBounds(600, 380, 500, 200);
        chart.setBackground(Color.lightGray);
        contentPane.add(chart);
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

        JLabel average = new JLabel("Average WaitingTime");
        JLabel execution = new JLabel("Total ExecutionTime");
        average.setBounds(600, 80, 150, 20);
        execution.setBounds(600, 130, 150, 20);
        contentPane.add(average);
        contentPane.add(execution);

        text_average.setBounds(750, 80, 50, 20);
        text_execution.setBounds(750, 130, 50, 20);
        text_average.setEditable(false);
        text_execution.setEditable(false);
        text_average.setBackground(Color.white);
        text_execution.setBackground(Color.white);
        text_average.setBorder(null);
        text_execution.setBorder(null);
        contentPane.add(text_average);
        contentPane.add(text_execution);

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
        chooseAlgorithm.setBounds(700, 10, 280, 30);
        contentPane.add(chooseAlgorithm);

        JButton runBtn = new JButton("Run");
        runBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while(output_model.getRowCount() > 0){
                    output_model.removeRow(0);
                }
                int scheduling_num = chooseAlgorithm.getSelectedIndex();

                average_waitingTime = Scheduling.run(input_model, output_model, chart, total_executionTime, scheduling_num, time_slice);
                Arr_Process.clear();
                average_waitingTime = ((double)Math.round(average_waitingTime*100)/100);
                total_executionTime = ((double)Math.round(total_executionTime*10)/10);
                text_average.setText(String.valueOf(average_waitingTime));
                text_execution.setText(String.valueOf(total_executionTime));
            }
        });
        runBtn.setBounds(121, 586, 97, 23);
        contentPane.add(runBtn);
    }

    private void File_Input() {
        while(input_model.getRowCount() > 0){
            input_model.removeRow(0);
        }
        text_average.setText(null);
        text_execution.setText(null);
        total_executionTime = 0;
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
                total_executionTime += Double.parseDouble(temp[3]);
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