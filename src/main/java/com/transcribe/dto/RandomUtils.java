package com.transcribe.dto;

import java.util.Random;

public class RandomUtils {

	public static String randomJob(final int length) {
		int initialLimit = 97;
		int finalLimit = 122;
		Random random = new Random();
		return random.ints(initialLimit, finalLimit + 1).limit(length)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}
}
