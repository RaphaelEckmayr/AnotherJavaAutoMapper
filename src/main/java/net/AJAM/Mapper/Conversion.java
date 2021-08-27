package net.AJAM.Mapper;

import net.AJAM.Mapper.Interfaces.ConversionFunction;

public class Conversion<S, T> {
    private Class<S> from;
    private Class<T> to;
    private ConversionFunction<S, T> conversionFunction;
    private MappingType mappingType;

    public Conversion(Class<S> from, Class<T> to, MappingType mappingType, ConversionFunction<S, T> conversionFunction) {
        this.from = from;
        this.to = to;
        this.conversionFunction = conversionFunction;
        this.mappingType = mappingType;
    }

    public Conversion() {
    }

    public Class<S> getFrom() {
        return from;
    }

    public void setFrom(Class<S> from) {
        this.from = from;
    }

    public Class<T> getTo() {
        return to;
    }

    public void setTo(Class<T> to) {
        this.to = to;
    }

    public ConversionFunction<S, T> getConversionFunction() {
        return conversionFunction;
    }

    public void setConversionFunction(ConversionFunction<S, T> conversionFunction) {
        this.conversionFunction = conversionFunction;
    }

    public MappingType getMappingType() {
        return mappingType;
    }

    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }
}
