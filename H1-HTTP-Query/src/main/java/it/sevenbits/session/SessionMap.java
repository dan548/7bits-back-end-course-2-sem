package it.sevenbits.session;

import java.util.concurrent.ConcurrentHashMap;

public class SessionMap {

    private ConcurrentHashMap<String, String> map;
    private static SessionMap instance;

    public SessionMap() {
        this.map = new ConcurrentHashMap<>();;
    }

    public void addSession(String id, String name) {
        map.putIfAbsent(id, name);
    }

    public String getSession(String id) {
        return map.get(id);
    }

    public static SessionMap getInstance() {
        if (instance == null) {
            instance = new SessionMap();
        }
        return instance;
    }

}
