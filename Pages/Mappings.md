# [AJAM: AnotherJavaAutoMapper](https://raphaeleckmayr.github.io/AnotherJavaAutoMapper)

# Mappings:
In some cases just matching up getters and setters by name is simply not enough. In these cases you can adjust AJAMs behavior by adding Mappings.

Mappings are just basic instructions for 2 specific classes which tell AJAM what to do in certain cases.

## Add Mappings
You can add Mappings to your Mapper in 3 different ways:

### In the MapperBuilder
```Java
Mapper mapper = new MapperBuilder().addMapping(
                new Mapping<>(PersonDto.class, PersonEntity.class)
                    .ignore(Person1::getName)
                    .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail))
                    .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber))
                    .mappingType(MappingType.LOOSE))
                .build()
```

### Manually to the Mapper instance
```java
mapper.addMapping(new Mapping<>(PersonDto.class, PersonEntity.class)
                    .ignore(PersonDto::getName)
                    .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail))
                    .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber))
                    .mappingType(MappingType.LOOSE));
```
### [Via Profiles](https://raphaeleckmayr.github.io/AnotherJavaAutoMapper/Pages/Profiles.html)


## Alter AJAMs's behaviour

### Ignoring behavior:
If AJAM automatically maps something you don't want you can simply ignore it via the Mapping:
```java
mapper.addMapping(new Mapping<>(PersonDto.class, PersonEntity.class)
                    .ignore(PersonDto::getName));
```

### Adding behavior
If AJAM does not map something you want to have mapped, you can add the behavior via the Mapping:
```java
mapper.addMapping(new Mapping<>(PersonDto.class, PersonEntity.class)
                    .forMember(PersonDto::getName + "@mail.com", opt->opt.mapTo(PersonEntity::seteMail)));
```

### Replacing behavior
Replacing AJAM's behavior can be achieved by combining ignoring and adding.
```java
mapper.addMapping(new Mapping<>(PersonDto.class, PersonEntity.class)
                    .ignore(PersonDto::getName)
                    .forMemeber(PersonDto:getLastName, opt -> opt.mapTo(PersonEntity::setName));
```

### Setting MappingType for Mapping
You can also set a MappingType which will be used for every mapping operation between the two classes of the Mapping.
```java
mapper.addMapping(new Mapping<>(PersonDto.class, PersonEntity.class).mappingType(MappingType.LOOSE));
```

## Types of Mapping
You can not only do OneToOne Mappings with AJAM. It is capible to handle different types of Mappings much like a database.
### OneToOne Mappings:
```java
new Mapping<>(PersonDto.class, PersonEntity.class)
                .forMember(PersonDto::getPhone, opt->opt.mapTo(PersonEntity::setPhoneNumber));
```

### ManyToOne Mappings
```java
new Mapping<>(PersonDto.class, PersonEntity.class)
                .forMember(x -> x.getFirstName() + " " + x.getLastName(),
                opt->opt.mapTo(PersonEntity::setName));
```

### OneToMany Mappings
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

## Deep mapping
AJAM also allows you to go deeper into your data structure by using Mappings. It doesn't do it automatically as this could cause recursions, however it is possible to achieve this behavior with Mappings.
```java
mapper.addMapping(new Mapping<>(PersonDto.class, PersonEntity.class)
                    .forMemeber(PersonDto:getAddressDto, opt -> opt.mapTo(x -> x.setAddress(mapper.map(Address.class, x))));
```
