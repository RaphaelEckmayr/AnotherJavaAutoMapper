package net.AJAM.Mapper;

import net.AJAM.Mapper.Exceptions.ReadProfilesFailedException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapperBuilder {
    private boolean isSingleton = false;
    private static Mapper instance;

    private boolean readProfiles = false;
    private final List<Mapping<?, ?>> mappings = new ArrayList<>();
    private final List<Profile> profiles = new ArrayList<>();
    private MappingType defaultMappingType = MappingType.MEDIUM;


    public MapperBuilder isSingleton() {
        this.isSingleton = true;
        return this;
    }

    public MapperBuilder autoDetectProfiles() {
        readProfiles = true;
        return this;
    }

    public MapperBuilder addMapping(Mapping<?, ?> mapping) {
        this.mappings.add(mapping);
        return this;
    }

    public MapperBuilder addProfile(Profile profile) {
        this.profiles.add(profile);
        return this;
    }

    public MapperBuilder defaultMappingType(MappingType mappingType) {
        this.defaultMappingType = mappingType;
        return this;
    }

    public MapperBuilder addProfile(Class<? extends Profile> profileClass) {
        try {
            Profile profile = profileClass.getDeclaredConstructor().newInstance();
            return addProfile(profile);

        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ReadProfilesFailedException("Error while creating Profile " + profileClass.getName() + ". Maybe you forgot the parameterless constructor");
        }
    }

    public MapperBuilder addMappings(List<Mapping<?, ?>> mappings) {
        this.mappings.addAll(mappings);
        return this;
    }

    public MapperBuilder addProfiles(List<Profile> profiles) {
        this.profiles.addAll(profiles);
        return this;
    }

    public MapperBuilder addProfiles(Profile... profiles) {
        this.profiles.addAll(Arrays.asList(profiles));
        return this;
    }

    public MapperBuilder addProfiles(Class<? extends Profile>... profiles) {
        for (Class<? extends Profile> profile : profiles) {
            addProfile(profile);
        }
        return this;
    }

    public MapperBuilder addMappings(Mapping<?, ?>... mapping) {
        this.mappings.addAll(Arrays.asList(mapping));
        return this;
    }

    public Mapper build() {
        Mapper result;
        if (!isSingleton)
            result = new Mapper(readProfiles, defaultMappingType);
        else {
            if (instance == null)
                instance = new Mapper(readProfiles, defaultMappingType);

            result = instance;
        }

        result.addProfiles(profiles);
        result.addMappings(mappings);

        return result;
    }
}
