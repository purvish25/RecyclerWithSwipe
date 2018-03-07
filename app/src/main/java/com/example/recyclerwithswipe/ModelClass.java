package com.example.recyclerwithswipe;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

/**
 * Created by ppurv on 26-12-2017.
 */

@Parcel(analyze = {ModelClass.class})
@Table(database = MyDatabase.class)
public class ModelClass extends BaseModel {

    @PrimaryKey
    @Column
    String id;
    @Column
    String name;
    @Column
    String email;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Parcel(analyze = {Address.class})
    @Table(database = MyDatabase.class)
    public static class Address extends BaseModel {

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Column
        @PrimaryKey(autoincrement = true)
        int id;

        @Column
        public String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
