package com.readingbooks.web.service.mail;

public interface MailService {
    void send(String toEmail, String tempPassword);
}
