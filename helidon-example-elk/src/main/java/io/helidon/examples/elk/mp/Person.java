package io.helidon.examples.elk.mp;


import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;

public class Person {

    private String personId;
    private String name;
    private JsonNode miscellaneous;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(JsonNode miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", name='" + name + '\'' +
                ", miscellaneous='" + miscellaneous + '\'' +
                '}';
    }
}
