package org.example.SimulatorModel;

import Model.Predefined.MetaClasses.Expression.LiteralUsage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.Attribute.ConstAttributeUsage;
import org.example.Model.Attribute.TwinAttributeUsage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ValueReader {

	private final ObjectMapper json;

	public ValueReader(ObjectMapper json) {
		this.json = json;
	}


	public boolean hasValue(ConstAttributeUsage attribute) {
		return raw(attribute).isPresent();
	}

	public String text(ConstAttributeUsage attribute) {
		return raw(attribute).orElseThrow(() -> new IllegalArgumentException("'" + attribute.getName() + "' has no value"));
	}

	public long integer(ConstAttributeUsage attribute) {
		return parse(attribute, Long::parseLong);
	}

	public double decimal(ConstAttributeUsage attribute) {
		return parse(attribute, Double::parseDouble);
	}


	public boolean bool(ConstAttributeUsage attribute) {
		String value = text(attribute).trim();
		if (!value.equals("true") && !value.equals("false")) {
			throw new IllegalArgumentException("'" + attribute.getName() + "': value '" + value + "' is no boolean, use true or false");
		}
		return Boolean.parseBoolean(value);
	}

	public List<Long> integers(ConstAttributeUsage attribute) {
		return vector(attribute).stream().map(element -> parse(attribute, element, Long::parseLong)).toList();
	}

	public List<Double> decimals(ConstAttributeUsage attribute) {
		return vector(attribute).stream().map(element -> parse(attribute, element, Double::parseDouble)).toList();
	}

	public List<String> texts(ConstAttributeUsage attribute) {
		return vector(attribute).stream().map(this::unquote).toList();
	}

	public List<Double> series(ConstAttributeUsage attribute, int steps) {
		Path file = Path.of(text(attribute));
		try {
			List<String> lines = Files.readAllLines(file).stream().filter(line -> !line.isBlank()).toList();
			if (lines.isEmpty()) throw new IllegalArgumentException("Time series " + file + " of '" + attribute.getName() + "' is empty");
			int column = Arrays.asList(lines.getFirst().split(",")).stream().map(String::trim).toList().indexOf("value");
			if (column < 0) throw new IllegalArgumentException("Time series " + file + " of '" + attribute.getName() + "' has no column 'value'");
			List<Double> data = new ArrayList<>();
			for (String line : lines.subList(1, lines.size())) {
				data.add(Double.parseDouble(line.split(",")[column].trim()));
			}
			return cyclic(attribute, data, steps);
		} catch (IOException e) {
			throw new IllegalArgumentException("Time series " + file + " of '" + attribute.getName() + "' cannot be read", e);
		}
	}

	public List<Long> thresholdsFile(ConstAttributeUsage attribute) {
		Path file = Path.of(text(attribute));
		try {
			JsonNode thresholds = json.readTree(file.toFile()).path("thresholds");
			if (!thresholds.isArray() || thresholds.isEmpty()) {
				throw new IllegalArgumentException("Thresholds file " + file + " needs {\"thresholds\":[..]}");
			}
			List<Long> result = new ArrayList<>();
			thresholds.forEach(value -> {
				if (!value.canConvertToLong() || value.asLong() < 0 || value.asLong() > 100) {
					throw new IllegalArgumentException("Thresholds file " + file + ": " + value + " is no integer between 0 and 100");
				}
				result.add(value.asLong());
			});
			for (int i = 1; i < result.size(); i++) {
				if (result.get(i) > result.get(i - 1)) throw new IllegalArgumentException("Thresholds file " + file + ": values have to be descending");
			}
			return result;
		} catch (IOException e) {
			throw new IllegalArgumentException("Thresholds file " + file + " of '" + attribute.getName() + "' cannot be read", e);
		}
	}

	private List<Double> cyclic(ConstAttributeUsage attribute, List<Double> data, int steps) {
		if (data.isEmpty()) throw new IllegalArgumentException("Time series of '" + attribute.getName() + "' has no values");
		return IntStream.range(0, steps).mapToObj(i -> data.get(i % data.size())).toList();
	}

	private Optional<String> raw(ConstAttributeUsage attribute) {
		return attribute.getValue().flatMap(TwinAttributeUsage::getExpression).filter(LiteralUsage.class::isInstance)
				.map(LiteralUsage.class::cast).map(LiteralUsage::getValue).map(String::valueOf);
	}

	private <V> V parse(ConstAttributeUsage attribute, Function<String, V> parser) {
		return parse(attribute, text(attribute), parser);
	}

	private <V> V parse(ConstAttributeUsage attribute, String value, Function<String, V> parser) {
		try {
			return parser.apply(value.trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("'" + attribute.getName() + "': '" + value + "' is no number", e);
		}
	}


	private List<String> vector(ConstAttributeUsage attribute) {
		String body = text(attribute).trim();
		boolean bracketed = (body.startsWith("[") && body.endsWith("]")) || (body.startsWith("(") && body.endsWith(")"));
		if (!bracketed) throw new IllegalArgumentException("'" + attribute.getName() + "': vector has to be written as [..]");
		body = body.substring(1, body.length() - 1).trim();
		return body.isEmpty() ? List.of() : Arrays.stream(body.split(",")).map(String::trim).toList();
	}

	private String unquote(String value) {
		boolean quoted = value.length() >= 2 && (value.startsWith("'") && value.endsWith("'") || value.startsWith("\"") && value.endsWith("\""));
		return quoted ? value.substring(1, value.length() - 1) : value;
	}
}
