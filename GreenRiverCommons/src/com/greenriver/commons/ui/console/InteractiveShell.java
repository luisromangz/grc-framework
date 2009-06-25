/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.ui.console;

import java.io.IOException;

/**
 * Abstract interactive shell. It has some abstract methods that implementors
 * should define where the basic interfaces get concrete implementations.
 *
 * This implementation also uses a generic type to make easy work with concrete
 * implementation sin client code.
 * 
 * @param <T> concrete session type
 */
public abstract class InteractiveShell<T extends ShellSession> {

    private T session;
    private String name = "iShell";
    private String promptText = "[@SHELL ]$ ";
    private String realPromptText = "";
    private LineReader reader;
    private OutputWriter writer;
    private ShellCommandFactory factory;

    /**
     * @return the session
     */
    public T getSession() {
        return session;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        this.realPromptText = promptText.replaceAll("@SHELL", name);
    }

    /**
     * @return the text to show when prompting for data
     */
    public String getPromptText() {
        return realPromptText;
    }

    /**
     * @param promptText text to show when prompting for data
     */
    public void setPromptText(String promptText) {
        this.promptText = promptText;
        this.realPromptText = promptText.replaceAll("@SHELL", name);
    }

    /**
     * @return the reader for input data
     */
    public LineReader getReader() {
        return reader;
    }

    /**
     * @return the writer for output messages
     */
    public OutputWriter getWriter() {
        return writer;
    }

    /**
     * Empty constructor
     */
    public InteractiveShell() {
    }

    /**
     * This method is calles at the very first of the run method filling with
     * fresh instances all the generic stuff here.
     */
    private void init() {
        reader = createLineReader();
        writer = createOutputWriter();
        factory = createCommandFactory();
        session = createSession();
    }

    /**
     * Creates the line reader
     * @return a line reader implementation
     */
    protected LineReader createLineReader() {
        return new DefaultLineReader();
    }

    /**
     * Creates the output writer
     * @return a writer for output messages implementation
     */
    protected OutputWriter createOutputWriter() {
        return new DefaultOutputWriter();
    }

    /**
     * Prompts to the user with the default prompt text and waits for user input
     * until the enter key is pressed
     * @return the input string
     */
    public String prompt() {

        System.out.print(realPromptText);

        try {
            return getReader().readLine();
        } catch (IOException ex) {
            err("\nCan't read command line");
            return null;
        }
    }

    /**
     * Prompts to the user with the default prompt text and waits for user input
     * until the enter key is pressed
     * @param message the message to show whithout instead of the default prompt
     * @return the input string
     */
    public String prompt(String message) {
        String line = null;
        try {
            line = getReader().readLine();
        } catch (IOException ex) {
            err("Failed to read line");
        }

        return line;
    }

    /**
     * Prints an error to output
     * @param message message to print
     */
    public void err(String message) {
        writer.writeln("ERROR: " + message);
    }

    /**
     * Prints an error to output
     * @param message message to print
     * @param t exception cause of the error
     */
    public void err(String message, Throwable t) {
        err("++++++ Exception message ++++++");
        err(message);
        err("++++++ Cause stack       ++++++");
        Throwable cause = t.getCause();
        while(cause!= null) {
            err(cause.getClass().getSimpleName()+" => "+t.getMessage());
            cause = cause.getCause();
        }
        err("++++++ Stack trace       ++++++");
        t.printStackTrace();
    }

    /**
     * Prints a message to output
     * @param message message to print
     */
    public void out(String message) {
        writer.writeln(message);
    }

    /**
     * Runs the shell until the flag isExit() is set to true. Clients can't
     * override this method.
     */
    public final void run() {
        init();

        configure();

        ShellCommand cmd = null;
        String line = null;
        String[] pieces = null;

        while (!session.isExit()) {
            line = prompt();
            if (line == null) {
                err("Can't read command input");
                continue;
            }

            getSession().history().add(line);
            pieces = splitTextCommand(line);
            cmd = factory.create(pieces);

            if (cmd == null) {
                out("Unsupported command " + pieces[0]);
            } else if ((pieces.length - 1) < cmd.getMinArgs() ||
                    (pieces.length - 1) > cmd.getMaxArgs()) {
                err("Wrong number or arguments");
                writer.write("Usage: " + cmd.getName());
                writer.writeln(cmd.getUsage());

            } else {
                getWriter().increasePad();

                try {
                    if (cmd.exec(pieces) == -1) {
                        writer.write("Usage: " + cmd.getName() + " " +
                                cmd.getUsage());
                        writer.writeln("");
                    }
                } catch (Throwable t) {
                    err("Last command failed to execute!", t);
                }

                getWriter().decreasePad();
            }
        }

        destroy();
    }

    /**
     * Creates all the objects, threads, open resources, etc, ...
     */
    protected abstract void configure();

    /**
     * Destroys and frees all the created objects
     */
    protected abstract void destroy();

    /**
     *
     * @return a new instance of the interface
     */
    protected abstract ShellCommandFactory createCommandFactory();

    /**
     * Splits the line so we can identify the command and his arguments
     * @param line to split
     * @return the arguments of the line as an array
     */
    protected abstract String[] splitTextCommand(String line);

    /**
     * Creates a new session instance for the shell that will hold status data
     * @return a new session instance
     */
    protected abstract T createSession();
}
