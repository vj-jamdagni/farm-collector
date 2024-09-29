package com.farm.collector.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FarmControllerTest {

    private static final String BASE_URL = "/v1/api";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddPlanting() throws Exception {
        String jsonPayload = "{" +
                "    \"season\" : \"SPRING\"," +
                "    \"farmName\": \"MyFarm101\"," +
                "    \"crops\": [" +
                "        {" +
                "            \"name\": \"corn\"," +
                "            \"areaInAcre\": 7," +
                "            \"expectedYield\": 25" +
                "        }," +
                "        {" +
                "            \"name\": \"wheat\"," +
                "            \"areaInAcre\": 12," +
                "            \"expectedYield\": 57" +
                "        }" +
                "    ]" +
                "}";

        mockMvc.perform(post(BASE_URL + "/farm/1/plantings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(content().string("Plantation details successfully added."));
    }

    @Test
    public void testAddHarvest() throws Exception {

        testAddPlanting();      // To add testing data into the DB.

        String jsonPayload = "{\n" +
                "  \"season\": \"SPRING\",\n" +
                "  \"crops\": [\n" +
                "    {\n" +
                "      \"name\": \"corn\",\n" +
                "      \"actualYield\": 18\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"wheat\",\n" +
                "      \"actualYield\": 12\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mockMvc.perform(post(BASE_URL + "/farm/1/harvests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(content().string("Harvest data added."));
    }

}

