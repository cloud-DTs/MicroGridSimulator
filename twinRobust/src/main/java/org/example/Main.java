package org.example;

import Executor.SemanticException;
import Main.ResultConverter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.Model.Twin.TwinDefinition;
import org.example.SimulatorModel.ExperimentMapper;
import org.example.SimulatorModel.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Main {
	public static void main(String[] args) throws IOException, SemanticException {
		System.out.println("Starting SysML to JSON conversion...");
		System.out.println(Arrays.toString(args));
		if (args.length < 2) {
			throw new IllegalArgumentException("Usage: <outputDir> <sysml file or directory>...");
		}
		Path outputDir = Path.of(args[0]);
		String[] modelPaths = Arrays.copyOfRange(args, 1, args.length);

		System.out.println("Parsing model paths: " + Arrays.toString(modelPaths));

		ResultConverter result = new DTLibraryParser().parse(modelPaths);

		ObjectMapper json = new ObjectMapper();
		ObjectWriter writer = json.writer(new DefaultPrettyPrinter().withObjectIndenter(new DefaultIndenter("    ", "\n")));
		ExperimentMapper experimentMapper = new ExperimentMapper(result, json);

		Files.createDirectories(outputDir);
		ArrayNode experiments = json.createArrayNode();
		for (Map.Entry<TwinDefinition, List<TwinDefinition>> entry : experimentMapper.getTwinToSubtwins().entrySet()) {
			ObjectNode experiment = experiments.addObject();
			experiment.put("base", write(writer, experimentMapper, outputDir, entry.getKey()));
			ArrayNode conditions = experiment.putArray("conditions");
			for (TwinDefinition condition : entry.getValue()) {
				conditions.add(write(writer, experimentMapper, outputDir, condition));
			}
		}
		writer.writeValue(outputDir.resolve("experiments.json").toFile(), experiments);
	}

	private static String write(ObjectWriter writer, ExperimentMapper experimentMapper, Path outputDir, TwinDefinition twin) throws IOException {
		Model model = new Model(experimentMapper).parse(twin).map(Model.class::cast)
				.orElseThrow(() -> new IllegalStateException("Twin '" + twin.getName() + "' is no simulator twin"));
		String file = twin.getSysmlElement().getQualifiedName().replace("::", "_") + ".json";
		writer.writeValue(outputDir.resolve(file).toFile(), model);
		System.out.println("Generated " + file);
		return file;
	}
}
