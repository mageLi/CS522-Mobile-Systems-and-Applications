package edu.stevens.cs522.bookstore.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable{

	// TODO Modify this to implement the Parcelable interface.
	public Author(String firstName, String middleInitial, String lastName){
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
	}
	public Author(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public Author(String firstName){
		this.firstName = firstName;
	}
	public Author(){}
	// NOTE: middleInitial may be NULL!

	public String firstName;

	public String middleInitial;

	public String lastName;

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
			= new Parcelable.Creator<Author>(){
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

