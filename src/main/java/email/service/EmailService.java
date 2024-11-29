package email.service;

import email.model.Email;
import email.repository.EmailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    public Email save(Email email) {
        return emailRepository.save(email);
    }

    public Email getEmailById(Integer id) {
        return emailRepository.getById(id);
    }

    public void delete(Integer setId) {
        emailRepository.deleteById(setId);
    }

}
