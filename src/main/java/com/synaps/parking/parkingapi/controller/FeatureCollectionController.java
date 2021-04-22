package com.synaps.parking.parkingapi.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


@RestController
@RequestMapping("/api")
public class FeatureCollectionController {

    @Autowired
    private RestTemplate restTemplate;


    //METH 1 BASIC CONNECTION: Connects to the remote api and consumes it in order to have a JSON Object of geoJSON data
    private JSONObject consumeApi() throws IOException, ParseException {
        try {
            String urlbase = "http://data.lacub.fr/geojson?key=9Y2RU3FTE8&SERVICE=WFS&VERSION=1.1.0&REQUEST=GetFeature&typename=ST_PARK_P&SRSNAME=EPSG:4326";
            //Object[] objects= restTemplate.getForObject(urlbase, Object[].class);
            URL url = new URL(urlbase);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

//Getting the response code
            int responsecode = conn.getResponseCode();
            System.out.println(responsecode);
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            //Close the scanner
            scanner.close();

            //Using the JSON simple library parse the string into a json object
            JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject) parse.parse(inline);

            //Get the required object from the above created object (to sort it )
          //  JSONObject obj = (JSONObject) data_obj.get("Global");
            return data_obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //METH 2: Using Templates
    @RequestMapping(value = "/template/featurecollection")
    public JSONObject getProductList() {
        String urlbase = "http://data.lacub.fr/geojson?key=9Y2RU3FTE8&SERVICE=WFS&VERSION=1.1.0&REQUEST=GetFeature&typename=ST_PARK_P&SRSNAME=EPSG:4326";
        JSONObject objects= restTemplate.getForObject(urlbase,JSONObject.class);

        return objects;

    }


    @GetMapping("/featurecollectionall")
    public JSONObject getFeatures() throws IOException, ParseException {
        return consumeApi();
    }

}
