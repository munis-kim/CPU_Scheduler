import java.awt.Container;
import java.awt.EventQueue;
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
import Algorithms.*;

public class CPU_Scheduler extends JFrame {
    private final ArrayList<Process> Arr_Process = new ArrayList<>();
    private DefaultTableModel input_model, output_model;
    private JTable input_table;
    private JTable output_table;
    private String input_table_header[] = {"Process", "PID", "도착시간", "실행시간", "우선순위"};
    private String output_table_header[] = {"Process", "PID", "Turnaround time", "Waiting time", "Response time"};
    private int process_count = 0;
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
        chooseAlgorithm.addItem("SJF Scheduling");
        chooseAlgorithm.addItem("SRTF Scheduling");
        chooseAlgorithm.addItem("Priority Scheduling");
        chooseAlgorithm.addItem("Round-Robin Scheduling");
        chooseAlgorithm.addItem("My Scheduling");
        chooseAlgorithm.setBounds(700, 10, 200, 30);
        contentPane.add(chooseAlgorithm);

        JButton runBtn = new JButton("Run");
        runBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int scheduling_num = chooseAlgorithm.getSelectedIndex();
                switch(scheduling_num){
                    case 0:
                        FCFS run_FCFS = new FCFS();
                        run_FCFS.Scheduling(input_model, output_model);
                        break;

                }
            }
        });
        runBtn.setBounds(121, 586, 97, 23);
        contentPane.add(runBtn);

    }

    private void File_Input() {
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
                Process tmp = new Process(name, pid, burstTime, arriveTime, priority);
                Arr_Process.add(tmp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < process_count; ++i) {
            Vector<String> row = new Vector<>();
            Process pro_tmp = Arr_Process.get(i);
            row.add(pro_tmp.name);
            row.add(Integer.toString(pro_tmp.PID));
            row.add(Integer.toString(pro_tmp.ArriveTime));
            row.add(Integer.toString(pro_tmp.BurstTime));
            row.add(Integer.toString((pro_tmp.Priority)));
            input_model.addRow(row);
        }
    }
}