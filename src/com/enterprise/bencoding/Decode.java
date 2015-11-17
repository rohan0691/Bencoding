package com.enterprise.bencoding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.LinkedList;

public class Decode {
   
    /**
     * Decodes a bencoded dictionary into a Map object.
     * @param String containing the bencoded dictionary.
     * @return a Map containing the decoded contents of the bencoded dictionary.
     */
    private  Map decodeDictionary(String dictionary) throws Exception {
        TreeMap result = new TreeMap();
    	List decodeList = new LinkedList();
    	
       	String[] splitList = dictionary.split("\\s+");
    	for(int i=0; i<splitList.length; i++){
    		if(splitList[i].length()>1 && splitList[i].contains(":")){
    			if(splitList[i].charAt(0)=='0'){
    				decodeList.add(decodeInteger(splitList[i]));
    			}else{
    				decodeList.add(decodeString(splitList[i]));
    			}
    		}
    	}
    for(int i=0; i<decodeList.size(); i+=2){
    	result.put(decodeList.get(i), decodeList.get(i+1));
    }
		return result;
    	}


    /**
     * decodes a encoded list into a List object.
     * @param stream the InputStream to read the list from.
     * @return a List representing the bencoded list in the input stream
     * @throws java.lang.Exception if the list ends unexpectedly, or if the
     * content is malformed.
     */
    private  List decodeList(String list) throws Exception {
    	List decodeList = new LinkedList();
   
    	String[] splitList = list.split("\\s+");
    	for(int i=0; i<splitList.length; i++){
    		if(splitList[i].length()>1 && splitList[i].contains(":")){
    			if(splitList[i].charAt(0)=='0'){
    				decodeList.add(decodeInteger(splitList[i]));
    			}else{
    				decodeList.add(decodeString(splitList[i]));
    			}
    		}
    	}

        return decodeList;
    }

    /**
     * Decodes a bencoded integer into a java.lang.Long object.
     * @param stringResult2 the input stream to decode the integer from.
     * @return a Long representing the bencoded integer in the input stream.
     * @throws java.lang.Exception if the Integer ended unexpectedly, contains
     * non-number data or due to problems converting to Long.
     */
    private  Long decodeInteger(String string) throws Exception {
      StringBuilder stringResult = new StringBuilder();
    	if(string.charAt(string.length()-1)!='e'){
    		throw new Exception("Integer not encoded properly.");
    	}
        for(int i=0; i<string.length(); i++){
        	char chrs = string.charAt(i);
        	if (Character.isDigit(chrs) || chrs=='-') {
        		stringResult.append(chrs);
			}
        }
        return Long.parseLong(stringResult.toString());
    }


    private  StringBuilder stringParser(String decodeString, StringBuilder result){
        for(int i=decodeString.length()-1; i>=0; i--){
      	  char chrs = decodeString.charAt(i);
      	  if (Character.isDigit(chrs)){
      		  if( decodeString.charAt(i+1)=='e')
      			result.deleteCharAt(i+1);  
      		   if(result.charAt(i-1)=='i')
      			 result.deleteCharAt(i-1);  
      	  }
        }
        return result;
    }
    /**
     * Decodes a bencoded string into a java.lang.String object.
     * @param string the input stream to parse the string from
     * @return a String representing the bencoded string given in the input stream
     * @throws java.lang.Exception
     */
    private  String decodeString(String string) throws Exception {
        StringBuilder stringResult = null;
    	if(string.contains(":")){
      String [] colonSplit = string.split(":");
      String stringData = colonSplit[1];
       stringResult = new StringBuilder(stringData);
       	stringParser(stringData, stringResult);
    }
    	else{
    		 stringResult = new StringBuilder(string);
    		 stringParser(string, stringResult);
    	}
        return stringResult.toString();
    }
    
    /**
     * Decodes a bencoded Object into a String, List, Integer or Map object.
     * @param string the input stream to parse the string from
     * @return a Object representing the bencoded Object given in the input stream
     * @throws java.lang.Exception
     */
    public  Object decode(InputStream stream) throws Exception {
    	
    	 StringBuilder stringResult = new StringBuilder();
         BufferedReader br = new BufferedReader(new InputStreamReader(stream));
         String read = br.readLine();
         Object decodedStream = null;
         while(read != null) {
        	 stringResult.append(read);
             read =br.readLine();
         }
    	switch(stringResult.charAt(0)){
        case 'd':
           decodedStream = decodeDictionary(stringResult.toString());
              break;
          case 'l':
          	decodedStream = decodeList(stringResult.toString());
               break;
          case 'i':
          	 decodedStream = decodeInteger(stringResult.toString());
               break;
          default:
              if(Character.isDigit(stringResult.charAt(0))) {
              	decodedStream = decodeString(stringResult.toString());
              	}
              else {
                  throw new Exception("Wrong character found as the starting index :"+stringResult.charAt(0));
              }
              break;
    	}
    
        return decodedStream;
    }

}
