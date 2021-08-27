# [AJAM - AnotherJavaAutoMapper](https://raphaeleckmayr.github.io/AnotherJavaAutoMapper)
# Getting Started:
AJAM is based on reflections and will math up the getters and setters of your classes by name. It will also correct their data types if necessary. By default it will only correct primitive datatypes (e.g. `String`, `int`, `long`, ...)
## Basics
To do a basic mapping operation you only have to create a new Mapper via the MapperBuilder and call the `map` method.
```java
Mapper mapper = new MapperBuilder().build();
mapper.map(TestClass.class, yourObject);
```
  
## Mappings
Mappings are instructions for AJAM to follow, for mapping operations between two specific classes. They can be added to the Mapper directly in two different ways.
```java
  Mapper mapper = new MapperBuilder().addMapping(
                new Mapping<>(PersonDto.class, PersonEntity.class)
                    .ignore(Person1::getName)
                    .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail))
                    .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber))
                    .mappingType(MappingType.LOOSE))
                .build()
```
or
```java
  mapper.addMapping(new Mapping<>(PersonDto.class, PersonEntity.class)
                    .ignore(Person1::getName)
                    .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail))
                    .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber))
                    .mappingType(MappingType.LOOSE));
```
More in depth explaination [here]("https://raphaeleckmayr.github.io/AnotherJavaAutoMapper/Pages/Mappings.html")

## MappingTypes
AJAM has 3 different `MappingTypes` with different meanings. MappingTypes tell AJAM which types it should automatically convert.

* LOOSE: [All present in ConversionManager](https://github.com/RaphaelEckmayr/AnotherJavaAutoMapper/blob/main/src/main/java/net/AJAM/Mapper/ConversionManager.java)
* MEDIUM: Only primitive data types
* STRICT: Do not convert

### You can set them in 3 different ways (ranked by priority)
Set default MappingType for the Mapper (Effects every mapping operation done with this Mapper):
```java
Mapper mapper = new MapperBuilder()
                .defaultMappingType(MappingType.STRICT)
                .build();
```
Set MappingType for Mapping (Only effects classes of Mapping):
```java
new Mapping<>(PersonDto.class, PersonEntity.class).mappingType(MappingType.STRICT);
```
Set MappingType for single operation:
```java
mapper.map(PersonDto.class, source, MappingType.LOOSE);
```

## Overloads of map()
### map(Class<T> targetType, S source)
  Used to create a new object of targetType and map everything from source to target.
  
### map(Class<T> targetType, S source, MappingType mappingType)
  Same as above, however you can override the default `MappingType` of the AJAM for this operation.
  
### map(T target, S source)
  Used to map onto an already existing object. This is overall faster and recommended when performance is a factor.

### map(T target, S source, MappingType mappingType)
  Same as above, however you can override the default `MappingType` of the AJAM for this operation.
  
## Profiles
Profiles are collections of Mappings which can be automatically detected on creation of the Mapper or added manually. They help to seperate your Mappings from the rest of your code.
Profiles need to extend the `Profile` class and have a parameterless constructor to work.
```java
public class TestProfile extends Profile
{
    public TestProfile()
    {
        addMapping(new Mapping<>(PersonDto.class, PersonEntity.class)
              .ignore(Person1::getName)
              .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail))
              .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber))
              .mappingType(MappingType.LOOSE);
    }
}
```
To automatically detect Profiles create your Mapper like this:
```java
  Mapper mapper = new MapperBuilder().detectProfiles().build();
```
And to add them manually use:
```java
  Mapper mapper = new MapperBuilder().addProfile(new TestProfile()).build();
```
or
```java
mapper.addProfile(TestProfile.class);
```
