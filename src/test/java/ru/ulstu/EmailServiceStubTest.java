package ru.ulstu;

import email.model.Email;
import email.repository.EmailRepository;
import email.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceStubTest {
    @Mock
    private EmailRepository emailRepository; // MOCK - для проверки взаимодействий

    @InjectMocks
    private EmailService emailService;

    private Email testEmail;
    private Email testEmail2;

    @BeforeEach
    void setUp() {
        testEmail = new Email();
        testEmail.setId(1);
        testEmail.setSubject("Test Subject");
        testEmail.setRecipient("test@example.com");

        testEmail2 = new Email();
        testEmail2.setId(2);
        testEmail2.setSubject("Test Subject 2");
        testEmail2.setRecipient("test2@example.com");
    }


    @Test
    @DisplayName("getAllEmails - должен возвращать список всех email (STUB)")
    void getAllEmails_ShouldReturnListOfEmails() {
        List<Email> expectedEmails = Arrays.asList(testEmail, testEmail2);
        when(emailRepository.findAll()).thenReturn(expectedEmails);
        List<Email> actualEmails = emailService.getAllEmails();
        assertThat(actualEmails)
                .isNotNull()
                .hasSize(2)
                .containsExactly(testEmail, testEmail2);
        verify(emailRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllEmails - должен возвращать пустой список (STUB)")
    void getAllEmails_ShouldReturnEmptyList() {
        when(emailRepository.findAll()).thenReturn(List.of());
        List<Email> actualEmails = emailService.getAllEmails();
        assertThat(actualEmails).isNotNull().isEmpty();
        verify(emailRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getEmailById - должен возвращать email по ID (STUB)")
    void getEmailById_ShouldReturnEmail() {
        when(emailRepository.getById(1)).thenReturn(testEmail);
        Email result = emailService.getEmailById(1);
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("subject", "Test Subject");

        verify(emailRepository, times(1)).getById(1);
    }
}
