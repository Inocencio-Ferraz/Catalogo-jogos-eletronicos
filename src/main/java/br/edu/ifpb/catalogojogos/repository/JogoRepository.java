package br.edu.ifpb.catalogojogos.repository;

import br.edu.ifpb.catalogojogos.config.BaseXConfig;
import br.edu.ifpb.catalogojogos.model.Jogo;

import org.basex.api.client.ClientSession;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Repository
public class JogoRepository {

    private static final String BANCO = "catalogo_jogos";

    private final BaseXConfig baseXConfig;

    public JogoRepository(BaseXConfig baseXConfig) {
        this.baseXConfig = baseXConfig;
    }

    public Jogo salvar(Jogo jogo) throws IOException {

        jogo.setId(UUID.randomUUID().toString());

        String xml = converterParaXML(jogo);

        String consulta = "insert node " + xml +
                " into db:get('" + BANCO + "')/catalogo";

        try (ClientSession session = baseXConfig.abrirSessao()) {
            session.query(consulta).execute();
        }

        return jogo;
    }

    public List<Jogo> listarTodos() throws IOException {

        String consulta =
                "<jogos>{db:get('" + BANCO +
                        "')/catalogo/jogo}</jogos>";

        try (ClientSession session = baseXConfig.abrirSessao()) {

            String resultado = session.query(consulta).execute();

            return converterListaXML(resultado);
        }
    }

    public Jogo buscarPorId(String id) throws IOException {

        String consulta =
                "<jogos>{db:get('" + BANCO +
                        "')/catalogo/jogo[@id='" + escaparXQuery(id) +
                        "']}</jogos>";

        try (ClientSession session = baseXConfig.abrirSessao()) {

            String resultado = session.query(consulta).execute();

            List<Jogo> jogos = converterListaXML(resultado);

            if (jogos.isEmpty()) {
                return null;
            }

            return jogos.get(0);
        }
    }

    public List<Jogo> buscarPorGenero(String genero) throws IOException {
        return buscarPorCampo("genero", genero);
    }

    public List<Jogo> buscarPorPlataforma(String plataforma) throws IOException {
        return buscarPorCampo("plataforma", plataforma);
    }

    public List<Jogo> buscarPorDesenvolvedora(String desenvolvedora) throws IOException {
        return buscarPorCampo("desenvolvedora", desenvolvedora);
    }

    private List<Jogo> buscarPorCampo(String campo, String valor) throws IOException {

        if (!List.of("genero", "plataforma", "desenvolvedora").contains(campo)) {
            throw new IllegalArgumentException("Campo de pesquisa inválido.");
        }

        String consulta =
                "let $valor := lower-case('" + escaparXQuery(valor) + "') " +
                        "return <jogos>{ " +
                        "for $jogo in db:get('" + BANCO + "')/catalogo/jogo " +
                        "where contains(lower-case(string($jogo/" + campo + ")), $valor) " +
                        "return $jogo " +
                        "}</jogos>";

        try (ClientSession session = baseXConfig.abrirSessao()) {

            String resultado = session.query(consulta).execute();

            return converterListaXML(resultado);
        }
    }

    public boolean atualizar(String id, Jogo jogo) throws IOException {

        if (buscarPorId(id) == null) {
            return false;
        }

        jogo.setId(id);

        String xml = converterParaXML(jogo);

        String consulta =
                "let $jogo := db:get('" + BANCO +
                        "')/catalogo/jogo[@id='" + escaparXQuery(id) + "'] " +
                        "return if (exists($jogo)) " +
                        "then replace node $jogo with " + xml +
                        "else ()";

        try (ClientSession session = baseXConfig.abrirSessao()) {

            session.query(consulta).execute();

            return true;
        }
    }

    public boolean excluir(String id) throws IOException {

        if (buscarPorId(id) == null) {
            return false;
        }

        String consulta =
                "let $jogo := db:get('" + BANCO +
                        "')/catalogo/jogo[@id='" + escaparXQuery(id) + "'] " +
                        "return if (exists($jogo)) " +
                        "then delete node $jogo " +
                        "else ()";

        try (ClientSession session = baseXConfig.abrirSessao()) {

            session.query(consulta).execute();

            return true;
        }
    }

    private String converterParaXML(Jogo jogo) {

        return "<jogo id='" + escaparXML(jogo.getId()) + "'>" +
                "<titulo>" + escaparXML(jogo.getTitulo()) + "</titulo>" +
                "<plataforma>" + escaparXML(jogo.getPlataforma()) + "</plataforma>" +
                "<genero>" + escaparXML(jogo.getGenero()) + "</genero>" +
                "<desenvolvedora>" + escaparXML(jogo.getDesenvolvedora()) + "</desenvolvedora>" +
                "<anoLancamento>" + jogo.getAnoLancamento() + "</anoLancamento>" +
                "<classificacaoIndicativa>" + jogo.getClassificacaoIndicativa() + "</classificacaoIndicativa>" +
                "<nota>" + jogo.getNota() + "</nota>" +
                "<descricao>" + escaparXML(jogo.getDescricao()) + "</descricao>" +
                "</jogo>";
    }

    private String escaparXML(String valor) {

        if (valor == null) {
            return "";
        }

        return valor.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String escaparXQuery(String valor) {
        return valor.replace("'", "''");
    }

    private List<Jogo> converterListaXML(String xml) {

        List<Jogo> jogos = new ArrayList<>();

        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true
            );

            Document documento = factory.newDocumentBuilder()
                    .parse(new java.io.ByteArrayInputStream(
                            xml.getBytes(java.nio.charset.StandardCharsets.UTF_8)
                    ));

            NodeList lista = documento.getElementsByTagName("jogo");

            for (int i = 0; i < lista.getLength(); i++) {

                Element elemento = (Element) lista.item(i);

                Jogo jogo = new Jogo();

                jogo.setId(elemento.getAttribute("id"));
                jogo.setTitulo(obterTexto(elemento, "titulo"));
                jogo.setPlataforma(obterTexto(elemento, "plataforma"));
                jogo.setGenero(obterTexto(elemento, "genero"));
                jogo.setDesenvolvedora(obterTexto(elemento, "desenvolvedora"));
                jogo.setAnoLancamento(Integer.parseInt(obterTexto(elemento, "anoLancamento")));
                jogo.setClassificacaoIndicativa(Integer.parseInt(obterTexto(elemento, "classificacaoIndicativa")));
                jogo.setNota(Double.parseDouble(obterTexto(elemento, "nota")));
                jogo.setDescricao(obterTexto(elemento, "descricao"));

                jogos.add(jogo);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter os dados XML.", e);
        }

        return jogos;
    }

    private String obterTexto(Element elemento, String tag) {

        NodeList lista = elemento.getElementsByTagName(tag);

        if (lista.getLength() == 0) {
            return "";
        }

        return lista.item(0).getTextContent();
    }
}