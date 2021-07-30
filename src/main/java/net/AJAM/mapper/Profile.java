package net.AJAM.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Profile
{
    private final List<Mapping<?,?>> mappings = new ArrayList<>();

    public <S,T> void addMapping(Mapping<S,T> mapping)
    {
        mappings.add(mapping);
    }

    public <S,T> void addAllMappings(List<Mapping<S,T>> mappingList)
    {
        mappings.addAll(mappingList);
    }

    public <S,T> void addAllMappings(Mapping<S,T>[] mappingArray)
    {
        Collections.addAll(mappings, mappingArray);
    }

    public List<Mapping<?,?>> getMappings() {
        return mappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }
}
