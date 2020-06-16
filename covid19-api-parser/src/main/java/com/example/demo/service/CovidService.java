package com.example.demo.service;

import com.example.demo.entity.CovidResponse;
import com.example.demo.entity.allRequestApi.CovidApiAllData;
import com.example.demo.entity.currentRequestApi.CovidApiCurrentData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.aspects.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CovidService {

    private static final String BASE_COVID_URL = "https://api.covid19api.com";
    private static final String CURRENT_DAY_DATA_URL = BASE_COVID_URL + "/summary";
    private static final String ALL_DATA_URL = BASE_COVID_URL + "/all";

    @Autowired
    KafkaService kafkaService;

    HttpURLConnection con;

    ObjectMapper mapper = new ObjectMapper();


    public void getCurrentData() {

        StringBuffer content = getContent(CURRENT_DAY_DATA_URL);

        CovidApiCurrentData covidApiResponse = null;
        try {
            covidApiResponse = mapper.readValue(content.toString(), CovidApiCurrentData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<CovidResponse> countryList = new ArrayList<>();
        covidApiResponse.getCountries().forEach(cntr -> {
            CovidResponse country = CovidResponse.map(cntr);
            countryList.add(country);
        });
        System.out.println( "Current data " + countryList.size());
        kafkaService.sendToTopic(countryList);

    }

    public void getAllData() {
        StringBuffer content = getContent(ALL_DATA_URL);

        String wrappedContent = "{\n" +
                "  \"countries\": \n" +
                content + "}";

        CovidApiAllData covidApiResponse = null;
        try {
            covidApiResponse = mapper.readValue(wrappedContent, CovidApiAllData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<CovidResponse> countryList = new ArrayList<>();
        covidApiResponse.getCountries().forEach(cntr -> {
            CovidResponse country = CovidResponse.map(cntr);
            countryList.add(country);
        });
        System.out.println( "all data " + countryList.size());
        kafkaService.sendToTopic(countryList);
    }

    @Cacheable(unit = TimeUnit.HOURS)
    public StringBuffer getContent(String apiUrl) {
        StringBuffer content = new StringBuffer();
        try {
            URL url = new URL(apiUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }
        return content;
    }
}
