package be.kdg.backend_game.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmailValidatorHelperTest {

    @Test
    void isValidEmail() {
        assertEquals(true, EmailValidatorHelper.isValidEmail("kristof@mail.com"));
        assertEquals(true, EmailValidatorHelper.isValidEmail("kristof.vandewalle@student.kdg.be"));
        assertEquals(true, EmailValidatorHelper.isValidEmail("kristof.vandewalle.1@student.kdg.be"));
        assertEquals(true, EmailValidatorHelper.isValidEmail("skullsaimer@gmail.com"));
    }

    @Test
    void isInvalidEmail() {
        assertEquals(false, EmailValidatorHelper.isValidEmail("kristof"));
        assertEquals(false, EmailValidatorHelper.isValidEmail("kristof@mail"));
        assertEquals(false, EmailValidatorHelper.isValidEmail("kristof@mail."));
        assertEquals(false, EmailValidatorHelper.isValidEmail("kristof@mail.c"));
    }
}