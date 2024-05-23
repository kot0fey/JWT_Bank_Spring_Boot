package com.kotofey.jwt_spring_boot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
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
                        .content("""
                                {
                                    "login": "login1",
                                    "email": "email1",
                                    "phoneNumber": "phoneNumber1",
                                    "password": "password",
                                    "deposit": 100,
                                    "lastName": "lastName",
                                    "firstName": "firstName",
                                    "middleName": "middleName",
                                    "dateOfBirth": "09.07.2002"
                                }"""))
                .andExpect(
                        status().isOk()
                )
                .andDo(result -> jwtToken = result.getResponse()
                        .getContentAsString()
                );
        jwtToken = JsonPath.read(jwtToken, "$.token");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "login2",
                                    "email": "email2",
                                    "phoneNumber": "phoneNumber2",
                                    "password": "password",
                                    "deposit": 100,
                                    "lastName": "lastName",
                                    "firstName": "firstName",
                                    "middleName": "middleName",
                                    "dateOfBirth": "09.07.2002"
                                }"""))
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void sendMoney() throws Exception {
        mockMvc.perform(post("/api/v1/user/sendMoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username" : "login2",
                                    "amount" : 100.0
                                }""")
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
                        .content("""
                                {
                                    "username" : "login2",
                                    "amount" : 10000.0
                                }""")
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
                        .content("""
                                {
                                    "username" : "no such login",
                                    "amount" : 10000.0
                                }""")
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
                        .content("""
                                {
                                    "username" : "login1",
                                    "amount" : 10000.0
                                }""")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(
                        status().isBadRequest()
                );
    }

}
