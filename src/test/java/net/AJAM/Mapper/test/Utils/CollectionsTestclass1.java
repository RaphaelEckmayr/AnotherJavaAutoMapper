package net.AJAM.Mapper.test.Utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionsTestclass1 {
    List<String> list;
    Set<String> set;
    Map<String, Integer> map;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public CollectionsTestclass1() {
    }

    public CollectionsTestclass1(List<String> list, Set<String> set, Map<String, Integer> map) {
        this.list = list;
        this.set = set;
        this.map = map;
    }
}
