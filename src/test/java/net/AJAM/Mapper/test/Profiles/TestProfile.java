package net.AJAM.Mapper.test.Profiles;

import net.AJAM.Mapper.Mapping;
import net.AJAM.Mapper.MappingOption;
import net.AJAM.Mapper.Profile;
import net.AJAM.Mapper.test.Utils.Person1;
import net.AJAM.Mapper.test.Utils.Person2;

public class TestProfile extends Profile
{
    public TestProfile()
    {
        addMapping(new Mapping<>(Person1.class, Person2.class)
            .forMember(x -> x.getName() + " " + x.getPhone(), MappingOption::ignore));
    }
}
