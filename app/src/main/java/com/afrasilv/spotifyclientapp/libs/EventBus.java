package com.afrasilv.spotifyclientapp.libs;

/**
 * Created by Alejandro Franco on 13/09/16.
 */
public interface EventBus {
    void register(Object subscriber);
    void unregister(Object subscriber);
    void post(Object event);
}
