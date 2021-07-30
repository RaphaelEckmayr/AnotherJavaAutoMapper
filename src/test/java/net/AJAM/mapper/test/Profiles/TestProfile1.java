package net.AJAM.mapper.test.Profiles;

import net.AJAM.mapper.Profile;
import net.AJAM.mapper.Mapping;
import net.AJAM.mapper.test.Utils.Person1;
import net.AJAM.mapper.test.Utils.Person2;

public class TestProfile1 extends Profile
{
    public TestProfile1()
    {
        addMapping(new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt->opt.mapTo(Person2::setPhone2)));
    }
}
