package control;

import model.Calculator;
import model.File_Connector;
import model.Pair;
import view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class
 * Handling the control flows of the program.
 */
public class Controller {

    /**
     * Path to the storage file.
     */
    private static final String STORAGE_PATH = "history.csv";
    /**
     * Program log
     */
    private final Logger logger;
    /**
     * Calculation object. Responsible for all math calculations.
     */
    private final Calculator calculator;
    /**
     * Connection object
     */
    private final File_Connector connector;
    /**
     * UI Object
     */
    private final View view;

    /**
     * Constructor
     * Initializing logger, storage connection object, calculator and ui.
     */
    public Controller() {

        this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        this.logger.setLevel(Level.ALL);

        this.logger.info("Controller initialization started.");

        logger.info("Initialize file connector ...");
        this.connector = new File_Connector(STORAGE_PATH);
        this.check_storage();

        logger.info("Initialize program calculator ...");
        this.calculator = new Calculator();
        //ToDo calculate rotation if program runs for the very first time or newest results are outdated
        if(!this.check_actuality()) {
            logger.info("Calculating a new two week inspection plan.");
            try {
                connector.write_to_file(this.calculator.calc_inspections(new Date(System.currentTimeMillis())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.info("Initializing program UI ...");
        this.view = new View(this);

        this.logger.info("Controller initialization finished.");
    }

    /**
     * Method that checks if the storage file exists or not.
     * In case of no storage file it creates a new one.
     *
     * @return
     */
    private boolean check_storage() {
        File file = new File(STORAGE_PATH);
        if(file.exists()) {
            this.logger.info("Storage file existing. Check ok");
            return true;
        } else {
            this.logger.warning("Storage file not existing. Check failed");
            this.connector.create_storage_file();
            return false;
        }
    }

    /**
     * Method to check if there is an inspection rotation for the current date.
     *
     * @return true if there are values for the current date. Else false.
     */
    private boolean check_actuality() {
        try {
            List<List<String>> actual_inspection = this.connector.get_current_inspection();
            for(List<String> day: actual_inspection) {
                if((new SimpleDateFormat("dd.MM.yyyy")).format(new Date(System.currentTimeMillis())).equals(day.get(0))) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            logger.warning("Could not access the storage file - " + e);
        }
        return false;
    }

    /**
     * Get all entries from the storage file.
     *
     * @return all entries from the storage file.
     */
    public List<List<String>> get_history_from_storage() {
        try {
            return this.connector.get_history();
        } catch (FileNotFoundException e) {
            logger.severe("Storage file not existing. Couldn't load history.");
        }
        return null;
    }

    /**
     * Get the last 14 entries of the storage file
     * @return the last 14 entries of the storage file
     */
    public List<List<String>> get_actual_inspection() {
        List<List<String>> actual_inspection = null;
        try {
            actual_inspection = this.connector.get_current_inspection();
        } catch (FileNotFoundException e) {
            logger.warning("Could not access the storage file - " + e);
        }

        return actual_inspection;
    }

    /**
     * Switch the GUI to the history view
     */
    public void switch_to_history_view() {
        this.view.switch_to_history_view();
    }

    /**
     * Switch the GUI to the default view.
     */
    public void switch_to_default_view() {
        this.view.switch_to_default_view();
    }

    /**
     * Calculates new inspection plan and stores it to the storage file.
     */
    public void calculate_new_inspection() {
        try {
            connector.write_to_file(this.calculator.calc_inspections(new Date(System.currentTimeMillis())));
        } catch (IOException e) {
            logger.warning("No storage file found. Could not write current inspection plan to file. Data will be lost after program restart.");
        }

        this.view.update_default_view();
    }

    /**
     * Safes a copy of the storage file to given location.
     * @param file location of the export file.
     */
    public void export_file(File file) {
        try {
            this.connector.safe_file(file);
        } catch (IOException e) {
            logger.warning(" " + e);
        }
    }
}
