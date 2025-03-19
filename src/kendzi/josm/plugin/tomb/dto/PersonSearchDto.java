/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.dto;

public class PersonSearchDto {

    private long id;

    private String name;

    private String lived_In;

    private String born;

    private String died;

    private String wikipedia;

    private String wikidata;

    private String description;

    private String inscription;

    private String family_name;

    private String birthplace;

    private String deathplace;
    
    /**
     * @return the id
     */
    public long getId() {
        return this.id;
    }
    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
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
     * @return the lived_In
     */
    public String getLived_In() {
        return this.lived_In;
    }
    /**
     * @param lived_In the lived_In to set
     */
    public void setLived_In(String lived_In) {
        this.lived_In = lived_In;
    }
    /**
     * @return the born
     */
    public String getBorn() {
        return this.born;
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
        return this.died;
    }
    /**
     * @param died the died to set
     */
    public void setDied(String died) {
        this.died = died;
    }
    /**
     * @return the wikipedia
     */
    public String getWikipedia() {
        return wikipedia;
    }
    /**
     * @param wikipedia the wikipedia to set
     */
    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }
/**
     * @return the wikidata
     */
    public String getWikidata() {
        return wikidata;
    }
    /**
     * @param wikidata the wikidata to set
     */
    public void setWikidata(String Wikidata) {
        this.wikidata = wikidata;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
        /**
     * @return the inscription
     */
    public String getInscription() {
        return inscription;
    }
    /**
     * @param inscription the inscription to set
     */
    public void setInscription(String inscription) {
        this.inscription = inscription;
    }
 
    /**
     * @return the family_name
     */
    public String getFamily_name() {
        return family_name;
    }
    /**
     * @param family_name the family_name to set
     */
    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

   /**
     * @return the lived_In
     */
    public String getLived_In() {
        return lived_In;
    }
    /**
     * @param lived_In the lived_In to set
     */
    public void setLived_In(String lived_In) {
        this.lived_In = lived_In;
    }


     /**
     * @return the birthplace
     */
    public String getBirthplace() {
        return this.birthplace;
    }
    /**
     * @param birthplace the birthplace to set
     */
    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

     /**
     * @return the deathplace
     */
    public String getDeathplace() {
        return this.deathplace;
    }
    /**
     * @param deathplace the deathplace to set
     */
    public void setDeathplace(String deathplace) {
        this.deathplace = deathplace;
    }

}
