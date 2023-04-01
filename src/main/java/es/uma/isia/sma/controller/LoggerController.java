package es.uma.isia.sma.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

/**
 * LoggerController es un Singleton que se encarga de configurar y proporcionar
 * instancias de Logger para la aplicación. Configura los loggers para mostrar
 * mensajes de información en la consola y almacenar mensajes de advertencia y
 * niveles superiores en un archivo de registro en la carpeta "log" del proyecto.
 * <p>
 * Para utilizar LoggerController en una clase, primero importe
 * {@code java.util.logging.Logger} y {@code es.uma.isia.sma.controller.LoggerController},
 * luego cree una instancia de Logger usando {@code LoggerController.getInstance().getLogger(Classname.class.getName())}.
 */
public class LoggerController {
    private static final String LOG_DIRECTORY = "log";
    private static LoggerController instance;

    private LoggerController() {
    }

    public static synchronized LoggerController getInstance() {
        if (instance == null) {
            instance = new LoggerController();
        }
        return instance;
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

    /**
     * Configura el logger proporcionado con los manejadores de consola y archivo.
     * Establece el nivel de registro en ALL y desactiva el uso de manejadores de nivel superior.
     *
     * @param logger El Logger a configurar.
     */
    private void setupLogger(Logger logger) {
        createLogDirectoryIfNotExists();
        logger.addHandler(createConsoleHandler());
        logger.addHandler(createFileHandler());
        logger.setLevel(Level.ALL);
        // Desactivar el uso de manejadores de nivel superior
        logger.setUseParentHandlers(false);
    }

    /**
     * Crea el directorio de logs si no existe.
     */
    private void createLogDirectoryIfNotExists() {
        Path logDirectory = Paths.get(LOG_DIRECTORY);
        if (!Files.exists(logDirectory)) {
            try {
                Files.createDirectory(logDirectory);
            } catch (IOException e) {
                System.err.println("Error al crear el directorio de logs: " + e.getMessage());
            }
        }
    }

    /**
     * Crea y devuelve un manejador de consola configurado para mostrar mensajes de nivel INFO
     * y usar un formateador personalizado que solo muestra el mensaje del registro.
     *
     * @return Un Handler para la consola configurado con el nivel INFO y el formateador personalizado.
     */
    private Handler createConsoleHandler() {
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(createCustomFormatter());
        return consoleHandler;
    }

    /**
     * Crea y devuelve un manejador de archivos configurado para almacenar mensajes de nivel WARNING
     * y niveles superiores en un archivo de registro en la carpeta "log" del proyecto.
     *
     * @return Un Handler para archivos configurado con el nivel WARNING y el formateador SimpleFormatter.
     * @throws RuntimeException Si ocurre un error al configurar el manejador de archivos del logger.
     */
    private Handler createFileHandler() {
        Path logPath = Paths.get(LOG_DIRECTORY, "project.log");
        try {
            Handler fileHandler = new FileHandler(logPath.toString(), true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.WARNING);
            return fileHandler;
        } catch (IOException e) {
            throw new RuntimeException("Error al configurar el manejador de archivos del logger", e);
        }
    }

    /**
     * Crea y devuelve un formateador personalizado que solo muestra el mensaje del registro.
     *
     * @return Un Formatter personalizado que solo muestra el mensaje del registro.
     */
    private Formatter createCustomFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        };
    }
}
