package edu.stevens.cs522.bookstoredatabase.entities;

import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.author;

public class Author implements Parcelable {

	// TODO Modify this to implement the Parcelable interface. finished

	// NOTE: middleInitial may be NULL!

	public long id;

	public String firstName;

	public String middleInitial;

	public String lastName;

	public Author(String authorText) {
		String[] name = authorText.split(" ");
		switch (name.length) {
			case 0:
				firstName = lastName = "";
				break;
			case 1:
				firstName = "";
				lastName = name[0];
				break;
			case 2:
				firstName = name[0];
				lastName = name[1];
				break;
			default:
				firstName = name[0];
				middleInitial = name[1];
				lastName = name[2];
		}
	}

	public Author() {

	}

	public Author(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Author(String firstName, String middleInitial, String lastName) {
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
	}

	public Author(long id, String firstName, String middleInitial, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Author(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (firstName != null && !"".equals(firstName)) {
			sb.append(firstName);
			sb.append(' ');
		}
		if (middleInitial != null && !"".equals(middleInitial)) {
			sb.append(middleInitial);
			sb.append(' ');
		}
		if (lastName != null && !"".equals(lastName)) {
			sb.append(lastName);
		}
		return sb.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(firstName);
		parcel.writeString(middleInitial);
		parcel.writeString(lastName);
	}

	public static final Parcelable.Creator<Author> CREATOR
			= new Parcelable.Creator<Author>() {
		public Author createFromParcel(Parcel in) {
			Author author = new Author();
			author.firstName = in.readString();
			author.middleInitial = in.readString();
			author.lastName = in.readString();
			return author;
		}

		public Author[] newArray(int size) {
			return new Author[size];
		}
	};
}