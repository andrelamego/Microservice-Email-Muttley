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
        String assunto = "Convite para criar sua conta no Muttley";
        String corpo = "Olá, " + dto.nome() + "!\n\n"
                + "Você se inscreveu em um evento e ainda não possui uma conta no Muttley.\n"
                + "Crie sua conta para acompanhar suas participações e certificados.\n\n"
                + "O convite é válido por 24 horas. Se você receber um novo convite, use o link mais recente.\n"
                + "Link: " + unirUrl(dto.baseUrl(), "/register?id=" + dto.id()) + "\n\n"
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
        String assunto = "Evento concluído — " + dto.tema();
        String corpo = "Olá, " + dto.nome() + "!\n\n"
                + "O evento \"" + dto.tema() + "\", realizado em " + dto.data() + ", foi concluído.\n\n"
                + "Se sua presença foi confirmada, você receberá outra mensagem com o link do certificado.\n\n"
                + "Equipe Muttley";

        enviar(dto.destinatario(), assunto, corpo);
    }

    public void enviarCertificados(CertificadoEmail dto) {
        String assunto = "Certificado — " + dto.tema();
        String corpo = "Olá, " + dto.nome() + "!\n\n"
                + "Seu certificado do evento \"" + dto.tema() + "\", realizado em " + dto.dataEvento() + ", foi emitido.\n\n"
                + "Data de emissão: " + dto.dataEmissao() + "\n"
                + "Acesse o certificado: " + unirUrl(dto.baseUrl(), dto.urlCert()) + "\n\n"
                + "Equipe Muttley";

        enviar(dto.destinatario(), assunto, corpo);
    }

    private String unirUrl(String baseUrl, String caminho) {
        return baseUrl.replaceAll("/+$", "") + "/" + caminho.replaceFirst("^/+", "");
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
