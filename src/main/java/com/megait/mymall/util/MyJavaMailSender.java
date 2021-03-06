package com.megait.mymall.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

@Component
@Profile("dev")
// 개발환경에서만 사용하겠다~ 현재는 테스트해야하니까 프로파일에서 액티브해주기
@Slf4j
public class MyJavaMailSender implements JavaMailSender {
    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {

    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {

    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        log.info("\nTo. {}\nTitle : {}\nText : {}\nFrom.{}",
                simpleMessage.getTo(),
                simpleMessage.getSubject(),
                simpleMessage.getText(),
                simpleMessage.getFrom());
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }
}
