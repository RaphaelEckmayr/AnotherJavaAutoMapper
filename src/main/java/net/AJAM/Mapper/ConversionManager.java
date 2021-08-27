package net.AJAM.Mapper;

import net.AJAM.Mapper.Interfaces.ConversionFunction;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConversionManager {
    private static final List<Conversion<?, ?>> conversions = Collections.synchronizedList(new ArrayList<>());

    protected static Conversion<?, ?> getConversion(Class<?> from, Class<?> to, MappingType mappingType) {
        for (Conversion<?, ?> conv : conversions) {
            if (from == conv.getFrom() && to == conv.getTo() &&
                    (mappingType == conv.getMappingType() || mappingType == MappingType.LOOSE)) {
                return conv;
            }
        }

        return null;
    }

    protected static List<Conversion<?, ?>> getConversions() {
        return conversions;
    }

    protected static void addConversion(Conversion<?,?> conversion){
        conversions.add(conversion);
    }

    protected static void addConversions(List<Conversion<?,?>> conversion){
        conversions.addAll(conversion);
    }

    protected static void removeConversion(Conversion<?,?> conversion){
        conversions.remove(conversion);
    }

    protected static void initLooseConversions() {
        conversions.add(new Conversion<>(String.class, Integer.class, MappingType.MEDIUM, Integer::parseInt));
        conversions.add(new Conversion<>(String.class, Double.class, MappingType.MEDIUM, Double::parseDouble));
        conversions.add(new Conversion<>(String.class, Long.class, MappingType.MEDIUM, Long::parseLong));
        conversions.add(new Conversion<>(String.class, Float.class, MappingType.MEDIUM, Float::parseFloat));
        conversions.add(new Conversion<>(String.class, Short.class, MappingType.MEDIUM, Short::parseShort));
        conversions.add(new Conversion<>(String.class, int.class, MappingType.MEDIUM, Integer::parseInt));
        conversions.add(new Conversion<>(String.class, double.class, MappingType.MEDIUM, Double::parseDouble));
        conversions.add(new Conversion<>(String.class, long.class, MappingType.MEDIUM, Long::parseLong));
        conversions.add(new Conversion<>(String.class, float.class, MappingType.MEDIUM, Float::parseFloat));
        conversions.add(new Conversion<>(String.class, short.class, MappingType.MEDIUM, Short::parseShort));
        conversions.add(new Conversion<>(String.class, boolean.class, MappingType.MEDIUM, Boolean::parseBoolean));
        conversions.add(new Conversion<>(String.class, Boolean.class, MappingType.MEDIUM, Boolean::parseBoolean));

        conversions.add(new Conversion<>(String.class, LocalDate.class, MappingType.LOOSE, LocalDate::parse));
        conversions.add(new Conversion<>(String.class, Instant.class, MappingType.LOOSE, Instant::parse));
        conversions.add(new Conversion<>(String.class, LocalDateTime.class, MappingType.LOOSE, LocalDateTime::parse));

        //Integer to ...
        conversions.add(new Conversion<>(Integer.class, String.class, MappingType.MEDIUM, (x) -> x + ""));
        conversions.add(new Conversion<>(int.class, String.class, MappingType.MEDIUM, (x) -> x + ""));
        conversions.add(new Conversion<>(int.class, Boolean.class, MappingType.MEDIUM, (x) -> x > 0));
        conversions.add(new Conversion<>(Integer.class, Boolean.class, MappingType.MEDIUM, (x) -> x > 0));
        conversions.add(new Conversion<>(int.class, boolean.class, MappingType.MEDIUM, (x) -> x > 0));
        conversions.add(new Conversion<>(Integer.class, boolean.class, MappingType.MEDIUM, (x) -> x > 0));

        //Double to ...
        conversions.add(new Conversion<>(Double.class, String.class, MappingType.MEDIUM, (x) -> x + ""));
        conversions.add(new Conversion<>(double.class, String.class, MappingType.MEDIUM, (x) -> x + ""));

        //Float to ...
        conversions.add(new Conversion<>(Float.class, String.class, MappingType.MEDIUM, (x) -> x + ""));
        conversions.add(new Conversion<>(float.class, String.class, MappingType.MEDIUM, (x) -> x + ""));

        //Long to ...
        conversions.add(new Conversion<>(Long.class, String.class, MappingType.MEDIUM, (x) -> x + ""));
        conversions.add(new Conversion<>(Long.class, LocalDate.class, MappingType.LOOSE, (x) -> Instant.ofEpochMilli(x).atZone(ZoneId.systemDefault()).toLocalDate()));
        conversions.add(new Conversion<>(Long.class, LocalDateTime.class, MappingType.LOOSE, (x) -> Instant.ofEpochMilli(x).atZone(ZoneId.systemDefault()).toLocalDateTime()));
        conversions.add(new Conversion<>(Long.class, Date.class, MappingType.LOOSE, Date::new));
        conversions.add(new Conversion<>(Long.class, Instant.class, MappingType.LOOSE, Instant::ofEpochMilli));
        conversions.add(new Conversion<>(Long.class, ZonedDateTime.class, MappingType.LOOSE, (x) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(x), ZoneId.systemDefault())));

        conversions.add(new Conversion<>(long.class, String.class, MappingType.MEDIUM, (x) -> x + ""));
        conversions.add(new Conversion<>(long.class, LocalDate.class, MappingType.LOOSE, (x) -> Instant.ofEpochMilli(x).atZone(ZoneId.systemDefault()).toLocalDate()));
        conversions.add(new Conversion<>(long.class, LocalDateTime.class, MappingType.LOOSE, (x) -> Instant.ofEpochMilli(x).atZone(ZoneId.systemDefault()).toLocalDateTime()));
        conversions.add(new Conversion<>(long.class, Date.class, MappingType.LOOSE, Date::new));
        conversions.add(new Conversion<>(long.class, Instant.class, MappingType.LOOSE, Instant::ofEpochMilli));
        conversions.add(new Conversion<>(long.class, ZonedDateTime.class, MappingType.LOOSE, (x) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(x), ZoneId.systemDefault())));

        //LocalDate and LocalDateTime to ...
        conversions.add(new Conversion<>(LocalDate.class, String.class, MappingType.LOOSE, (x) -> (x).format(DateTimeFormatter.ISO_DATE)));
        conversions.add(new Conversion<>(LocalDateTime.class, String.class, MappingType.LOOSE, (x) -> (x).format(DateTimeFormatter.ISO_DATE)));
        conversions.add(new Conversion<>(LocalDateTime.class, Long.class, MappingType.LOOSE, (x) -> x.toInstant(ZoneOffset.UTC).toEpochMilli()));
        conversions.add(new Conversion<>(LocalDateTime.class, long.class, MappingType.LOOSE, (x) -> x.toInstant(ZoneOffset.UTC).toEpochMilli()));

        //Instant to ...
        conversions.add(new Conversion<>(Instant.class, String.class, MappingType.LOOSE, Instant::toString));
        conversions.add(new Conversion<>(Instant.class, Long.class, MappingType.LOOSE, Instant::toEpochMilli));
        conversions.add(new Conversion<>(Instant.class, long.class, MappingType.LOOSE, Instant::toEpochMilli));

        //ZonedDateTime to ...
        conversions.add(new Conversion<>(ZonedDateTime.class, long.class, MappingType.LOOSE, (x) -> x.toInstant().toEpochMilli()));
        conversions.add(new Conversion<>(ZonedDateTime.class, Long.class, MappingType.LOOSE, (x) -> x.toInstant().toEpochMilli()));
        conversions.add(new Conversion<>(ZonedDateTime.class, String.class, MappingType.LOOSE, (x) -> x.format(DateTimeFormatter.ISO_DATE)));
    }
}
