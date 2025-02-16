package com.murlan.game.connection.model;

import java.security.Principal;

public class StompPrincipal implements Principal {

    final String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
