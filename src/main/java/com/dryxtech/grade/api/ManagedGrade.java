package com.dryxtech.grade.api;

import java.util.Map;

/**
 * This interface represents a managed grade.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public interface ManagedGrade extends Grade {

    /**
     * Getter of management information.
     * @return management information
     */
    Map<String, Object> getManagement();
}
