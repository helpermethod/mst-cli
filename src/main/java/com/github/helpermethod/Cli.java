package com.github.helpermethod;

import com.github.mustachejava.DefaultMustacheFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Command(name = "mst-cli", mixinStandardHelpOptions = true)
public class Cli implements Runnable {
    @Parameters(index = "0")
    private String json;
    @Parameters(index = "1")
    private String template;

    @Override
    public void run() {
        try (Reader reader = json.equals("-") ? new BufferedReader(new InputStreamReader(System.in)) : Files.newBufferedReader(Paths.get(json))) {
            Map<String, Object> model =
                new Gson()
                    .fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());

            new DefaultMustacheFactory()
                    .compile(Files.newBufferedReader(Paths.get(template)), template)
                    .execute(new PrintWriter(System.out), model)
                    .flush();
        } catch (IOException e) {

        }
    }

    public static void main(String[] args) {
        CommandLine.run(new Cli(), args);
    }
}