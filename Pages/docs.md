## Usage
### Basics
To do a very basic mapping operation you only have to do the following:
```java
Mapper mapper = new MapperBuilder().build();
mapper.map(TestClass.class, yourObject);
```
It will automatically detect the getters and setters of your classes and will match them up by name and correct their datatypes if they don't match up (Default only primitive datatypes).


### MappingTypes
If you want AJAM to not correct datatypes at all, or want it to detect other commonly used datatypes aswell, you can adjust the MappingType
```java
Mapper mapper = new MapperBuilder().build();
mapper.map(TestClass.class, yourObject, MappingType.LOOSE);
```
There are 3 different values of MappingTypes:
1. STRICT: Don't convert any datatypes
2. MEDIUM: Only convert primitive datatypes (Default)
3. LOOSE: Convert commonly used datatypes (Could lead to unexpected behavior)

### Mappings
In some cases just matching up getters and setters by name is simply not enough. In these cases you can adjust AJAMs behavior by adding Mappings.

Mappings are just basic instructions which tell AJAM what to do in certain cases.

For example:
```Java
Mapper mapper = new MapperBuilder().addMapping(
                new Mapping<>(PersonDto.class, PersonEntity.class)
                    .ignore(Person1::getName)
                    .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail))
                    .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber))
                    .mappingType(MappingType.LOOSE))
                .build()
```
or
```
mapper.addMapping(new Mapping<>(PersonDto.class, PersonEntity.class)
                    .ignore(Person1::getName)
                    .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail))
                    .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber))
                    .mappingType(MappingType.LOOSE));
```
If you don't want AJAMs default behavior in a certain case you can use `.ignore(x -> x.getWhatever())` and overwrite (or add) by using `.forMember(x -> x.getWhatever(), opt -> opt.mapTo((x,y) -> x.setWhatever2(y)))`

#### OneToOne Mappings
```java
new Mapping<>(PersonDto.class, PersonEntity.class)
                .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber));
```

#### ManyToOne Mappings
```java
new Mapping<>(PersonDto.class, PersonEntity.class)
                .forMember(x -> x.getFirstName() + " " + x.getLastName(),
                opt->opt.mapTo(PersonEntity::setName));
```

#### OneToMany Mappings
```java
new Mapping<>(PersonEntity.class, PersonDto.class)
                .forMembers(x -> x.getName.split(" "),
                opt->opt.mapTo(PersonDto::setFirstName), opt->opt.mapTo(PersonDto::setLastName));
```
You can also skip elements of the array
```java
new Mapping<>(PersonEntity.class, PersonDto.class)
                .forMembers(x -> x.getName.split(" "),
                opt->opt.mapTo(PersonDto::setFirstName), opt->opt.ignore());
```

### Profiles
Profiles are just a collection of Mappings which can be automatically loaded when creating a new Mapper with `new MapperBuilder().detectProfiles().build()`. It is turned off by default for performance reasons.

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
A custom profile must always extend `Profile` otherwise it will be ignored.

You can also add them manually for better performance:
```java
Mapper mapper = new MapperBuilder().addProfile(new TestProfile()).build();
```
or
```java
mapper.addProfile(TestProfile.class);
```

### Instances
You can use AJAM either as a singleton have multiple instances.
```java
Mapper mapper = new MapperBuilder().isSingleton().build();
        mapper.addProfile(TestProfile.class);
Mapper mapper1 = new MapperBuilder().isSingleton().build();
```
In this case mapper and mapper1 are the same instance of `Mapper`and share all profiles, mappings and default `MappingType`.

```java
Mapper mapper = new MapperBuilder().build();
        mapper.addProfile(TestProfile.class);
Mapper mapper1 = new MapperBuilder().build();
```
On the other hand you can have completly different instances of Mapper with different mappings, which makes sense when your mappings are coliding and AJAM has unwanted behavior.

```java
Mapper mapper = new MapperBuilder().isSingleton().build();
        mapper.addProfile(TestProfile.class);
Mapper mapper1 = new MapperBuilder().isSingleton().build();
Mapper mapper2 = new MapperBuilder().build();
```
