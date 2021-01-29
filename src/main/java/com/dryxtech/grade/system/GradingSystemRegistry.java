package com.dryxtech.grade.system;

import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.util.IdentifierUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A registry/store of grading systems
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradingSystemRegistry {

    private final String registryId;
    private final Map<String, GradingSystem> systemRegistry;
    private final Map<String, GradeConverter> converterRegistry;

    public GradingSystemRegistry() {
        this(IdentifierUtil.generateIdString(), null, null);
    }

    public GradingSystemRegistry(final String registryId, final Map<String, GradingSystem> systemRegistry,
                                 final Map<String, GradeConverter> converterRegistry) {
        Objects.requireNonNull(registryId, "registryId must not be null");

        this.registryId = registryId;

        if (Objects.nonNull(systemRegistry)) {
            this.systemRegistry = Collections.synchronizedMap(systemRegistry);
        } else {
            this.systemRegistry = Collections.synchronizedMap(new HashMap<>());
        }

        if (Objects.nonNull(converterRegistry)) {
            this.converterRegistry = Collections.synchronizedMap(converterRegistry);
        } else {
            this.converterRegistry = Collections.synchronizedMap(new HashMap<>());
        }
    }

    public Optional<GradingSystem> lookupSystem(String id) {
        return Optional.ofNullable(systemRegistry.get(id));
    }

    public Optional<GradeConverter> lookupConverter(String fromGradingSystemId, String toGradingSystemId) {
        return Optional.ofNullable(converterRegistry.get(fromGradingSystemId + "->" + toGradingSystemId));
    }

    public void registerSystem(String id, final GradingSystem gradingSystem) {
        systemRegistry.put(id, gradingSystem);
    }

    public void registerConverter(String fromGradingSystemId, String toGradingSystemId, GradeConverter converter) {
        converterRegistry.put(fromGradingSystemId + "->" + toGradingSystemId, converter);
    }

    public void unregisterSystem(String id) {
        systemRegistry.remove(id);
    }

    public void unregisterConverter(String fromGradingSystemId, String toGradingSystemId) {
        systemRegistry.remove(fromGradingSystemId + "->" + toGradingSystemId);
    }

    public Collection<GradingSystem> getRegisteredSystems() {
        return systemRegistry.values();
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryId, systemRegistry, converterRegistry);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradingSystemRegistry that = (GradingSystemRegistry) o;

        return Objects.equals(registryId, that.registryId) &&
                Objects.equals(systemRegistry, that.systemRegistry) &&
                Objects.equals(converterRegistry, that.converterRegistry);
    }

    @Override
    public String toString() {
        return "grading_system_registry{registryId=\"" + getRegistryId() + "\"}";
    }

    public String getRegistryId() {
        return registryId;
    }
}
