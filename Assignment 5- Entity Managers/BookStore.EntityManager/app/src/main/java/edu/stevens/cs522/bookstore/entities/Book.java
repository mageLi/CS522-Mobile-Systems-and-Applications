package edu.stevens.cs522.bookstore.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import edu.stevens.cs522.bookstore.contracts.BookContract;

public class Book implements Parcelable {

	// TODO Modify this to implement the Parcelable interface. finished

	public int id;

	public String title;

	public Author[] authors;

	public String isbn;

	public String price;

	//generated getter and setter methods for book details class
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
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

	public Book(int id, String title, Author[] author, String isbn, String price) {
		this.id = id;
		this.title = title;

		this.authors = author;
		this.isbn = isbn;
		this.price = price;
	}

	public String getFirstAuthor() {
		if (authors != null && authors.length > 0) {
			return authors[0].getName();
		} else {
			return "";
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeTypedArray(authors, flags);
		dest.writeString(isbn);
		dest.writeString(price);
	}

	public Book(Parcel in) {
		// TODO init from parcel
		id = in.readInt();
		title = in.readString();
		authors = in.createTypedArray(Author.CREATOR);
		isbn = in.readString();
		price = in.readString();
	}

	public void writeToParcel(Parcel out) {
		// TODO save state to parcel
	}

	public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
		public Book createFromParcel(Parcel in) {
			return new Book(in);
		}

		public Book[] newArray(int size) {
			return new Book[size];
		}
	};

	public Book(Cursor cursor) {
		// TODO init from cursor
		this.title = BookContract.getTitle(cursor);
		this.id = Integer.parseInt(BookContract.getId(cursor));
		String[] authorsName = BookContract.getAuthors(cursor);
		Author[] authors = Author.CREATOR.newArray(authorsName.length);
		int index = 0;
		for (String i : authorsName) {
			authors[index] = new Author(i);
			index++;
		}
		this.authors = authors;
		this.price = BookContract.getPrice(cursor);
		this.isbn = BookContract.getIsbn(cursor);
	}

	public void writeToProvider(ContentValues out) {
		// TODO write to ContentValues
		BookContract.putTitle(out, this.title);
		String authorsName = "";
		for (int i = 0; i < this.authors.length; i++) {
			if(i<=this.authors.length-2)
			{
				authorsName += authors[i].name;
				authorsName += ",";
			}
			else
				authorsName+=authors[i].name;
		}
		BookContract.putAuthors(out, authorsName);
		BookContract.putIsbn(out, this.isbn);
		BookContract.putPrice(out, this.price);
	}

	@Override
	public String toString() {
		return title + " " + getFirstAuthor();
	}

	public String authorObjectToString(Author[] authors) {
		String authorsName = "";
		for (Author a : authors) {
			authorsName += a.name;
			authorsName += ",";
		}
		return authorsName;
	}

}