package com.example.application.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@BrowserCallable
@AnonymousAllowed
@Service
public class HomeService {

    private List<String> stringList;

    HomeService() {

        stringList = new ArrayList<>();

        stringList.add("GPT-4o mini");
        stringList.add("Gpt 3.5 turbo");
        stringList.add("Gpt 4o");
        stringList.add("Claude 3.5");

    }

    public List<String> getCountries() {

        return stringList;

    }


}
