import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {
    private final JTextField display = new JTextField("0");
    private double first = 0;
    private String op = "";
    private boolean startNew = true;

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        display.setFont(new Font("SansSerif", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        String[] labels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };
        JPanel grid = new JPanel(new GridLayout(4, 4, 5, 5));
        for (String l : labels) grid.add(makeButton(l));
        add(grid, BorderLayout.CENTER);
        add(makeButton("C"), BorderLayout.SOUTH);

        setSize(320, 420);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 20));
        b.addActionListener(this);
        return b;
    }

    private double current() {
        try {
            return Double.parseDouble(display.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void show(double v) {
        if (v == (long) v) display.setText(String.valueOf((long) v));
        else display.setText(String.valueOf(v));
    }

    private void compute() {
        double second = current();
        switch (op) {
            case "+": show(first + second); break;
            case "-": show(first - second); break;
            case "*": show(first * second); break;
            case "/":
                if (second == 0) { display.setText("Error"); op = ""; return; }
                show(first / second);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.matches("[0-9]")) {
            if (startNew || display.getText().equals("0")) display.setText(cmd);
            else display.setText(display.getText() + cmd);
            startNew = false;
        } else if (cmd.equals(".")) {
            if (startNew) { display.setText("0."); startNew = false; }
            else if (!display.getText().contains(".")) display.setText(display.getText() + ".");
        } else if (cmd.equals("C")) {
            display.setText("0");
            first = 0;
            op = "";
            startNew = true;
        } else if (cmd.equals("=")) {
            if (!op.isEmpty()) compute();
            op = "";
            startNew = true;
        } else { // + - * /
            if (!op.isEmpty() && !startNew) compute();
            first = current();
            op = cmd;
            startNew = true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
