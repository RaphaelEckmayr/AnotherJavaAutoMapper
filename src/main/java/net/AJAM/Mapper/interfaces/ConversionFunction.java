package net.AJAM.Mapper.Interfaces;

public interface ConversionFunction <S,T>
{
    T convert(S bean);
}
