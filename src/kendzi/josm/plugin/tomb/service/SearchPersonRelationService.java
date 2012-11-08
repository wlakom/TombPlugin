package kendzi.josm.plugin.tomb.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import kendzi.josm.plugin.tomb.dto.PersonSearchDto;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SearchPersonRelationService {

    OverpassService overpassService = new OverpassService();

    public List<PersonSearchDto> findPerson(String name) throws Exception {

        try {
            String query = createQuery(name);

            String findResult = overpassService.findQuery(query);

            List<PersonSearchDto> ret = new ArrayList<PersonSearchDto>();


            Document doc = createDoc(findResult);

            XPathExpression expr = createXpath("//osm/relation");

            XPathExpression nameX = createXpath("tag[@k='name']/@v");

            NodeList nodes = findNodes(doc, expr);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element node = (Element) nodes.item(i);

                String idValue = node.getAttribute("id");
                String nameValue = findString(node, nameX);


                System.out.println(idValue);
                System.out.println(nameValue);

                PersonSearchDto p = new PersonSearchDto();
                p.setId(Long.parseLong(idValue));
                p.setName(nameValue);

                ret.add(p);
            }

            return ret;

        } catch (Exception e) {
            throw new Exception("error findPerson", e);
        }
    }

    private String createQuery(String name) {
        //[Cc]zernik
        // FIXME TODO XXX unescape name!!!
        return "<osm-script>"
        + " <query into=\"_\" type=\"relation\">"
        + " <has-kv k=\"type\" v=\"person\" />"
        + " <has-kv k=\"name\" regv=\"" + name + "\" />"
        + " </query>"
        + " <print from=\"_\" limit=\"\" mode=\"meta\" order=\"id\"/>"
        + " </osm-script>";
    }



    private static String findString(Node node, XPathExpression name) throws XPathExpressionException {
        return (String) name.evaluate(node, XPathConstants.STRING);
    }

    /**
     * @param doc
     * @param expr
     * @return
     * @throws XPathExpressionException
     */
    public static NodeList findNodes(Document doc, XPathExpression expr)
            throws XPathExpressionException {
        Object evaluate = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) evaluate;
        return nodes;
    }
    /**
     * @param xpathExp
     * @return
     * @throws XPathExpressionException
     */
    public static XPathExpression createXpath(String xpathExp)
            throws XPathExpressionException {
        XPathFactory xFactory = XPathFactory.newInstance();
        XPath xPath = xFactory.newXPath();

        XPathExpression expr = xPath.compile(xpathExp);
        return expr;
    }
    /**
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document createDoc(String xml)
            throws ParserConfigurationException, SAXException, IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);
        return doc;
    }


}
