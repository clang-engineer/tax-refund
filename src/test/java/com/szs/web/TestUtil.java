package com.szs.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class TestUtil {
    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    public static Map<String, Object> getMapFromJsonObject(JSONObject jsonObj) {
        Map<String, Object> map = null;

        try {
            map = new ObjectMapper().readValue(jsonObj.toString(), Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static JSONObject createScrapJSONObject() throws Exception {
        JSONObject result = new JSONObject();

        result.put("appVer", "test-appVer");
        result.put("hostNm", "test-hostNm");
        result.put("workerReqDt", "2022-01-11T04:59:59.731688");
        result.put("workerResDt", "2022-01-11T04:59:59.731688");

        JSONObject jsonListObject = new JSONObject();


        jsonListObject.put("errMsg", "test-errMsg");
        jsonListObject.put("company", "test-company");
        jsonListObject.put("svcCd", "test-svcCd");
        jsonListObject.put("userId", "1");

        JSONArray scrap001List = new JSONArray();
        JSONObject scrap001Object = new JSONObject();
        scrap001Object.put("소득내역", "급여");
        scrap001Object.put("총지급액", 123456789);
        scrap001Object.put("업무시작일", "2020.01.01");
        scrap001Object.put("기업명", "(주)활빈당");
        scrap001Object.put("이름", "홍길동");
        scrap001Object.put("지급일", "2020.01.02");
        scrap001Object.put("업무종료일", "2020.01.03");
        scrap001Object.put("주민등록번호", "911111-1234567");
        scrap001Object.put("소득구분", "근로소득");
        scrap001Object.put("사업자등록번호", "123-12-123456");
        scrap001List.put(scrap001Object);
        jsonListObject.put("scrap001", scrap001List);

        JSONArray scrap002List = new JSONArray();
        JSONObject scrap002Object = new JSONObject();
        scrap002Object.put("총사용금액", "200000");
        scrap002Object.put("소득구분", "산출세액");
        scrap002List.put(scrap002Object);
        jsonListObject.put("scrap002", scrap002List);

        result.put("jsonList", jsonListObject);

        return result;
    }
}
