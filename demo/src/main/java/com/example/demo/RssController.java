package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RestController
public class RssController {

    private final RestTemplate restTemplate;

    public RssController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping(path = "/rss", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonNode rss() throws Exception {
        final JsonNode forObject = restTemplate.getForObject("https://www.cepea.esalq.usp.br/rss.php", JsonNode.class);

        return forObject;

//        XmlMapper xmlMapper = new XmlMapper();
//        assert forObject != null;
//        JsonNode node = xmlMapper.readTree(forObject.getBytes(StandardCharsets.UTF_8));
//        System.out.println(decodeUTF8(encodeUTF8(forObject)));
//
//        return node;
    }

    @GetMapping(path = "/rss2", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<Object, Object>> rss2() throws Exception {
        final String forObject = restTemplate.getForObject("https://www.cepea.esalq.usp.br/rss.php", String.class);
        final Document document = convertStringToXMLDocument(forObject);
        final NodeList items = document.getElementsByTagName("item");

        //https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        final List<Map<Object, Object>> retorno = new ArrayList<>();
        for(int i = 0 ; i < items.getLength(); i++) {
            final Node item = items.item(i);
            Element itemElement = (Element) item;

            String titulo = itemElement.getElementsByTagName("title").item(0).getTextContent();
            String descricao = itemElement.getElementsByTagName("description").item(0).getTextContent();
            if(titulo.contains("/") && titulo.contains(":") && descricao != null && !descricao.isEmpty()) {
                final Map<Object, Object> itemObject = new HashMap<>();
                String link = itemElement.getElementsByTagName("link").item(0).getTextContent();
                String data = itemElement.getElementsByTagName("pubDate").item(0).getTextContent();


                titulo = StringEscapeUtils.unescapeHtml4(titulo);
                itemObject.put("tipo", titulo.split(":")[0]);
                itemObject.put("titulo", titulo);
                itemObject.put("link", link);

                //https://stackoverflow.com/questions/994331/how-to-unescape-html-character-entities-in-java
                itemObject.put("descricao", StringEscapeUtils.unescapeHtml4(descricao));
                try {

                    final LocalDateTime parse = LocalDateTime.parse(data,
                            DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z"));
                    itemObject.put("data", parse);
                    itemObject.put("dataFormatada", parse.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy",
                            new Locale("pt", "BR"))));
                } catch (Exception e) {
                    itemObject.put("data", data);
                    itemObject.put("dataFormatada", data);
                    System.err.println(e.getMessage());
                }
                retorno.add(itemObject);
            }
            System.out.println();
        }

        return retorno;

    }

    public static void main(String[] args) {

        String data = "Mon, 02 Dec 2019 11:32:53 -0300";
        final LocalDateTime parse = LocalDateTime.parse(data,
                DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z" , new Locale("pt", "BR")));
        System.out.println(parse);
    }


    private static Document convertStringToXMLDocument(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlString)));
    }

    private final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

    String flush(String val){
        return decodeUTF8(encodeUTF8(val));
    }
    String decodeUTF8(byte[] bytes) {
        return new String(bytes, UTF8_CHARSET);
    }

    byte[] encodeUTF8(String string) {
        return string.getBytes(UTF8_CHARSET);
    }
}

//        á   &aacute;
//        Â   &Acirc;
//        â   &acirc;
//        À   &Agrave;
//        à   &agrave;
//        Å   &Aring;
//        å   &aring;
//        Ã   &Atilde;
//        ã   &atilde;
//        Ä   &Auml;
//        ä   &auml;
//        Æ   &AElig;
//        æ   &aelig;
//        É   &Eacute;
//        é   &eacute;
//        Ê   &Ecirc;
//        ê   &ecirc;
//        È   &Egrave;
//        è   &egrave;
//        Ë   &Euml;
//        ë   &euml;
//        Ð   &ETH;
//        ð   &eth;
//        Í   &Iacute;
//        í   &iacute;
//        Î   &Icirc;
//        î   &icirc;
//        Ì   &Igrave;
//        ì   &igrave;
//        Ï   &Iuml;
//        ï   &iuml;
//        Ó   &Oacute;
//        ó   &oacute;
//        Ô   &Ocirc;
//        ô   &ocirc;
//        Ò   &Ograve;
//        ò   &ograve;
//        Ø   &Oslash;
//        ø   &oslash;
//        Õ   &Otilde;
//        õ   &otilde;
//        Ö   &Ouml;
//        ö   &ouml;
//        Ú   &Uacute;
//        ú   &uacute;
//        Û   &Ucirc;
//        û   &ucirc;
//        Ù   &Ugrave;
//        ù   &ugrave;
//        Ü   &Uuml;
//        ü   &uuml;
//        Ç   &Ccedil;
//        ç   &ccedil;
//        Ñ   &Ntilde;
//        ñ   &ntilde;
//        <   &lt;
//        >   &gt;
//        &   &amp;
//        "   &quot;
//        ®   &reg;
//        ©   &copy;
//        Ý   &Yacute;
//        ý   &yacute;
//        Þ   &THORN;
//        þ   &thorn;
//        ß   &szlig;
