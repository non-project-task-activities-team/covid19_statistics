package com.example.demo.service;

import com.example.demo.entity.CountryCode;
import com.example.demo.entity.CovidResponse;
import com.example.demo.entity.allRequestApi.TimelineItemInfo;
import com.example.demo.entity.currentRequestApi.Country;
import com.example.demo.entity.currentRequestApi.CovidApiCurrentData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.aspects.Cacheable;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CovidService {

    private static final String BASE_COVID_URL = "https://api.covid19api.com";
    private static final String CURRENT_DAY_DATA_URL = BASE_COVID_URL + "/summary";
    private static final String ALL_DATA_URL = BASE_COVID_URL + "/all";
    private static final String VIRUS_TRACKER_API_URL = "https://api.thevirustracker.com/free-api?countryTimeline=";
    private static final String COUNTRY_CODES_API = "https://api.covid19api.com/countries";

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
        System.out.println("Current data " + countryList.size());
        kafkaService.sendToTopic(countryList);

    }

    public void getAllData() {
        StringBuffer countries = getContent(COUNTRY_CODES_API);
        List<String> countryCodes = new ArrayList<>();
        try {
            List<CountryCode> coco = mapper.readValue(countries.toString(), new TypeReference<List<CountryCode>>() {
            });
            coco.forEach(c ->
                    countryCodes.add(c.getCountryCode()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<CovidResponse> countryList = new ArrayList<>();
        countryCodes.forEach(countryCode -> {
            String countryContent = getContent(VIRUS_TRACKER_API_URL + countryCode).toString();
            try {
                JSONObject jsonObject = new JSONObject(countryContent);
                String timelines = jsonObject.getString("timelineitems");
                if (!timelines.isEmpty()) {
                    List<Map<String, Object>> asd = mapper.readValue(timelines, new TypeReference<List<Map<String, Object>>>() {
                    });
                    asd.get(0).remove("stat");
                    for (Map.Entry<String, Object> entry : asd.get(0).entrySet()) {
                        LinkedHashMap<String, Integer> data = (LinkedHashMap<String, Integer>) entry.getValue();
                        String date = entry.getKey();
                        String newDailyCases = data.get("new_daily_cases").toString();
                        String newDailyDeaths = data.get("new_daily_deaths").toString();

                        CovidResponse country = CovidResponse.map(countryCode, date, newDailyCases, newDailyDeaths);
                        countryList.add(country);
                    }
                }
            } catch (JSONException e) {
            } catch (JsonMappingException e) {
            } catch (JsonProcessingException e) {
            }
        });
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
