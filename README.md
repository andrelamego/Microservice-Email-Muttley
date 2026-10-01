# Microsserviço de e-mail do Muttley

Consome os eventos Kafka publicados pela API e envia notificações de confirmação de inscrição, convite para criar conta, cancelamento e conclusão de evento e emissão de certificado. O serviço usa SMTP por padrão. O Compose de [Infra-Muttley](../Infra-Muttley/README.md) aponta para o Mailpit, que captura as mensagens localmente sem entregá-las na internet.

## Configuração SMTP

| Variável | Padrão | Uso |
| --- | --- | --- |
| `MUTTLEY_EMAIL_SMTP_HOST` | `localhost` | Servidor SMTP |
| `MUTTLEY_EMAIL_SMTP_PORT` | `1025` | Porta SMTP |
| `MUTTLEY_EMAIL_SMTP_FROM` | `muttley@localhost` | Endereço remetente |
| `MUTTLEY_EMAIL_SMTP_USERNAME` | vazio | Usuário de autenticação |
| `MUTTLEY_EMAIL_SMTP_PASSWORD` | vazio | Senha ou credencial SMTP |
| `MUTTLEY_EMAIL_SMTP_STARTTLS` | `false` | Exige STARTTLS |
| `MUTTLEY_EMAIL_SMTP_SSL` | `false` | Usa SSL implícito |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Broker Kafka |

Para entrega real, configure host, porta, remetente, usuário e senha conforme o provedor de SMTP. Habilite STARTTLS **ou** SSL quando necessário. O serviço rejeita configuração com apenas usuário ou apenas senha; falhas de envio são propagadas ao consumidor Kafka. Use um remetente autorizado pelo provedor. Não grave credenciais no Git.

O provedor Gmail OAuth legado só é selecionado com `MUTTLEY_EMAIL_PROVIDER=gmail`; ele depende de autorização interativa e não é indicado para a execução em contêiner. Para o ambiente local, mantenha SMTP e veja as mensagens em `http://localhost:8025`.

Execute os testes com `./mvnw test` ou `mvn test`.
