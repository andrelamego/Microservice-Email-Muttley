package com.ms.email.email;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "muttley.email.provider", havingValue = "smtp")
public class SmtpEmailSender implements EmailSender {
    private final String host;
    private final int port;
    private final String remetente;

    public SmtpEmailSender(
            @Value("${muttley.email.smtp.host}") String host,
            @Value("${muttley.email.smtp.port:1025}") int port,
            @Value("${muttley.email.smtp.from:muttley@localhost}") String remetente) {
        this.host = host;
        this.port = port;
        this.remetente = remetente;
    }

    @Override
    public void enviarEmail(String destinatario, String assunto, String corpo) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", Integer.toString(port));
        properties.put("mail.smtp.connectiontimeout", "5000");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.writetimeout", "5000");

        Session sessao = Session.getInstance(properties);
        MimeMessage mensagem = new MimeMessage(sessao);
        mensagem.setFrom(new InternetAddress(remetente));
        mensagem.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensagem.setSubject(assunto, "UTF-8");
        mensagem.setText(corpo, "UTF-8");
        Transport transporte = sessao.getTransport("smtp");
        try {
            transporte.connect(host, port, null, null);
            transporte.sendMessage(mensagem, mensagem.getAllRecipients());
        } finally {
            transporte.close();
        }
    }
}
