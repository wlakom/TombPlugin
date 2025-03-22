/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.dto;

import kendzi.josm.plugin.tomb.domain.Person;

import org.openstreetmap.josm.data.osm.Relation;


/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class PersonModel extends Person {

    Relation relation;


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

    private String lived_In;
    private String image;
    private String wikimedia_commons;
    private String flickr;
    private String denomination;
    private String ref;
    private String section_name;
    private String section_row;
    private String section_place;

    // Konstruktor (opcjonalny)
    public PersonModel() {
    }
// Gettery i Settery dla pola lived_In
    public String getLived_in() {
        return lived_In;
    }

    public void setLived_in(String lived_In) {
        this.lived_In = lived_In;
    }

    // Gettery i Settery dla pola image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Gettery i Settery dla pola wikimedia_commons
  public String getWikimedia_commons() {
        return wikimedia_commons;
    }

    public void setWikimedia_commons(String wikimedia_commons) {
        this.wikimedia_commons = wikimedia_commons;
    }

    // Gettery i Settery dla pola flickr
    public String getFlickr() {
        return flickr;
    }

    public void setFlickr(String flickr) {
        this.flickr = flickr;
    }

    // Gettery i Settery dla pola denomination
    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    // Gettery i Settery dla pola ref
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    // Gettery i Settery dla pola section_name
 public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    // Gettery i Settery dla pola section_row
   public String getSection_row() {
        return section_row;
    }

    public void setSection_row(String section_row) {
        this.section_row = section_row;
    }

    // Gettery i Settery dla pola section_place
   public String getSection_place() {
        return section_place;
    }

    public void setSection_place(String section_place) {
        this.section_place = section_place;
    }
}
