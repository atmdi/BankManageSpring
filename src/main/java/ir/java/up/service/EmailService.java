package ir.java.up.service;

import ir.java.up.document.ExceptionDocument;
import ir.java.up.dto.AccountInfo;
import ir.java.up.dto.BankResponse;
import ir.java.up.dto.EmailDetails;
import ir.java.up.service.Impl.EmailServiceImpl;
import ir.java.up.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailServiceImpl {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ExceptionDocumentService exceptionDocumentService;
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMassage=new SimpleMailMessage();
            mailMassage.setFrom(senderEmail);
            mailMassage.setTo(emailDetails.getRecipient());
            mailMassage.setText(emailDetails.getMessageBody());
            mailMassage.setSubject(emailDetails.getSubject());
            javaMailSender.send(mailMassage);
            System.out.println("mail sent successfully");
        }catch (MailException e){

            ExceptionDocument exceptionDocument = new ExceptionDocument();

            exceptionDocument.setMessage("input parameter :" + "MailException" + " required this type : " + MailException.class);
            exceptionDocument.setCode("internal.server.error");
            exceptionDocumentService.saveException(exceptionDocument);

            throw new RuntimeException(e);

        }

    }
}
