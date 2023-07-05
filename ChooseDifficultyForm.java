import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseDifficultyForm {
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton beginnerDiffButton;
    private JButton intermediateDiffButton;
    private JButton expertDiffButton;

    public ChooseDifficultyForm() {
        JFrame frame = new JFrame(Game.TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(this.mainPanel);
        frame.pack();
        frame.setVisible(true);


        this.beginnerDiffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                //new GameWindowForm(Difficulty.BEGINNER);
                new GameWindow(Difficulty.BEGINNER);
            }
        });


        this.intermediateDiffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new GameWindowForm(Difficulty.INTERMEDIATE);
                new GameWindow(Difficulty.INTERMEDIATE);
            }
        });


        this.expertDiffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new GameWindowForm(Difficulty.EXPERT);
                new GameWindow(Difficulty.EXPERT);
            }
        });
    }
}
