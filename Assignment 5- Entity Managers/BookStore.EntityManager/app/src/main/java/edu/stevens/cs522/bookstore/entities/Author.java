package edu.stevens.cs522.bookstore.entities;

import android.os.Parcel;
import android.os.Parcelable;
import static android.R.attr.author;

public class Author implements Parcelable {

	// TODO Modify this to implement the Parcelable interface. finished

	public long id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String name;

	public Author(String authorText) {
		this.name = authorText;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
	}

	public Author(Parcel in) {
		name = in.readString();
	}

	public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
		public Author createFromParcel(Parcel in) {
			return new Author(in);
		}

		public Author[] newArray(int size) {
			return new Author[size];
		}
	};

	public static Author[] authorsList(Author[] authors){
		String singleAuthor[]=authors[0].getName().split(",");
		int index=0;
		Author[] authorsList=new Author[singleAuthor.length];
		for(String name:singleAuthor){
			authorsList[index]=new Author(name);
			index++;
		}
		return authorsList;
	}
}