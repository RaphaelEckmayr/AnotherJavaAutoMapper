package net.AJAM.Mapper;

import java.util.function.Function;

public class Conversion<S,T>
{
    private Class<S> from;
    private Class<T> to;
    private Function<S,T> conversionFunction;
    private MappingType mappingType;

    public Conversion(Class<S> from, Class<T> to, MappingType mappingType, Function<S, T> conversionFunction)
    {
        this.from = from;
        this.to = to;
        this.conversionFunction = conversionFunction;
        this.mappingType = mappingType;
    }

    public Conversion()
    {
    }

    public Class<S> getFrom()
    {
        return from;
    }

    public void setFrom(Class<S> from)
    {
        this.from = from;
    }

    public Class<T> getTo()
    {
        return to;
    }

    public void setTo(Class<T> to)
    {
        this.to = to;
    }

    public Function<S, T> getConversionFunction()
    {
        return conversionFunction;
    }

    public void setConversionFunction(Function<S, T> conversionFunction)
    {
        this.conversionFunction = conversionFunction;
    }

    public MappingType getMappingType()
    {
        return mappingType;
    }

    public void setMappingType(MappingType mappingType)
    {
        this.mappingType = mappingType;
    }
}
