import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;

public class LeaderboardForm {
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JButton mainMenuButton;
    private JTable leaderboardTable;
    private JButton resetLeaderboardButton;

    public LeaderboardForm(Point location) {
        JFrame frame = new JFrame(Game.TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(this.mainPanel);
        frame.setSize(350, 160);
        frame.setVisible(true);
        frame.setLocation(location);

        DefaultTableModel model = (DefaultTableModel)this.leaderboardTable.getModel();
        model.addColumn("Player");
        model.addColumn("Score");

        try {
            this.addRows(model);
        } catch (IOException e) {

        }

        frame.revalidate();
        frame.repaint();

        this.mainMenuButton.addActionListener(e -> {
            frame.dispose();
            new MainMenuForm(location);
        });

        this.resetLeaderboardButton.addActionListener(e -> {
            try {
                LeaderboardForm.this.resetLeaderboard();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred while reseting the leaderboard.");
            }
            frame.dispose();
            new LeaderboardForm(location);
        });
    }

    private void resetLeaderboard() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(Game.LEADERBOARD_FILE));
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

        dataOutputStream.writeUTF("");
        dataOutputStream.close();
    }

    private void addRows(DefaultTableModel model) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(Game.LEADERBOARD_FILE));
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);

        int version = dataInputStream.readInt();
        if (version == 1) {
            boolean reachedEndOfFile = false;
            while (!reachedEndOfFile) {
                try {
                    String player = dataInputStream.readUTF();
                    int score = dataInputStream.readInt();
                    model.addRow(new Object[]{player, score});
                } catch (EOFException e) {
                    reachedEndOfFile = true;
                }
            }
        }
    }
}
