package com.ms.email.email;

import com.ms.email.email.dto.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class EmailServiceTest {
    @Test void confirmacaoIncluiIdentificacaoHorarioLocalENumero() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarConfirmacaoCadastro(new InscricaoEmail("teste@example.invalid","Ana","Semana","2026-09-03","09:00","11:00","Auditorio","42"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Inscrição confirmada"),argThat(c ->
                c.contains("Ana") && c.contains("Semana") && c.contains("2026-09-03") && c.contains("09:00 - 11:00") && c.contains("Auditorio") && c.contains("42")));
    }
    @Test void complementacaoIncluiLinkComIdCodificado() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarCredenciaisLogin(new CadastroEmail("teste@example.invalid","Ana","id-codificado","https://example.invalid"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Complete seu cadastro"),contains("https://example.invalid/register?id=id-codificado"));
    }
    @Test void cancelamentoInformaEventoEData() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarEventoCancelado(new EventoEmail("teste@example.invalid","Ana","Semana","2026-09-03"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Evento cancelado"),argThat(c->c.contains("Semana")&&c.contains("2026-09-03")));
    }
    @Test void conclusaoIdentificaEvento() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarEventoConcluido(new EventoEmail("teste@example.invalid","Ana","Semana","2026-09-03"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Semana"),contains("concluído"));
    }
    @Test void certificadoIncluiUrlPublicaEDatas() throws Exception {
        GmailService gmail=mock(GmailService.class);
        new EmailService(gmail).enviarCertificados(new CertificadoEmail("teste@example.invalid","Ana","Semana","2026-09-02","2026-09-03","https://example.invalid","/certificados/codigo"));
        verify(gmail).enviarEmail(eq("teste@example.invalid"),contains("Certificado"),argThat(c->
                c.contains("https://example.invalid/certificados/codigo")&&c.contains("2026-09-02")&&c.contains("2026-09-03")));
    }
}
