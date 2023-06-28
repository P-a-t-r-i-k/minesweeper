import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class LeaderboardForm {
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JButton mainMenuButton;
    private JTable leaderboardTable;

    public LeaderboardForm() {
        JFrame frame = new JFrame(Game.TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(this.mainPanel);
        frame.pack();
        frame.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) this.leaderboardTable.getModel();
        model.addColumn("Player");
        model.addColumn("Score");

        try {
            this.addRows(model);
        } catch (IOException e) {

        }

        frame.revalidate();
        frame.repaint();

        this.mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new MainMenuForm();
            }
        });
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
