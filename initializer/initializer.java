///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
//DEPS de.codeshelf.consoleui:consoleui:0.0.13

import de.codeshelf.consoleui.elements.PromptableElementIF;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "initializer", mixinStandardHelpOptions = true, version = "initializer 0.1",
        description = "initializer made with jbang")
class initializer implements Callable<Integer> {

    enum ProjectType {
        web("example-web", "Example Web Project", "ExampleWeb", "com.robintegg.web", "web,jdbc,h2,lombok"),
        shell("example-shell", "Example Shell Project", "ExampleShell", "com.robintegg.shell", "spring-shell,spring-ai-openai,spring-ai-mcp-client");
        private final String artifact;
        private final String description;
        private final String name;
        private final String packageName;
        private final String dependencies;

        ProjectType(String artifact, String description, String name, String packageName, String dependencies) {
            this.artifact = artifact;
            this.description = description;
            this.name = name;
            this.packageName = packageName;
            this.dependencies = dependencies;
        }
    }

    @Parameters(index = "0", description = "The project type", defaultValue = "web")
    private String type;

    public static void main(String... args) {
        int exitCode = new CommandLine(new initializer()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...


        ProjectType projectType = ProjectType.valueOf(type);

        AnsiConsole.systemInstall();

        ConsolePrompt prompt = new ConsolePrompt();

        String groupName = "group";
        String artifactName = "artifact";
        String descriptionName = "description";
        String nameName = "name";
        String packageNameName = "package";
        List<PromptableElementIF> promptList = prompt.getPromptBuilder()
                .createInputPrompt()
                    .message("Group Id")
                    .name(groupName)
                    .defaultValue("com.robintegg")
                .addPrompt()
                .createInputPrompt()
                    .message("Artifact Id")
                    .name(artifactName)
                    .defaultValue(projectType.artifact)
                .addPrompt()
                .createInputPrompt()
                    .message("Description")
                    .name(descriptionName)
                    .defaultValue(projectType.description)
                .addPrompt()
                .createInputPrompt()
                    .message("Name")
                    .name(nameName)
                    .defaultValue(projectType.name)
                .addPrompt()
                .createInputPrompt()
                    .message("Package Name")
                    .name(packageNameName)
                    .defaultValue(projectType.packageName)
                .addPrompt()
                .build();

        HashMap<String, ? extends PromtResultItemIF> results = prompt.prompt(promptList);
        String group = ((InputResult)results.get(groupName)).getInput();
        String artifact = ((InputResult)results.get(artifactName)).getInput();
        String description = ((InputResult)results.get(descriptionName)).getInput();
        String name = ((InputResult)results.get(nameName)).getInput();
        String packageName = ((InputResult)results.get(packageNameName)).getInput();


        try {
            // Build the spring init command
            List<String> command = Arrays.asList(
                    "cmd.exe", "/c", "spring", "init",
                    "--type=maven-project",
                    "--java=21",
                    "--group-id=" + group,
                    "--artifact-id=" + artifact,
                    "--description=" + description,
                    "--name=" + name,
                    "--package-name=" + packageName,
                    "--dependencies=" + projectType.dependencies,
                    "--extract"
            );

            // Set up and start the process
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Optionally, set the working directory
            // processBuilder.directory(new File(System.getProperty("user.dir")));

            // Redirect process output to the console
            processBuilder.inheritIO();

            Process process = processBuilder.start();

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Process completed with exit code: " + exitCode);

            // TODO: do we want extras with this?
            // use open rewrite at this point?

            // mvn open-rewrite active= with a temp yaml file?



            return exitCode;

        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return 1;
        }

    }

}
