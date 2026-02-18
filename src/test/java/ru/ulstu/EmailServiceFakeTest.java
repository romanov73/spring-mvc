package ru.ulstu;

import email.model.Email;
import email.repository.EmailRepository;
import email.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceFakeTest {
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
    @DisplayName("Интеграционный тест с Fake репозиторием")
    void integrationWithFakeRepository() {
        EmailRepository fakeRepository = new FakeEmailRepository();
        EmailService serviceWithFake = new EmailService(fakeRepository);
        Email savedEmail = serviceWithFake.save(testEmail);
        assertThat(savedEmail.getId()).isNotNull();
        List<Email> allEmails = serviceWithFake.getAllEmails();
        assertThat(allEmails).hasSize(1);
        Email foundEmail = serviceWithFake.getEmailById(savedEmail.getId());
        assertThat(foundEmail).isEqualTo(testEmail);
        serviceWithFake.delete(savedEmail.getId());
        assertThat(serviceWithFake.getAllEmails()).isEmpty();
    }

    @Test
    @DisplayName("Полный цикл операций с FAKE репозиторием")
    void fullLifecycleWithFakeRepository() {
        EmailRepository fakeRepository = new FakeEmailRepository();
        EmailService serviceWithFake = new EmailService(fakeRepository);
        Email email1 = serviceWithFake.save(testEmail);
        Email email2 = serviceWithFake.save(testEmail2);
        assertThat(serviceWithFake.getAllEmails()).hasSize(2);

        Email retrievedEmail = serviceWithFake.getEmailById(email1.getId());
        assertThat(retrievedEmail).isEqualTo(testEmail);
        serviceWithFake.delete(email1.getId());
        assertThat(serviceWithFake.getAllEmails())
                .hasSize(1)
                .containsExactly(testEmail2);
    }

    @Test
    @DisplayName("Тест с использованием ArgumentCaptor для захвата аргументов")
    void save_ShouldCaptureEmailArgument() {
        when(emailRepository.save(any(Email.class))).thenReturn(testEmail);
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        emailService.save(testEmail);
        verify(emailRepository).save(emailCaptor.capture());
        Email capturedEmail = emailCaptor.getValue();

        assertThat(capturedEmail)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("subject", "Test Subject");
    }

    private static class FakeEmailRepository implements EmailRepository {
        private final java.util.Map<Integer, Email> storage = new java.util.HashMap<>();
        private int nextId = 1;

        @Override
        public List<Email> findAll() {
            return List.copyOf(storage.values());
        }

        @Override
        public List<Email> findAll(Sort sort) {
            // Простая реализация - игнорируем сортировку
            return findAll();
        }

        @Override
        public Page<Email> findAll(Pageable pageable) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public List<Email> findAllById(Iterable<Integer> ids) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public long count() {
            return storage.size();
        }

        @Override
        public void deleteById(Integer id) {
            storage.remove(id);
        }

        @Override
        public void delete(Email entity) {
            storage.remove(entity.getId());
        }

        @Override
        public void deleteAllById(Iterable<? extends Integer> ids) {
            for (Integer id : ids) {
                storage.remove(id);
            }
        }

        @Override
        public void deleteAll(Iterable<? extends Email> entities) {
            for (Email entity : entities) {
                storage.remove(entity.getId());
            }
        }

        @Override
        public void deleteAll() {
            storage.clear();
        }

        @Override
        public <S extends Email> S save(S entity) {
            if (entity.getId() == null) {
                entity.setId(nextId++);
            }
            storage.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public <S extends Email> List<S> saveAll(Iterable<S> entities) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public Optional<Email> findById(Integer id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public boolean existsById(Integer id) {
            return storage.containsKey(id);
        }

        @Override
        public void flush() {
            // Ничего не делаем
        }

        @Override
        public <S extends Email> S saveAndFlush(S entity) {
            return save(entity);
        }

        @Override
        public <S extends Email> List<S> saveAllAndFlush(Iterable<S> entities) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public void deleteAllInBatch(Iterable<Email> entities) {
            deleteAll(entities);
        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Integer> ids) {
            deleteAllById(ids);
        }

        @Override
        public void deleteAllInBatch() {
            deleteAll();
        }

        @Override
        public Email getOne(Integer id) {
            return getById(id);
        }

        @Override
        public Email getById(Integer id) {
            return storage.get(id);
        }

        @Override
        public Email getReferenceById(Integer id) {
            return getById(id);
        }

        @Override
        public <S extends Email> Optional<S> findOne(Example<S> example) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public <S extends Email> List<S> findAll(Example<S> example) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public <S extends Email> List<S> findAll(Example<S> example, Sort sort) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public <S extends Email> Page<S> findAll(Example<S> example, Pageable pageable) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public <S extends Email> long count(Example<S> example) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public <S extends Email> boolean exists(Example<S> example) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }

        @Override
        public <S extends Email, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            throw new UnsupportedOperationException("Метод не поддерживается в Fake реализации");
        }
    }
}