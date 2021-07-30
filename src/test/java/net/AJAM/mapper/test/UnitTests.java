package net.AJAM.mapper.test;

import net.AJAM.mapper.Mapper;
import net.AJAM.mapper.Mapping;
import net.AJAM.mapper.MappingType;
import net.AJAM.mapper.test.Profiles.IgnoreAllProfile;
import net.AJAM.mapper.test.Profiles.TestProfile;
import net.AJAM.mapper.test.Profiles.TestProfile1;
import net.AJAM.mapper.test.Utils.Person1;
import net.AJAM.mapper.test.Utils.Person2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UnitTests
{
    @Test
    @DisplayName("Test strict mapping type")
    public void testMapStrict()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12", LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2(null, "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);
        var actual = mapper.map(Person2.class, person, MappingType.STRICT);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test medium mapping type")
    public void testMapMedium()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12", LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        Mapper mapper = new Mapper(false);
        var actual = mapper.map(Person2.class, person, MappingType.MEDIUM);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test loose mapping type")
    public void testMapLoose()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12", LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", LocalDate.of(2004, 12,12), "2020-12-10", null);

        Mapper mapper = new Mapper(false);
        var actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test ignore in profile")
    public void testIgnore()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12", LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, "2020-12-10", null);

        Mapper mapper = new Mapper(false);
        mapper.addProfile(TestProfile.class);
        var actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test mapTo in profile")
    public void testMapTo()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12", LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", LocalDate.of(2004, 12,12), "2020-12-10", "+43 452 234234512");

        Mapper mapper = new Mapper(false);
        mapper.addProfile(TestProfile1.class);
        var actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test MappingType hierarchy")
    public void testMappingTypeHierarchy()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12", LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", null, null, null);

        //hierarchy = Mapping, Parameter, Constructor of mapper
        Mapper mapper = new Mapper(MappingType.LOOSE,false);
        mapper.addMapping(new Mapping<>(Person1.class, Person2.class).mappingType(MappingType.MEDIUM));
        var actual = mapper.map(Person2.class, person, MappingType.STRICT);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test addMapping on Mapper")
    public void testAddMapping()
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12", LocalDate.of(2020, 12,10), "+43 452 234234512");
        Person2 expected = new Person2("12", "john doe", "john.doe@foo.bar", LocalDate.of(2004, 12,12), "2020-12-10", "+43 452 234234512");

        Mapper mapper = new Mapper(false);
        mapper.addMapping(new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt->opt.mapTo(Person2::setPhone2)));

        var actual = mapper.map(Person2.class, person, MappingType.LOOSE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test remove Profile")
    public void testAddRemoveProfile()
    {
        Mapper mapper = new Mapper(true);

        Mapping mapping = new Mapping<>(Person1.class, Person2.class)
                .ignore(Person1::getBirthDate)
                .ignore(Person1::getPhone)
                .ignore(Person1::geteMail)
                .ignore(Person1::getId)
                .ignore(Person1::getName)
                .ignore(Person1::getRegistrationDate);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getProfiles().contains(new IgnoreAllProfile()));

        mapper.removeProfile(IgnoreAllProfile.class);

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getProfiles().contains(new IgnoreAllProfile()));
    }

    @Test
    @DisplayName("Test add and removeProfile")
    public void testRemoveProfile()
    {
        Mapper mapper = new Mapper(false);
        mapper.addProfile(new IgnoreAllProfile());

        Mapping mapping = new Mapping<>(Person1.class, Person2.class)
                .ignore(Person1::getBirthDate)
                .ignore(Person1::getPhone)
                .ignore(Person1::geteMail)
                .ignore(Person1::getId)
                .ignore(Person1::getName)
                .ignore(Person1::getRegistrationDate);

        Assertions.assertTrue(mapper.getMappings().contains(mapping));
        Assertions.assertTrue(mapper.getProfiles().contains(new IgnoreAllProfile()));

        mapper.removeProfile(new IgnoreAllProfile());

        Assertions.assertFalse(mapper.getMappings().contains(mapping));
        Assertions.assertFalse(mapper.getProfiles().contains(new IgnoreAllProfile()));
    }
}
