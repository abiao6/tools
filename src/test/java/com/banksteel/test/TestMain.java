package com.banksteel.test;

import java.util.UUID;

public class TestMain {

	public static void main(String[] args) throws Exception {
		System.out.println(UUID.fromString(UUID.nameUUIDFromBytes(UUID.randomUUID().toString().getBytes()).toString()).toString());
		
		System.out.println("/v3/client/EB12B06110F611E6BB6F00163E004A9A/detail/EB12B06110F611E6BB6F00163E004A9A".replaceAll("([A-Z0-9]{32})", "{}"));
		System.out.println(System.currentTimeMillis());
		
		long start = System.currentTimeMillis();
		char[] b1 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".toCharArray();
		char[] b2 = "11111111111111111111111111111111111111111101111111111111111111111111111111111111111111111111111111111".toCharArray();
		for (int i=0; i<b1.length; i++) {
			if ((b1[i] & b2[i]) != 0) {
				System.out.println(b1[i] + "   " + (b1[i] & b2[i]));
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(end + " - " + start + " = " + (end - start));
	}
}
