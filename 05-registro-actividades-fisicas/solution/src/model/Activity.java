package model;

/*
* Representa una actividad fisica regisrada en el sistema.
* POJO puro sin anotaciones de frameworks
*/
public class Activity {
    
    private Long id;
    private String exerciseType;
    private Integer durationMinutes;
    private Double distanceKm;
    private Integer caloriesBurned;
    private String date; // Formato ISO: 2025-11-30
    private String notes;
    private Integer heartRate;

    // constructor vacio
    public Activity(){

    }

    // constructor completo - sin ID (se genera automaticamente)
    public Activity(String exerciseType, Integer durationMinutes, Double distanceKm, 
                    Integer caloriesBurned, String date, String notes, Integer heartRate) {
        this.exerciseType = exerciseType;
        this.durationMinutes = durationMinutes;
        this.distanceKm = distanceKm;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
        this.notes = notes;
        this.heartRate = heartRate;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", exerciseType='" + exerciseType + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", distanceKm=" + distanceKm +
                ", caloriesBurned=" + caloriesBurned +
                ", date='" + date + '\'' +
                ", notes='" + notes + '\'' +
                ", heartRate=" + heartRate +
                '}';
    }
}
