package com.ms.email.email;

public interface EmailSender {
    void enviarEmail(String destinatario, String assunto, String corpo) throws Exception;
}
