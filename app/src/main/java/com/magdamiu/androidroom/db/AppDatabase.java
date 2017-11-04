package com.magdamiu.androidroom.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.magdamiu.androidroom.db.dao.CompanyDao;
import com.magdamiu.androidroom.db.dao.CompanyDepartmentsDao;
import com.magdamiu.androidroom.db.dao.DepartmentDao;
import com.magdamiu.androidroom.db.dao.EmployeeDao;
import com.magdamiu.androidroom.db.entity.Company;
import com.magdamiu.androidroom.db.entity.Department;
import com.magdamiu.androidroom.db.entity.Employee;

@Database(entities = {Company.class, Employee.class, Department.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "company-db";

    private static AppDatabase INSTANCE;

    public abstract CompanyDao companyDao();
    public abstract EmployeeDao employeeDao();
    public abstract CompanyDepartmentsDao companyDepartmentsDao();
    public abstract DepartmentDao departmentDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            //.addMigrations(MIGRATION_1_2)
                            .build();
        }
        return INSTANCE;
    }

    /**
     * Migrate from:
     * version 1 - using Room
     * to
     * version 2 - using Room where the {@link Company} has an extra field: iso3
     */
    @VisibleForTesting
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Company "
                    + " ADD COLUMN ref_no TEXT");
        }
    };

    public static void destroyInstance() {
        INSTANCE = null;
    }
}