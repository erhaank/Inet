package com.quote.model;

import java.util.Random;

public class QuoteGenerator {
    String[] quotes = {"It takes a fool to remain sane", "Cool story brah",
"Yeahyeahyeah", "There is a fine line between fishing and just standing on the shore like an idiot.",
"I think animal testing is a terrible idea; they get all nervous and give the wrong answers.",
"Live fast, die young, leave a good looking corpse.",
"I love the smell of napalm in the morning... Smells like victory.",
"Truth hurts. Maybe not as much as jumping on a bicycle with a seat missing, but it hurts.",
"I find your lack of faith disturbing."};
    Random rand;
    public QuoteGenerator() {
        rand = new Random();
    }

    public String getQuote(String name) {
        int i = rand.nextInt(quotes.length);
        String s = "'"+quotes[i]+"'<br><b>-"+name+"</b>";
        return s;
    }

}