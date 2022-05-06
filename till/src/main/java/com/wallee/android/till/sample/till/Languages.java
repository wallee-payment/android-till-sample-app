package com.wallee.android.till.sample.till;

import java.util.ArrayList;

public class Languages {
    private  ArrayList<Language> languages;
    public Languages() {
        languages = new ArrayList<>();
        languages.add(new Language("English", "en"));
        languages.add(new Language("Czech", "cz"));
        languages.add(new Language("Croatian", "hr"));
        languages.add(new Language("Dutch", "nl"));
        languages.add(new Language("Esperanto", "eo"));
        languages.add(new Language("Finnish", "fi"));
        languages.add(new Language("French", "fr"));
        languages.add(new Language("German", "de"));
        languages.add(new Language("Greek", "el"));
        languages.add(new Language("Hungarian", "hu"));
        languages.add(new Language("Italian", "it"));
        languages.add(new Language("Polish", "pl"));
        languages.add(new Language("Portuguese", "pt"));
        languages.add(new Language("Romanian", "ro"));
        languages.add(new Language("Slovak", "sk"));
        languages.add(new Language("Swedish", "sv"));
        languages.add(new Language("Spanish", "es"));
    }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

}
