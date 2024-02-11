package be.kdg.backend_game.domain.friend;

import be.kdg.backend_game.service.mail.Mailservice;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FriendTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Mailservice mailService;

    @Test
    @Order(1)
    public void testSendFriendRequestShouldReturnCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/send-friend-request-with-username-or-email?usernameOrEmail=thibeastmo")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5")
                                        .claim(StandardClaimNames.GIVEN_NAME, "Pavelski")
                                        .claim(StandardClaimNames.FAMILY_NAME, "Pavelski")
                                        .claim(StandardClaimNames.EMAIL, "pavel.ski@gmail.com"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.friendRequestId").exists())
                .andExpect(jsonPath("$.nickname").value("Pavelski"));
    }

    @Test
    @Order(2)
    public void testSendFriendRequestAgainShouldReturnConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/send-friend-request-with-username-or-email?usernameOrEmail=thibeastmo")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5")
                                        .claim(StandardClaimNames.GIVEN_NAME, "Pavelski")
                                        .claim(StandardClaimNames.FAMILY_NAME, "Pavelski")
                                        .claim(StandardClaimNames.EMAIL, "pavel.ski@gmail.com"))))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    public void testSendFriendRequestWithNonExistingUsernameShouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/send-friend-request-with-username-or-email?usernameOrEmail=omtsaebiht")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5")
                                        .claim(StandardClaimNames.GIVEN_NAME, "Pavelski")
                                        .claim(StandardClaimNames.FAMILY_NAME, "Pavelski")
                                        .claim(StandardClaimNames.EMAIL, "pavel.ski@gmail.com"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    public void testGetFriendRequestsShouldReturnOKAndList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-friend-requests")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].friendRequestId").exists())
                .andExpect(jsonPath("[0].nickname").value("Pavelski"));
    }

    @Test
    @Order(5)
    public void testGetFriendRequestsWithBadSubIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-friend-requests")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "2d37c982-983a-11ee-b9d1-0242ac120002"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    public void testAcceptFriendRequestShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-friend-requests")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b"))))
                .andDo(result -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
                    String friendRequestId = rootNode.get(0).at("/friendRequestId").asText();
                    mockMvc.perform(MockMvcRequestBuilders.post("/api/account/accept-friend-request?friendRequestId=" + friendRequestId)
                                    .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                            .jwt(jwt -> jwt
                                                    .claim(StandardClaimNames.SUB, "60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b"))))
                            .andExpect(status().isOk());
                });

    }

    @Test
    @Order(7)
    public void testAcceptFriendRequestWithBadIdShouldReturnConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/accept-friend-request?friendRequestId=60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b"))))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(8)
    public void testDeleteFriendRequestShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/send-friend-request-with-username-or-email?usernameOrEmail=Atrophius")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5"))))
                .andDo(result -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
                    String friendRequestId = rootNode.at("/friendRequestId").asText();
                    mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/delete-friend-request?friendRequestId=" + friendRequestId)
                                    .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                            .jwt(jwt -> jwt
                                                    .claim(StandardClaimNames.SUB, "bcf903ae-997d-466c-b4ac-410d1fe420be"))))
                            .andExpect(status().isOk());
                });
    }

    @Test
    @Order(9)
    public void testDeleteFriendRequestWithBadIdShouldReturnConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/delete-friend-request?friendRequestId=bcf903ae-997d-466c-b4ac-410d1fe420be")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "bcf903ae-997d-466c-b4ac-410d1fe420be"))))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(10)
    public void testGetFriendsShouldReturnOkAndList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-friends")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].accountId").exists())
                .andExpect(jsonPath("[0].nickname").value("Pavelski"))
                .andExpect(jsonPath("[0].experiencePoints").value(10));
    }

    @Test
    @Order(11)
    public void testDeleteFriendshipShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-friends")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b"))))
                .andDo(result -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
                    String friendId = rootNode.get(0).at("/accountId").asText();
                    mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/delete-friendship?friendId=" + friendId)
                                    .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                            .jwt(jwt -> jwt
                                                    .claim(StandardClaimNames.SUB, "60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b"))))
                            .andExpect(status().isOk());
                });
    }

    @Test
    @Order(12)
    public void testAddFriendWithEmailWithoutAccountShouldSendMailAndSucceed() throws Exception {
        doNothing().when(mailService).sendEmail(anyString(), anyString(), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/send-friend-request-with-username-or-email?usernameOrEmail=kristof.vandewalle@student.kdg.be")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5")
                                        .claim(StandardClaimNames.GIVEN_NAME, "Pavelski")
                                        .claim(StandardClaimNames.FAMILY_NAME, "Pavelski")
                                        .claim(StandardClaimNames.EMAIL, "pavel.ski@gmail.com"))))
                .andExpect(status().isCreated());
    }
}
