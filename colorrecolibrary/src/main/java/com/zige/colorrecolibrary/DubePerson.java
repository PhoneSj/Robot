package com.zige.colorrecolibrary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/23.
 */

public class DubePerson implements Parcelable {
    public static final String _ID = "id";

    public static final String CREATE_TABLE = "create table if not exists " + DubePerson.TABLE_NAME
            + " (" + DubePerson._ID + " integer primary key, "
            + DubePerson.PERSON_ID + " text, "
            + DubePerson.PERSON_NAME + " text, "
            + DubePerson.PERSON_AGE + " text, "
            + DubePerson.PERSON_GENDER + " text, "
            + DubePerson.FACE_ID + " text, "
            + DubePerson.PERSON_TYPE + " text) ";

    public static final String TABLE_NAME = "dube_person";
    public static final String PERSON_ID = "person_id";
    public static final String PERSON_NAME = "person_name";
    public static final String PERSON_GENDER = "person_gender";
    public static final String PERSON_AGE = "person_age";
    public static final String PERSON_TYPE = "person_type";
    public static final String FACE_ID = "faceId";

    public static final String SQL_DELETE = "DROP TABLE IF EXISTS " + DubePerson.TABLE_NAME;


    String name, personId, faceId, gender, age, type;


    public DubePerson(String name, String personId, String faceId) {
        this.name = name;
        this.personId = personId;
        this.faceId = faceId;
    }

    protected DubePerson(Parcel in) {
        name = in.readString();
        personId = in.readString();
        faceId = in.readString();
        gender = in.readString();
        age = in.readString();
        type = in.readString();
    }

    public static final Creator<DubePerson> CREATOR = new Creator<DubePerson>() {
        @Override
        public DubePerson createFromParcel(Parcel in) {
            return new DubePerson(in);
        }

        @Override
        public DubePerson[] newArray(int size) {
            return new DubePerson[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(personId);
        parcel.writeString(faceId);
        parcel.writeString(gender);
        parcel.writeString(age);
        parcel.writeString(type);
    }

    @Override
    public String toString() {
        return "Person : [" + name + ", " + personId + ", " + faceId + ", " + gender + ", " + age + ", " + type + "]";
    }
}
