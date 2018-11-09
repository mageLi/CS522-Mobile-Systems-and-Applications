package edu.stevens.cs522.bookstore.util;

import edu.stevens.cs522.bookstore.entities.Author;

import static android.R.attr.author;

/**
 * Created by dduggan
 */

public class Utils {

    public static Author[] parseAuthors(String text) {
        String[] names = text.split(",");
        Author[] authors = new Author[names.length];
        for (int ix=0; ix<names.length; ix++) {
            Author author = new Author();
            String[] name = names[ix].split(" ");
            switch (name.length) {
                case 0:
                    author.firstName = author.lastName = "";
                    break;
                case 1:
                    author.firstName = "";
                    author.lastName = name[0];
                    break;
                case 2:
                    author.firstName = name[0];
                    author.lastName = name[1];
                    break;
                default:
                    author.firstName = name[0];
                    author.middleInitial = name[1];
                    author.lastName = name[2];
            }
            authors[ix] = author;
        }
        return authors;
    }
}
