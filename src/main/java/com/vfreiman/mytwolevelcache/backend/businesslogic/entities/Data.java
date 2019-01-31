package com.vfreiman.mytwolevelcache.backend.businesslogic.entities;

import java.io.Serializable;
import java.util.Objects;

public class Data implements Serializable {
    private String content;

    public Data(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Objects.equals(content, data.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }


    @Override
    public String toString() {
        return content;
    }
}
