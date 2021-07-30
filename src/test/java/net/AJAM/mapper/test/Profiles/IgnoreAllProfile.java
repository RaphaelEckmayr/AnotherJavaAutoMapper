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
                .ignore(Person1::getBirthDate)
                .ignore(Person1::getPhone)
                .ignore(Person1::geteMail)
                .ignore(Person1::getId)
                .ignore(Person1::getName)
                .ignore(Person1::getRegistrationDate)
        );
    }
}
