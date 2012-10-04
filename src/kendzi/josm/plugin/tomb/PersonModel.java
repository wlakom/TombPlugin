package kendzi.josm.plugin.tomb;

import org.openstreetmap.josm.data.osm.Relation;


public class PersonModel {
    Relation relation;

    //	name=imie nazwisko
    String name;

    //			wikipedia=jezyk:nazwa
    String wikipedia;

    //			birth=YYYY-MM-DD
    String birth;

    //			death=YYYY-MM-DD
    String death;

    //			description - luzny opis, np. grupy zawodowej
    String description;

    /**
     * @return the relation
     */
    public Relation getRelation() {
        return relation;
    }

    /**
     * @param relation the relation to set
     */
    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
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
        return wikipedia;
    }

    /**
     * @param wikipedia the wikipedia to set
     */
    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }

    /**
     * @return the birth
     */
    public String getBirth() {
        return birth;
    }

    /**
     * @param birth the birth to set
     */
    public void setBirth(String birth) {
        this.birth = birth;
    }

    /**
     * @return the death
     */
    public String getDeath() {
        return death;
    }

    /**
     * @param death the death to set
     */
    public void setDeath(String death) {
        this.death = death;
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



}
