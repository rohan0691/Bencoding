package com.enterprise.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.enterprise.bencoding.Decode;
import com.enterprise.bencoding.Encode;


public class BencodingTest {
    Encode encode = null;
    Decode decode = null;
    Map testMap = null;
    List testList = null;
    String test = null;
    @Before
    public void setup() {
    	encode = new Encode();
        decode = new Decode();
        test = "Test this string 44312 times";
        testMap = new HashMap();
        testList = new ArrayList();
        testMap.put("string", "firstvalue");
        testMap.put("int", "44521");
        testMap.put("intandstring", "testing123");
        testList.add("1addingstring2again");
        testList.add(101100);
        
    }

	@Test
	public void testEncode() throws Exception {
        assertNotNull("object should not be null", encode.bencodeElement(test));
        assertNotNull("object should not be null", encode.bencodeElement(testMap));
        assertNotNull("object should not be null", encode.bencodeElement(testMap));
	}
	
	@Test
	public void testDecode() throws Exception {
	//	new ByteArrayInputStream(testMap.toString().getBytes(
		//		Charset.forName("UTF-8")));
        assertNotNull("object should not be null", decode.decode(encode.bencodeElement(testList)));
        assertNotNull("object should not be null", decode.decode(encode.bencodeElement(testMap)));
        assertNotNull("object should not be null", decode.decode(encode.bencodeElement(test)));
        
	}

	

}
