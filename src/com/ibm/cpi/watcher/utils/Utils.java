package com.ibm.cpi.watcher.utils;

public class Utils 
{
	public static String getterName(String name)
	{
		return "get" + name.substring(0,1) + name.substring(1);
	}
	public static String setterName(String name)
	{
		return "set" + name.substring(0,1).toUpperCase() + name.substring(1);
	}
}
