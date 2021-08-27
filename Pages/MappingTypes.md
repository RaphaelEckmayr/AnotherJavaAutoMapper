# [AJAM: AnotherJavaAutoMapper](https://raphaeleckmayr.github.io/AnotherJavaAutoMapper)
# MappingTypes:
AJAM has 3 different `MappingTypes` with different meanings. MappingTypes tell AJAM which types it should automatically convert within the given classes.
* LOOSE
* MEDIUM
* STRICT

The behavior is defined in the [ConversionManager](https://github.com/RaphaelEckmayr/AnotherJavaAutoMapper/blob/main/src/main/java/net/AJAM/Mapper/ConversionManager.java) and can also be modified.
A MappingType always includes all MappingTypes above. So if a Conversion is `MappingType.MEDIUM` it will also be executed when the MappingType of an operation is `LOOSE`.

## You can set the MappingType in 3 different ways (ranked by priority)
Set default MappingType for the Mapper (Effects every mapping operation done with this Mapper):
```java
Mapper mapper = new MapperBuilder()
                .defaultMappingType(MappingType.STRICT)
                .build();
```
Set MappingType for Mapping (Only effects classes of Mapping)
```java
new Mapping<>(PersonDto.class, PersonEntity.class).mappingType(MappingType.STRICT);
```
Set MappingType for single mapping operation
```java
mapper.map(PersonDto.class, source, MappingType.STICT);
```
