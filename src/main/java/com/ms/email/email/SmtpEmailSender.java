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
@ConditionalOnProperty(name = "muttley.email.provider", havingValue = "smtp", matchIfMissing = true)
public class SmtpEmailSender implements EmailSender {
    private final String host;
    private final int port;
    private final String remetente;
    private final String usuario;
    private final String senha;
    private final boolean startTls;
    private final boolean ssl;

    public SmtpEmailSender(
            @Value("${muttley.email.smtp.host:localhost}") String host,
            @Value("${muttley.email.smtp.port:1025}") int port,
            @Value("${muttley.email.smtp.from:muttley@localhost}") String remetente,
            @Value("${muttley.email.smtp.username:}") String usuario,
            @Value("${muttley.email.smtp.password:}") String senha,
            @Value("${muttley.email.smtp.starttls:false}") boolean startTls,
            @Value("${muttley.email.smtp.ssl:false}") boolean ssl) {
        if (usuario.isBlank() != senha.isBlank()) {
            throw new IllegalArgumentException("Usuário e senha SMTP devem ser configurados juntos.");
        }
        if (startTls && ssl) {
            throw new IllegalArgumentException("Configure STARTTLS ou SSL para SMTP, não ambos.");
        }
        this.host = host;
        this.port = port;
        this.remetente = remetente;
        this.usuario = usuario;
        this.senha = senha;
        this.startTls = startTls;
        this.ssl = ssl;
    }

    @Override
    public void enviarEmail(String destinatario, String assunto, String corpo) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", Integer.toString(port));
        properties.put("mail.smtp.connectiontimeout", "5000");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.writetimeout", "5000");
        properties.put("mail.smtp.auth", Boolean.toString(!usuario.isBlank()));
        properties.put("mail.smtp.starttls.enable", Boolean.toString(startTls));
        properties.put("mail.smtp.starttls.required", Boolean.toString(startTls));
        properties.put("mail.smtp.ssl.enable", Boolean.toString(ssl));
        properties.put("mail.smtp.ssl.checkserveridentity", "true");

        Session sessao = Session.getInstance(properties);
        MimeMessage mensagem = new MimeMessage(sessao);
        mensagem.setFrom(new InternetAddress(remetente, true));
        mensagem.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario, true));
        mensagem.setSubject(assunto, "UTF-8");
        mensagem.setText(corpo, "UTF-8");
        Transport transporte = sessao.getTransport("smtp");
        try {
            transporte.connect(host, port, usuario.isBlank() ? null : usuario, senha.isBlank() ? null : senha);
            transporte.sendMessage(mensagem, mensagem.getAllRecipients());
        } finally {
            transporte.close();
        }
    }
}
