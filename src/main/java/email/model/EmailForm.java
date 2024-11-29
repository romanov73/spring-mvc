package email.model;

public class EmailForm {
    private String subject;
    private String to;
    private String message;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmailForm{" +
                "subject='" + subject + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
