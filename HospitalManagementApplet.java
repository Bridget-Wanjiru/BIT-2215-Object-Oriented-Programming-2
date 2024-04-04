import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HospitalManagementApplet extends JApplet {
    private JTextArea outputArea;
    private JButton addPatientButton;
    private JButton generateReportButton;
    private ArrayList<Patient> patientsList = new ArrayList<>();

    @Override
    public void init() {
        // Initialize the applet components
        addPatientButton = new JButton("Add Patient");
        generateReportButton = new JButton("Generate Report");

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addPatientButton);
        buttonPanel.add(generateReportButton);

        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Patient.createAndDisplayPatient(patientsList);
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateReport.displayReport(patientsList);
            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // This method is called when the applet is started
    @Override
    public void start() {
        // Nothing to start in this case
    }

    // This method is called when the applet is stopped
    @Override
    public void stop() {
        // Nothing to stop in this case
    }

    // This method is called when the applet is destroyed
    @Override
    public void destroy() {
        // Nothing to destroy in this case
    }

    // This method is called to paint the applet
    @Override
    public void paint(Graphics g) {
        // Nothing to paint in this case
    }
}
