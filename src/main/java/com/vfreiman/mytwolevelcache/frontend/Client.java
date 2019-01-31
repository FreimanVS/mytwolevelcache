package com.vfreiman.mytwolevelcache.frontend;

import com.vfreiman.mytwolevelcache.backend.businesslogic.Server;

import java.util.stream.IntStream;

public class Client {

    private Server server;

    public Client(Server server) {
        this.server = server;
    }

    public void work() {
        final int TIMES = 10;
        IntStream.range(0, TIMES).forEach(n -> {
            server.get("a");
            server.get("b");
            server.get("c");
            server.get("d");
            server.get("e");
        });
    }
}
