package com.noble.noble.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class OpenAPIService {
    private static final LocalDate BASE_DATE = LocalDate.of(2022, 11, 25);
    private static final String MASTER_URL = "https://public.api.nexon.com/openapi/maplestory/v1/cube-use-results";
    private static final String BLACK_CUBE = "블랙 큐브";
    private static final String RED_CUBE = "레드 큐브";
            
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCubeCount(Map<String, Object> param) throws IOException {
        LocalDate list = BASE_DATE;
        LocalDate today = LocalDate.now();
        //int compare = today.compareTo(BASE_DATE);
        int compare = (int)ChronoUnit.DAYS.between(BASE_DATE, today);
        System.out.println(compare);
        
        String itemName = (String) param.get("item");
        List<Map<String, Object>> cubeList = new ArrayList<>();
        for (int i = 0; i < compare; i++) {
            String urlDate = list.plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            URL url = new URL(MASTER_URL + "?count=1000&date=" + urlDate);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authorization", (String)param.get("token"));
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            
            conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String sb = br.readLine();
            
            br.close();
            conn.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> totalMap = objectMapper.readValue(sb, Map.class);
            List<Map<String, Object>> cubeHistories = (List<Map<String, Object>>) totalMap.get("cube_histories");
            String cursor = (String)totalMap.get("next_cursor");

            while (cursor != "") {    
                Map<String, Object> nextTotalMap = getNextCursor((String)param.get("token"), cursor);
                cursor = (String)nextTotalMap.get("next_cursor");
                List<Map<String, Object>> nextCubeHistories = (List<Map<String, Object>>) nextTotalMap.get("cube_histories");
                for (Map<String, Object> cubeHistory : nextCubeHistories) {
                    cubeHistories.add(cubeHistory);
                }
            }

            for (Map<String, Object> cubeHistory : cubeHistories) {
                //System.out.println(cubeHistory.get("target_item"));
                if (cubeHistory.get("target_item").equals(itemName)) {
                    if (cubeList.size() == 0) {
                        System.out.println("null");
                        Map<String, Object> newCube = new HashMap<>();
                        newCube.put("charName", cubeHistory.get("character_name"));
                        newCube.put("item", cubeHistory.get("target_item"));
                        if (cubeHistory.get("cube_type").equals(BLACK_CUBE)) {
                            newCube.put("blackCube", 1);
                            newCube.put("redCube", 0);
                        } else if (cubeHistory.get("cube_type").equals(RED_CUBE)) {
                            newCube.put("blackCube", 0);
                            newCube.put("redCube", 1);
                        }
                        cubeList.add(newCube);
                    } else {
                        for (int j = 0; j < cubeList.size(); j++) {
                            Map<String, Object> cube = cubeList.get(j);
                            if (cube.get("charName").equals(cubeHistory.get("character_name")) && cube.get("item").equals(cubeHistory.get("target_item"))) {
                                System.out.println("있음");
                                if (cubeHistory.get("cube_type").equals(BLACK_CUBE)) {
                                    cube.put("blackCube", (int)cube.get("blackCube") + 1);
                                } else if (cubeHistory.get("cube_type").equals(RED_CUBE)) {
                                    cube.put("redCube", (int)cube.get("redCube") + 1);
                                }
                            } else if (!cube.get("charName").equals(cubeHistory.get("character_name")) && cube.get("item").equals(cubeHistory.get("target_item"))){
                                System.out.println("없음");
                                Map<String, Object> newCube = new HashMap<>();
                                newCube.put("charName", cubeHistory.get("character_name"));
                                newCube.put("item", cubeHistory.get("target_item"));
                                if (cubeHistory.get("cube_type").equals(BLACK_CUBE)) {
                                    newCube.put("blackCube", 1);
                                    newCube.put("redCube", 0);
                                } else if (cubeHistory.get("cube_type").equals(RED_CUBE)) {
                                    newCube.put("blackCube", 0);
                                    newCube.put("redCube", 1);
                                }
                                cubeList.add(newCube);
                                break;
                            } else {
                                continue;
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return cubeList;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getNextCursor(String token, String cursor) throws IOException {
        URL url = new URL(MASTER_URL + "?count=1000&cursor=" + cursor);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("authorization", token);
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(1000);
        
        conn.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String sb = br.readLine();
        
        br.close();
        conn.disconnect();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> totalMap = objectMapper.readValue(sb, Map.class);

        return totalMap;
    }
}
