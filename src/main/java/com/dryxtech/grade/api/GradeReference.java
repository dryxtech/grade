package com.dryxtech.grade.api;

import java.util.Map;
import java.util.Optional;

/**
 * This interface represents a reference to some item/information related to a grade.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public interface GradeReference {

    /**
     * Getter for grade reference identification.
     * @return Identification for a grade related item
     */
    String getId();

    /**
     * Getter for grade reference type.
     * @return Type of a grade related item
     */
    String getType();

    /**
     * Getter for grade reference description.
     * @return Description of a grade related item
     */
    String getDescription();

    /**
     * Getter for grade reference extension.
     *
     * @param key attribute lookup name
     * @return Extended attribute of a grade related item
     */
    Optional<Object> getExtension(String key);

    /**
     * Getter for all grade reference extensions.
     * @return All extended attributes of a grade related item
     */
    Map<String, Object> getExtensions();
}
