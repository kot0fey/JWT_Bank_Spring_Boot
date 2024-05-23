package com.kotofey.jwt_spring_boot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.kotofey.jwt_spring_boot.service.ControllerService;
import com.kotofey.jwt_spring_boot.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserControllerTest {

    @Autowired
    private UserController userController;
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private MockMvc mockMvc;
    private static String jwtToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController, authenticationController).build();
    }

    @Test
    @Order(1)
    void createUsers() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "    \"login\": \"login1\",\n" +
                                 "    \"email\": \"email1\",\n" +
                                 "    \"phoneNumber\": \"phoneNumber1\",\n" +
                                 "    \"password\": \"password\",\n" +
                                 "    \"deposit\": 100,\n" +
                                 "    \"lastName\": \"lastName\",\n" +
                                 "    \"firstName\": \"firstName\",\n" +
                                 "    \"middleName\": \"middleName\",\n" +
                                 "    \"dateOfBirth\": \"09.07.2002\"\n" +
                                 "}"))
                .andExpect(
                        status().isOk()
                )
                .andDo(result -> jwtToken = result.getResponse()
                        .getContentAsString()
                );
        jwtToken = JsonPath.read(jwtToken, "$.token");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "    \"login\": \"login2\",\n" +
                                 "    \"email\": \"email2\",\n" +
                                 "    \"phoneNumber\": \"phoneNumber2\",\n" +
                                 "    \"password\": \"password\",\n" +
                                 "    \"deposit\": 100,\n" +
                                 "    \"lastName\": \"lastName\",\n" +
                                 "    \"firstName\": \"firstName\",\n" +
                                 "    \"middleName\": \"middleName\",\n" +
                                 "    \"dateOfBirth\": \"09.07.2002\"\n" +
                                 "}"))
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void sendMoney() throws Exception {
        mockMvc.perform(post("/api/v1/user/sendMoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "    \"username\" : \"login2\",\n" +
                                 "    \"amount\" : 100.0\n" +
                                 "}")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void sendMoneyTooMuch() throws Exception {
        mockMvc.perform(post("/api/v1/user/sendMoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "    \"username\" : \"login2\",\n" +
                                 "    \"amount\" : 10000.0\n" +
                                 "}")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void sendMoneyToNonExistentUser() throws Exception {
        mockMvc.perform(post("/api/v1/user/sendMoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "    \"username\" : \"no such login\",\n" +
                                 "    \"amount\" : 10000.0\n" +
                                 "}")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void sendMoneyToYourself() throws Exception {
        mockMvc.perform(post("/api/v1/user/sendMoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "    \"username\" : \"login1\",\n" +
                                 "    \"amount\" : 10000.0\n" +
                                 "}")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(
                        status().isBadRequest()
                );
    }

    /*
    OK
    too much
    non existent user
    to yourself
     */
}
