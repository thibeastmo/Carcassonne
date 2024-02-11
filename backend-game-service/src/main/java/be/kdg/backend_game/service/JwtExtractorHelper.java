package be.kdg.backend_game.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JwtExtractorHelper {
    public static UUID convertjwtToSubjectId(Jwt jwt) {
        String subject = jwt.getSubject();
        return UUID.fromString(subject);
    }

    public static String convertjwtToUsername(Jwt jwt) {
        return jwt.getClaimAsString("preferred_username");
    }
    public static String convertjwtToEmail(Jwt jwt) {
        return jwt.getClaimAsString("email");
    }
}
