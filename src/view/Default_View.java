package view;

import control.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Program UI based on Java AWT.
 */
public class Default_View extends JPanel {

    /**
     * Projects controller reference.
     */
    private final Controller controller;

    /**
     *  Constructor
     *  Initializing the program UI.
     *
     * @param controller program controller
     */
    public Default_View(Controller controller) {
        //Calling JPanel Contructor
        super();
        //Setting controller
        this.controller = controller;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //Add content to the program window
        this.set_contents();
    }

    private void set_contents() {
        this.add(new JLabel("Current date is " + new SimpleDateFormat("EEE, dd.MM.yyyy").format(System.currentTimeMillis())));

        JPanel control_panel = new JPanel();
        //Show only for testing reasons?
        JButton new_btn = new JButton("New Rotation");
        new_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.calculate_new_inspection();
            }
        });
        control_panel.add(new_btn);

        JButton history_btn = new JButton("Show History");
        history_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.switch_to_history_view();
            }
        });
        control_panel.add(history_btn);
        this.add(control_panel);

        this.add(new JPanel());
        this.update_inspection_view();
    }


    private void update_inspection_view() {
        JPanel inspection_view_panel = new JPanel();
        GridLayout layout = new GridLayout(0, 5);
        inspection_view_panel.setLayout(layout);

        List<List<String>> actual_inspections = this.controller.get_actual_inspection();


        if(actual_inspections == null || actual_inspections.isEmpty()) {
            inspection_view_panel.add(new JLabel("Currently no data available."));
        } else {
            for(int i = actual_inspections.size() - 1; i >= 0; i--) {
                for(int j = 0; j < actual_inspections.get(i).size(); j++) {
                    inspection_view_panel.add(new JLabel(actual_inspections.get(i).get(j)));
                }
            }
        }

        this.remove(this.getComponents().length - 1);
        this.add(inspection_view_panel);
    }

}
