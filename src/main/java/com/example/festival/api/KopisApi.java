package com.example.festival.api;

import com.example.festival.domain.Musical;
import com.example.festival.dto.MusicalDetailResponseDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.XML;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class KopisApi {

    @Value("${external.kopis}")
    private String apiKey;

    private static final int ROWS_PER_PAGE = 100;

    public List<Musical> fetchAndParseMusicals(int cpage){
        StringBuffer result = new StringBuffer();
        String jsonPrintStr = null;
        List<Musical> musicals = new CopyOnWriteArrayList<>();

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.MONTH,1);

            String startDate =  sdf.format(currentDate);
            String endDate =  sdf.format(calendar.getTime());

            String urlStr = "http://www.kopis.or.kr/openApi/restful/pblprfr?service=" +
                    apiKey +
                    "&stdate=" + startDate +
                    "&eddate=" + endDate +
                    "&cpage=" + cpage +
                    "&rows=" + ROWS_PER_PAGE +
                    "&prfstate=02&signgucode=11&shcate=GGGA";

            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            /* XML to JSON 파싱 */
            BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            org.json.JSONObject xmlToJson = XML.toJSONObject(result.toString());

            // 2. org.json.JSONObject -> String으로 변환 (로그용 변수에도 할당)
            jsonPrintStr = xmlToJson.toString();
            log.info("KOPIS JSON Response: {}", jsonPrintStr);

            /* 파싱 및 리스트 추가 */
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(jsonPrintStr);
            JSONObject jsonObject1 = (org.json.simple.JSONObject) obj;
            JSONObject parseResult = (org.json.simple.JSONObject) jsonObject1.get("dbs");

            Object dbObject = parseResult.get("db");
            JSONArray parseMusicalList = new JSONArray();

            if (dbObject instanceof org.json.simple.JSONObject) {
                // 결과가 1개인 경우
                parseMusicalList.add(dbObject);
            } else if (dbObject instanceof JSONArray) {
                // 결과가 2개 이상인 경우
                parseMusicalList = (JSONArray) dbObject;
            }

            ObjectMapper objMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            for(int i = 0; i < parseMusicalList.size(); i++){
                JSONObject parseMusical = (JSONObject) parseMusicalList.get(i);
                Musical musical = objMapper.readValue(parseMusical.toString(), Musical.class);
                musicals.add(musical);
            }

        }
        catch (Exception e){
            if (e instanceof NullPointerException || e instanceof ParseException) {
                log.warn("KOPIS API 파싱 중 Null 또는 ParseException 발생 (데이터 끝으로 간주): cpage={}", cpage);
            } else {
                log.error("KOPIS API 호출 및 파싱 중 오류 발생: cpage={}", cpage, e);
            }
        }
        return musicals;
    }

    public MusicalDetailResponseDto fetchMusicalDetail(String mt20id) {
        StringBuffer result = new StringBuffer();
        String jsonPrintStr = null;

        try {
            // [신규] 상세 조회 API 엔드포인트
            String urlStr = "http://www.kopis.or.kr/openApi/restful/pblprfr/" +
                    mt20id +
                    "?service=" +
                    apiKey;// mt20id로 조회

            log.debug("KOPIS Detail API Request URL: {}", urlStr);

            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            // ... (BufferedReader로 result에 XML 담기) ...
            BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            // 1. XML -> org.json.JSONObject
            org.json.JSONObject xmlToJson = XML.toJSONObject(result.toString());

            // 2. String으로 변환
            jsonPrintStr = xmlToJson.toString();
            log.info("KOPIS Detail JSON Response: {}", jsonPrintStr);

            // 3. String -> org.json.simple.JSONObject
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(jsonPrintStr);
            JSONObject jsonObject1 = (JSONObject) obj;
            JSONObject parseResult = (JSONObject) jsonObject1.get("dbs");

            // [신규] 상세 API는 "db"가 객체 1개이므로 배열 처리가 필요 없음
            JSONObject musicalDetailJson = (JSONObject) parseResult.get("db");

            ObjectMapper objMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // 4. JSON -> MusicalDetailResponseDto로 *바로* 변환
            MusicalDetailResponseDto detailDto = objMapper.readValue(musicalDetailJson.toString(), MusicalDetailResponseDto.class);

            return detailDto;

        } catch (Exception e) {
            log.error("KOPIS 상세 API 호출 및 파싱 중 오류 발생: mt20id={}", mt20id, e);
            return null;
        }
    }
}
