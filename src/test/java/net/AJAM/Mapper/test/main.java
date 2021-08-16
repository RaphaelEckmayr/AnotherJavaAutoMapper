package net.AJAM.Mapper.test;

import net.AJAM.Mapper.Mapper;
import net.AJAM.Mapper.MappingType;
import net.AJAM.Mapper.test.Utils.Person1;
import net.AJAM.Mapper.test.Utils.Person2;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class main {
    public static void main(String[] args)
    {
        Person1 person = new Person1(12, "john doe", "john.doe@foo.bar", "2004-12-12",
                LocalDate.of(2020, 12, 10), "+43 452 234234512");

        long start = System.currentTimeMillis();
        Mapper mapper = new Mapper(false);
        long stop = System.currentTimeMillis();
        System.out.println("Init time AJAM: " + (stop - start) + "ms");

        start = System.currentTimeMillis();
        ModelMapper modelMapper = new ModelMapper();
        stop = System.currentTimeMillis();
        System.out.println("Init time ModelMapper: " + (stop - start) + "ms");

        System.out.println("----------------------------------------------------------------");

        start = System.currentTimeMillis();
        Person2 res = modelMapper.map(person, Person2.class);
        stop = System.currentTimeMillis();
        System.out.println(res);
        System.out.println("Map time ModelMapper: " + (stop - start) + "ms");

        start = System.currentTimeMillis();
        res = mapper.map(Person2.class, person, MappingType.LOOSE);
        stop = System.currentTimeMillis();
        System.out.println(res);
        System.out.println("Map time AJAM: " + (stop - start) + "ms");

        System.out.println("----------------------------------------------------------------");

        BigTestclass bigTestclass = new BigTestclass(1,2,3,4,5,6,3,2,5,43,345,2354,124,45,1,3,4,3,2,4,65,3,25,23,154,135,151345,124,2634,236);

        start = System.currentTimeMillis();
        BigTestclass2 res2 = modelMapper.map(bigTestclass, BigTestclass2.class);
        stop = System.currentTimeMillis();
        System.out.println(res2);
        System.out.println("Map BigClass time ModelMapper: " + (stop - start) + "ms");

        start = System.currentTimeMillis();
        res2 = mapper.map(BigTestclass2.class, bigTestclass, MappingType.LOOSE);
        stop = System.currentTimeMillis();
        System.out.println(res2);
        System.out.println("Map BigClass time AJAM: " + (stop - start) + "ms");

        System.out.println("----------------------------------------------------------------");

        start = System.currentTimeMillis();
        for(int i = 0; i < 100; i++)
        {
            Person2 idk = mapper.map(Person2.class, person, MappingType.LOOSE);
        }
        stop = System.currentTimeMillis();
        System.out.println("100 Map time AJAM: " + (stop - start) + "ms");

        List<Person1> data = new ArrayList<>();
        for(int i = 0; i < 100; i++)
        {
           data.add(person);
        }
        start = System.currentTimeMillis();

        CompletableFuture<List<Person2>> future = mapper.mapListAsync(Person2.class, data, MappingType.LOOSE);
        future.join();

        stop = System.currentTimeMillis();
        System.out.println("100 MapListAsync time AJAM: " + (stop - start) + "ms");

        start = System.currentTimeMillis();
        for(int i = 0; i < 100; i++)
        {
            Person2 idk2 = modelMapper.map(person, Person2.class);
        }
        stop = System.currentTimeMillis();
        System.out.println("100 Map time ModelMapper: " + (stop - start) + "ms");
    }
}
