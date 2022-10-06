package server.yogoyogu.service.email;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.yogoyogu.entity.member.EmailAuth;
import server.yogoyogu.repository.Member.EmailAuthRepository;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender emailSender; // Bean 등록해둔 MailConfig 를 emailsender 라는 이름으로 autowired
    private final String ePw = createKey(); // 인증번호 생성 => ePw 가 불러와지면 createKey() 메서드가 실행됨

    // 메일 내용 작성
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);// to => 보내는 대상
        message.setSubject("요고요구 회원가입 이메일 인증");// 메일 제목

        // 메일 내용
        // 아래에서 메일의 subtype 을 html 로 지정해주었기 때문인지 html 문법을 사용가능하다
        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 명지대학교 요고요구</h1>";
        msgg += "<h1 style='color:blue;'>인증번호</h1> <h1> 안내 메일입니다.</h1>";
        msgg += "<br>";
        msgg += "<p>요고요구에 오신 것을 환영합니다!<p>";
        msgg += "<br>";
        msgg += "<p>해당 이메일은 회원가입을 위한 인증번호 안내 메일입니다.<p>";
        msgg += "<br>";
        msgg += "<p>하단 인증번호를 '이메일 인증번호' 칸에 입력하여 가입을 완료해주세요..<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> "; // 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("yogoyogu@naver.com", "yogoyogu"));

        return message;
    }

    // 랜덤 인증 코드 생성
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97)); // 영어 소문자
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65)); // 영어 대문자
                    // A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10))); // 숫자
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    // 메일 발송
    // sendSimpleMessage 의 매개변수로 들어온 to 는 곧 이메일 주소가 되고,
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다.
    // 그리고 bean 으로 등록해둔 javaMail 객체를 사용해서 이메일 send!!
    @Transactional
    public String sendSimpleMessage(String to) throws Exception {

        MimeMessage message = createMessage(to);

        try {// 예외처리
            if(emailAuthRepository.existsByEmail(to)) {
                // 중첩 인증키는 삭제 후 재전송
                emailAuthRepository.deleteByEmail(to);
            }

            emailSender.send(message); // 메일 발송
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        EmailAuth emailAuth = new EmailAuth(ePw, to);
        emailAuthRepository.save(emailAuth);
        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }
}