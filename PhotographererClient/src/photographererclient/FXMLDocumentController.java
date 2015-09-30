/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photographererclient;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

/**
 *
 * @author Bjork
 */
public class FXMLDocumentController implements Initializable {
    
    private String photographerCode;
    private ArrayList<String> usedCodes;
    private ArrayList<Image> sessionImages;
    private Image selectedImage;    
    
    @FXML
    private Label codeLabel;
    private ScrollPane scrollLayout;
    
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
        System.out.println("You clicked me!");
        codeLabel.setText("Hello World!");
    }
    
    private String generateNewSessionCode() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        Boolean generatingCode = true;
        while(generatingCode) {
            while (salt.length() < 7) {
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            if(!usedCodes.contains(photographerCode + salt.toString().toUpperCase())) {
                generatingCode = false;
            }
        }
        String saltStr = salt.toString().toUpperCase();
        
        saltStr = photographerCode + saltStr;
        
        usedCodes.add(saltStr);
        
        return saltStr;
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
