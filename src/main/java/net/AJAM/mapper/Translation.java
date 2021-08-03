package net.AJAM.mapper;

public abstract class Translation<S,T,V>
{
    protected abstract void translate(S source, T target);
}
