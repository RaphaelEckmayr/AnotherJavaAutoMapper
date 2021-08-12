package net.AJAM.Mapper.test.Profiles;

import net.AJAM.Mapper.Profile;
import net.AJAM.Mapper.Mapping;
import net.AJAM.Mapper.test.Utils.Person1;
import net.AJAM.Mapper.test.Utils.Person2;

public class TestProfile1 extends Profile
{
    public TestProfile1()
    {
        addMapping(new Mapping<>(Person1.class, Person2.class)
                .forMember(Person1::getPhone, opt->opt.mapTo(Person2::setPhone2)));
    }
}
