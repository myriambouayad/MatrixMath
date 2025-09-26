import javax.swing.SwingUtilities;

public class Main {
    public static void main(String args[]){
    SwingUtilities.invokeLater(() -> {
        MatrixView view = new MatrixView();
        new MatrixController(view);
    });
}
}
