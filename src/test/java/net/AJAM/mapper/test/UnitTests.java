package net.AJAM.mapper.test;

import net.AJAM.mapper.*;
import net.AJAM.mapper.test.Profiles.IgnoreAllProfile;
import net.AJAM.mapper.test.Profiles.TestProfile;
import net.AJAM.mapper.test.Profiles.TestProfile1;
import net.AJAM.mapper.test.Utils.Person1;
import net.AJAM.mapper.test.Utils.Person2;
import net.AJAM.mapper.test.Utils.Person3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UnitTests
{
    @Test
    @DisplayName("Usual way vs Mapper")
    public void testBasics()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");

        Person2 person2 = new Person2();
        person2.setName(person.getName());
        person2.seteMail(person.geteMail());
        person2.setId(person.getId() + "");
        person2.setRegistrationDate(person.registrationDate.toString());
        person2.setBirthDate(LocalDate.parse(person.getBirthDate()));
        person2.setPhone2(person.getPhone());

        Mapper mapper = new Mapper(false);
        mapper.addMapping(new Mapping<>(Person1.class, Person2.class).forMember(Person1::getPhone, options -> options.mapTo(Person2::setPhone2)));
        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(person2, actual);
    }

    @Test
    @DisplayName("Test map method 1")
    public void testMap1()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");

        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);
        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map method 2")
    public void testMap2()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);

        Person2 actual = mapper.map(new Person2(), person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map method 3")
    public void testMap3()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", null);

        Mapper mapper = new Mapper(false);

        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map method 4")
    public void testMap4()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", null);

        Mapper mapper = new Mapper(false);

        Person2 actual = mapper.map(new Person2(), person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map async method 1")
    public void testMap1Async()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);

        CompletableFuture<Person2> actual = mapper.mapAsync(Person2.class, person);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map async method 2")
    public void testMap2Async()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);

        CompletableFuture<Person2> actual = mapper.mapAsync(new Person2(), person);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map async method 3")
    public void testMap3Async()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", null);

        Mapper mapper = new Mapper(false);

        CompletableFuture<Person2> actual = mapper.mapAsync(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map async method 4")
    public void testMapAsync4()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", null);

        Mapper mapper = new Mapper(false);

        CompletableFuture<Person2> actual = mapper.mapAsync(new Person2(), person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map list method 1")
    public void testMapList1()
    {
        List<Person1> personList = new ArrayList<>();
        personList.add(new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512"));
        personList.add(new Person1(1, "jon do", "idk@what", "2001-12-10", LocalDate.of(2018, 10,1), ""));

        List<Person2> expected = new ArrayList<>();
        expected.add(new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", null));
        expected.add(new Person2("1", "jon do", "idk@what", LocalDate.of(2001, 12,10),
                "2018-10-01", null));

        Mapper mapper = new Mapper(false);

        List<Person2> actual = mapper.mapList(Person2.class, personList, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map list method 2")
    public void testMapList2()
    {
        List<Person1> personList = new ArrayList<>();
        personList.add(new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512"));
        personList.add(new Person1(1, "jon do", "idk@what", "2001-12-10",
                LocalDate.of(2018, 10,1), ""));

        List<Person2> expected = new ArrayList<>();
        expected.add(new Person2("12", "john doe", "john.doe@foo.bar", null, null, null));
        expected.add(new Person2("1", "jon do", "idk@what", null, null, null));

        Mapper mapper = new Mapper(false);

        List<Person2> actual = mapper.mapList(Person2.class, personList);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test map list async method 1")
    public void testMapListAsync1()
    {
        List<Person1> personList = new ArrayList<>();
        personList.add(new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512"));
        personList.add(new Person1(1, "jon do", "idk@what", "2001-12-10",
                LocalDate.of(2018, 10,1), ""));

        List<Person2> expected = new ArrayList<>();
        expected.add(new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", null));
        expected.add(new Person2("1", "jon do", "idk@what",
                LocalDate.of(2001, 12,10), "2018-10-01", null));

        Mapper mapper = new Mapper(false);

        CompletableFuture<List<Person2>> actual = mapper.mapListAsync(Person2.class, personList, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test map list async method 2")
    public void testMapListAsync2()
    {
        List<Person1> personList = new ArrayList<>();
        personList.add(new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512"));
        personList.add(new Person1(1, "jon do", "idk@what", "2001-12-10",
                LocalDate.of(2018, 10,1), ""));

        List<Person2> expected = new ArrayList<>();
        expected.add(new Person2("12", "john doe", "john.doe@foo.bar", null, null, null));
        expected.add(new Person2("1", "jon do", "idk@what", null, null, null));

        Mapper mapper = new Mapper(false);

        CompletableFuture<List<Person2>> actual = mapper.mapListAsync(Person2.class, personList);

        Assertions.assertEquals(expected, actual.join());
    }

    @Test
    @DisplayName("Test strict mapping type as parameter")
    public void testMapStrictParameter()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2(null, "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);
        Person2 actual = mapper.map(Person2.class, person, MappingType.STRICT);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test medium mapping type as parameter")
    public void testMapMediumParameter()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);
        Person2 actual = mapper.map(Person2.class, person, MappingType.MEDIUM);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test loose mapping type as parameter")
    public void testMapLooseParameter()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", null);

        Mapper mapper = new Mapper(false);
        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test none mapping type as parameter")
    public void testMapNoneParameter()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);
        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test strict mapping type as mapping")
    public void testMapStrictMapping()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2(null, "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);
        mapper.addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.STRICT));
        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test medium mapping type as mapping")
    public void testMapMediumMapping()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);
        mapper.addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.MEDIUM));
        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test loose mapping type as mapping")
    public void testMapLooseMapping()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", null);

        Mapper mapper = new Mapper(false);
        mapper.addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.LOOSE));
        Person2 actual = mapper.map(Person2.class, person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test ignore in profile")
    public void testIgnore()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", LocalDate.of(2004, 12,12), "2020-12-10", null);

        Mapper mapper = new Mapper(false);
        mapper.addProfile(TestProfile.class);
        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test mapTo in profile")
    public void testMapTo()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", "+43 452 234234512");

        Mapper mapper = new Mapper(false);
        mapper.addProfile(TestProfile1.class);
        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test MappingType hierarchy")
    public void testMappingTypeHierarchy()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        //hierarchy = Parameter, Mapping
        Mapper mapper = new Mapper(false);
        mapper.addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.STRICT));
        Person2 actual = mapper.map(Person2.class, person, MappingType.MEDIUM);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test add mapping on Mapper")
    public void testAddMapping()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar",
                LocalDate.of(2004, 12,12), "2020-12-10", "+43 452 234234512");

        Mapper mapper = new Mapper(false);

        Mapping mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt->opt.mapTo(Person2::setPhone2));
        mapper.addMapping(mapping);

        Person2 actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test add and remove mapping on Mapper")
    public void testAddRemoveMapping()
    {
        Mapper mapper = new Mapper(false);

        Mapping mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt->opt.mapTo(Person2::setPhone2));
        mapper.addMapping(mapping);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));

        mapper.removeMapping(mapping);

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
    }

    @Test
    @DisplayName("Test add and remove mappings on Mapper 1")
    public void testAddRemoveMappings1()
    {
        Mapper mapper = new Mapper(false);

        Mapping mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt->opt.mapTo(Person2::setPhone2));

        Mapping mapping2 = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::geteMail, MappingOption::ignore);

        List<Mapping<?,?>> mappings = new ArrayList<>();
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
    public void testAddRemoveMappings2()
    {
        Mapper mapper = new Mapper(false);

        Mapping mapping = new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt->opt.mapTo(Person2::setPhone2));

        Mapping mapping2 = new Mapping<>(Person1.class, Person2.class)
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
    public void testRemoveProfile()
    {
        Mapper mapper = new Mapper(true);

        Profile prof1 = new IgnoreAllProfile();
        Mapping mapping = prof1.getMappings().get(0);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getProfiles().contains(new IgnoreAllProfile()));

        mapper.removeProfile(IgnoreAllProfile.class);

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getProfiles().contains(new IgnoreAllProfile()));
    }

    @Test
    @DisplayName("Test add and removeProfile")
    public void testAddRemoveProfile()
    {
        Mapper mapper = new Mapper(false);
        mapper.addProfile(new IgnoreAllProfile());

        Profile prof1 = new IgnoreAllProfile();
        Mapping mapping = prof1.getMappings().get(0);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getProfiles().contains(new IgnoreAllProfile()));

        mapper.removeProfile(new IgnoreAllProfile());

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getProfiles().contains(new IgnoreAllProfile()));
    }

    @Test
    @DisplayName("Test add and remove Profiles 1")
    public void testAddRemoveProfiles1()
    {
        Mapper mapper = new Mapper(false);
        mapper.addProfiles(new IgnoreAllProfile(), new TestProfile());

        Profile prof1 = new IgnoreAllProfile();
        Mapping mapping = prof1.getMappings().get(0);

        Profile prof2 = new TestProfile();
        Mapping mapping2 = prof2.getMappings().get(0);

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
    public void testAddRemoveProfiles2()
    {
        Mapper mapper = new Mapper(false);

        List<Profile> profiles = new ArrayList<>();
        profiles.add(new IgnoreAllProfile());
        profiles.add(new TestProfile());

        mapper.addProfiles(profiles);

        Profile prof1 = new IgnoreAllProfile();
        Mapping mapping = prof1.getMappings().get(0);

        Profile prof2 = new TestProfile();
        Mapping mapping2 = prof2.getMappings().get(0);

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
    public void testOneToMany()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person3 expected = new Person3(12, "john", "doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12,10), "+43 452 234234512");

        Mapper mapper = new Mapper(false);
        mapper.addMapping(new Mapping<>(Person1.class, Person3.class).forMembers(x -> x.getName().split(" "), opt -> opt.mapTo(Person3::setFirstname), x -> x.mapTo(Person3::setLastname)));
        Person3 actual = mapper.map(Person3.class, person, MappingType.STRICT);

        Assertions.assertEquals(expected, actual);
    }
}
