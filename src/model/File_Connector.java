package model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class File_Connector {
    /**
     * Path of the storage file
     */
    private final String path;

    /**
     * Constructor
     * Initializes the connector
     * @param storage_path path to the storage file
     */
    public File_Connector(String storage_path) {
        this.path = storage_path;
    }

    /**
     * Create a new storage file
     */
    public void create_storage_file() {
        File file = new File(this.path);
        try {
            file.createNewFile();
            //Create new file with header line
            FileWriter writer = new FileWriter(this.path);
            writer.write("Creation date;Inspection date;Cell1;Cell2;Cell3;Cell4\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @throws ParseException
     */
    private void delete_old_entries() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -365);

        String line = null;
        if(cal.after(null)) {

        }
//        if((Calendar.getInstance().setTime(formatter.parse(line.split(";")[1]))).after(cal.add(Calendar.DATE, -365))) {
            //remove line from file
//        }
    }

    /**
     * Method to write data to the storage file
     * @param input data to write
     * @throws IOException
     */
    public void write_to_file(List<Pair<String, List<String>>> input) throws IOException {
            FileWriter file = new FileWriter(this.path, true);
            BufferedWriter writer = new BufferedWriter(file);
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for (Pair<String, List<String>> stringListPair : input) {
            String cells = "";
            for (int j = 0; j < stringListPair.getB().size(); j++) {
                cells = cells + ";" + stringListPair.getB().get(j);
            }
            writer.write(formatter.format(new Date(System.currentTimeMillis())) + ";" + stringListPair.getA() + cells + "\n");
        }
            writer.close();
    }

    /**
     * Method getting the stored history.
     *
     * @return
     */
    public List<List<String>> get_history() throws FileNotFoundException {
        //Output: Inspection date and a list of four cells to inspect.
        List<List<String>> history = new ArrayList<>();

            File file = new File(this.path);
            Scanner scanner = new Scanner(file);

            String line;
            List<String> line_elements;

            //Skip first line
            if(scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {

                line = scanner.nextLine();
                line_elements = Arrays.asList(line.split(";"));

                history.add(line_elements);
            }
        return history;
    }

    /**
     * Method that returns the 14 newest entries from the storage file.
     *
     * @return the current inspection rotation
     */
    public List<List<String>> get_current_inspection() throws FileNotFoundException {
        //Output: Inspection date and a list of four cells to inspect.
        List<List<String>> current_inspection = new ArrayList<>();
        //String representation of the storage.
        List<String> doc = new ArrayList<>();

        //Connect to the storage file
        File file = new File(this.path);
        Scanner scanner = new Scanner(file);
        //Skip first line
        if(scanner.hasNextLine()) {
            scanner.nextLine();
        }
        //Read storage file line by line to list
        while (scanner.hasNextLine()) {
            doc.add(scanner.nextLine());
        }
        if(!doc.isEmpty() && doc.get(doc.size() - 1).trim().isEmpty()) {
            doc.remove(doc.size() - 1);
        }
        //Check if storage has at least 14 entries = one inspection rotation
        if(doc.size() >= 14) {
            //Add the 14 newest entries to the output.
            for(int i = doc.size() - 1; i >= doc.size() - 14; i--) {
                List<String> line = new ArrayList(Arrays.asList(doc.get(i).split(";")));
                line.remove(0);
                current_inspection.add(line);
            }
        } else {
            //Throw Error
        }
        return current_inspection;
    }

    /**
     * Copying the storage file to a given file.
     * @param file to copy storage to
     * @throws IOException
     */
    public void safe_file(File file) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(new File(this.path));
            os = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
