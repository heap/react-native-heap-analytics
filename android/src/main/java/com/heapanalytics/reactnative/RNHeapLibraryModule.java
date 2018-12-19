package com.heapanalytics.reactnative;

import android.text.TextUtils;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.heapanalytics.android.Heap;
import com.heapanalytics.android.internal.HeapImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RNHeapLibraryModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNHeapLibraryModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNHeap";
  }

  @ReactMethod
  public void setAppId(String appId) {
    try {
      Field skipInstrumentorCheckField = HeapImpl.class.getDeclaredField("skipInstrumentorChecks");
      skipInstrumentorCheckField.setAccessible(true);
      skipInstrumentorCheckField.setBoolean(null, true);
    } catch (NoSuchFieldException e) {
      System.out.println("Caught NoSuchFieldException when trying to skip the instrumentor checks");
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      System.out.println("Caught IllegalAccessException when trying to skip the instrumentor checks");
      e.printStackTrace();
    }

    Heap.init(this.reactContext, appId);
  }

  @ReactMethod
  public void identify(String identity) {
    Heap.identify(identity);
  }

  private static Map<String, String> convertToStringMap(ReadableMap readableMap) {
    Map<String, String> stringMap = new HashMap<>();
    ReadableMapKeySetIterator mapIterator = readableMap.keySetIterator();
    while (mapIterator.hasNextKey()) {
      String key = mapIterator.nextKey();
      switch (readableMap.getType(key)) {
        case Null:
          stringMap.put(key, null);
          break;
        case Number:
          stringMap.put(key, "" + readableMap.getDouble(key));
          break;
        case Boolean:
          stringMap.put(key, readableMap.getBoolean(key) ? "true" : "false");
          break;
        case String:
          stringMap.put(key, readableMap.getString(key));
          break;
        case Array:
          List<Object> list = readableMap.getArray(key).toArrayList();
          List<String> stringList = new ArrayList<>();
          for (Object o : list) {
            stringList.add(o.toString());
          }
          stringMap.put(key, TextUtils.join(",", stringList));
          break;
        case Map:
          // In many cases, this won't get used, since we flatten incoming
          // objects before they get to this point.
          stringMap.put(key, readableMap.getMap(key).toHashMap().toString());
          break;
      }
    }
    return stringMap;
  }

  @ReactMethod
  public void addUserProperties(ReadableMap properties) {
    Heap.addUserProperties(RNHeapLibraryModule.convertToStringMap(properties));
  }

  @ReactMethod
  public void addEventProperties(ReadableMap properties) {
    Heap.addEventProperties(RNHeapLibraryModule.convertToStringMap(properties));
  }

  @ReactMethod
  public void clearEventProperties() {
    Heap.clearEventProperties();
  }

  @ReactMethod
  public void removeEventProperty(String property) {
    Heap.removeEventProperty(property);
  }

  @ReactMethod
  public void track(String event, ReadableMap payload) {
    Heap.track(event, RNHeapLibraryModule.convertToStringMap(payload));
  }
}
