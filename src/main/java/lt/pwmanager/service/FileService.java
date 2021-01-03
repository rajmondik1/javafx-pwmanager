package lt.pwmanager.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lt.pwmanager.model.CredentialGroup;
import lt.pwmanager.model.Credentials;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileService {
    private static FileService INSTANCE;
    public File currentFile;

    private FileService() {

    }

    public static FileService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileService();
        }

        return INSTANCE;
    }


    public void newFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            this.currentFile = file;
        }
    }

    public void open() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            this.currentFile = file;
        }
    }

    // TODO: Extract to converter utility
    public List<CredentialGroup> getContents() {
        if (this.currentFile.canRead()) {
            try {
                String contents = read(this.currentFile);

                if (contents.length() > 0) {
                    Gson g = new Gson();

                    Type listType = new TypeToken<ArrayList<CredentialGroup>>() {
                    }.getType();
                    return g.fromJson(contents, listType);
                }
                return null;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                System.out.println("FUCK");
            }
        }
        return null;
    }

    public void writeToFile(List<CredentialGroup> credentialGroupList) {
        if (null == this.currentFile) {
            return;
        }
        try {
            Gson g = new Gson();
            System.out.println("Saving to file");
            saveToFile(g.toJson(credentialGroupList));
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            throw new Exception();
        }
    }

    public String read(File file) throws IOException {
        Scanner scanner = new Scanner(Path.of(file.getAbsolutePath()), StandardCharsets.UTF_8.name());
        String content = scanner.useDelimiter("\\A").next();
        scanner.close();
        return content;
    }


    private void saveToFile(String content) throws IOException {
        if (!currentFile.canRead() || !currentFile.canWrite()) return;

        if (null != this.currentFile) {

            FileWriter fileWriter = new FileWriter(this.currentFile, false);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            System.out.println("File written");
        } else {
            // new file creation thing
        }
    }

//    public static void write() {
//        File file = open();
//        if (file.canRead() && file.canWrite()) {
//            try {
//
//
////                String contents = read(file);
//                // TODO: Extract to converter utility
////
//
//
//                CredentialGroup credentialGroup = new CredentialGroup();
//                Credentials credentials = new Credentials();
//                credentials.setName("amazon pw");
//                credentials.setEmail("asdfa@asdfas.ff");
//                credentials.setPassword("asdfadfsf");
//
//                credentialGroup.setName("testinis goruaps");
//                credentialGroup.addCredentials(credentials);
//
//                System.out.println(Arrays.toString(credentialGroup.getCredentials().toArray()));
//                System.out.println(credentialGroup);
//
//                Gson g = new Gson();
//
//
//                String c = g.toJson(credentialGroup);
//
//                System.out.println(c);
//                System.out.println(file.getAbsolutePath());
//
//                FileWriter fw = new FileWriter(file);
//                fw.write("abcd");
//                fw.flush();
//                fw.close();
//
//                System.out.println("WRITTEN");
//
//
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//                System.out.println(e.getStackTrace());
//                System.out.println("FUCK");
//            }
//        }
//    }









    public String getFileName() {
        if (this.currentFile != null) {
            return this.currentFile.getName();
        }
        return "";
    }
}
