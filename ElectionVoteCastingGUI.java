import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

class Electorate {
    private int processId;
    private VoteCounter voteCounter;
    private ElectionVoteCastingGUI gui;
    private boolean voted;

    public Electorate(int processId, VoteCounter voteCounter, ElectionVoteCastingGUI gui) {
        this.processId = processId;
        this.voteCounter = voteCounter;
        this.gui = gui;
        this.voted = false;
    }

    public void vote(String candidate) {
        if (!voted) {
            voted = true;
            voteCounter.castVote(processId, candidate);
            gui.updateMessageBox("Electorate " + processId + " voted for candidate " + candidate);
            if (processId == 5) {
                voteCounter.determineWinner();
            }
        } else {
            gui.updateMessageBox("Electorate " + processId + " has already voted.");
        }
    }

    public boolean hasVoted() {
        return voted;
    }
}

class VoteCounter {
    private int[] votes;
    private int numberOfElectorates;
    private JLabel winnerLabel;
    private ElectionVoteCastingGUI gui;

    public VoteCounter(int numberOfElectorates, JLabel winnerLabel, ElectionVoteCastingGUI gui) {
        this.numberOfElectorates = numberOfElectorates;
        this.winnerLabel = winnerLabel;
        this.gui = gui;
        votes = new int[2]; // 0: A, 1: B
    }

    public synchronized void castVote(int processId, String vote) {
        if (vote.equals("A")) {
            votes[0]++;
        } else if (vote.equals("B")) {
            votes[1]++;
        } else {
            System.out.println("Invalid vote cast by Electorate " + processId);
            return;
        }
    }

    public void determineWinner() {
        char winner = votes[0] > votes[1] ? 'A' : 'B';
        String result = "Winner is candidate " + winner + "\n\n";
        for (int i = 0; i < votes.length; i++) {
            result += "Votes for candidate " + (char) ('A' + i) + ": " + votes[i] + "\n";
        }
        winnerLabel.setText(result);

        gui.disableVoteButtons();
    }
}

public class ElectionVoteCastingGUI extends JFrame {

    private int numberOfElectorates;
    private VoteCounter voteCounter;
    private JButton[] voteButtons;
    private JTextArea messageBox;
    private Electorate[] electorates;

    public ElectionVoteCastingGUI() {
        numberOfElectorates = 5;
        voteButtons = new JButton[2];
        voteButtons[0] = new JButton("Vote for A");
        voteButtons[1] = new JButton("Vote for B");

        JLabel winnerLabel = new JLabel();

        VoteCounter voteCounter = new VoteCounter(numberOfElectorates, winnerLabel, this);
        electorates = new Electorate[numberOfElectorates];

        for (int i = 0; i < numberOfElectorates; i++) {
            Electorate electorate = new Electorate(i + 1, voteCounter, this);
            electorates[i] = electorate;
        }

        voteButtons[0].addActionListener(e -> electorates[getCurrentElectorate() - 1].vote("A"));
        voteButtons[1].addActionListener(e -> electorates[getCurrentElectorate() - 1].vote("B"));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(voteButtons[0]);
        panel.add(voteButtons[1]);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.NORTH);

        messageBox = new JTextArea();
        messageBox.setEditable(false);
        updateMessageBox("Electorate 1: Cast your vote (A or B)");

        mainPanel.add(new JScrollPane(messageBox), BorderLayout.CENTER);
        mainPanel.add(winnerLabel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setTitle("Election Vote Casting");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the initial size of the frame
        setSize(400, 200);

        // Maximize the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Center the frame
        setLocationRelativeTo(null);
    }

    public void updateMessageBox(String message) {
        messageBox.append(message + "\n");
    }

    public void disableVoteButtons() {
        for (JButton button : voteButtons) {
            button.setEnabled(false);
        }
    }

    private int getCurrentElectorate() {
        for (int i = 0; i < electorates.length; i++) {
            if (!electorates[i].hasVoted()) {
                return i + 1;
            }
        }
        return -1; // All electorates have voted
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ElectionVoteCastingGUI gui = new ElectionVoteCastingGUI();
            gui.setVisible(true);
        });
    }
}
