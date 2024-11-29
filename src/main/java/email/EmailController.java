package email;

import email.model.EmailForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmailController {

    @GetMapping("/")
    public String indexForm(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "index";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@ModelAttribute EmailForm emailForm, Model model) {
        if (emailForm.getTo().isEmpty()) {
            model.addAttribute("error", "'Кому' не должно быть пустым");
            return "index";
        }
        return "result";
    }

}
