package sample;

import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.*;
import javafx.scene.image.Image;

import javax.swing.text.html.ImageView;

public class Controller {

    @FXML
    private Label tempLabel;
    @FXML
    private Label skyLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label windLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Button button1;
    @FXML
    private javafx.scene.image.ImageView cityImageView;
    @FXML
    private RadioButton radioButton0;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;

    @FXML
    private RadioButton radioButton4;

    ToggleGroup group=new ToggleGroup();
    File currentDirectory = new File(new File(".").getAbsolutePath());
    String[] images=new String[5];


    //Makes a togglegroup for the buttons
    public void initialize()
    {
        radioButton0.setToggleGroup(group);
        radioButton1.setToggleGroup(group);
        radioButton2.setToggleGroup(group);
        radioButton3.setToggleGroup(group);
        radioButton4.setToggleGroup(group);
        radioButton0.setSelected(true);
        try {
            String imgUrl= "images/";
            images[0]=imgUrl+"sthlm.jpg";
            images[1]=imgUrl+"gothenburg.jpg";
            images[2]=imgUrl+"malmo.jpeg";
            images[3]=imgUrl+"london.jpg";
            images[4]=imgUrl+"paris.jpg";
        }catch (Exception e){e.printStackTrace();}
        updateWeather(0);
    }


    //Id's for cities
    String APPID = "&APPID=13f26582659a6990aea327a98fbe9562";
    String[] location = {"Stockholm", "Gothenburg", "Malmo", "London", "Paris"};
    String urlString = "";


    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return map;
    }

    //Updates the weather labels and city picture
    public void updateWeather(int i) {
        urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + location[i] + APPID + "&units=metric";

        //tries to open a buffered reader from the url specified
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            Map<String, Object> resultMap = jsonToMap(result.toString());
            Map<String, Object> mainMap = jsonToMap(resultMap.get("main").toString());
            Map<String, Object> windMap = jsonToMap(resultMap.get("wind").toString());
            Scanner sc = new Scanner(jsonToMap(result.toString()).toString());

            //Not so nice solution
            boolean found = false;
            String sky = "";
            while (!found) {
                String temp = sc.next();
                if (temp.length() >= 12 || !sc.hasNext()) {
                    if (temp.substring(0, 12).equals("description=") || !sc.hasNext())
                        found = true;
                    sky = temp.substring(12);
                }
            }

            //Set labels
            tempLabel.setText("Temp: "+mainMap.get("temp").toString()+"° C");
            humidityLabel.setText("Humidity: " + mainMap.get("humidity").toString()+" %");
            skyLabel.setText("Sky: " + sky);
            windLabel.setText("Wind: " + " " + windMap.get("speed") + " m/s");
            cityLabel.setText(location[i]);

            //Set picture
            cityImageView.setImage(new Image("images/gothenburg.jpg"));
            cityImageView.setImage(new Image(images[i]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method for updating the weather with the right info when the button is clicked
    public void onButtonClicked() {
        int i = 0;
        if (radioButton0.isSelected()) {
            i = 0;
        }
        if (radioButton1.isSelected()) {
            i = 1;
        }
        if (radioButton2.isSelected()) {
            i = 2;
        }
        if (radioButton3.isSelected()) {
            i = 3;
        }
        if (radioButton4.isSelected()) {
            i = 4;
        }
        updateWeather(i);
    }


}
