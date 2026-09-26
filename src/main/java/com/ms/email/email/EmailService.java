package com.ms.email.email;

import com.ms.email.email.dto.CertificadoEmail;
import com.ms.email.email.dto.CadastroEmail;
import com.ms.email.email.dto.EventoEmail;
import com.ms.email.email.dto.InscricaoEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailSender emailSender;

    public void enviarConfirmacaoCadastro(InscricaoEmail dto) {
        String assunto = "Inscrição confirmada — " + dto.tema();
        String corpo = "Olá, " + dto.nome() + "!\n\n"
                + "Sua inscrição no evento \"" + dto.tema() + "\" foi confirmada com sucesso.\n\n"
                + "Data: " + dto.data() + "\n"
                + "Horário: " + dto.horarioInicio() + " - " + dto.horarioFim() + "\n"
                + "Local: " + dto.local() + "\n\n"
                + "Número de inscrição: " + dto.inscricao() + "\n\n"
                + "Até lá!\n"
                + "Equipe Muttley";

        enviar(dto.destinatario(), assunto, corpo);
    }

    public void enviarCredenciaisLogin(CadastroEmail dto) {
        String assunto = "Complete seu cadastro, " + dto.nome() + "!";
        String corpo = "Olá, " + dto.nome() + "!\n\n"
                + "Para acessar seu dashboard pessoal posteriomente, é necessário se cadastrar!\n\n"
                + "Segue link para criação da sua conta:" + "\n"
                + "Link: " + dto.baseUrl() + "/register?id=" + dto.id() + "\n\n"
                + "Equipe Muttley";

        enviar(dto.destinatario(), assunto, corpo);
    }

    public void enviarEventoCancelado(EventoEmail dto) {
        String assunto = "Evento cancelado — " + dto.tema();
        String corpo = "Olá, " + dto.nome() + "!\n\n"
                + "Informamos que o evento \"" + dto.tema() + "\", "
                + "agendado para " + dto.data() + ", foi cancelado.\n\n"
                + "Lamentamos o inconveniente e esperamos contar com sua presença em futuros eventos.\n\n"
                + "Equipe Muttley";

        enviar(dto.destinatario(), assunto, corpo);
    }

    public void enviarEventoConcluido(EventoEmail dto) {
        String assunto = "Obrigado pela participação — " + dto.tema();
        String corpo = "Olá, " + dto.nome() + "!\n\n"
                + "O evento \"" + dto.tema() + "\" foi concluído com sucesso. "
                + "Agradecemos sua participação!\n\n"
                + "Em breve seu certificado estará disponível.\n\n"
                + "Equipe Muttley";

        enviar(dto.destinatario(), assunto, corpo);
    }

    public void enviarCertificados(CertificadoEmail dto) {
        String assunto = "Certificado — " + dto.tema();
        String corpo = "Olá, " + dto.nome() + "!\n\n"
                + "Segue o certificicado relacionado ao evento — " + dto.tema() + " realizado no dia " + dto.dataEvento() +"\n\n"
                + "Data de emissão: " + dto.dataEmissao() +"\n\n"
                + "Link para acesso: " + dto.baseUrl() + dto.urlCert() +"\n\n"
                + "Equipe Muttley";

        enviar(dto.destinatario(), assunto, corpo);
    }

    private void enviar(String destinatario, String assunto, String corpo) {
        try {
            emailSender.enviarEmail(destinatario, assunto, corpo);
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail para {}", destinatario, e);
            throw new IllegalStateException("Falha ao enviar e-mail.", e);
        }
    }
}
