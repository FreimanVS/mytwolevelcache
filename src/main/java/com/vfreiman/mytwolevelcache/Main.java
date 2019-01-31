package com.vfreiman.mytwolevelcache;

import com.vfreiman.mytwolevelcache.backend.businesslogic.Server;
import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;
import com.vfreiman.mytwolevelcache.backend.database.Database;
import com.vfreiman.mytwolevelcache.frontend.Client;

public class Main {
    public static void main(String[] args) {
        Database.add("a", new Data("content A"));
        Database.add("b", new Data("content B"));
        Database.add("c", new Data("content C"));
        Database.add("d", new Data("content D"));
        Database.add("e", new Data("content E"));

        final int size = 4;
        new Client(new Server(size)).work();
    }
}
