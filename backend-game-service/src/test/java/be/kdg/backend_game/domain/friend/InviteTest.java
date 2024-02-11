package be.kdg.backend_game.domain.friend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InviteTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testSendInviteReturnCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/lobby/send-invite")
                        .param("lobbyId", "1bc3f9bb-6e16-4526-a82a-af8c25604ef7")
                        .param("accountId", "c47e8e47-92d7-4ee9-a77d-3f6a3d63e289")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inviteId").exists())
                .andExpect(jsonPath("$.nickname").value("Pavelski"))
                .andExpect(jsonPath("$.lobbyName").value("lobby1"));
    }
    @Test
    @Order(2)
    public void testGetInvitesShouldReturnOkAndList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-invites")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "58540443-4003-49e6-9b4c-336f2e915a34"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].inviteId").exists())
                .andExpect(jsonPath("[0].nickname").value("Pavelski"))
                .andExpect(jsonPath("[0].lobbyName").value("lobby1"))
                .andExpect(jsonPath("[0].gameTypeEnum").value("SHORT"))
                .andExpect(jsonPath("[0].maxPlayers").value(4));
    }
    @Test
    @Order(3)
    public void testDeleteInviteShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-invites")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "58540443-4003-49e6-9b4c-336f2e915a34"))))
                .andDo(result -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
                    String inviteId = rootNode.get(0).at("/inviteId").asText();
                    mockMvc.perform(MockMvcRequestBuilders.delete("/api/lobby/delete-invite?inviteId=" + inviteId)
                                    .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                            .jwt(jwt -> jwt
                                                    .claim(StandardClaimNames.SUB, "58540443-4003-49e6-9b4c-336f2e915a34"))))
                            .andExpect(status().isOk());
                });
    }
    @Test
    @Order(4)
    public void testAcceptInviteShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/lobby/send-invite")
                        .param("lobbyId", "1bc3f9bb-6e16-4526-a82a-af8c25604ef7")
                        .param("accountId", "c47e8e47-92d7-4ee9-a77d-3f6a3d63e289")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5"))))
                .andDo(result -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
                    String inviteId = rootNode.at("/inviteId").asText();
                    mockMvc.perform(MockMvcRequestBuilders.post("/api/lobby/accept-invite?inviteId=" + inviteId)
                                    .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                            .jwt(jwt -> jwt
                                                    .claim(StandardClaimNames.SUB, "58540443-4003-49e6-9b4c-336f2e915a34"))))
                            .andExpect(status().isOk());
                });
    }
}
