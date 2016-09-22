package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileController {
	private String path;

	public FileController(String path) {
		this.path = path;
	}

	public List<String> readFile() throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		
		List<String> dbLines = new ArrayList<String>();
		String line = new String();
		
		while((line = br.readLine()) != null) {
			dbLines.add(line);
		}
		br.close();
		
		return dbLines;
	}
	
	public void appendToFile(String text) throws IOException {
		FileWriter fw = new FileWriter(path, true);
		PrintWriter pw = new PrintWriter(fw);
		
		pw.println(text);
		pw.close();
	}
}
