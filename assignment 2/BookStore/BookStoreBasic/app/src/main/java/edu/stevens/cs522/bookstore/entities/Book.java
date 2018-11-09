package edu.stevens.cs522.bookstore.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Book implements Parcelable {
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
//	public void writeToParcel(Parcel parcel, int i) {
//		parcel.writeString(title);
//		parcel.writeString(isbn);
//		parcel.writeString(price);
//		parcel.writeInt(id);
//		parcel.writeTypedArray(authors,i);
//
//	}

	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(id);
		parcel.writeString(title);
		parcel.writeTypedArray(authors,i);
		parcel.writeString(isbn);
		parcel.writeString(price);
	}

	public static final Parcelable.Creator<Book> CREATOR
			= new Parcelable.Creator<Book>() {
		public Book createFromParcel(Parcel in) {
			Book book = new Book();
			book.id = in.readInt();
			book.title = in.readString();
			book.authors = in.createTypedArray(Author.CREATOR);
			book.isbn = in.readString();
			book.price = in.readString();

			Log.i("TAG", "book.title= "+book.getTitle());
			Log.i("TAG", "book.id= "+book.getId());
			Log.i("TAG", "book.isbn= "+book.getIsbn());
			Log.i("TAG", "book.price= "+book.getPrice());
			Log.i("TAG", "book.authors= "+book.getFirstAuthor());
			return book;
		}

		public Book[] newArray(int size) {
			return new Book[size];
		}
	};

	// TODO Modify this to implement the Parcelable interface.

	public Book() {
	}

	public int id;

	public String title;

	public Author[] authors;

	public String isbn;

	public String price;

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
			return authors[0].toString();
		} else {
			return "";
		}
	}

}