//Copyright (C) 2014 Wooky83

package com.sktelecom.sktsmartcallcenter.comm.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.JsonReader;
import android.util.JsonToken;

public class JsonParser {
    
    //Object Value값을 무조건 String 형으로 받느냐
    private static boolean isOnlyString = true;
    
    public static Object readJsonStream(byte[] data) throws IOException {
        InputStream in = new ByteArrayInputStream(data);
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        Object collection;
        try {
            collection = recursiveParsing(reader, reader.peek());
        } finally {
            reader.close();
        }
        return collection;
    }
    
    private static Object recursiveParsing(JsonReader reader, JsonToken token) throws IOException {
        JsonToken nextToken;
        if (JsonToken.BEGIN_OBJECT == token) {
            HashMap<String, Object> dic = new HashMap<String, Object>();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                nextToken = reader.peek();
                if (JsonToken.BEGIN_OBJECT == nextToken || JsonToken.BEGIN_ARRAY == nextToken) {
                    Object obj = recursiveParsing(reader, nextToken);
                    dic.put(name, obj);
                } else if (JsonToken.NULL == nextToken) {
                    reader.nextNull();
                    dic.put(name, null);
                } else if(JsonToken.BOOLEAN == nextToken) {
                    boolean content = reader.nextBoolean();
                    dic.put(name, content);
                } else if(JsonToken.NUMBER == nextToken && !isOnlyString) {
                    long content = reader.nextLong();
                    dic.put(name, content);
                } else {
                    String content = reader.nextString();
                    dic.put(name, content);
                }
            }
            reader.endObject();
            return dic;
        } else if (JsonToken.BEGIN_ARRAY == token) {
            ArrayList<Object> array = new ArrayList<Object>();
            reader.beginArray();
            while (reader.hasNext()) {
                nextToken = reader.peek();
                if (JsonToken.BEGIN_OBJECT == nextToken || JsonToken.BEGIN_ARRAY == nextToken) {
                    Object obj = recursiveParsing(reader, nextToken);
                    array.add(obj);
                } else if (JsonToken.NULL == nextToken) {
                    reader.nextNull();
                } else if(JsonToken.BOOLEAN == nextToken) {
                    boolean content = reader.nextBoolean();
                    array.add(content);
                } else if(JsonToken.NUMBER == nextToken && !isOnlyString) {
                    long content = reader.nextLong();
                    array.add(content);
                } else {
                    String content = reader.nextString();
                    array.add(content);
                }	
            }
            reader.endArray();
            return array;
        } else
            return null;
    }
}
