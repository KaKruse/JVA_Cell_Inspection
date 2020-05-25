package view;

import control.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class History_View extends JPanel {

    /**
     * Projects controller reference.
     */
    private final Controller controller;

    public History_View(Controller controller) {
        //Calling JPanel Contructor
        super();
        //Setting controller
        this.controller = controller;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.set_contents();

    }

    private void set_contents() {
        JPanel control_panel = new JPanel();

        JButton back_btn = new JButton("Back");
        back_btn.addActionListener(e -> controller.switch_to_default_view());
        control_panel.add(back_btn);

        JButton export_btn = new JButton("Export");
        export_btn.addActionListener(e -> start_export_dialog());
        control_panel.add(export_btn);
        this.add(control_panel);

        this.create_history();
    }

    private void create_history() {
        JPanel history = new JPanel();
        GridLayout layout = new GridLayout(0, 6);
        history.setLayout(layout);
        List<List<String>> history_data = controller.get_history_from_storage();
        for(List<String> line: history_data) {
            for(String entry: line) {
                history.add(new JLabel(entry));
            }
        }
        JScrollPane scroll_pane = new JScrollPane(history, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll_pane);
    }

    private void start_export_dialog() {
        JFileChooser file_chooser = new JFileChooser();
        int result = file_chooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            controller.export_file(file_chooser.getSelectedFile());
        }
    }
}
