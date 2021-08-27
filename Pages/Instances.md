# [AJAM - AnotherJavaAutoMapper](https://raphaeleckmayr.github.io/AnotherJavaAutoMapper)
# Instances:
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
You can also mix them. In this case mapper and mapper1 are the same instance and mapper2 is a different one.
