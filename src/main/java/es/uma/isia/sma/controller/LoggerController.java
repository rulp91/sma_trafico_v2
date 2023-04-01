package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoCoche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * La clase LoggerController es un Singleton que se encarga de configurar el Logger
 * para la aplicación. Configura el logger para mostrar mensajes de información
 * en la consola y almacenar mensajes de advertencia y niveles superiores en un
 * archivo de registro en la carpeta "log" del proyecto.
 */
public class LoggerController {

    private static LoggerController instance;

    private LoggerController() {
    }

    public static synchronized LoggerController getInstance() {
        if (instance == null)
            instance = new LoggerController();

        return instance;
    }

    /**
     * Método para configurar un logger proporcionado. Configura el logger para mostrar
     * mensajes de información en la consola y almacenar mensajes de advertencia
     * y niveles superiores en un archivo de registro en la carpeta "log" del proyecto.
     *
     * @param logger El Logger a configurar.
     */
    private void setupLogger(Logger logger) {
        try {
            // Crear el directorio "log" si no existe
            Path logDirectory = Paths.get("log");
            if (!Files.exists(logDirectory)) {
                Files.createDirectory(logDirectory);
            }

            // Configurar el manejador de la consola para mostrar solo mensajes de información
            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

            // Configurar el manejador de archivos para almacenar mensajes de advertencia y niveles superiores
            Handler fileHandler = new FileHandler(logDirectory.resolve("project.log").toString(), true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.WARNING);
            logger.addHandler(fileHandler);

            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al configurar el manejador de archivos del logger", e);
        }
    }

    /**
     * Obtiene un Logger configurado para la clase especificada por classname. El Logger está configurado
     * para mostrar mensajes de información en la consola y almacenar mensajes de advertencia y niveles
     * superiores en un archivo de registro en la carpeta "log" del proyecto.
     *
     * @param classname El nombre completo de la clase para la cual se quiere obtener un Logger.
     * @return Un Logger configurado para la clase especificada.
     */
    public Logger getLogger(String classname) {
        Logger logger = Logger.getLogger(classname);
        setupLogger(logger);
        return logger;
    }
}
