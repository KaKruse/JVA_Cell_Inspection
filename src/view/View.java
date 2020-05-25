package view;

import control.Controller;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {

    private Controller controller;

    public View(Controller controller) {
        super();

        this.controller = controller;

        this.setTitle("JVA CIP");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Setting the window size to screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize);
        this.setContentPane(new Default_View(this.controller));
        this.setVisible(true);
    }

    public void switch_to_history_view() {
        this.setContentPane(new History_View(this.controller));
        this.revalidate();
    }

    public void switch_to_default_view() {
        this.setContentPane(new Default_View(this.controller));
        this.revalidate();
    }

    public void update_default_view() {
        this.switch_to_default_view();
    }
}
