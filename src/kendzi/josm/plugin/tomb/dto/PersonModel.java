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


}
