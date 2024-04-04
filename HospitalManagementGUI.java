import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

interface NHIF {
    double calculateNHIF(double totalBill);
}

class Patient {
    private String name;
    private int age;
    private String contactNumber;
    public int patientID;
    private ArrayList<Payment> payments;
    private static JTextArea outputArea;

    public Patient(String name, int age, String contactNumber, int patientID) {
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
        this.patientID = patientID;
        this.payments = new ArrayList<>();
    }

    public static void setOutputArea(JTextArea area) {
        outputArea = area;
    }

    public static void createAndDisplayPatient(ArrayList<Patient> patientsList) {
        JFrame frame = new JFrame("Add Patient");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();
        JLabel contactLabel = new JLabel("Contact Number:");
        JTextField contactField = new JTextField();
        JLabel paymentLabel = new JLabel("Payment:");
        JTextField paymentField = new JTextField();

        // Generate unique random patient ID
        Random random = new Random();
        int patientID = random.nextInt(10000);

        JButton addButton = new JButton("Add");
        JButton clearButton = new JButton("Clear");
        JButton exitButton = new JButton("Exit");

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                ageField.setText("");
                contactField.setText("");
                paymentField.setText("");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(ageLabel);
        formPanel.add(ageField);
        formPanel.add(contactLabel);
        formPanel.add(contactField);
        formPanel.add(paymentLabel);
        formPanel.add(paymentField);
        formPanel.add(clearButton);
        formPanel.add(exitButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String contactNumber = contactField.getText();
                double paymentAmount = Double.parseDouble(paymentField.getText());

                double nhifAmount = new NHIFImplementation().calculateNHIF(paymentAmount);
                double netPayment = paymentAmount - nhifAmount;

                Payment payment = new Payment(netPayment, nhifAmount);
                Patient patient = new Patient(name, age, contactNumber, patientID);
                patient.addPayment(payment);
                patientsList.add(patient);
                displayPatientDetails(patientsList);

                // Display success dialog
                JOptionPane.showMessageDialog(frame, "Patient successfully added with ID: " + patientID);

                frame.dispose();
            }
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(addButton, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    public static void displayPatientDetails(ArrayList<Patient> patientsList) {
        StringBuilder details = new StringBuilder("Patients Details:\n");
        for (int i = 0; i < patientsList.size(); i++) {
            Patient patient = patientsList.get(i);
            details.append("Patient ").append(i + 1).append(": ");
            details.append("Name: ").append(patient.getName()).append(", Age: ").append(patient.getAge());
            details.append(", Contact: ").append(patient.getContactNumber());
            if (!patient.getPayments().isEmpty()) {
                details.append(", ").append(patient.getPayments().get(0).formatPaymentDetails());
            }
            details.append("\n");
        }
        if (outputArea != null) {
            outputArea.setText(details.toString());
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public int getPatientID() {
        return patientID;
    }
}

class GenerateReport {
    public static void displayReport(ArrayList<Patient> patientsList) {
        JFrame frame = new JFrame("Patient Details Report");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a table model with appropriate column names
        String[] columnNames = {"Patient ID", "Name", "Age", "Contact Number", "Net Payment", "NHIF Amount", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add patient data to the table model
        for (Patient patient : patientsList) {
            if (!patient.getPayments().isEmpty()) {
                Payment payment = patient.getPayments().get(0);
                Object[] rowData = {patient.getPatientID(), patient.getName(), patient.getAge(), patient.getContactNumber(), payment.getNetPayment(), payment.getNhifAmount(), payment.getLastUpdatedDateFormatted()};
                model.addRow(rowData);
            }
        }

        // Create JTable with the created table model
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
}

class NHIFImplementation implements NHIF {
    @Override
    public double calculateNHIF(double totalBill) {
        return totalBill * 0.1; // Assuming NHIF rate is 10%
    }
}

public class HospitalManagementGUI extends JFrame {
    private JTextArea outputArea;
    private JButton addPatientButton;
    private JButton generateReportButton;
    private ArrayList<Patient> patientsList = new ArrayList<>();

    public HospitalManagementGUI() {
        setTitle("Hospital Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addPatientButton = new JButton("Add Patient");
        generateReportButton = new JButton("Generate Report");

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        Patient.setOutputArea(outputArea);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalManagementGUI().setVisible(true);
        });
    }
}
