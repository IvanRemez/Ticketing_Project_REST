package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    static String token;

    static UserDTO manager;
    static ProjectDTO project;
    // ^^ have to be STATIC to use in @BeforeAll method

    @BeforeAll
    static void setUp() {

        token = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlbjlNZTM4LVJ5UXkzODBySXFJdk9CSjRlUmUxS1I0RjdOTm5yLVNHUDBrIn0.eyJleHAiOjE2ODc1NjA1NDUsImlhdCI6MTY4NzU0MjU0NSwianRpIjoiY2QzMTRiOTctMDc2ZC00ZDE2LWEwNmItMjZlZWVmYzc3ZTYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2N5ZGVvLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI1Y2ViYjc4ZS0zOTFmLTRlNDUtOWEyOS00ZDEzNWZlMjE0ODUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0aWNrZXRpbmctYXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjQxNWM1MWQ2LTlmODYtNDI0Yi1iMWMyLWU4OWE0OTdmOGZhOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgxIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtY3lkZW8tZGV2IiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0aWNrZXRpbmctYXBwIjp7InJvbGVzIjpbIk1hbmFnZXIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJzaWQiOiI0MTVjNTFkNi05Zjg2LTQyNGItYjFjMi1lODlhNDk3ZjhmYTkiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoib3p6eSJ9.XlxJneE1GAZneGlbICkFXtpA9gaa-ZU5dOQMm7ddLuzKXR4PJF6C-JSQyOXDXdQe2D-fyEWJoHrylzQDOKV8g6L_Po7SD6Vo-JJrYgXY2gvodqElvtC0rsCFYIZH1h0QA1lXvHKRwEXZiba6DvxEPMvzmmqxeAoKSGVJ5z_J8ZWTPJbyQTPQ2PwXf6wcYBXflNnhQngAtl_8rZwyM3MF005RE_93-VePwXSRUM0MDQTGyCwZcMlhGgJsgDxmU31Q6N-DJ_4SnKHf0LyrHa_0JOProOfcGOR_RGaCcM448AR3K1I8oC2o90Ex5cX_hdvyDg79I7SRppLzGSNY_PnvyQ";

        manager = new UserDTO(2L, "", "", "ozzy",
                "abc1", "", true, "",
                new RoleDTO(2L, "Manager"), Gender.MALE);

        project = new ProjectDTO("API Project", "PR001",
                manager, LocalDate.now(), LocalDate.now().plusDays(5),
                "Some details", Status.OPEN);
    }

    @Test
    void givenNoToken_getProjects() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project"))
                .andExpect(status().is4xxClientError());
        // ^^ 400 error status expected b/c Token is not passed
    }

    @Test
    void givenToken_getProjects() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].projectCode").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isNotEmpty())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isString())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").value("ozzy")
                );
    }

    @Test
    void givenToken_createProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/project")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON) // since post requires a RequestBody
                .accept(MediaType.APPLICATION_JSON)     // response type
                .content(toJsonString(project)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Project created"));
    }

    @Test
    void givenToken_updateProject() throws Exception {

        project.setProjectName("API Project-2");

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/project")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJsonString(project)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project updated"));
    }

    @Test
    void givenToken_deleteProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/project/" + project.getProjectCode())
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project deleted"));
    }

    private String toJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());  // 2022,12,18 -> 2022/12/18
        return objectMapper.writeValueAsString(obj);
    }
    // ^^ Converts Object to JSON String format

}