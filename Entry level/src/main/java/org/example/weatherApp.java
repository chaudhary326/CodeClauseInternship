package org.example;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class weatherApp {
    private static final String API_KEY = "75cddfd83ce381ff2c254160d9a4682d";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter city name: ");
            String city = reader.readLine();

            // URL encode the city name to handle spaces or special characters
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String urlString = BASE_URL + encodedCity + "&appid=" + API_KEY + "&units=metric";

            @SuppressWarnings("deprecation")
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.toString());

                String cityName = rootNode.get("name").asText();
                double temperature = rootNode.get("main").get("temp").asDouble();
                String weatherDescription = rootNode.get("weather").get(0).get("description").asText();

                // Display weather information
                System.out.println("Weather in " + cityName + ":");
                System.out.println("Temperature: " + temperature + "°C");
                System.out.println("Description: " + weatherDescription);
            } else {
                System.out.println("City not found or API error occurred.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}