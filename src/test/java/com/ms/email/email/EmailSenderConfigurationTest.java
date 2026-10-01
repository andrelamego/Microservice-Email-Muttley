package com.ms.email.email;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class EmailSenderConfigurationTest {
    private final ApplicationContextRunner contexto = new ApplicationContextRunner()
            .withUserConfiguration(GmailService.class, SmtpEmailSender.class);

    @Test
    void composeUsaSmtpSemInstanciarGmail() {
        contexto.withPropertyValues(
                "muttley.email.provider=smtp",
                "muttley.email.smtp.host=mailpit")
                .run(context -> {
                    assertThat(context).hasSingleBean(EmailSender.class);
                    assertThat(context.getBean(EmailSender.class)).isInstanceOf(SmtpEmailSender.class);
                    assertThat(context).doesNotHaveBean(GmailService.class);
                });
    }

    @Test
    void smtpEhPadraoTambemForaDoCompose() {
        contexto.run(context -> {
            assertThat(context).hasSingleBean(EmailSender.class);
            assertThat(context.getBean(EmailSender.class)).isInstanceOf(SmtpEmailSender.class);
            assertThat(context).doesNotHaveBean(GmailService.class);
        });
    }

    @Test
    void gmailExigeSelecaoExplicita() {
        contexto.withPropertyValues("muttley.email.provider=gmail").run(context -> {
            assertThat(context).hasSingleBean(EmailSender.class);
            assertThat(context.getBean(EmailSender.class)).isInstanceOf(GmailService.class);
            assertThat(context).doesNotHaveBean(SmtpEmailSender.class);
        });
    }

    @Test
    void credenciaisSmtpIncompletasImpedemInicializacao() {
        contexto.withPropertyValues("muttley.email.smtp.username=usuario")
                .run(context -> assertThat(context).hasFailed());
    }
}
