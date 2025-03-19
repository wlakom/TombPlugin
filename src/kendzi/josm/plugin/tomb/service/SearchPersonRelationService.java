/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpression;

import kendzi.josm.plugin.tomb.dto.PersonSearchDto;
import kendzi.josm.plugin.tomb.util.XmlUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SearchPersonRelationService {

    OverpassService overpassService = new OverpassService();

    public List<PersonSearchDto> findPerson(String name) throws Exception {

        try {
            String query = createQuery(name);

            String findResult = this.overpassService.findQuery(query);

            List<PersonSearchDto> ret = new ArrayList<PersonSearchDto>();

            Document doc = null;
            try {
                doc = XmlUtil.createDoc(findResult);
            } catch (Exception e) {
                throw new Exception("error parsing xml: \n" + findResult, e);
            }

            XPathExpression expr = XmlUtil.createXpath("//osm/relation");

            XPathExpression nameX = XmlUtil.createXpath("tag[@k='name']/@v");
            XPathExpression bornX = XmlUtil.createXpath("tag[@k='born']/@v");
            XPathExpression diedX = XmlUtil.createXpath("tag[@k='died']/@v");

            XPathExpression wikipediaX = XmlUtil.createXpath("tag[@k='wikipedia']/@v");
            XPathExpression wikidataX = XmlUtil.createXpath("tag[@k='wikidata']/@v");
            XPathExpression descriptionX = XmlUtil.createXpath("tag[@k='description']/@v");
            XPathExpression inscriptionX = XmlUtil.createXpath("tag[@k='inscription']/@v");
            XPathExpression family_nameX = XmlUtil.createXpath("tag[@k='family_name']/@v");
            XPathExpression lived_InX = XmlUtil.createXpath("tag[@k='lived_In']/@v");
            XPathExpression birthplaceX = XmlUtil.createXpath("tag[@k='birthplace']/@v");
            XPathExpression deathplaceX = XmlUtil.createXpath("tag[@k='deathplace']/@v");

            NodeList nodes = XmlUtil.findNodes(doc, expr);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element node = (Element) nodes.item(i);

                String idValue = node.getAttribute("id");
                PersonSearchDto p = new PersonSearchDto();

                p.setId(Long.parseLong(idValue));

                p.setName(XmlUtil.findString(node, nameX));

                p.setBorn(XmlUtil.findString(node, bornX));
                p.setDied(XmlUtil.findString(node, diedX));

                p.setWikipedia(XmlUtil.findString(node, wikipediaX));
                p.setWikidata(XmlUtil.findString(node, wikidataX));

                p.setDescription(XmlUtil.findString(node, descriptionX));
                p.setInscription(XmlUtil.findString(node, inscriptionX));
                p.setFamily_name(XmlUtil.findString(node, family_nameX));
                p.setLived_In(XmlUtil.findString(node, lived_InX));

                p.setBirthplace(XmlUtil.findString(node, birthplaceX));
                p.setDeathplace(XmlUtil.findString(node, deathplaceX));

                ret.add(p);
            }

            return ret;

        } catch (Exception e) {
            throw new Exception("error findPerson", e);
        }
    }

    private String createQuery(String name) throws Exception {
        //[Cc]zernik

        String query =
                "<osm-script>"
                        + " <query into=\"_\" type=\"relation\">"
                        + "   <has-kv k=\"type\" v=\"person\" />"
                        + "   <has-kv case=\"ignore\" k=\"name\" regv=\"\" />"
                        + " </query>"
                        + " <print from=\"_\" limit=\"100\" mode=\"meta\" order=\"id\"/>"
                        + " </osm-script>";
        //        " + name + "\

        Document doc = XmlUtil.createDoc(query);

        //        XPathExpression regv = XmlUtli.createXpath("//osm-script/query/has-kv[@k='name']/@regv");
        XPathExpression queryE = XmlUtil.createXpath("//osm-script/query/has-kv[@k='name']");

        Element element = XmlUtil.findElement(doc, queryE);

        element.setAttribute("regv", name);

        return XmlUtil.format(doc);

    }




}
