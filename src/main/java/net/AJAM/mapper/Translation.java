package net.AJAM.Mapper;

import java.lang.reflect.Method;

public abstract class Translation<S,T,V>
{
    protected abstract boolean translate(S source, T target, MappingType mappingType);
}