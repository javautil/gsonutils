package org.javautil.gsonutils;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.google.gson.GsonBuilder;

public class GsonUtilsTest {
	
	
	private static final transient Logger logger = LoggerFactory.getLogger(GsonUtilsTest.class);

	public class LocalDateClass {
		private LocalDate dt;
	}
	@Test
	public void testLocalDate() {
		var bean = new LocalDateClass();
		bean.dt = LocalDate.of(2022, 5, 22);
		String json = GsonUtils.toJson(bean);
		assertEquals("{\"dt\":\"2022-05-22\"}",json);
		var bean2 = GsonUtils.toObjectFromJson(json, LocalDateClass.class);
		assert bean2.dt.equals(bean.dt);
	}

	public class BigDecimalClass {
		BigDecimal bd;
	}
	@Test
	public void testBigDecimal() {
		var bean = new BigDecimalClass();
		bean.bd = new BigDecimal("3.14159265358979");
		String json = GsonUtils.toJson(bean);
		String expected = """
				{"bd":"3.14159265358979"}""";
		assertEquals(json,expected);
		var bean2  = GsonUtils.toObjectFromJson(json,BigDecimalClass.class);
		assert bean.bd.equals(bean2.bd);
	}
	
	public class ZonedDateTimeClass {
		private ZonedDateTime dt;
	}
	
	@Test
	public void testZonedDateTimeClass() {
		var bean = new ZonedDateTimeClass();
		bean.dt = ZonedDateTime.of(2022, 5, 22, 9, 49, 12, 0, ZoneId.of("America/New_York"));
		String json = GsonUtils.toJson(bean);
		assertEquals(json,"{\"dt\":\"2022-05-22T09:49:12-04:00[America/New_York]\"}");
		var bean2  = GsonUtils.toObjectFromJson(json,ZonedDateTimeClass.class);
		assert bean.dt.equals(bean2.dt);
	}

	//
	
	public class LocalTimeClass {
		private LocalTime dt;
	}
	@Test
	public void testLocalTimeClass() {
		var bean = new LocalTimeClass();
		bean.dt = LocalTime.of(16,20);
		String json = GsonUtils.toJson(bean);
		assertEquals("{\"dt\":\"16:20\"}",json);
		var bean2  = GsonUtils.toObjectFromJson(json,LocalTimeClass.class);
		assert bean.dt.equals(bean2.dt);
	}

	//
	public class OffsetDateTimeClass {
		private OffsetDateTime dt;
	}
	@Test
	public void testOffsetDateTime() {
		var bean = new OffsetDateTimeClass();
		bean.dt = OffsetDateTime.parse("1988-04-08T08:20:45+07:00");
		String json = GsonUtils.toJson(bean);
		System.out.println("json\n"+ json);
		assertEquals(json,"{\"dt\":\"1988-04-08T08:20:45+07:00\"}");
		var bean2  = GsonUtils.toObjectFromJson(json,OffsetDateTimeClass.class);
		assert bean.dt.equals(bean2.dt);
	}


	@Test
	public void testNoEscaping() {
		String text = "Tom & Jerry";
		String json = GsonUtils.toJson(text);
		logger.info("html json {}",json);
		assertEquals(json,"\"Tom & Jerry\"");
		
	}
	
	@Test
	public void testHtmlEscaping() {
		var builder = new GsonBuilder();
	    var gson = builder.create();
		String text = "Tom & <Jerry>";
		String expected = "\"Tom \\u0026 \\u003cJerry\\u003e\"";
		String json = gson.toJson(text);
		assertEquals(json,expected);
	    //	
		json = GsonUtils.toHtmlJson(text);
		logger.info("html json {}",json);
		assertEquals(json,expected);
	}

	@Test
	public void testTolerant() {
		double notNumber = Double.NaN;
		String json = GsonUtils.toJsonPrettyTolerant(notNumber);
		String expected = "NaN";
		assertEquals(json,expected);
	}
	
	@Test (expectedExceptions = java.lang.IllegalArgumentException.class)
	public void testIntolerant() {
		GsonUtils.toJsonPretty(Double.NaN);
	}

	
	

	@Test
	public void testJsonToPrettyJson() {
		var bean = new OffsetDateTimeClass();
		bean.dt = OffsetDateTime.parse("1988-04-08T08:20:45+07:00");
		// bean to json
		String json = GsonUtils.toJson(bean);
		assertEquals(json,"{\"dt\":\"1988-04-08T08:20:45+07:00\"}");
		// json to pretty json 
		String prettyJson = GsonUtils.jsonToPrettyJson(json);
		System.out.println(prettyJson);
				String expectedPretty = "{\n"
				+ "  \"dt\": \"1988-04-08T08:20:45+07:00\"\n"
				+ "}";
		assertEquals(prettyJson,expectedPretty);
		// bean to pretty json
		assertEquals(GsonUtils.toPrettyJson(bean),expectedPretty);
	}
	
