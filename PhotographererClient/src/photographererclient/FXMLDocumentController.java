/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photographererclient;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javax.swing.JFileChooser;

/**
 *
 * @author Bjork
 */
public class FXMLDocumentController implements Initializable {

    private String photographerCode;
    private ArrayList<String> usedCodes;
    private ArrayList<Image> sessionImages;
    private Image selectedImage;

    // Watchservice
    Thread watchThread;
    private Boolean hasFileChanges;
    private String fileName;
    private String prevFileName;

    @FXML
    private Label codeLabel;
    @FXML
    private ScrollPane imageScroller;
    @FXML
    private Pane imagePane;

    @FXML
    private void newSession(ActionEvent event) {
        codeLabel.setText(generateNewSessionCode());
    }

    @FXML
    private void printCode(ActionEvent event) {
        // TEMP 

        System.out.println("You clicked me!");
        codeLabel.setText("Hello World!");
    }

    @FXML
    private void deleteSelectedPhoto(ActionEvent event) {
        System.out.println("You clicked me!");
        codeLabel.setText("Hello World!");
    }

    @FXML
    private void uploadPhotos(ActionEvent event) {
        System.out.println("You clicked me!");
        codeLabel.setText("Hello World!");
    }

    @FXML
    private void fileLocation(ActionEvent event) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        Component parent;
        parent = new Component() {
        };

        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try {
                watchDir(fc.getSelectedFile().getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String generateNewSessionCode() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        Boolean generatingCode = true;
        while (generatingCode) {
            while (salt.length() < 7) {
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            if (!usedCodes.contains(photographerCode + salt.toString().toUpperCase())) {
                generatingCode = false;
            }
        }
        String saltStr = salt.toString().toUpperCase();

        saltStr = photographerCode + saltStr;

        usedCodes.add(saltStr);

        return saltStr;
    }

    public void watchDir(final String dir) throws IOException, InterruptedException {
        final WatchService service = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(dir);
        path.register(service,
                StandardWatchEventKinds.ENTRY_MODIFY);

        watchThread = new Thread(new Runnable() {

            @Override
            public void run() {
                hasFileChanges = false;
                WatchKey key = null;
                try {
                    key = service.take();
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (WatchEvent<?> event : key.pollEvents()) {
                    // WatchEvent<Path> ev = (WatchEvent<Path>)event;
                    // Path filename = ev.context();
                    // Kind<?> kind = watchEvent.kind();
                    // System.out.println(kind + ": " + filename);
                    // System.out.println(event.kind() + ": "+ ev.context());
                    System.out.println(event.kind() + ": " + event.context());

                    hasFileChanges = true;

                    fileName = "" + event.context();

                    final ImageView imageView = new ImageView();

                    try {
                        File file = new File(dir + "//" + event.context());
                        Image image = new Image(file.toURI().toString());
                        imageView.setImage(image);
                    } catch (Exception ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    imageView.maxWidth(200);
                    imageView.maxHeight(200);
                    imageView.setPreserveRatio(false);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            imagePane.getChildren().add(imageView);
                        }
                    });

                    /*
                     

                     //if (prevFileName.equals(fileName)) {
                     EdgeAdmin edgeAdmin = new EdgeAdmin();
                     prevFileName = "";

                     String events = event.context() + "";

                     if (events.equals("datat.edg")) {
                     try (BufferedReader br = new BufferedReader(new FileReader(dir + "\\" + event.context()))) {
                     edgeAdmin.level = Integer.valueOf(br.readLine());
                     for (String line; (line = br.readLine()) != null;) {
                     String[] splited = line.split(" ");
                     Edge edge = new Edge(Double.parseDouble(splited[0]), Double.parseDouble(splited[1]), Double.parseDouble(splited[2]), Double.parseDouble(splited[3]), Double.parseDouble(splited[4]), Double.parseDouble(splited[5]), Double.parseDouble(splited[6]));
                     edgeAdmin.edges.add(edge);
                     // process the line.
                     }
                     // line is not visible here.
                     } catch (Exception ex) {
                     System.out.println("File not finished");
                     }
                     }
                     if (events.equals("datab.edg")) {
                     FileInputStream inputFileStream;
                     ObjectInputStream objectInputStream;
                     try {
                     inputFileStream = new FileInputStream(dir + "\\" + event.context());
                     objectInputStream = new ObjectInputStream(inputFileStream);
                     edgeAdmin = (EdgeAdmin) objectInputStream.readObject();

                     objectInputStream.close();
                     inputFileStream.close();
                     } catch (Exception ex) {
                     System.out.println("File not finished");
                     }
                     }
                     */

                    /*
                     final String level = edgeAdmin.level + "";

                     Platform.runLater(new Runnable() {
                     @Override
                     public void run() {
                     labelLevel.setText("Level: " + level);
                     }
                     });

                     kochManager.changeLevel(edgeAdmin.edges);
                     */
                    //} else {
                    prevFileName = fileName;
                    //}

                }
                boolean valid = key.reset();
                if (!valid) {
                    // break;
                }
            }
        });
        watchThread.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //DEBUG
        photographerCode = "BV1";

        //INIT
        usedCodes = new ArrayList<>();
        sessionImages = new ArrayList<>();
    }

}
