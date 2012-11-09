package kendzi.josm.plugin.tomb.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XmlUtli {


    public static String findString(Node node, XPathExpression name) throws XPathExpressionException {
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
     * @param doc
     * @param expr
     * @return
     * @throws XPathExpressionException
     */
    public static Element findElement(Document doc, XPathExpression expr)
            throws XPathExpressionException {
        Object evaluate = expr.evaluate(doc, XPathConstants.NODE);
        return (Element) evaluate;
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
    public static Document createDoc(String xml) throws ParserConfigurationException, SAXException, IOException {
        return createDoc(xml, "UTF-8");
    }

    public static Document createDoc(String xml, String encoding) throws ParserConfigurationException, SAXException, IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes(encoding));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);
        return doc;
    }

    public static String format(String unformattedXml) {
        return format(parseXmlFile(unformattedXml));
    }

    public static String format(Document document) {
        try {

            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
