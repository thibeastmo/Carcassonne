package be.kdg.backend_game.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.*;

@SpringBootTest
class MailserviceTest {
    @Autowired
    Mailservice mailservice;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void testSendingMail() throws MessagingException {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, true);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        mailservice.sendEmail(to, subject, body);
        verify(javaMailSender).send(mimeMessage);
    }
}