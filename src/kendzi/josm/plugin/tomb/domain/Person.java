/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.domain;



/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class Person {

    //	name=imie nazwisko
    String name;

    //			wikipedia=jezyk:nazwa
    String wikipedia;

     //			wikidata
    String wikidata;

    //			birth=YYYY-MM-DD
    String born;

    //			death=YYYY-MM-DD
    String died;

    //			description - luzny opis, np. grupy zawodowej
    String description;

    String livedIn;

    //         family_name - nazwisko rodowe
    String family_name;

    //			birthplace - Miejsce urodzenia
    String birthplace;

     //			deathplace - Miejsce zgonu
    String deathplace;
    
    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the wikipedia
     */
    public String getWikipedia() {
        return this.wikipedia;
    }

    /**
     * @param wikipedia the wikipedia to set
     */
    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }



    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the livedIn
     */
    public String getLivedIn() {
        return livedIn;
    }

    /**
     * @param livedIn the livedIn to set
     */
    public void setLivedIn(String livedIn) {
        this.livedIn = livedIn;
    }

    /**
     * @return the family_name
     */
    public String getFamily_name() {
        return Family_name;
    }

    /**
     * @param family_name the Family_name to set
     */
    public void setFamily_name(String Family_name) {
        this.Family_name = Family_name;
    }

    /**
     * @return the born
     */
    public String getBorn() {
        return born;
    }

    /**
     * @param born the born to set
     */
    public void setBorn(String born) {
        this.born = born;
    }

    /**
     * @return the died
     */
    public String getDied() {
        return died;
    }

    /**
     * @param died the died to set
     */
    public void setDied(String died) {
        this.died = died;
    }

 /**
     * @return the birthplace
     */
    public String getBirthplace() {
        return birthplace;
    }

    /**
     * @param birthplace the Birthplace to set
     */
    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

 /**
     * @return the deathplace
     */
    public String getDeathplace() {
        return deathplace;
    }

    /**
     * @param deathplace the Deathplace to set
     */
    public void setDeathplace(String deathplace) {
        this.deathplace = deathplace;
    }

}
