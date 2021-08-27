# [AJAM: AnotherJavaAutoMapper](https://raphaeleckmayr.github.io/AnotherJavaAutoMapper)
# Conversions:
Conversions are basically global mappings. They are shared between all instanced of `Mapper` and are supposed to convert types between the mapped classes.
They are categorized by their [MappingType](http://https://raphaeleckmayr.github.io/AnotherJavaAutoMapper/Pages/MappingTypes.html) and give AJAM instructions on how to convert types.

## Add Conversion:
```java
Mapper.addConversion(new Conversion<>(AddressDto.class, String.class, MappingType.LOOSE, x -> x.getStreet() + ", " x.getPostalCode());
```

## Remove Conversion:
```java
Mapper.removeConversion(new Conversion<>(int.class, String.class));
```

## Deep Mapping via Conversion:
```java
Mapper.addConversion(new Conversion<>(Detail1.class, Detail2.class, MappingType.MEDIUM, x -> mapper.map(Detail2.class, x)));
```
## Standard Conversion:
By default there are some basic Conversions already defined in the [ConversionManager](https://github.com/RaphaelEckmayr/AnotherJavaAutoMapper/blob/Feature/src/main/java/net/AJAM/Mapper/ConversionManager.java).
