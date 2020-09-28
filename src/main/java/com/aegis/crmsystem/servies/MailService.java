package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${frontend}")
    private String frontend;

    public void sendAuthInfo(String emailTo, String firstName, String password){
        MimeMessagePreparator mailBody = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            messageHelper.setFrom(username);
            messageHelper.setTo(emailTo);
            messageHelper.setSubject("Информация об аккаунте");

            Context context = new Context();
            context.setVariable("firstName", firstName);
            context.setVariable("email", emailTo);
            context.setVariable("password", password);

            String content = templateEngine.process("emailAuthInfo", context);

            messageHelper.setText(content, true);
        };

        mailSender.send(mailBody);
    }

    public void sendNewTaskInfo(User user, Task task){
        MimeMessagePreparator mailBody = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            messageHelper.setFrom(username);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Новая задача");

            Context context = new Context();
            context.setVariable("task", task);
            context.setVariable("user", user);
            context.setVariable("taskUrl", frontend + "/tasks/" + task.getId());

            String content = templateEngine.process("newTaskInfo", context);

            messageHelper.setText(content, true);
        };

        mailSender.send(mailBody);
    }

//    public void send(String emailTo, String firstName, String password) {
//        MimeMessagePreparator mailBody = mimeMessage -> {
//            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
//            messageHelper.setFrom(username);
//            messageHelper.setTo(emailTo);
//            messageHelper.setSubject("Информация об аккаунте");
//
//            String content = this.build(emailTo, firstName, password);
//            messageHelper.setText(content, true);
//        };
//
//        mailSender.send(mailBody);
//    }
//
//    public String build(String email, String firstName, String password) {
//        Context context = new Context();
//        context.setVariable("firstName", firstName);
//        context.setVariable("email", email);
//        context.setVariable("password", password);
//
//        return templateEngine.process("emailAuthInfo", context);
//    }
}
