package net.AJAM.mapper.test.Profiles;

import net.AJAM.mapper.Mapping;
import net.AJAM.mapper.MappingOption;
import net.AJAM.mapper.Profile;
import net.AJAM.mapper.test.Utils.Person1;
import net.AJAM.mapper.test.Utils.Person2;

public class TestProfile extends Profile
{
    public TestProfile()
    {
        addMapping(new Mapping<>(Person1.class, Person2.class)
            .forMember(Person1::getPhone, MappingOption::ignore)
            .ignore(Person1::getBirthDate));
    }
}
