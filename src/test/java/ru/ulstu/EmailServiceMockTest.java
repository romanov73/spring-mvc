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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceMockTest {
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
    @DisplayName("save - должен вызывать repository.save и возвращать сохраненный email")
    void save_ShouldCallRepositorySaveAndReturnSavedEmail() {
        when(emailRepository.save(any(Email.class))).thenReturn(testEmail);
        Email result = emailService.save(testEmail);
        verify(emailRepository, times(1)).save(testEmail);
        verifyNoMoreInteractions(emailRepository);
        assertThat(result).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("delete - должен вызывать repository.deleteById")
    void delete_ShouldCallRepositoryDeleteById() {
        Integer emailId = 1;
        emailService.delete(emailId);
        verify(emailRepository, times(1)).deleteById(emailId);
        verifyNoMoreInteractions(emailRepository);
    }

    @Test
    @DisplayName("delete - должен корректно обрабатывать вызов даже при передаче null")
    void delete_ShouldHandleNullId() {
        Integer nullId = null;
        assertThrows(NullPointerException.class, () -> emailService.delete(nullId));
    }
}
