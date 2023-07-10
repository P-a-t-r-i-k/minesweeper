import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseDifficultyForm {
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton beginnerDiffButton;
    private JButton intermediateDiffButton;
    private JButton expertDiffButton;

    public ChooseDifficultyForm(Point location) {
        JFrame frame = new JFrame(Game.TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(this.mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(location);


        this.beginnerDiffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new GameWindow(Difficulty.BEGINNER, location);
            }
        });


        this.intermediateDiffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new GameWindow(Difficulty.INTERMEDIATE, location);
            }
        });


        this.expertDiffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new GameWindow(Difficulty.EXPERT, location);
            }
        });
    }
}
