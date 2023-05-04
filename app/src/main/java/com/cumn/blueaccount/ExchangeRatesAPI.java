package com.cumn.blueaccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ExchangeRatesAPI {

    public static void main(String[] args) {
        List<String> divisas = getDivisasDisponibles();
        System.out.println("Divisas disponibles en ExchangeRates API:");
        for (String divisa : divisas) {
            System.out.println(divisa);
        }
    }

    public static List<String> getDivisasDisponibles() {
        List<String> divisas = new ArrayList<>();

        try {
            URL url = new URL("https://v6.exchangerate-api.com/v6/b81434a917841a6199be1cc7/latest/EUR");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Error al obtener las divisas disponibles. Código de respuesta: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                String json = scanner.useDelimiter("\\Z").next();
                scanner.close();
                JSONObject jsonObject = new JSONObject(json);
                JSONObject rates = jsonObject.getJSONObject("conversion_rates");
                Iterator<String> keys = rates.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    divisas.add(key);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return divisas;
    }
}