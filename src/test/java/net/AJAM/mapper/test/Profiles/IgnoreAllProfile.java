package net.AJAM.mapper.test.Profiles;

import net.AJAM.mapper.Profile;
import net.AJAM.mapper.Mapping;
import net.AJAM.mapper.test.Utils.Person1;
import net.AJAM.mapper.test.Utils.Person2;

public class IgnoreAllProfile extends Profile
{
    public IgnoreAllProfile()
    {
        addMapping(new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getBirthDate, opt -> opt.ignore())
                .forMember(Person1::getPhone, opt -> opt.ignore())
                .forMember(Person1::geteMail, opt -> opt.ignore())
                .forMember(Person1::getId, opt -> opt.ignore())
                .forMember(Person1::getName, opt -> opt.ignore())
                .forMember(Person1::getRegistrationDate, opt -> opt.ignore())
        );
    }
}
