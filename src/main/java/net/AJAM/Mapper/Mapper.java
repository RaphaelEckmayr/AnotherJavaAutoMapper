package net.AJAM.Mapper;

import net.AJAM.Mapper.Exceptions.ReadProfilesFaildedException;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Mapper {
    private final List<Mapping<?, ?>> mappings = Collections.synchronizedList(new ArrayList<>());
    private final List<Profile> profiles = Collections.synchronizedList(new ArrayList<>());
    private MapperEngine mapperEngine = MapperEngine.getInstance();

    public Mapper() {
        init(true);
    }

    public Mapper(boolean readProfiles) {
        init(readProfiles);
    }

    public <T, S> T map(Class<T> targetType, S source) {
        return map(targetType, source, null);
    }

    public <T, S> T map(Class<T> targetType, S source, MappingType mappingType) {
        T target;
        try {
            target = targetType.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }

        return map(target, source, mappingType);
    }

    public <S, T> T map(T target, S source) {
        return map(target, source, null);
    }

    public <S, T> T map(T target, S source, MappingType mappingType) {
        return mapperEngine.internalMap(target, source, mappingType, mappings);
    }

    public <T, S> CompletableFuture<T> mapAsync(T target, S source, MappingType mappingType) {
        return CompletableFuture.supplyAsync(() -> map(target, source, mappingType));
    }

    public <T, S> CompletableFuture<T> mapAsync(T target, S source) {
        return CompletableFuture.supplyAsync(() -> map(target, source));
    }

    public <T, S> CompletableFuture<T> mapAsync(Class<T> targetType, S source, MappingType mappingType) {
        return CompletableFuture.supplyAsync(() -> map(targetType, source, mappingType));
    }

    public <T, S> CompletableFuture<T> mapAsync(Class<T> targetType, S source) {
        return CompletableFuture.supplyAsync(() -> map(targetType, source));
    }


    public <T, S> List<T> mapList(Class<T> targetType, List<S> source) {
        return mapList(targetType, source, null);
    }

    public <T, S> List<T> mapList(Class<T> targetType, List<S> source, MappingType mappingType) {
        List<T> target = new ArrayList<>();

        for (S item : source) {
            target.add(map(targetType, item, mappingType));
        }

        return target;
    }


    public <T, S> CompletableFuture<List<T>> mapListAsync(Class<T> targetType, List<S> source) {
        return CompletableFuture.supplyAsync(() -> mapList(targetType, source));
    }

    public <T, S> CompletableFuture<List<T>> mapListAsync(Class<T> targetType, List<S> source, MappingType mappingType) {
        return CompletableFuture.supplyAsync(() -> mapList(targetType, source, mappingType));
    }

    public Profile addProfile(Class<? extends Profile> profileClass) {
        try {
            Profile profile = profileClass.getDeclaredConstructor().newInstance();
            addProfile(profile);

            return profile;
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ReadProfilesFaildedException("Error while creating Profile " + profileClass.getName() + ". Maybe you forgot the parameterless constructor");
        }
    }

    public void addProfile(Profile profile) {
        profiles.add(profile);
        mappings.addAll(profile.getMappings());
    }

    public void addProfiles(List<Profile> profiles) {
        for (Profile profile : profiles) {
            addProfile(profile);
        }
    }

    public void addProfiles(Profile... profiles) {
        for (Profile profile : profiles) {
            addProfile(profile);
        }
    }

    public boolean removeProfile(Class<? extends Profile> profileClass) {
        try {
            Profile profile = profileClass.getDeclaredConstructor().newInstance();
            return removeProfile(profile);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

    public boolean removeProfile(Profile profile) {
        profiles.remove(profile);
        return mappings.removeAll(profile.getMappings());
    }

    public boolean removeProfiles(List<Profile> profiles) {
        boolean result = true;

        for (Profile profile : profiles) {
            if (!removeProfile(profile))
                result = false;
        }

        return result;
    }

    public boolean removeProfiles(Profile... profiles) {
        boolean result = true;

        for (Profile profile : profiles) {
            if (!removeProfile(profile))
                result = false;
        }

        return result;
    }

    public void addMapping(Mapping<?, ?> mapping) {
        mappings.add(mapping);
    }

    public boolean removeMapping(Mapping<?, ?> mapping) {
        return mappings.remove(mapping);
    }

    public void addMappings(List<Mapping<?, ?>> mappingList) {
        mappings.addAll(mappingList);
    }

    public void addMappings(Mapping<?, ?>... mappingList) {
        mappings.addAll(Arrays.asList(mappingList));
    }

    public boolean removeMappings(List<Mapping<?, ?>> mappingList) {
        return mappings.removeAll(mappingList);
    }

    public boolean removeMappings(Mapping<?, ?>... mappingList) {
        return mappings.removeAll(Arrays.asList(mappingList));
    }

    public List<Mapping<?, ?>> getMappings() {
        return this.mappings;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    private void init(boolean readProfiles) {
        if(readProfiles) {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(""))
                    //Exclude common packages where no profiles can be found
                    .filterInputsBy(new FilterBuilder()
                            .include(".*")
                            .exclude("java.*")
                            .exclude("org.springframework.*")
                            .exclude("org.hibernate.*")
                            .exclude("sun.*")
                            .exclude("android.*")
                    ));
            Set<Class<? extends Profile>> profileTypes = reflections.getSubTypesOf(Profile.class);

            for (Class<? extends Profile> profileType : profileTypes) {
                try {
                    Profile profile = profileType.getDeclaredConstructor().newInstance();
                    profiles.add(profile);

                    if (profile.getMappings() != null)
                        mappings.addAll(profile.getMappings());
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new ReadProfilesFaildedException("Error while creating Profile " + profileType.getName() + ". Maybe you forgot the parameterless constructor");
                }
            }
        }

        ConversionManager.initLooseConversions();
    }
}
