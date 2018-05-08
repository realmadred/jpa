package com.example.jpa.reactor;

import java.util.List;

public interface MyMessageListener<T> {

    void onMessage(List<T> messages);
}
