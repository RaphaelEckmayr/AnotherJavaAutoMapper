package net.AJAM.Mapper.test.Utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionsTestclass2 {
    List<String> list;
    Set<Integer> set;
    Map<Integer, String> map;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<Integer> getSet() {
        return set;
    }

    public void setSet(Set<Integer> set) {
        this.set = set;
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    public void setMap(Map<Integer, String> map) {
        this.map = map;
    }

    public CollectionsTestclass2() {
    }

    public CollectionsTestclass2(List<String> list, Set<Integer> set, Map<Integer, String> map) {
        this.list = list;
        this.set = set;
        this.map = map;
    }

    @Override
    public String toString() {
        return "CollectionsTestclass2{" +
                "list=" + list +
                ", set=" + set +
                ", map=" + map +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectionsTestclass2 that = (CollectionsTestclass2) o;

        if (list != null ? !list.equals(that.list) : that.list != null) return false;
        if (set != null ? !set.equals(that.set) : that.set != null) return false;
        return map != null ? map.equals(that.map) : that.map == null;
    }
}
