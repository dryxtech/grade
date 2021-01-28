package com.dryxtech.grade.util;

import java.util.UUID;

/**
 * A identification utility class
 *
 * @author Drew Griffin
 * @since 1.0
 */
public final class IdentifierUtil {

    private IdentifierUtil() {
        // Utility
    }

    /***
     * Generates a 32 character unique alphanumeric string
     *
     * @return id string
     */
    public static String generateIdString() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
