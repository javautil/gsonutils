package org.javautil.gsonutils;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 *
 * @author jjs
 *
 */
public class GsonUtils {

	public static final String version = "May 28, 2022 11:50:53 PM";
	/**
	 * This is the only method that escapes html.
	 * <ul>
	 * <li>pretty - no
	 * <li>escapeHtml - no
	 * </ul>
	 * 
	 * @param o
	 * @return
	 */
	public static final Gson htmlMapper = getGsonBuilderEscapeHtml().create();

	// support NaN floating point

	/**
	 * Base mapper that supports java.time
	 */
	public static final Gson mapper = getGsonBuilder().create();

	public static final Gson nullMapper = getGsonBuilder().serializeNulls().create();

	public static final Gson nullPrettyMapper = getGsonBuilder().serializeNulls().setPrettyPrinting().create();

	public final static Gson prettyMapper = getGsonBuilder().setPrettyPrinting().create();

	/**
	 * <ul>
	 * <li>pretty - yes
	 * <li>escapeHtml - yes
	 * <li>allows NaN in numbers - yes
	 * </ul>
	 * 
	 * @param o
	 * @return
	 */

	public final static Gson prettyTolerantMapper = getGsonBuilder().setPrettyPrinting()
			.serializeSpecialFloatingPointValues().create();

	private static final Yaml yaml = new Yaml();

	/**
	 * @param jsonString
	 * @return prettyPrint of the input JSON
	 */
	public static String jsonToPrettyJson(String jsonString) {
		final var element = JsonParser.parseString(jsonString);
		return prettyMapper.toJson(element);
	}
	
	public static String toHtmlJson(Object o) {
		return htmlMapper.toJson(o);
	}

	public static String toJson(Object o) {
		return mapper.toJson(o);
	}

	public static String toJsonCompact(Object o) {
		return mapper.toJson(o).replaceAll("\\}\\,", "},\n");
	}

	public static String toJsonPretty(Object o) {
		return prettyMapper.toJson(o);
	}

	public static String toJsonPrettyTolerant(Object o) {
		return prettyTolerantMapper.toJson(o);
	}

	public static String toJsonPrettyWithNulls(Object o) {
		return nullPrettyMapper.toJson(o);
	}

	public static String toJsonWithNulls(Object o) {
		return nullMapper.toJson(o);
	}

	public static LinkedHashMap<String, Object> toMapFromBean(Object bean) {
		final var json = toJsonPretty(bean);
		return toMapFromJson(json);
	}

	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> toMapFromBeanWithNulls(Object bean) {
		final var json = nullMapper.toJson(bean);
		return mapper.fromJson(json, LinkedHashMap.class);
	}

	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> toMapFromJson(String json) {
		return mapper.fromJson(json, LinkedHashMap.class);
	}

	public static Object toObjectFromJson(String json) {
		return mapper.fromJson(json, Object.class);
	}

	public static <T> T toObjectFromJson(String json, Class<T> clazz) {
		return mapper.fromJson(json, clazz);
	}

	public static String toPrettyJson(Object o) {
		return prettyMapper.toJson(o);
	}

	
	public static String toPrettyJsonWithNulls(Object o) {
		return nullPrettyMapper.toJson(o);
	}

	public static String yamlToJson(String yamlString) {
		final var yamlObj = yaml.load(yamlString);
		return mapper.toJson(yamlObj);
	}

	/**
	 * @param yamlString
	 * @return yaml as pretty JSON
	 */
	public static String yamlToPrettyJson(String yamlString) {
		final var yamlObj = yaml.load(yamlString);
		return prettyMapper.toJson(yamlObj);
	}

	/**
	 * Get a base builder that registers TypeAdapters for classes that Gson cannot
	 * otherwise serialize.
	 * 
	 * Note this also disables HTML escaping, so override if emitting JSON to web
	 * pages by calling
	 */
	public static GsonBuilder getGsonBuilder() {
		return getGsonBuilderEscapeHtml().disableHtmlEscaping();
	}

	/**
	 * Get a base builder that registers TypeAdapters for classes that Gson cannot
	 * otherwise serialize.
	 * 
	 * Note this escapes html
	 * pages by calling
	 */

	public static GsonBuilder getGsonBuilderEscapeHtml() {
		final var builder = new GsonBuilder();

		builder.registerTypeAdapter(BigDecimal.class,
				(JsonDeserializer<BigDecimal>) (json, typeOfT, context) -> new BigDecimal(json.getAsString()));
		builder.registerTypeAdapter(BigDecimal.class,
				(JsonSerializer<BigDecimal>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));

		builder.registerTypeAdapter(ZonedDateTime.class,
				(JsonDeserializer<ZonedDateTime>) (json, typeOfT, context) -> ZonedDateTime.parse(json.getAsString()));
		builder.registerTypeAdapter(ZonedDateTime.class,
				(JsonSerializer<ZonedDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));

		builder.registerTypeAdapter(OffsetDateTime.class, (JsonDeserializer<OffsetDateTime>) (json, typeOfT,
				context) -> OffsetDateTime.parse(json.getAsString()));
		builder.registerTypeAdapter(OffsetDateTime.class,
				(JsonSerializer<OffsetDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));

		builder.registerTypeAdapter(LocalDateTime.class,
				(JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString()));
		builder.registerTypeAdapter(LocalDateTime.class,
				(JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));

		builder.registerTypeAdapter(LocalDate.class,
				(JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()));
		builder.registerTypeAdapter(LocalDate.class,
				(JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));

		builder.registerTypeAdapter(Instant.class,
				(JsonDeserializer<Instant>) (json, typeOfT, context) -> Instant.parse(json.getAsString()));
		builder.registerTypeAdapter(Instant.class,
				(JsonSerializer<Instant>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));

		builder.registerTypeAdapter(LocalTime.class,
				(JsonDeserializer<LocalTime>) (json, typeOfT, context) ->  LocalTime.parse(json.getAsString()));
		builder.registerTypeAdapter(LocalTime.class,
				(JsonSerializer<LocalTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));

		builder.registerTypeAdapter(File.class,
				(JsonDeserializer<File>) (json, typeOfT, context) -> new File(json.getAsString()));
		builder.registerTypeAdapter(File.class,
				(JsonSerializer<File>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));
		
		builder.registerTypeAdapter(SimpleDateFormat.class,
				(JsonDeserializer<SimpleDateFormat>) (json, typeOfT, context) -> new SimpleDateFormat (json.getAsString()));
		builder.registerTypeAdapter(SimpleDateFormat.class,
				(JsonSerializer<SimpleDateFormat>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toPattern()));

		return builder;
	}


}
