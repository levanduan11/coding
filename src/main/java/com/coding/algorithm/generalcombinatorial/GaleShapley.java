package com.coding.algorithm.generalcombinatorial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class GaleShapley {
    public static void main(String[] args) {

    }

    static class Person {
        String name;
        List<String> preferences;
        String currentEngagement;

        public Person(String name, List<String> preferences) {
            this.name = name;
            this.preferences = new ArrayList<>();
            this.currentEngagement = null;
        }

        public String getTopPreference() {
            for (String preference : preferences) {
                if (!preference.equals(currentEngagement))
                    return preference;
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public String getCurrentEngagement() {
            return currentEngagement;
        }
    }

    public static Map<String, String> stableMarriage(
            Map<String, List<String>> menPreferences, Map<String, List<String>> womenPreferences) {
        List<Person> men = initializePersons(menPreferences);
        List<Person> women = initializePersons(womenPreferences);

        while (thereAreUnengagedMen(men)) {
            for (Person man : men) {
                if (man.currentEngagement == null) {
                    String topPreference = man.getTopPreference();
                    Person woman = findPersonByName(women, topPreference);
                    if (woman != null) {
                        if (woman.currentEngagement == null)
                            engage(man, woman);
                        else {
                            String currentEngagement = woman.currentEngagement;
                            Person currentFiance = findPersonByName(men, currentEngagement);
                            if (currentFiance != null && prefers(man, woman, currentFiance)) {
                                engage(man, woman);
                                disengage(currentFiance);
                            }
                        }
                    }
                }
            }
        }
        return men
                .stream()
                .collect(toMap(Person::getName, Person::getCurrentEngagement));
    }

    private static List<Person> initializePersons(Map<String, List<String>> preferences) {
        return preferences.entrySet().stream()
                .map(entry -> new Person(entry.getKey(), entry.getValue()))
                .toList();
    }

    private static boolean thereAreUnengagedMen(List<Person> men) {
        for (Person man : men) {
            if (man.currentEngagement == null)
                return true;
        }
        return false;
    }

    private static Person findPersonByName(List<Person> people, String name) {
        for (Person person : people) {
            if (person.name.equals(name))
                return person;
        }
        return null;
    }

    private static boolean prefers(Person man, Person woman, Person currentFiance) {
        return woman.preferences.indexOf(man.name) < woman.preferences.indexOf(currentFiance.name);
    }

    private static void engage(Person man, Person woman) {
        man.currentEngagement = woman.name;
        woman.currentEngagement = man.name;
    }

    private static void disengage(Person person) {
        person.currentEngagement = null;
    }
}
