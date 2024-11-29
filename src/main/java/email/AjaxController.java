package email;

import email.model.Email;
import email.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/ajax")
public class AjaxController {
    private final EmailService emailService;

    public AjaxController(EmailService emailService) {
        this.emailService = emailService;
    }

    @ResponseBody
    @GetMapping("/getEmail/{id}")
    public Email getEmail(@PathVariable("id") Integer id) {
        return emailService.getEmailById(id);
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Email> getList() {
        return emailService.getAllEmails();
    }

    @ResponseBody
    @PostMapping("/saveEmail")
    public Email saveEmail(@RequestBody Email email) {
        if (email.getTo().isEmpty()) {
            throw new RuntimeException("Поле 'Кому' не должно быть пустым");
        }
        Email previousEmail = emailService.getEmailById(email.getId());
        previousEmail.setMessage(email.getMessage());
        previousEmail.setSubject(email.getSubject());
        previousEmail.setTo(email.getTo());
        return emailService.save(previousEmail);
    }
}
