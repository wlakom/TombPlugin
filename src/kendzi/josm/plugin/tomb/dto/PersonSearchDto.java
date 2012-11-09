package kendzi.josm.plugin.tomb.dto;

public class PersonSearchDto {

    private long id;

    private String name;

    private String livedIn;

    private String born;

    private String died;

    private String wikipedia;

    private String description;

    private String familyName;


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
     * @return the livedIn
     */
    public String getLivedIn() {
        return this.livedIn;
    }
    /**
     * @param livedIn the livedIn to set
     */
    public void setLivedIn(String livedIn) {
        this.livedIn = livedIn;
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
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }
    /**
     * @param familyName the familyName to set
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }


}

