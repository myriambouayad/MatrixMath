import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class MatrixController implements ActionListener {
    private MatrixView view;
    private MatrixModel model;
    private double[][] matrixA, matrixB;
    private JTextField[][] fieldsA, fieldsB;

    public MatrixController(MatrixView view) {
        this.view = view;
        this.model = new MatrixModel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Create Matrices")) {
            createMatrices();
        } else if (command.equals("Execute Operation")) {
            executeOperation();
        }
    }

    private void createMatrices() {
        try {
            int rowsA = view.getRowsA();
            int colsA = view.getColsA();
            int rowsB = view.getRowsB();
            int colsB = view.getColsB();

            matrixA = new double[rowsA][colsA];
            matrixB = new double[rowsB][colsB];
            model.setMatrices(matrixA, matrixB);
            view.updateStatus("Matrices created");

            createMatrixWindows(1, rowsA, colsA, "Matrix A");
            createMatrixWindows(2, rowsB, colsB, "Matrix B");
        } catch (NumberFormatException ex) {
            view.updateStatus("Invalid input");
        }
    }

    private void createMatrixWindows(int opt, int rows, int cols, String title) {
        JFrame frame = new JFrame(title);
        frame.setSize(300, 300);
        frame.setLayout(new BorderLayout());

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();

        p1.setLayout(new GridLayout(rows, cols));
        p2.setLayout(new GridLayout(1, 4));
        frame.add(p1, BorderLayout.CENTER);
        frame.add(p2, BorderLayout.SOUTH);

        JTextField file = new JTextField();
        JButton save = new JButton("Save");
        JButton open = new JButton("Open");
        JLabel fileLabel = new JLabel("File");
        p2.add(fileLabel);
        p2.add(file);
        p2.add(save);
        p2.add(open);

        if (opt == 1) {
            fieldsA = new JTextField[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    fieldsA[i][j] = new JTextField("", 3);
                    p1.add(fieldsA[i][j]);
                }
            }
            save.addActionListener(e -> save(1, file.getText()));
            open.addActionListener(e -> open(1, file.getText()));
        } else if (opt == 2) {
            fieldsB = new JTextField[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    fieldsB[i][j] = new JTextField("", 3);
                    p1.add(fieldsB[i][j]);
                }
            }
            save.addActionListener(e -> save(2, file.getText()));
            open.addActionListener(e -> open(2, file.getText()));
        }

        frame.setVisible(true);
    }

    private void save(int opt, String filename) {
        if (opt == 1) {
            saveFile(filename, matrixA);
        } else if (opt == 2) {
            saveFile(filename, matrixB);
        }
    }

    private void open(int opt, String filename) {
        if (opt == 1) {
            matrixA = readFile(filename);
            updateMatrixFields(1, matrixA);
        } else if (opt == 2) {
            matrixB = readFile(filename);
            updateMatrixFields(2, matrixB);
        }
    }

    private void saveFile(String filename, double[][] matrix) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(matrix);
            view.updateStatus("Matrix saved to " + filename);
        } catch (IOException e) {
            view.updateStatus("Error saving matrix: " + e.getMessage());
        }
    }

    private double[][] readFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (double[][]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            view.updateStatus("Error reading matrix: " + e.getMessage());
            return null;
        }
    }

    private void updateMatrixFields(int opt, double[][] matrix) {
        JTextField[][] fields = opt == 1 ? fieldsA : fieldsB;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                fields[i][j].setText(String.valueOf(matrix[i][j]));
            }
        }
    }

    private void executeOperation() {
        // Collect values from the text fields into the matrices
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                matrixA[i][j] = Double.parseDouble(fieldsA[i][j].getText());
            }
        }

        for (int i = 0; i < matrixB.length; i++) {
            for (int j = 0; j < matrixB[i].length; j++) {
                matrixB[i][j] = Double.parseDouble(fieldsB[i][j].getText());
            }
        }

        // Perform the selected operation
        String operation = view.getSelectedOperation();
        double[][] result = null;

        if (operation.equals("Add")) {
            result = model.add();
        } else if (operation.equals("Subtract")) {
            result = model.subtract();
        } else if (operation.equals("Multiply")) {
            result = model.multiply();
        } else if (operation.equals("Transpose")) {
            result = model.transpose();
        }

        if (result != null) {
            displayResult(result);
        } else {
            view.updateStatus("Error: Operation not supported");
        }
    }

    private void displayResult(double[][] result) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : result) {
            for (double value : row) {
                sb.append(String.format("%.2f ", value));
            }
            sb.append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Result Matrix", JOptionPane.INFORMATION_MESSAGE);
    }
}