	public class SimpleDateFormatClass {
		private SimpleDateFormat dt;
	}
	
	@Test
	public void testSimpleDateFormat() {
		String format = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		var bean = new SimpleDateFormatClass();
		bean.dt = sdf;
		String json = GsonUtils.toJson(bean);
		assertEquals(json,"{\"dt\":\"yyyy-MM-dd\"}");
		var bean2   = GsonUtils.toObjectFromJson(json,SimpleDateFormatClass.class);
		assert bean.dt.equals(bean2.dt);
	}
	
	public class LocalDateTimeClass {
		private LocalDateTime dt;
	}
	
	@Test
	public void testLocalDateTime() {
		var bean = new LocalDateTimeClass();
		bean.dt = LocalDateTime.of(1988,4,8,20,45,0);
		String json = GsonUtils.toJson(bean);
		assertEquals(json,"""
				{"dt":"1988-04-08T20:45"}""");
		LocalDateTimeClass bean2  = GsonUtils.toObjectFromJson(json,LocalDateTimeClass.class);
		assert bean.dt.equals(bean2.dt);
	}
	
	public class FileClass {
		private File file;
	}
	
	@Test
	public void testFile() {
		String fileName = "/tmp/xyzzy";
		File file = new File(fileName);
		var bean = new FileClass();
		bean.file = file;
		String json = GsonUtils.toJson(bean);
		assertEquals(json,"{\"file\":\"/tmp/xyzzy\"}");
		var bean2   = GsonUtils.toObjectFromJson(json,FileClass.class);
		assert bean.file.equals(bean2.file);
	}

	@Test
	public void testEmitNull() {
		String json = GsonUtils.toJsonPrettyWithNulls(new OffsetDateTimeClass());
		String expected = "{\n"
				+ "  \"dt\": null\n"
				+ "}";
		assertEquals(json,expected);
	}
	
	@Test
	public void testCompact() {
		ArrayList<HashMap<String,String>> animalList = new ArrayList<>();
		HashMap<String,String> catmap = new HashMap<>();
		catmap.put("cat","meow");
		
		HashMap<String,String> dogmap = new HashMap<>();
		dogmap.put("dog","bark");
		
		animalList.add(catmap);
		animalList.add(dogmap);
		String json = GsonUtils.toJsonCompact(animalList);
		String expected = "[{\"cat\":\"meow\"},\n"
				+ "{\"dog\":\"bark\"}]";
		assertEquals(json,expected);
	}

	public class InstantClass {
		Instant instant = Instant.now();
	}
	
	@Test
	public void testInstant() {
		InstantClass clazz = new InstantClass();
	    String json = GsonUtils.toJson(clazz);
	    InstantClass clazz2  = GsonUtils.toObjectFromJson(json,clazz.getClass());
	    assertEquals(clazz2.instant,clazz.instant);
	}
	
	public class TimestampClass {
		 Timestamp timestamp;
	}
	
	@Test
	public void testTimestamp() {
		TimestampClass clazz = new TimestampClass();
		 Date date = new Date();
		 Timestamp timestamp = new Timestamp(date.getTime());
		 clazz.timestamp = timestamp;
	    String json = GsonUtils.toJson(clazz);
	    TimestampClass clazz2  = GsonUtils.toObjectFromJson(json,TimestampClass.class);
	    assertEquals(clazz2.timestamp,clazz.timestamp);
	}
	
	@Test
	public void testYaml() {
		String yaml = """
            garter:
               eyes: round
            rattlesnake: 
              eyes: eliptical
            """;
		String json = GsonUtils.yamlToJson(yaml);
		String expected = """
				{"garter":{"eyes":"round"},"rattlesnake":{"eyes":"eliptical"}}""";
		assertEquals(json,expected);
		
		String prettyjson = GsonUtils.yamlToPrettyJson(yaml);
		String expectedpretty = """
            {
              "garter": {
                "eyes": "round"
              },
              "rattlesnake": {
                "eyes": "eliptical"
              }
            }""";
		System.out.println("'" + prettyjson + "'");
		System.out.println("'" + expectedpretty + "'");
		assertEquals(prettyjson,expectedpretty);
		
	}
}
