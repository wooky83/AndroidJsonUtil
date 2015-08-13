//Copyright (C) 2014 Wooky83

package com.sktelecom.sktsmartcallcenter.comm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class JsonComposer {

	public static Object objectToJsonString(Object mObj) {
		Object lObj = null;
		try {
			lObj = recursiveComposing(mObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lObj;
	}

	private static Object recursiveComposing(Object reader) throws JSONException {
		if (reader instanceof HashMap) {
			JSONObject dic = new JSONObject();
			HashMap jObj = (HashMap) reader;

			Set set = jObj.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				String nKey = (String) e.getKey();
				Object nValue = e.getValue();
				if (nValue instanceof HashMap || nValue instanceof ArrayList) {
					Object obj = recursiveComposing(nValue);
					dic.put(nKey, obj);
				} else {
					dic.put(nKey, nValue);
				}
			}
			return dic;
		} else if (reader instanceof ArrayList) {
			JSONArray dic = new JSONArray();
			ArrayList jAry = (ArrayList) reader;
			Iterator it = jAry.iterator();
			while (it.hasNext()) {
				Object nValue = it.next();
				if (nValue instanceof HashMap || nValue instanceof ArrayList) {
					Object obj = recursiveComposing(nValue);
					dic.put(obj);
				} else {
					dic.put(nValue);
				}
			}
			return dic;
		}

		return null;
	}

}
