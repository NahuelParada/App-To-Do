import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ToDoApp {
    private ArrayList<Tarea> tareas;

    public ToDoApp() {
        tareas = new ArrayList<>();
    }

    public void agregarTarea(String descripcion) {
        int id = tareas.size() + 1;
        tareas.add(new Tarea(id,descripcion));
    }

    public String listarTareas() {

        StringBuilder sb = new StringBuilder();
        if (tareas.isEmpty()) {
            sb.append("No Tareas");
            return sb.toString();
        }

        for (Tarea tarea : tareas) {
            sb.append(tarea.toString()).append("\n");
            return sb.toString();
        }

        return sb.toString();
    }


    public boolean completarTareas(int id) {
        for (Tarea tarea : tareas) {
            if(tarea.getId() == id){
                tarea.setCompletada(true);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarTareas(int id) {
        return tareas.removeIf(tarea -> tarea.getId() == id);
    }


    public boolean guardarEnJSON() {
        try {
            JSONArray arr = new JSONArray();

            for (Tarea t : tareas) {
                JSONObject obj = new JSONObject();
                obj.put("id", t.getId());
                obj.put("descripcion", t.getDescripcion());
                obj.put("completada", t.isCompletada());
                arr.put(obj);
            }

            Files.writeString(Path.of("tareas.json"), arr.toString(2));
            return true;

        } catch (Exception e) {
            return false;
        }
    }


    public boolean cargarDesdeJSON() {
        try {
            String data = Files.readString(Path.of("tareas.json"));

            JSONArray arr = new JSONArray(data);
            tareas.clear();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Tarea t = new Tarea(
                        obj.getInt("id"),
                        obj.getString("descripcion")
                );
                t.setCompletada(obj.getBoolean("completada"));
                tareas.add(t);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
