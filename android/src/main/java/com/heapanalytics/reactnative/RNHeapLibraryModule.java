package com.heapanalytics.reactnative;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.heapanalytics.android.Heap;

import java.util.HashMap;
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
            stringMap.put(key, readableMap.getString(key));
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
    public void track(String event, ReadableMap payload) {
        Heap.track(event, RNHeapLibraryModule.convertToStringMap(payload));
    }

    @ReactMethod
    public void enableVisualizer() {
        //FIXME: this is a noop for now, just so that we expose the methods used by the js
    }

    @ReactMethod
    public void changeInterval(Double interval) {
        //FIXME: this is a noop for now, just so that we expose the methods used by the js
    }
}
