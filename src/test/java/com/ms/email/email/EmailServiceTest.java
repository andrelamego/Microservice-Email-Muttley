package com.ms.email.email;

import com.ms.email.email.dto.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class EmailServiceTest {
    @Test void falhaNoSmtpNaoEhDescartada() throws Exception {
        EmailSender remetente=mock(EmailSender.class);
        doThrow(new IllegalStateException("SMTP indisponível"))
                .when(remetente).enviarEmail(anyString(),anyString(),anyString());
        assertThatThrownBy(() -> new EmailService(remetente).enviarCredenciaisLogin(
                new CadastroEmail("teste@example.invalid","Ana","convite","https://example.invalid")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Falha ao enviar e-mail.");
    }
    @Test void confirmacaoIncluiIdentificacaoHorarioLocalENumero() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarConfirmacaoCadastro(new InscricaoEmail("teste@example.invalid","Ana","Semana","2026-09-03","09:00","11:00","Auditorio","42"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Inscrição confirmada"),argThat(c ->
                c.contains("Ana") && c.contains("Semana") && c.contains("2026-09-03") && c.contains("09:00 - 11:00") && c.contains("Auditorio") && c.contains("42")));
    }
    @Test void complementacaoIncluiLinkComIdCodificado() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarCredenciaisLogin(new CadastroEmail("teste@example.invalid","Ana","id-codificado","https://example.invalid"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Convite para criar sua conta"),argThat(c ->
                c.contains("https://example.invalid/register?id=id-codificado") && c.contains("24 horas")));
    }
    @Test void cancelamentoInformaEventoEData() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarEventoCancelado(new EventoEmail("teste@example.invalid","Ana","Semana","2026-09-03"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Evento cancelado"),argThat(c->c.contains("Semana")&&c.contains("2026-09-03")));
    }
    @Test void conclusaoNaoPrometeCertificadoParaAusente() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarEventoConcluido(new EventoEmail("teste@example.invalid","Ana","Semana","2026-09-03"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Semana"),argThat(c ->
                c.contains("concluído") && c.contains("Se sua presença foi confirmada")
                        && !c.contains("seu certificado estará disponível")));
    }
    @Test void certificadoIncluiUrlPublicaEDatas() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarCertificados(new CertificadoEmail("teste@example.invalid","Ana","Semana","2026-09-02","2026-09-03","https://example.invalid","/certificados/codigo"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Certificado"),argThat(c->
                c.contains("https://example.invalid/certificados/codigo")&&c.contains("2026-09-02")&&c.contains("2026-09-03")));
    }

    @Test void linksContinuamValidosComBarraFinalNaUrlBase() throws Exception {
        EmailSender remetente=mock(EmailSender.class);
        EmailService service=new EmailService(remetente);
        service.enviarCredenciaisLogin(new CadastroEmail("teste@example.invalid","Ana","token","https://example.invalid/"));
        service.enviarCertificados(new CertificadoEmail("teste@example.invalid","Ana","Semana","2026-09-02","2026-09-03","https://example.invalid/","/certificados/codigo"));
        verify(remetente).enviarEmail(eq("teste@example.invalid"),contains("Convite"),contains("https://example.invalid/register?id=token"));
        verify(remetente).enviarEmail(eq("teste@example.invalid"),contains("Certificado"),contains("https://example.invalid/certificados/codigo"));
    }
}
