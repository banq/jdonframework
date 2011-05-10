package sample.service;

import sample.domain.User;

import com.jdon.controller.events.EventModel;
import com.jdon.controller.model.PageIterator;

public interface HelloService {

    void create(EventModel em);
    void update(EventModel em);
    void delete(EventModel em);
    User getUser(String id);
    
    PageIterator getAllUsers(int start, int count);
    
    User init(EventModel em);
    
}
