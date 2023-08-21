import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainMenuForm {
    private JPanel mainPanel;
    private JButton newGameButton;
    private JButton loadGameButton;
    private JButton leaderboardButton;
    private JButton exitButton;
    private JPanel buttonPanel;

    public MainMenuForm(Point location) {
        JFrame frame = new JFrame(Game.TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(this.mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(location);

        this.newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new ChooseDifficultyForm(frame.getLocation());
            }
        });

        this.loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuForm.this.loadGame(frame);
            }
        });

        this.leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LeaderboardForm(frame.getLocation());
            }
        });

        this.exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    private void loadGame(Frame frame) {
        String[] saveSlots = new String[]{"Save Slot 1", "Save Slot 2", "Save Slot 3"};
        int chosedSaveSlot = JOptionPane.showOptionDialog(null, "Choose the save slot where your game is saved.", "Click a button", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, saveSlots, saveSlots[0]);

        try {
            FileInputStream fileInputStream = new FileInputStream(".\\saves\\save_slot" + (chosedSaveSlot + 1) + ".sav");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            GameWindow gameWindow = (GameWindow) objectInputStream.readObject();
            frame.dispose();
            gameWindow.run();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occured while loading the game.");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "An error occured while loading the game.");
        }
    }
}
