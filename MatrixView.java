import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MatrixView extends JFrame {
    private JTextField rowsFieldA, colsFieldA;
    private JTextField rowsFieldB, colsFieldB;
    private JButton createButton, executeButton, quitButton, resetButton;
    private JComboBox<String> operationBox;
    private JLabel statusLabel;
    private MatrixController controller;

    public MatrixView() {
        setTitle("Matrix Math Program");
        setSize(400, 200);
        setLayout(new GridLayout(6, 2));

        rowsFieldA = new JTextField();
        colsFieldA = new JTextField();

        rowsFieldB = new JTextField();
        colsFieldB = new JTextField();

        operationBox = new JComboBox<>(new String[]{"Add", "Subtract", "Multiply", "Transpose"});
        createButton = new JButton("Create Matrices");
        executeButton = new JButton("Execute Operation");
        statusLabel = new JLabel("Status: Waiting");
        resetButton = new JButton("Reset");
        quitButton = new JButton("Quit");

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                rowsFieldA.setText("");
                colsFieldA.setText("");
                rowsFieldB.setText("");
                colsFieldB.setText("");
            }
        });

        quitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });

        add(new JLabel("Rows/ Columns A:"));
        add(new JLabel("Rows/ Columns B:"));
        add(rowsFieldA);
        add(rowsFieldB);
        
        add(colsFieldA);
        add(colsFieldB);

        add(operationBox);
        add(createButton);
        add(quitButton);
        add(resetButton);
        add(executeButton);
        add(statusLabel);

        controller = new MatrixController(this);
        createButton.addActionListener(controller);
        executeButton.addActionListener(controller);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public int getRowsA() { return Integer.parseInt(rowsFieldA.getText()); }
    public int getColsA() { return Integer.parseInt(colsFieldA.getText()); }
    public int getRowsB() { return Integer.parseInt(rowsFieldB.getText()); }
    public int getColsB() { return Integer.parseInt(colsFieldB.getText()); }
    public String getSelectedOperation() { return (String) operationBox.getSelectedItem(); }
    public void updateStatus(String message) { statusLabel.setText("Status: " + message); }
}
