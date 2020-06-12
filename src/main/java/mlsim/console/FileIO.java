package mlsim.console;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import mlsim.solution.GeneticAlgorithm;
import mlsim.wrapper.GAWrapper;

public class FileIO {
	// Temporary constants.
	private static final int PRE = GAWrapper.PRE, POST = GAWrapper.POST;
	
	List<GAWrapper> load(String fileName) throws IOException {
		fileName = fileName + ".dupa";
		
		String file = new String(Files.readAllBytes(Paths.get(fileName)), Charset.defaultCharset());
		String[] byLine = file.split("\n");
		
		List<GAWrapper> gas = new ArrayList<GAWrapper>();
		for (String code : byLine) {
			gas.add(new GAWrapper(new GeneticAlgorithm(stringToBooleanArray(code), PRE + POST, PRE)));
		}
		
		assert gas.size() == byLine.length : "Deeze niggas must have the same length.";
		return gas;
	}
	
	boolean[] stringToBooleanArray(String s) {
		boolean[] binary = new boolean[s.length()];
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '0') {
				binary[i] = false;
			} else if (c == '1') {
				binary[i] = true;
			} else {
				throw new RuntimeException("Error at stringToBooleanArray. Invalid character. Too busy to handle it properly.");
			}
		}
		
		return binary;
	}
	
	void save(String fileName, List<GAWrapper> population) throws IOException {
		fileName = fileName + ".dupa";
		
		StringBuilder sb = new StringBuilder();
		for (GAWrapper ga : population) {
			sb.append(ga.asString()).append('\n');
		}
		sb.deleteCharAt(sb.length() - 1);
		
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(fileName), Charset.defaultCharset(), StandardOpenOption.CREATE)) {
			bw.write(sb.toString());
			bw.flush();
		}
		
	}
}
