package com.example.recyclerwithswipe;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by ppurv on 25-12-2017.
 */
@Database(name = MyDatabase.Name, version = MyDatabase.version)
public class MyDatabase {
    public static final String Name = "MyDB";
    public static final int version=1;
}
