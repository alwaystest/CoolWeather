package com.software.eric.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.software.eric.coolweather.entity.City;
import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.entity.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mzz on 2015/10/30.
 */
public class CoolWeatherDB {

    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getName());
            values.put("province_code", province.getCode());
            db.insert("Province", null, values);
        }
    }

    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getName());
            values.put("city_code", city.getCode());
            values.put("province_id", city.getProvinceId());
            db.insert("city", null, values);
        }
    }

    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getName());
            values.put("county_code", county.getCode());
            values.put("city_id", county.getCityId());
            db.insert("county", null, values);
        }
    }

    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            for (boolean flag = true; flag; flag = cursor.moveToNext()) {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }
        }
        if (cursor != null)
            cursor.close();
        return list;
    }

    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            for (boolean flag = true; flag; flag = cursor.moveToNext()) {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }
        }
        if (cursor != null)
            cursor.close();
        return list;
    }

    public List<County> loadCounty(int cityId) {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            for (boolean flag = true; flag; flag = cursor.moveToNext()) {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            }
        }
        if (cursor != null)
            cursor.close();
        return list;
    }
}
