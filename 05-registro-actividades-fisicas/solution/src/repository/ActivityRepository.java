package repository;

import model.Activity;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio para gestionar la persistencia de Activities en archivo CSV.
 * Mantiene datos en memoria y sincroniza con archivo tras cada modificación.
 */
public class ActivityRepository {
    
    private static final String CSV_FILE = "activities.csv";
    private static final String CSV_HEADER = "id,exerciseType,durationMinutes,distanceKm,caloriesBurned,date,notes,heartRate";
    
    private List<Activity> activities; // Almacenamiento en memoria
    private AtomicLong idGenerator; // Generador de IDs autoincrementales

    public ActivityRepository() {
        this.activities = new ArrayList<>();
        this.idGenerator = new AtomicLong(0);
        loadFromCSV();
    }

    /**
     * Carga todas las actividades desde el archivo CSV.
     * Si el archivo no existe, lo crea con el header.
     */
    private void loadFromCSV() {
        Path path = Paths.get(CSV_FILE);
        
        if (!Files.exists(path)) {
            createCSVFile();
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine(); // Saltar header
            long maxId = 0;

            while ((line = br.readLine()) != null) {
                Activity activity = parseCSVLine(line);
                activities.add(activity);
                if (activity.getId() > maxId) {
                    maxId = activity.getId();
                }
            }
            
            idGenerator.set(maxId); // Inicializar generador con el último ID
        } catch (IOException e) {
            System.err.println("Error al cargar CSV: " + e.getMessage());
        }
    }

    /**
     * Crea el archivo CSV con el header si no existe.
     */
    private void createCSVFile() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(CSV_FILE))) {
            bw.write(CSV_HEADER);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al crear CSV: " + e.getMessage());
        }
    }

    /**
     * Convierte una línea CSV en un objeto Activity.
     * Maneja campos vacíos/null correctamente.
     */
    private Activity parseCSVLine(String line) {
        String[] parts = line.split(",", -1); // -1 para mantener campos vacíos
        
        Activity activity = new Activity();
        activity.setId(Long.parseLong(parts[0]));
        activity.setExerciseType(parts[1]);
        activity.setDurationMinutes(Integer.parseInt(parts[2]));
        activity.setDistanceKm(parts[3].isEmpty() ? null : Double.parseDouble(parts[3]));
        activity.setCaloriesBurned(Integer.parseInt(parts[4]));
        activity.setDate(parts[5]);
        activity.setNotes(parts[6].isEmpty() ? null : parts[6]);
        activity.setHeartRate(parts[7].isEmpty() ? null : Integer.parseInt(parts[7]));
        
        return activity;
    }

    /**
     * Guarda todas las actividades en el archivo CSV.
     * Sobrescribe el archivo completo.
     */
    private void saveToCSV() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(CSV_FILE))) {
            bw.write(CSV_HEADER);
            bw.newLine();

            for (Activity activity : activities) {
                bw.write(toCSVLine(activity));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar CSV: " + e.getMessage());
        }
    }

    /**
     * Convierte un objeto Activity a formato CSV.
     * Campos null se escriben como vacíos.
     */
    private String toCSVLine(Activity activity) {
        return String.join(",",
            activity.getId().toString(),
            activity.getExerciseType(),
            activity.getDurationMinutes().toString(),
            activity.getDistanceKm() != null ? activity.getDistanceKm().toString() : "",
            activity.getCaloriesBurned().toString(),
            activity.getDate(),
            activity.getNotes() != null ? activity.getNotes() : "",
            activity.getHeartRate() != null ? activity.getHeartRate().toString() : ""
        );
    }

    // ========== CRUD Operations ==========

    /**
     * Retorna todas las actividades.
     */
    public List<Activity> findAll() {
        return new ArrayList<>(activities); // Copia defensiva
    }

    /**
     * Busca una actividad por ID.
     * @return Optional con la actividad o vacío si no existe
     */
    public Optional<Activity> findById(Long id) {
        return activities.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    /**
     * Guarda una nueva actividad o actualiza una existente.
     * Si el ID es null, genera uno nuevo (insert).
     * Si el ID existe, actualiza (update).
     */
    public Activity save(Activity activity) {
        if (activity.getId() == null) {
            // INSERT: Generar nuevo ID
            activity.setId(idGenerator.incrementAndGet());
            activities.add(activity);
        } else {
            // UPDATE: Reemplazar existente
            activities.removeIf(a -> a.getId().equals(activity.getId()));
            activities.add(activity);
        }
        
        saveToCSV();
        return activity;
    }

    /**
     * Elimina una actividad por ID.
     * @return true si se eliminó, false si no existía
     */
    public boolean deleteById(Long id) {
        boolean removed = activities.removeIf(a -> a.getId().equals(id));
        if (removed) {
            saveToCSV();
        }
        return removed;
    }

    /**
     * Filtra actividades por tipo de ejercicio.
     */
    public List<Activity> findByType(String exerciseType) {
        return activities.stream()
                .filter(a -> a.getExerciseType().equalsIgnoreCase(exerciseType))
                .collect(Collectors.toList());
    }

    /**
     * Filtra actividades por rango de fechas (ambas inclusive).
     */
    public List<Activity> findByDateRange(String startDate, String endDate) {
        return activities.stream()
                .filter(a -> a.getDate().compareTo(startDate) >= 0 && 
                             a.getDate().compareTo(endDate) <= 0)
                .collect(Collectors.toList());
    }
}