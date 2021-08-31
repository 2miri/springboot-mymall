package com.megait.mymall.service;

import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    //드래그 우클릭해서 이사시켰음
    @Transactional
    public void sendEmail(Member member) {

        // TOKEN 생성
        String token = UUID.randomUUID().toString();

        // TOKEN 을 DB에 update
        member.setEmailCheckToken(token);

        // sendSimpleMailMessage(member);
        sendHtmlMailMessage(member);


    }

    private void sendHtmlMailMessage(Member member) {
        // 이메일 날리기 (html으로 꾸민 버전)
        String html = "<html><body>" +
                "<p style=\"background:red\">링크 : <a href=\"http://localhost:8080/email-check?email=" + member.getEmail()
                + "&token=" + member.getEmailCheckToken() + "\">이메일 인증을 원하시는 경우 이곳을 클릭하세요.</a></p>" +
                "</body></html>";

        // html 페이지를 보낼때는 일반 메세지가 아니라, MIME 타입의 메세지를 보내야함
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, false, "UTF-8");
            // false = 멀티포트 (파일 다운로드도 같이해야하니?)

            String title = "[My Mall] 회원 가입에 감사드립니다. 딱 한 가지 과정이 남았습니다!";
            mimeMessageHelper.setTo(member.getEmail());
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(html, true);
            // true라고 써줘야지 반드시 html으로 날아간다.

            javaMailSender.send(mimeMessage);

        }catch (MessagingException e){
            log.error("이메일 전송 실패. ({})", member.getEmail());
        }
    }

    private void sendSimpleMailMessage(Member member){
        // 이메일 날리기 (간단한버전)
        String url = "http://localhost:8080/email-check?email="+member.getEmail()
                +"&token="+member.getEmailCheckToken();
        String title = "[My Mall] 회원 가입에 감사드립니다. 딱 한 가지 과정이 남았습니다!";
        String message ="다음 링크를 브라우저에 붙여넣어주세요. 링크 : " + url;
        String sender = "mymall-admin-norely@mymall.com";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(member.getEmail());
        mailMessage.setSubject(title); // 제목
        mailMessage.setText(message); // 본문
        mailMessage.setFrom(sender);
        javaMailSender.send(mailMessage);
    }
}
