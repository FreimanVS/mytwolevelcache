package com.vfreiman.mytwolevelcache.frontend;

import com.vfreiman.mytwolevelcache.backend.businesslogic.Server;

public abstract class Client {

    protected Server server;

    public Client(Server server) {
        this.server = server;
    }

    public abstract void request();
}
