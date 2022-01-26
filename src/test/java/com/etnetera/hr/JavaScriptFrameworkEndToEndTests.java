package com.etnetera.hr;

import com.etnetera.hr.dto.JSFrameworkDto;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class used for Spring Boot/MVC based tests.
 *
 * @author Etnetera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JavaScriptFrameworkEndToEndTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JavaScriptFrameworkService service;

    @Test
    public void givenFramework_whenGetFrameworks_thenReturn200() throws Exception {
        createFramework("test framework", "2022-01-01", 10, List.of("0.0.1", "1.0.0"));
        createFramework("test framework2", "2023-01-01", 20, List.of("1.0.0", "2.0"));

        mvc.perform(get("/frameworks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("test framework")))
                .andExpect(jsonPath("$[1].name", is("test framework2")));
    }

    @Test
    public void whenCreateFramework_thenReturnFrameworkAndLocation() throws Exception {
        mvc.perform(post("/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"Framework1\"," +
                                "    \"version\": [" +
                                "        \"0.0.1\"," +
                                "        \"1.0.0\"" +
                                "    ]," +
                                "    \"deprecationDate\": \"2022-01-01\"," +
                                "    \"hypeLevel\": 10" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Framework1")));
    }

    @Test
    public void givenFrameworkId_whenCreateFramework_thenReturnBadRequest() throws Exception {
        mvc.perform(post("/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"id\": 1,"+
                                "    \"name\": \"Framework1\"," +
                                "    \"version\": [" +
                                "        \"0.0.1\"," +
                                "        \"1.0.0\"" +
                                "    ]," +
                                "    \"deprecationDate\": \"2022-01-01\"," +
                                "    \"hypeLevel\": 10" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenEmptyBody_whenCreateFramework_thenReturnBadRequest() throws Exception {
        mvc.perform(post("/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFramework_whenFrameworkById_thenReturnFramework() throws Exception {
        JSFrameworkDto framework = createFramework("framework", "2022-01-01", 0, List.of("1"));
        mvc.perform(get("/frameworks/{id}", framework.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("framework")));
    }

    @Test
    public void givenFramework_whenFrameworkById_thenReturnNotFound() throws Exception {
        mvc.perform(get("/frameworks/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void givenFramework_whenDeleteFramework_thenReturnFramework() throws Exception {
        JSFrameworkDto framework = createFramework("framework", "2022-01-01", 0, List.of("1"));
        mvc.perform(delete("/frameworks/{id}", framework.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("framework")));

        assertThat(service.getAllFrameworks(), is(empty()));
    }

    @Test
    public void givenFramework_whenDeleteFramework_thenNotFound() throws Exception {
        createFramework("framework", "2022-01-01", 0, List.of("1"));
        mvc.perform(delete("/frameworks/{id}", 2))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

        assertThat(service.getAllFrameworks(), is(not(empty())));
    }

    @Test
    public void givenFramework_whenEditFramework_thenReturnEditedFramework() throws Exception {
        createFramework("framework", "2022-01-01", 0, List.of("1"));
        mvc.perform(put("/frameworks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"name\": \"new name\"," +
                                "    \"version\": [" +
                                "        \"2\"" +
                                "    ]," +
                                "    \"deprecationDate\": \"2023-01-01\"," +
                                "    \"hypeLevel\": 10" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("new name")))
                .andExpect(jsonPath("$.version", iterableWithSize(1)))
                .andExpect(jsonPath("$.version", contains("2")))
                .andExpect(jsonPath("$.deprecationDate", is("2023-01-01")))
                .andExpect(jsonPath("$.hypeLevel", is(10)));
    }

    @Test
    public void givenFramework_whenEditFramework_thenNotFound() throws Exception {
        mvc.perform(put("/frameworks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"name\": \"new name\"," +
                                "    \"version\": [" +
                                "        \"2\"" +
                                "    ]," +
                                "    \"deprecationDate\": \"2023-01-01\"," +
                                "    \"hypeLevel\": 10" +
                                "}")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void givenEmptyBody_whenEditFramework_thenBadRequest() throws Exception {
        mvc.perform(put("/frameworks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenParamName_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework", "2022-01-01", 0, List.of("1"));
        createFramework("framework script", "2022-01-01", 0, List.of("1"));
        mvc.perform(get("/frameworks/search")
                        .param("name", "script")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(1))))
                .andExpect(jsonPath("$[0].name", is("framework script")));
    }

    @Test
    public void givenParamNameMultiple_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework", "2022-01-01", 0, List.of("1"));
        createFramework("framework script", "2022-01-01", 0, List.of("1"));
        createFramework("script", "2022-01-01", 0, List.of("1"));
        createFramework("totally random name", "2022-01-01", 0, List.of("1"));
        mvc.perform(get("/frameworks/search")
                        .param("name", "script")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(2))))
                .andExpect(jsonPath("$[0].name", is("framework script")))
                .andExpect(jsonPath("$[1].name", is("script")));
    }

    @Test
    public void givenParamVersion_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework1", "2022-01-01", 0, List.of("1"));
        createFramework("framework2", "2022-01-01", 0, List.of("2"));
        createFramework("framework3", "2022-01-01", 0, List.of("3"));
        mvc.perform(get("/frameworks/search")
                        .param("version", "2", "3")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(2))))
                .andExpect(jsonPath("$[0].name", is("framework2")))
                .andExpect(jsonPath("$[1].name", is("framework3")));
    }

    @Test
    public void givenParamDeprecationDateBefore_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework1", "2021-01-01", 0, List.of("1"));
        createFramework("framework2", "2022-01-01", 0, List.of("2"));
        createFramework("framework3", "2023-01-01", 0, List.of("3"));
        mvc.perform(get("/frameworks/search")
                        .param("deprecationDateBefore", "2022-02-01")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(2))))
                .andExpect(jsonPath("$[0].name", is("framework1")))
                .andExpect(jsonPath("$[1].name", is("framework2")));
    }

    @Test
    public void givenParamDeprecationDateAfter_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework1", "2021-01-01", 0, List.of("1"));
        createFramework("framework2", "2022-01-01", 0, List.of("2"));
        createFramework("framework3", "2023-01-01", 0, List.of("3"));
        mvc.perform(get("/frameworks/search")
                        .param("deprecationDateAfter", "2021-01-01")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(2))))
                .andExpect(jsonPath("$[0].name", is("framework2")))
                .andExpect(jsonPath("$[1].name", is("framework3")));
    }

    @Test
    public void givenParamDeprecationDateAfterAndBefore_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework1", "2021-01-01", 0, List.of("1"));
        createFramework("framework2", "2022-01-01", 0, List.of("2"));
        createFramework("framework3", "2023-01-01", 0, List.of("3"));
        createFramework("framework4", "2024-01-01", 0, List.of("3"));
        mvc.perform(get("/frameworks/search")
                        .param("deprecationDateBefore", "2023-02-01")
                        .param("deprecationDateAfter", "2021-01-01")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(2))))
                .andExpect(jsonPath("$[0].name", is("framework2")))
                .andExpect(jsonPath("$[1].name", is("framework3")));
    }

    @Test
    public void givenParamHypeLevelMin_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework1", "2021-01-01", 1, List.of("1"));
        createFramework("framework2", "2022-01-01", 2, List.of("2"));
        createFramework("framework3", "2023-01-01", 3, List.of("3"));
        createFramework("framework4", "2024-01-01", 4, List.of("3"));
        mvc.perform(get("/frameworks/search")
                        .param("minHypeLevel", "3")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(2))))
                .andExpect(jsonPath("$[0].name", is("framework3")))
                .andExpect(jsonPath("$[1].name", is("framework4")));
    }

    @Test
    public void givenParamHypeLevelMax_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework1", "2021-01-01", 1, List.of("1"));
        createFramework("framework2", "2022-01-01", 2, List.of("2"));
        createFramework("framework3", "2023-01-01", 3, List.of("3"));
        createFramework("framework4", "2024-01-01", 4, List.of("3"));
        mvc.perform(get("/frameworks/search")
                        .param("maxHypeLevel", "3")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(3))))
                .andExpect(jsonPath("$[0].name", is("framework1")))
                .andExpect(jsonPath("$[1].name", is("framework2")))
                .andExpect(jsonPath("$[2].name", is("framework3")));
    }

    @Test
    public void givenParamHypeLevelMinAndMax_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework1", "2021-01-01", 1, List.of("1"));
        createFramework("framework2", "2022-01-01", 2, List.of("2"));
        createFramework("framework3", "2023-01-01", 3, List.of("3"));
        createFramework("framework4", "2024-01-01", 4, List.of("3"));
        mvc.perform(get("/frameworks/search")
                        .param("minHypeLevel", "3")
                        .param("maxHypeLevel", "3")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(1))))
                .andExpect(jsonPath("$[0].name", is("framework3")));
    }

    @Test
    public void givenAllParams_whenSearchFrameworks_thenReturnFound() throws Exception {
        createFramework("framework1 lib", "2021-01-01", 1, List.of("1"));
        createFramework("framework2 lib", "2022-01-01", 2, List.of("2"));
        createFramework("framework3 lib", "2023-01-01", 3, List.of("3"));
        createFramework("framework4 lib", "2024-01-01", 4, List.of("4"));
        createFramework("framework5 lib", "2025-01-01", 5, List.of("5"));
        mvc.perform(get("/frameworks/search")
                        .param("name", "lib")
                        .param("version", "3", "4")
                        .param("deprecationDateBefore", "2024-02-01")
                        .param("deprecationDateAfter", "2022-01-01")
                        .param("minHypeLevel", "3")
                        .param("maxHypeLevel", "4")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(iterableWithSize(2))))
                .andExpect(jsonPath("$[0].name", is("framework3 lib")))
                .andExpect(jsonPath("$[1].name", is("framework4 lib")));
    }

    private JSFrameworkDto createFramework(String name, String deprecationDate, Integer hypeLevel, List<String> version) {
        return service.createFramework(JSFrameworkDto.builder()
                .name(name)
                .deprecationDate(LocalDate.parse(deprecationDate))
                .hypeLevel(hypeLevel)
                .version(version)
                .build());
    }
}
