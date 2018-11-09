package edu.stevens.cs522.bookstoredatabase.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

import edu.stevens.cs522.bookstoredatabase.contracts.BookContract;

public class Book implements Parcelable {

    // TODO Modify this to implement the Parcelable interface. finished

    public int id;

    public String title;

    public Author[] authors;

    public String isbn;

    public String price;

    public String authorsName = "";
    public Book() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Book(int id, String title, Author[] authors, String isbn, String price) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFirstAuthor() {
        if (authors != null && authors.length > 0) {
            return authors[0].toString();
        } else {
            return "";
        }
    }

    public Book(Parcel in) {
        // TODO init from parcel  finished
        id = in.readInt();
        title = in.readString();
        authors = in.createTypedArray(Author.CREATOR);
        isbn = in.readString();
        price = in.readString();
    }

    public void writeToParcel(Parcel out) {
        // TODO save state to parcel finished
    }

    public Book(Cursor cursor) {
        // TODO init from cursor finished
        Log.i("TAG", "cursortitle= "+BookContract.getTitle(cursor));
        this.title = BookContract.getTitle(cursor);
        this.authors = createAuthors(cursor);
        this.price = BookContract.getPrice(cursor);
        this.isbn = BookContract.getIsbn(cursor);
    }

    public Author[] createAuthors(Cursor cursor) {
        String[] authorsName = BookContract.getAuthors(cursor);
        Author[] authors = Author.CREATOR.newArray(authorsName.length);
        int index = 0;
        for (String i : authorsName) {
            Author a = null;
            String[] authorFullName = i.split("\\s+");
            switch (authorFullName.length) {
                case 1:
                    a = new Author(authorFullName[0]);
                    break;
                case 2:
                    a = new Author(authorFullName[0], authorFullName[1]);
                    break;
                case 3:
                    a = new Author(authorFullName[0], authorFullName[1], authorFullName[2]);
                    break;
            }
            authors[index] = a;
            index++;
        }
        return authors;
    }

    public void writeToProvider(ContentValues out) {
        // TODO write to ContentValues  finished
        BookContract.putTitle(out, this.title);
        for (int i = 0; i < this.authors.length; i++) {
            if (authors[i].middleInitial != null) {
                authorsName += authors[i].firstName + " " + authors[i].middleInitial + " " + authors[i].lastName;
            } else {
                authorsName += authors[i].firstName + " " + authors[i].lastName;
            }
            authorsName += "\n";
        }
        BookContract.putAuthors(out, authorsName);
        BookContract.putIsbn(out, this.isbn);
        BookContract.putPrice(out, this.price);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeTypedArray(authors, i);
        parcel.writeString(isbn);
        parcel.writeString(price);

    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors=" + Arrays.toString(authors) +
                ", isbn='" + isbn + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}