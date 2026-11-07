package br.edu.ifpb.catalogojogos.config;

import org.basex.api.client.ClientSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BaseXConfig {

    private static final String BANCO = "catalogo_jogos";

    @Value("${basex.host:localhost}")
    private String host;

    @Value("${basex.port:1984}")
    private int port;

    @Value("${basex.username:admin}")
    private String username;

    @Value("${basex.password:admin}")
    private String password;

    public ClientSession abrirSessao() throws IOException {

        ClientSession session = new ClientSession(
                host, port, username, password
        );

        try {
            String resultado = session.query(
                    "db:exists('" + BANCO + "')"
            ).execute();

            if ("false".equals(resultado.trim())) {
                session.query(
                        "db:create(<catalogo/>, '" + BANCO + "')"
                ).execute();
            }

            return session;

        } catch (IOException e) {
            session.close();
            throw e;
        }
    }
}