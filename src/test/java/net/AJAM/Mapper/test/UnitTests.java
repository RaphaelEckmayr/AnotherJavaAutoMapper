package net.AJAM.Mapper.test;

import net.AJAM.Mapper.*;
import net.AJAM.Mapper.test.Profiles.IgnoreAllProfile;
import net.AJAM.Mapper.test.Profiles.TestProfile;
import net.AJAM.Mapper.test.Profiles.TestProfile1;
import net.AJAM.Mapper.test.Utils.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UnitTests {
    @Test
    @DisplayName("Usual way vs Mapper")
    public void testBasics() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");

        Person2 person2 = new Person2();
        person2.setName(person.getName());
        person2.seteMail(person.geteMail());
        person2.setId(person.getId() + "");
        person2.setRegistrationDate(person.registrationDate.toString());
        person2.setBirthDate(LocalDate.parse(person.getBirthDate()));
        person2.setPhone2(person.getPhone());

        Mapper mapper = new MapperBuilder()
                .addMapping(new Mapping<>(Person1.class, Person2.class).forMember(Person1::getPhone, options -> options.mapTo(Person2::setPhone2)))
                .build();
        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(person2, actual);
    }

    @Test
    @DisplayName("Test map method 1")
    public void testMap1() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");

        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder().build();
        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map method 2")
    public void testMap2() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder().build();

        Person2 actual = mapper.map(new Person2(), person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map method 3")
    public void testMap3() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null);

        Mapper mapper = new MapperBuilder().build();

        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map method 4")
    public void testMap4() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null);

        Mapper mapper = new MapperBuilder().build();

        Person2 actual = mapper.map(new Person2(), person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map async method 1")
    public void testMap1Async() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder().build();

        CompletableFuture<Person2> actual = mapper.mapAsync(Person2.class, person);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map async method 2")
    public void testMap2Async() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder().build();

        CompletableFuture<Person2> actual = mapper.mapAsync(new Person2(), person);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map async method 3")
    public void testMap3Async() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null);

        Mapper mapper = new MapperBuilder().build();

        CompletableFuture<Person2> actual = mapper.mapAsync(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map async method 4")
    public void testMapAsync4() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null);

        Mapper mapper = new MapperBuilder().build();

        CompletableFuture<Person2> actual = mapper.mapAsync(new Person2(), person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map list method 1")
    public void testMapList1() {
        List<Person1> personList = new ArrayList<>();
        personList.add(new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512"));
        personList.add(new Person1(1, "jon do", "idk@what", "2001-12-10", LocalDate.of(2018, 10, 1), ""));

        List<Person2> expected = new ArrayList<>();
        expected.add(new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null));
        expected.add(new Person2("1", "jon do", "idk@what", LocalDate.of(2001, 12, 10),
                "2018-10-01", null));

        Mapper mapper = new MapperBuilder().build();

        List<Person2> actual = mapper.mapList(Person2.class, personList, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map list method 2")
    public void testMapList2() {
        List<Person1> personList = new ArrayList<>();
        personList.add(new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512"));
        personList.add(new Person1(1, "jon do", "idk@what", "2001-12-10",
                LocalDate.of(2018, 10, 1), ""));

        List<Person2> expected = new ArrayList<>();
        expected.add(new Person2("12", "john doe", "john.doe@foo.bar", null, null, null));
        expected.add(new Person2("1", "jon do", "idk@what", null, null, null));

        Mapper mapper = new MapperBuilder().build();

        List<Person2> actual = mapper.mapList(Person2.class, personList);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map list async method 1")
    public void testMapListAsync1() {
        List<Person1> personList = new ArrayList<>();
        personList.add(new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512"));
        personList.add(new Person1(1, "jon do", "idk@what", "2001-12-10",
                LocalDate.of(2018, 10, 1), ""));

        List<Person2> expected = new ArrayList<>();
        expected.add(new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null));
        expected.add(new Person2("1", "jon do", "idk@what",
                LocalDate.of(2001, 12, 10), "2018-10-01", null));

        Mapper mapper = new MapperBuilder().build();

        CompletableFuture<List<Person2>> actual = mapper.mapListAsync(Person2.class, personList, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map list async method 2")
    public void testMapListAsync2() {
        List<Person1> personList = new ArrayList<>();
        personList.add(new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512"));
        personList.add(new Person1(1, "jon do", "idk@what", "2001-12-10",
                LocalDate.of(2018, 10, 1), ""));

        List<Person2> expected = new ArrayList<>();
        expected.add(new Person2("12", "john doe", "john.doe@foo.bar", null, null, null));
        expected.add(new Person2("1", "jon do", "idk@what", null, null, null));

        Mapper mapper = new MapperBuilder().build();

        CompletableFuture<List<Person2>> actual = mapper.mapListAsync(Person2.class, personList);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test strict mapping type as parameter")
    public void testMapStrictParameter() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2(null, "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder().build();
        Person2 actual = mapper.map(Person2.class, person, MappingType.STRICT);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test medium mapping type as parameter")
    public void testMapMediumParameter() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder().build();
        Person2 actual = mapper.map(Person2.class, person, MappingType.MEDIUM);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test loose mapping type as parameter")
    public void testMapLooseParameter() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null);

        Mapper mapper = new MapperBuilder().build();
        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test none mapping type as parameter")
    public void testMapNoneParameter() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder().build();
        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test strict mapping type as mapping")
    public void testMapStrictMapping() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2(null, "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder()
                .addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.STRICT))
                .build();
        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test medium mapping type as mapping")
    public void testMapMediumMapping() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new MapperBuilder().addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.MEDIUM)).build();

        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test loose mapping type as mapping")
    public void testMapLooseMapping() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null);

        Mapper mapper = new MapperBuilder()
                .addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.LOOSE))
                .build();

        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test MapperBuilder1")
    public void testMapperBuilder1()
    {
        Mapper mapper = new MapperBuilder().isSingleton().addProfile(new TestProfile1()).build();
        Mapper mapper1 = new MapperBuilder().isSingleton().build();

        Assertions.assertEquals(mapper.getProfiles(), mapper1.getProfiles());
    }

    @Test
    @DisplayName("Test MapperBuilder2")
    public void testMapperBuilder2()
    {
        Mapper mapper = new MapperBuilder().isSingleton().addMapping(new Mapping<>(Person1.class, Person2.class)).build();
        Mapper mapper1 = new MapperBuilder().build();

        Assertions.assertNotEquals(mapper.getMappings(), mapper1.getMappings());
    }

    @Test
    @DisplayName("Test ignore in profile")
    public void testIgnore() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", null);

        Mapper mapper = new MapperBuilder().addProfile(new TestProfile()).build();
        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test mapTo in profile")
    public void testMapTo() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", "+43 452 234234512");

        Mapper mapper = new MapperBuilder().addProfile(new TestProfile1()).build();
        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test MappingType hierarchy")
    public void testMappingTypeHierarchy() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        //hierarchy = Parameter, Mapping
        Mapper mapper = new MapperBuilder()
                .addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.STRICT))
                .build();
        Person2 actual = mapper.map(Person2.class, person, MappingType.MEDIUM);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test ignore and remapping")
    public void testIgnoreAndRemap() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", null, "john doe",
                LocalDate.of(2004, 12, 12), "2020-12-10", "+43 452 234234512");

        Mapper mapper = new MapperBuilder()
                .addMapping(new Mapping<>(Person1.class, Person2.class)
                .ignore(Person1::getName)
                .forMember(Person1::getName, opt -> opt.mapTo(Person2::seteMail))
                .forMember(Person1::getPhone, opt -> opt.mapTo(Person2::setPhone2))
                .mappingType(MappingType.LOOSE))
                .build();

        Person2 actual = mapper.map(Person2.class, person);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test ignore and remapping reverse order")
    public void testIgnoreAndRemapReverseOrder() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", null, "john doe",
                LocalDate.of(2004, 12, 12), "2020-12-10", "+43 452 234234512");

        Mapper mapper = new MapperBuilder().addMapping(new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getName, opt -> opt.mapTo(Person2::seteMail))
                .forMember(Person1::getPhone, opt -> opt.mapTo(Person2::setPhone2))
                .ignore(Person1::getName)
                .mappingType(MappingType.LOOSE))
                .build();

        Person2 actual = mapper.map(Person2.class, person);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test add mapping on Mapper")
    public void testAddMapping() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12, 12), "2020-12-10", "+43 452 234234512");

        Mapper mapper = new MapperBuilder().build();

        Mapping<Person1, Person2> mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt -> opt.mapTo(Person2::setPhone2));
        mapper.addMapping(mapping);

        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test merging mappings")
    public void mergeMapping() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", null,
                LocalDate.of(2004, 12, 12), "2020-12-10", "+43 452 234234512");

        Mapping<Person1, Person2> mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt -> opt.mapTo(Person2::setPhone2)).mappingType(MappingType.STRICT);
        Mapping<Person1, Person2> mapping1 = new Mapping<>(Person1.class, Person2.class).ignore(Person1::geteMail).mappingType(MappingType.LOOSE);

        Mapper mapper = new MapperBuilder().addMappings(mapping, mapping1).build();

        Person2 actual = mapper.map(Person2.class, person);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test add and remove mapping on Mapper")
    public void testAddRemoveMapping() {
        Mapper mapper = new MapperBuilder().build();

        Mapping<Person1, Person2> mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt -> opt.mapTo(Person2::setPhone2));
        mapper.addMapping(mapping);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));

        mapper.removeMapping(mapping);

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
    }

    @Test
    @DisplayName("Test add and remove mappings on Mapper 1")
    public void testAddRemoveMappings1() {
        Mapper mapper = new MapperBuilder().build();

        Mapping<Person1, Person2> mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt -> opt.mapTo(Person2::setPhone2));

        Mapping<Person1, Person2> mapping2 = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::geteMail, MappingOption::ignore);

        List<Mapping<?, ?>> mappings = new ArrayList<>();
        mappings.add(mapping);
        mappings.add(mapping2);

        mapper.addMappings(mappings);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getMappings().contains(mapping2));

        mapper.removeMappings(mappings);

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getMappings().contains(mapping2));
    }

    @Test
    @DisplayName("Test add and remove mappings on Mapper 2")
    public void testAddRemoveMappings2() {
        Mapper mapper = new MapperBuilder().build();

        Mapping<Person1, Person2> mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt -> opt.mapTo(Person2::setPhone2));

        Mapping<Person1, Person2> mapping2 = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::geteMail, MappingOption::ignore);

        mapper.addMappings(mapping, mapping2);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getMappings().contains(mapping2));

        mapper.removeMappings(mapping, mapping2);

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getMappings().contains(mapping2));
    }

    @Test
    @DisplayName("Test remove Profile")
    public void testRemoveProfile() {
        Mapper mapper = new MapperBuilder().autoDetectProfiles().build();

        Profile prof1 = new IgnoreAllProfile();
        Mapping<?, ?> mapping = prof1.getMappings().get(0);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getProfiles().contains(new IgnoreAllProfile()));

        mapper.removeProfile(IgnoreAllProfile.class);

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getProfiles().contains(new IgnoreAllProfile()));
    }

    @Test
    @DisplayName("Test add and removeProfile")
    public void testAddRemoveProfile() {
        Mapper mapper = new MapperBuilder().build();

        Profile prof1 = new IgnoreAllProfile();
        mapper.addProfile(prof1);

        Mapping<?, ?> mapping = prof1.getMappings().get(0);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getProfiles().contains(new IgnoreAllProfile()));

        mapper.removeProfile(new IgnoreAllProfile());

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getProfiles().contains(new IgnoreAllProfile()));
    }

    @Test
    @DisplayName("Test add and remove Profiles 1")
    public void testAddRemoveProfiles1() {
        Mapper mapper = new MapperBuilder().build();
        mapper.addProfiles(new IgnoreAllProfile(), new TestProfile());

        Profile prof1 = new IgnoreAllProfile();
        Mapping<?, ?> mapping = prof1.getMappings().get(0);

        Profile prof2 = new TestProfile();
        Mapping<?, ?> mapping2 = prof2.getMappings().get(0);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getMappings().contains(mapping2));
        Assertions.assertTrue(mapper.getProfiles().contains(new IgnoreAllProfile()));
        Assertions.assertTrue(mapper.getProfiles().contains(new TestProfile()));

        mapper.removeProfiles(new IgnoreAllProfile(), new TestProfile());

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getMappings().contains(mapping2));
        Assertions.assertFalse(mapper.getProfiles().contains(new IgnoreAllProfile()));
        Assertions.assertFalse(mapper.getProfiles().contains(new TestProfile()));
    }

    @Test
    @DisplayName("Test add and remove Profiles 2")
    public void testAddRemoveProfiles2() {
        Mapper mapper = new MapperBuilder().build();

        List<Profile> profiles = new ArrayList<>();
        profiles.add(new IgnoreAllProfile());
        profiles.add(new TestProfile());

        mapper.addProfiles(profiles);

        Profile prof1 = new IgnoreAllProfile();
        Mapping<?, ?> mapping = prof1.getMappings().get(0);

        Profile prof2 = new TestProfile();
        Mapping<?, ?> mapping2 = prof2.getMappings().get(0);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getMappings().contains(mapping2));
        Assertions.assertTrue(mapper.getProfiles().contains(profiles.get(0)));
        Assertions.assertTrue(mapper.getProfiles().contains(profiles.get(1)));

        mapper.removeProfiles(profiles);

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getMappings().contains(mapping2));
        Assertions.assertFalse(mapper.getProfiles().contains(profiles.get(0)));
        Assertions.assertFalse(mapper.getProfiles().contains(profiles.get(1)));
    }

    @Test
    @DisplayName("Test one to many")
    public void testOneToMany() {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");
        Person3 expected = new Person3(12, "john", "doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");

        Mapper mapper = new MapperBuilder()
                .addMapping(new Mapping<>(Person1.class, Person3.class).forMembers(x -> x.getName().split(" "),
                opt -> opt.mapTo(Person3::setFirstname), x -> x.mapTo(Person3::setLastname)))
                .build();

        Person3 actual = mapper.map(Person3.class, person, MappingType.STRICT);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Map in Mapping")
    public void testMapInMapping()
    {
        List<Detail1> details1 = new ArrayList<>();
        details1.add(new Detail1(1, "Something"));
        details1.add(new Detail1(4, "Something else"));

        List<Detail2> details2 = new ArrayList<>();
        details2.add(new Detail2(1, "Something"));
        details2.add(new Detail2(4, "Something else"));

        Thing1 thing1 = new Thing1(12, "thing", details1);
        Thing2 expected = new Thing2(12, "thing", details2);


        Mapper mapper = new MapperBuilder().build();

        mapper.addMapping(new Mapping<>(Thing1.class, Thing2.class)
                .forMember(Thing1::getDetails, options -> options.mapTo((x,y) -> x.setDetails(mapper.mapList(Detail2.class, y)))));

        Thing2 actual = mapper.map(Thing2.class, thing1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Map Collections")
    public void testMapCollections()
    {
        List<String> list1 = Arrays.asList("1", "2", "3");
        Set<String> set1 = new HashSet<String>() {{add("1"); add("2"); add("3");}};
        Map<String, Integer> map1= new HashMap<String, Integer>() {{put("1", 1); put("2",2); put("3", 3);}};

        List<String> list2 = Arrays.asList("1", "2", "3");
        Set<Integer> set2 = new HashSet<Integer>() {{add(1); add(2); add(3);}};
        Map<Integer, String> map2= new HashMap<Integer, String>() {{put(1, "1"); put(2,"2"); put(3, "3");}};

        CollectionsTestclass1 testclass = new CollectionsTestclass1(list1, set1, map1);
        CollectionsTestclass2 expected = new CollectionsTestclass2(list2, set2, map2);

        Mapper mapper = new MapperBuilder().build();
        CollectionsTestclass2 actual = mapper.map(CollectionsTestclass2.class, testclass);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Map in Conversion")
    public void testMapInConversion()
    {
        List<Detail1> details1 = new ArrayList<>();
        details1.add(new Detail1(1, "Something"));
        details1.add(new Detail1(4, "Something else"));

        List<Detail2> details2 = new ArrayList<>();
        details2.add(new Detail2(1, "Something"));
        details2.add(new Detail2(4, "Something else"));

        Thing1 thing1 = new Thing1(12, "thing", details1);
        Thing2 expected = new Thing2(12, "thing", details2);


        Mapper mapper = new MapperBuilder().build();

        ConversionManager.addConversion(new Conversion<>(Detail1.class, Detail2.class, MappingType.MEDIUM, x -> mapper.map(Detail2.class, x)));

        Thing2 actual = mapper.map(Thing2.class, thing1, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }
}
