package com.readingbooks.web.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    private final JavaMailSender javaMailSender;

    /**
     * 임시 비밀번호를 전송하는 메소드
     * @param toEmail
     * @param tempPassword
     */
    @Override
    public void send(String toEmail, String tempPassword) {
        MimeMessage message = createMessage(toEmail, tempPassword);
        javaMailSender.send(message);
    }

    private MimeMessage createMessage(String toEmail, String tempPassword){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("[리딩북스] 비밀번호 변경 결과입니다.");

            String sendMessage = "";
            sendMessage += "<h2>임시 비밀번호 재설정 결과</h2>";
            sendMessage += "<strong>임시 비밀번호 : </strong>";
            sendMessage += "<span>"+tempPassword+"</span>";
            message.setText(sendMessage, "utf-8", "html");

        } catch (MessagingException e) {
            throw new EmailException(e.getMessage());
        }

        return message;
    }
}
