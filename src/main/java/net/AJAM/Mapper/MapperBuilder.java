package net.AJAM.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapperBuilder {
    private boolean isSingleton = false;
    private static Mapper instance;

    private boolean readProfiles = false;
    private final List<Mapping<?,?>> mappings = new ArrayList<>();
    private final List<Profile> profiles = new ArrayList<>();


    public MapperBuilder isSingleton()
    {
        return isSingleton(true);
    }

    public MapperBuilder isSingleton(boolean isSingleton)
    {
        this.isSingleton = isSingleton;

        return this;
    }

    public MapperBuilder autoDetectProfiles()
    {
        readProfiles = true;
        return this;
    }

    public MapperBuilder addMapping(Mapping<?,?> mapping)
    {
        this.mappings.add(mapping);
        return this;
    }

    public MapperBuilder addProfile(Profile profile)
    {
        this.profiles.add(profile);

        return this;
    }

    public MapperBuilder addMappings(List<Mapping<?,?>> mappings)
    {
        this.mappings.addAll(mappings);
        return this;
    }

    public MapperBuilder addProfiles(List<Profile> profiles)
    {
        this.profiles.addAll(profiles);

        return this;
    }

    public MapperBuilder addProfiles(Profile... profiles) {
        this.profiles.addAll(Arrays.asList(profiles));

        return this;
    }

    public MapperBuilder addMappings(Mapping<?, ?>... mapping) {
        this.mappings.addAll(Arrays.asList(mapping));

        return this;
    }

    public Mapper build()
    {
        Mapper result;
        if(!isSingleton)
            result = new Mapper(readProfiles);
        else
        {
            if(instance == null)
                instance = new Mapper(readProfiles);

            result = instance;
        }

        isSingleton = false;

        result.addProfiles(profiles);
        result.addMappings(mappings);

        return result;
    }
}
