package org.javautil.gsonutils;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
	 * prevent constructions
	 */
	private GsonUtils() {
	}
	/**
	 * @param jsonString
	 * @return prettyPrint of the input JSON
	 */
	public static String jsonToPrettyJson(String jsonString) {
		final var element = JsonParser.parseString(jsonString);
		return prettyMapper.toJson(element);
	}
	
	/**
	 * For use in json to be used in HTML, escapes HTML 
	 * @param o the object to be serialized
	 * @return json representation
	 */
	public static String toHtmlJson(Object o) {
		return htmlMapper.toJson(o);
	}

	/**
	 * Converts the object to a dense json 
	 * @param o the object to be serialized
	 * @return json representation
	 */
	public static String toJson(Object o) {
		return mapper.toJson(o);
	}

	/**
	 * Converts the object to a compact pretty json 
	 * ArrayList entries are separated by line but unlike
	 * pretty map keys and values are not no separate lines.
	 * @param o the object to be serialized
	 * @return json representation
	 */
	public static String toJsonCompact(Object o) {
		return mapper.toJson(o).replaceAll("\\}\\,", "},\n");
	}


	/**
	 * Converts the object to a pretty json 
	 * allows NaN double and float values
	 * @param o the object to be serialized
	 * @return json representation
	 */
	public static String toPrettyJsonTolerant(Object o) {
		return prettyTolerantMapper.toJson(o);
	}

//	/**
//	 * Converts the object to a pretty json 
//	 * will serialize null values
//	 * @param o the object to be serialized
//	 * @return json representation
//	 */
//	public static String toPrettyJsonWithNulls(Object o) {
//		return nullPrettyMapper.toJson(o);
//	}

	/**
	 * Converts the object to a dense json 
	 * will serialize null values
	 * @param o the object to be serialized
	 * @return json representation
	 */
	public static String toJsonWithNulls(Object o) {
		return nullMapper.toJson(o);
	}

	
	/**
	 * Converts bean object to map of name values
	 * @param o the object to be serialized
	 * @return json representation
	 */
	public static LinkedHashMap<String, Object> toMapFromBean(Object bean) {
		return toMapFromJson(toJson(bean));
	}

	/**
	 * Converts bean object to map of name values
	 * preserves nulls
	 * @param o the object to be serialized
	 * @return json representation
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> toMapFromBeanWithNulls(Object bean) {
		final var json = nullMapper.toJson(bean);
		return mapper.fromJson(json, LinkedHashMap.class);
	}

	/**
	 * Converts json to map of name values
	 * @param json be serialized
	 * @return map of values 
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> toMapFromJson(String json) {
		return mapper.fromJson(json, LinkedHashMap.class);
	}

//	/**
//	 * Converts json to map of name values
//	 * @param json be serialized
//	 * @return map of values 
//	 */
//	public static Object toObjectFromJson(String json) {
//		return mapper.fromJson(json, Object.class);
//	}

	/**
	 * Converts json to the specified bean
	 * @param json be deserialized
	 * @parame clazz the type of bean to be returned
	 * @return the populated clazz
	 */
	public static <T> T toBeanFromJson(String json, Class<T> clazz) {
		return mapper.fromJson(json, clazz);
	}

	/**
	 * Converts the object to Json
	 * @param o the object to be serialized
	 * @return json
	 */
	public static String toPrettyJson(Object o) {
		return prettyMapper.toJson(o);
	}

	
	/**
	 * Converts the object to Json, preserving nulls
	 * @param o the object to be serialized
	 * @return json
	 */
	public static String toPrettyJsonWithNulls(Object o) {
		return nullPrettyMapper.toJson(o);
	}

	/**
	 * Converts a yaml string to dense json
	 * @param yamlString the yaml to be converted
	 * @return json
	 */
	public static String yamlToJson(String yamlString) {
		final var yamlObj = yaml.load(yamlString);
		return mapper.toJson(yamlObj);
	}

	/**
	 * Converts a yaml string to pretty json
	 * @param yamlString
	 * @param yamlString the yaml to be converted
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

		builder.registerTypeAdapter(Timestamp.class,
				(JsonDeserializer<Timestamp>) (json, typeOfT, context) -> Timestamp.valueOf (json.getAsString()));
		builder.registerTypeAdapter(Timestamp.class,
				(JsonSerializer<Timestamp>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));
		return builder;
	}


}
