package net.AJAM.Mapper;

import net.AJAM.Mapper.Exceptions.ReadProfilesFailedException;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Mapper {
    private final List<Mapping<?, ?>> mappings = Collections.synchronizedList(new ArrayList<>());
    private final List<Profile> profiles = Collections.synchronizedList(new ArrayList<>());

    private static List<Profile> detectedProfiles;

    private MappingType defaultMappingType;

    protected Mapper(boolean readProfiles, MappingType defaultMappingType) {
        this.defaultMappingType = defaultMappingType;

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
        return MapperEngine.internalMap(target, source, mappings, mappingType, defaultMappingType);
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

    public void addProfile(Class<? extends Profile> profileClass) {
        try {
            Profile profile = profileClass.getDeclaredConstructor().newInstance();
            addProfile(profile);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ReadProfilesFailedException("Error while creating Profile " + profileClass.getName() + ". Maybe you forgot the parameterless constructor");
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

    public void addProfiles(Class<? extends Profile>... profiles) {
        for (Class<? extends Profile> profile : profiles) {
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

    public void addMappings(Mapping<?, ?>... mapping) {
        mappings.addAll(Arrays.asList(mapping));
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

    public static List<Conversion<?, ?>> getConversions() {
        return ConversionManager.getConversions();
    }

    public static void addConversion(Conversion<?,?> conversion){
        ConversionManager.addConversion(conversion);
    }

    public static void addConversions(Conversion<?,?>... conversion){
        ConversionManager.addConversions(Arrays.asList(conversion));
    }

    public static void addConversions(List<Conversion<?,?>> conversion){
        ConversionManager.addConversions(conversion);
    }

    public static void removeConversion(Class<?> source, Class<?> target){
        ConversionManager.removeConversion(ConversionManager.getConversion(source, target, MappingType.LOOSE));
    }

    public void reloadProfiles() {
        detectProfiles();
    }

    public MappingType getDefaultMappingType() {
        return defaultMappingType;
    }

    public void setDefaultMappingType(MappingType defaultMappingType) {
        this.defaultMappingType = defaultMappingType;
    }

    private void init(boolean readProfiles) {
        if (readProfiles) {
            if (detectedProfiles == null)
                detectProfiles();

            profiles.addAll(detectedProfiles);
            for (Profile prof : detectedProfiles) {
                mappings.addAll(prof.getMappings());
            }
        }

        ConversionManager.initLooseConversions();
    }

    private void detectProfiles() {
        detectedProfiles = new ArrayList<>();

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
                detectedProfiles.add(profile);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ReadProfilesFailedException("Error while creating Profile " + profileType.getName() + ". Maybe you forgot the parameterless constructor");
            }
        }
    }
}
