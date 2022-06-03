package org.javautil.gsonutils;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;

import org.javautil.gsonutils.GsonUtilsTest.LocalDateClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;

public class Java18NoWork {
	@Test(expectedExceptions = com.google.gson.JsonIOException.class)
	public void testLocalDate() {
		var localDate = LocalDate.of(2022, 5, 22);
		new Gson().toJson(localDate);
	}
}
