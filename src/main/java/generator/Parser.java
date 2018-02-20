package generator;

import org.json.JSONObject;

import java.io.*;

public class Parser {
    public static JSONObject parseFile(long id, Class clazz, String FILE_NAME){
        String currentLine = "";

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((currentLine = br.readLine()) != null) {
                String idField = clazz.getDeclaredFields()[0].getName();
                String fromIDString = currentLine.substring(currentLine.indexOf(idField) + idField.length() + 2);
                String idString = fromIDString.substring(0, fromIDString.indexOf(","));

                if (id == Long.parseLong(idString)){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject(currentLine);
    }

    public static void removeLineFromFile(String FILE_NAME, String lineToRemove) {

        try {
            File inFile = new File(FILE_NAME);
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = "";

            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(lineToRemove)) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");

        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
