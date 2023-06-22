import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class LeaderBoardForm {
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JButton mainMenuButton;
    private JTable leaderboardTable;

    public LeaderBoardForm() {
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
                new MainMenuForm();
                frame.dispose();
            }
        });
    }

    private void addRows(DefaultTableModel model) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(".\\leaderboard.ldr"));
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);

        int version = dataInputStream.readInt();
        if (version == 1) {
            boolean run = true;
            while (run) {
                try {
                    String player = dataInputStream.readUTF();
                    int score = dataInputStream.readInt();
                    model.addRow(new Object[]{player, score});
                } catch (EOFException e) {
                    run = false;
                }
            }
        }
    }
}
