# [AJAM: AnotherJavaAutoMapper](https://raphaeleckmayr.github.io/AnotherJavaAutoMapper)
# Profiles:
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

### Detect Automatically:
This is operation is really expensive and slow for the first time. It is cached after the first initialization.
```java
  Mapper mapper = new MapperBuilder().detectProfiles().build();
```
### Add manually:
```java
  Mapper mapper = new MapperBuilder().addProfile(new TestProfile()).build();
```
or
```java
mapper.addProfile(TestProfile.class);
```

### Remove Profile:
You can also remove Profiles during runtime. 
```java
mapper.removeProfile(TestProfile.class);
```
