package com.ms.email.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ms.email.email.dto.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailConsumerTest {
    @Test void inscricaoAceitaNumeroInteiroEmitidoPeloBackend() throws Exception {
        var service=mock(EmailService.class);
        new EmailConsumer(service).consumirConfirmacaoInscricao("{\"destinatario\":\"teste@example.invalid\",\"nome\":\"Ana\",\"tema\":\"Semana\",\"data\":\"2026-09-03\",\"horarioInicio\":\"09:00\",\"horarioFim\":\"11:00\",\"local\":\"Auditorio\",\"inscricao\":42}");
        verify(service).enviarConfirmacaoCadastro(new InscricaoEmail("teste@example.invalid","Ana","Semana","2026-09-03","09:00","11:00","Auditorio","42"));
    }
    @Test void roteiaComplementacaoDoCadastro() throws Exception {
        var service=mock(EmailService.class);
        new EmailConsumer(service).consumirCompletarCadastro("{\"destinatario\":\"teste@example.invalid\",\"nome\":\"Ana\",\"id\":\"codigo\",\"baseUrl\":\"https://example.invalid\"}");
        verify(service).enviarCredenciaisLogin(new CadastroEmail("teste@example.invalid","Ana","codigo","https://example.invalid"));
    }
    @Test void roteiaCancelamentoEConclusaoSeparadamente() throws Exception {
        var service=mock(EmailService.class);var consumer=new EmailConsumer(service);
        String json="{\"destinatario\":\"teste@example.invalid\",\"nome\":\"Ana\",\"tema\":\"Semana\",\"data\":\"2026-09-03\"}";
        consumer.consumirCancelado(json);consumer.consumirConcluido(json);
        var dto=new EventoEmail("teste@example.invalid","Ana","Semana","2026-09-03");
        verify(service).enviarEventoCancelado(dto);verify(service).enviarEventoConcluido(dto);
    }
    @Test void roteiaCertificadoComLinkEDatas() throws Exception {
        var service=mock(EmailService.class);
        new EmailConsumer(service).consumirCertificados("{\"destinatario\":\"teste@example.invalid\",\"nome\":\"Ana\",\"tema\":\"Semana\",\"dataEvento\":\"2026-09-02\",\"dataEmissao\":\"2026-09-03\",\"baseUrl\":\"https://example.invalid\",\"urlCert\":\"/certificados/codigo\"}");
        verify(service).enviarCertificados(new CertificadoEmail("teste@example.invalid","Ana","Semana","2026-09-02","2026-09-03","https://example.invalid","/certificados/codigo"));
    }
    @Test void jsonInvalidoNaoEnviaEmail() {
        var service=mock(EmailService.class);
        assertThatThrownBy(() -> new EmailConsumer(service).consumirCertificados("{invalido")).isInstanceOf(JsonProcessingException.class);
        verifyNoInteractions(service);
    }
}
