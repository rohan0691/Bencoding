package com.enterprise.bencoding;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Encode {
	public Encode() {

	}


	/**
	 * Method to bencode String, Integer and Long objects to inputstream reader objects
	 * 
	 * @param Any
	 *            object that needs to be encoded.
	 * @throws expetion
	 *             if error while parsing the reader
	 * @return InputStream object
	 */
	public  InputStream bencodeElement(Object encodeObject) throws Exception{
		return (new ByteArrayInputStream(encodeElement(encodeObject).getBytes(
				Charset.forName("UTF-8"))));
	}

	/**
	 * Encodes a Long object 
	 * 
	 * @param encodeLong
	 *            is the string to be encoded
	 * @return Long object is returned with numeric characters represented as i
	 *         followed by number and then suffixed with e.
	 */
	private  String encode(Long encodeLong) {
		return ("i" + Long.toString(encodeLong) + "e");
	}

	/**
	 * Encodes a Integer object 
	 * 
	 * @param encodeLong
	 *            is the string to be encoded
	 * @return Integer object is returned with numeric characters represented as
	 *         i followed by number and then suffixed with e.
	 */
	private  String encode(Integer encodeInteger) {
		return ("i" + Integer.toString(encodeInteger) + "e");

	}

	/**
	 * Encodes a String object 
	 * 
	 * @param encodeString
	 *            is the string to be encoded
	 * @return String object is returned with length of all non numeric
	 *         characters followed by the : and then the String itself with
	 *         numeric characters represented as i followed by number and then
	 *         suffixed with e. Numeric characters are not counted in the string
	 *         length
	 */
	private  String encode(String encodeString) {
		String removedIntegers = new String(encodeString);
		removedIntegers = removedIntegers.replaceAll("[^A-Za-z]", "");
		encodeString = intFinder(encodeString);
		return (removedIntegers.length() + ":" + encodeString);
	}


	/**
	 * Encodes all numeric characters in a String object by prefixing with i and
	 * suffixing with e
	 * 
	 * @param encode
	 *            is the string to be encoded
	 * @return String object is returned with String length of all numeric
	 *         characters prefixed by i and suffixed by e.
	 */
	private  String intFinder(String encode) {
		StringBuilder encodedStringBuffer = new StringBuilder(encode);
		for (int i = encode.length() - 1; i >= 0; i--) {
			char chrs = encode.charAt(i);
			
			if (Character.isDigit(chrs)) {
				if(encode.length()==1){
					return encode(Integer.parseInt(encode)).toString();
				}
				if(i == encode.length()-1){
					encodedStringBuffer.replace(i,i+1, Character.valueOf(chrs)+"e");
				}
				if(i ==0){
					
					if(!Character.isDigit(encode.charAt(i+1))){
						encodedStringBuffer.replace(i,i+1, Character.valueOf(chrs)+"e");
					}
					encodedStringBuffer.replace(i,i+1, "i"+Character.valueOf(chrs));
				}	
				if(  i!=encode.length()-1 &&  i!=0 && !Character.isDigit(encode.charAt(i+1))){

					encodedStringBuffer.replace(i,i+1, Character.valueOf(chrs)+"e");
				}
				if( i!=0 && !Character.isDigit(encode.charAt(i-1)) ){

					encodedStringBuffer.replace(i,i, "i");
				}
		
				
			}
		}
		return encodedStringBuffer.toString();
	}

	/**
	 * Encodes a List
	 * 
	 * @param encodeList
	 *            is to be encoded
	 * @return a input stream containing the encoded list
	 * @throws java.lang.Exception
	 *             when an object in the list cannot be bencoded
	 */
	private  String encode(List encodeList) throws Exception {
		StringBuilder encodeObjects = new StringBuilder();
		encodeObjects.append("l ");

		for (Object eachElement : encodeList){
			if(eachElement instanceof Integer || eachElement instanceof Long){
				//Integer is changed to String in List so we can add the argument lenght in the list to decode the string back to integer
				encodeObjects.append(encodeElement(eachElement.toString()) +" ");
			}else
			encodeObjects.append(encodeElement(eachElement)+" ");
		}
		encodeObjects.append(" e");
		return encodeObjects.toString();
	}

	/**
	 * Encodes a map as a dictionary.
	 * 
	 * @param arg encodeMap to encode
	 *            
	 * @return a string containing the encoded dictionary

	 */
private String encode(Map encodeMap) throws Exception {
		StringBuilder encodeMapBuilder = new StringBuilder();
		encodeMapBuilder.append("d ");

		TreeMap<Object, Object> sortedMap = new TreeMap<Object, Object>(
				encodeMap);
		for (Map.Entry<Object, Object> entry : sortedMap.entrySet()) {
			if ((entry.getKey() instanceof String)) {
				encodeMapBuilder.append(encodeElement((String) entry.getKey())+" ");
				//Integer or Long instance changed to String to add string length value to decode later on
				if(entry.getValue() instanceof Integer || entry.getValue() instanceof Long)
				encodeMapBuilder.append(encodeElement(entry.getValue().toString())+" ");
				else
					encodeMapBuilder.append(encodeElement(entry.getValue())+" ");

			}else{
				throw new Exception("Invalid key passed in dictionary");
			}

		}
		encodeMapBuilder.append(" e");
		return encodeMapBuilder.toString();
	}
	/**
	 * Method to encode String, Integer and Long objects
	 * 
	 * @param Any
	 *            object that needs to be encoded.
	 * @throws expetion
	 *             if object is not String, Ineger or Long
	 * @return String object
	 */
	private  String encodeElement(Object encodeObject) throws Exception {
		if (encodeObject instanceof String) {
			return encode((String) encodeObject);
		} else if (encodeObject instanceof Integer) {
			return encode((Integer) encodeObject);
		} else if (encodeObject instanceof Long) {
			return encode((Long) encodeObject);
		} 
		else if (encodeObject instanceof List) {
			return encode((List) encodeObject);
		} 
		else if (encodeObject instanceof Map) {
			return encode((Map) encodeObject);
		} 
		else
			throw new Exception("Invalid Object.");
	
	}
	
}
