package be.kdg.backend_game.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(0)
    void creatingAccountShouldSucceedWithCorrectNickname() throws Exception {
        mockMvc.perform(post("/api/account")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority(("player"))))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "ec3fa735-44f0-4cdb-a930-d965ad7c6e69")
                                        .claim(StandardClaimNames.GIVEN_NAME, "user")
                                        .claim(StandardClaimNames.FAMILY_NAME, "user")
                                        .claim(StandardClaimNames.PREFERRED_USERNAME, "user")
                                        .claim(StandardClaimNames.EMAIL, "user@user.be"))).accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("nickname").value("user"))
                .andExpect(jsonPath("subjectId").value("ec3fa735-44f0-4cdb-a930-d965ad7c6e69"))
        ;
    }

    @Test
    @Order(1)
    void updatingAccountShouldUpdateData() throws Exception {
        mockMvc.perform(post("/api/account")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority(("player"))))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "ec3fa735-44f0-4cdb-a930-d965ad7c6e69")
                                        .claim(StandardClaimNames.GIVEN_NAME, "user")
                                        .claim(StandardClaimNames.FAMILY_NAME, "user")
                                        .claim(StandardClaimNames.PREFERRED_USERNAME, "user2")
                                        .claim(StandardClaimNames.EMAIL, "user@user.be"))).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("user2"))
                .andExpect(jsonPath("$.subjectId").value("ec3fa735-44f0-4cdb-a930-d965ad7c6e69"));
    }
}


