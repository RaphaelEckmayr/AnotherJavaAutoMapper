# AJAM: AnotherJavaAutoMapper

AJAM is a more light weight and thread safe object mapping library for Java. It also includes a refactoring safe API for handling special cases.

## Usage
### Basics
To do a very basic mapping operation you only need to do the following:
```java
Mapper mapper = new Mapper();
mapper.map(TestClass.class, yourObject);
```
It will automatically detect the getters and setters of your classes and will match up them by name and correct their datatypes if they don't match up (Default only primitive datatypes).


### MappingTypes
If you want the AJAM to not correct datatypes at all or want it to detect other commonly used datatypes aswell you can adjust the MappingType
```java
Mapper mapper = new Mapper();
mapper.map(TestClass.class, yourObject, MappingType.LOOSE);
```
There are 3 different values of MappingTypes:
1. STRICT: Don't convert any datatypes
2. MEDIUM: Only convert primitive datatypes
3. LOOSE: Convert commonly used datatypes (Could lead to unexpected behavior)

### Mappings
In some cases just matching getters and setters by name is simply not enough. In these cases you can adjust AJAMs behabior by adding Mappings.

Mappings are just basic instructions which tell AJAM what to do in certain cases.

For example:
```Java
Mapper mapper = new Mapper();
Mapping mapping = new Mapping<>(PersonDto.class, PersonEntity.class)
                .ignore(Person1::getName)
                .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail))
                .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber))
                .mappingType(MappingType.LOOSE);
mapper.addMapping(mapping);
```
If you don't want AJAMs default behavior in a certain case you can use `.ignore(x -> x.getWhatever())` and overwrite (or add) it by using `.forMember(x -> x.getWhatever(), opt -> opt.mapTo((x,y) -> x.setWhatever2(y)))`

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
In this case you can also skip elements of the array
```java
new Mapping<>(PersonEntity.class, PersonDto.class)
                .forMembers(x -> x.getName.split(" "),
                opt->opt.mapTo(PersonDto::setFirstName), opt->opt.ignore());
```

### Profiles
Profiles are just a collection of Mappings which will automatically be loaded when creating a new Mapper with `new Mapper()`.

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
A custom profile must always extend `Profile` otherwise it will be ignored. You can also ignore all Profiles on creation of the Mapper by using `new Mapper(false)` or add them by hand `mapper.addProfile(new CustomProfile())`.

## Saving processing resources
There are several ways to save processing resources at the cost of usability. That's because usually reflections are always slower than creating an object the normal way.

1. Use `mapper.map(new TestClass, yourObject)` instead of `mapper.map(TestClass.class, yourObject, MappingType.LOOSE)`
2. Add profiles and mappings manually by using `new Mapper(false)` and adding Profiles by `mapper.addProfile(new TestProfile)`
3. If it's still to slow, do it the old fashion way
