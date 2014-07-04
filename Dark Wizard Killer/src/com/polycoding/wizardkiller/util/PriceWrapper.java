package com.polycoding.wizardkiller.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class PriceWrapper {

	private HashMap<String, Integer> priceMap = new HashMap<>();

	public PriceWrapper() {
	}

	public PriceWrapper(String... names) {
		addItemsToMap(names);
	}

	/**
	 * Get the price of a item in our price map, if our map doesn't contain
	 * price, then look it up and add it to map.
	 * 
	 * @param name
	 *            The name of the item you wish to get the value of.
	 * @return Item value that was stored in our pricemap.
	 */
	public int getItemPrice(String name) {
		if (!priceMap.containsKey(name))
			addItemsToMap(name);
		return priceMap.get(name);
	}

	/**
	 * Adds items to the price map, to be retrieved for later use.
	 * 
	 * @param names
	 *            Names of the items you want to store values of.
	 */
	public void addItemsToMap(String... names) {
		for (String s : names) {
			priceMap.put(s, lookUpItemPrice(s));
		}
	}

	/**
	 * 
	 * @param name
	 *            The name of the item you wish to update the price
	 */
	public void updatePrice(String name) {
		priceMap.remove(name);
		priceMap.put(name, lookUpItemPrice(name));
	}

	/**
	 * Directly looks up item prices using Zybez.
	 * 
	 * @param name
	 *            Name of the item you wish to check the price for.
	 * @return int of the amount of the items value.
	 */
	public int lookUpItemPrice(String name) {
		String info = getInfo(name);
		if (info != null && info.contains(name)) {
			info = info.substring(info.indexOf("average") + 9);
			info = info.substring(0, info.indexOf(",") - 1);
			if (info.contains(".")) {
				info = info.substring(0, info.indexOf('.'));
			}
			//System.out.println(Integer.parseInt(info));
			return Integer.parseInt(info);
		}
		return 0;
	}

	/**
	 * 
	 * @param name
	 *            Name of the item you wish to check the price for.
	 * @return String of data we retrieve from Zybez.
	 */
	private String getInfo(String name) {
		name = name.replaceAll(" ", "%20");
		try {
			String text = "http://forums.zybez.net/runescape-2007-prices/api/"
					+ name;
			System.out.println(text);
			URL url = new URL(text);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.2; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String f = in.readLine();
			in.close();
			return f;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}