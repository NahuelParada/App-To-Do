import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ToDoApp {
    private ArrayList<Tarea> tareas;
    private int nextId; // nuevo contador que garantiza ids únicos

    public ToDoApp() {
        tareas = new ArrayList<>();
        nextId = 1;
    }

    private void reindexar() {
        int nuevoId = 1;
        for (Tarea t : tareas) {
            t.setId(nuevoId++);
        }
        nextId = nuevoId;
    }


    // Agregar tarea usa nextId y lo incrementa
    public void agregarTarea(String descripcion) {
        int id = nextId++;
        tareas.add(new Tarea(id, descripcion));
    }

    public String listarTareas() {
        StringBuilder sb = new StringBuilder();
        if (tareas.isEmpty()) {
            sb.append("No hay tareas");
            return sb.toString();
        }

        for (Tarea tarea : tareas) {
            sb.append(tarea.toString()).append("\n");
        }

        return sb.toString();
    }

    public boolean completarTareas(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                tarea.setCompletada(true);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarTareas(int id) {
        boolean eliminada = tareas.removeIf(tarea -> tarea.getId() == id);
        if (eliminada) {
            reindexar();
        }
        return eliminada;
    }

    public boolean guardarEnJSON() {
        try {
            JSONObject root = new JSONObject();
            JSONArray arr = new JSONArray();

            for (Tarea t : tareas) {
                JSONObject obj = new JSONObject();
                obj.put("id", t.getId());
                obj.put("descripcion", t.getDescripcion());
                obj.put("completada", t.isCompletada());
                arr.put(obj);
            }

            root.put("tareas", arr);
            root.put("nextId", nextId); // guardamos nextId también

            Files.writeString(Path.of("tareas.json"), root.toString(2));
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean cargarDesdeJSON() {
        try {
            String data = Files.readString(Path.of("tareas.json"));

            JSONObject root = new JSONObject(data);
            JSONArray arr = root.optJSONArray("tareas");
            tareas.clear();

            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Tarea t = new Tarea(
                            obj.getInt("id"),
                            obj.getString("descripcion")
                    );
                    t.setCompletada(obj.getBoolean("completada"));
                    tareas.add(t);
                }
            }

            // Si en el JSON hay nextId, úsalo; si no, recalculamos safe
            if (root.has("nextId")) {
                nextId = root.getInt("nextId");
            } else {
                // fallback: nextId = max(id) + 1
                int max = 0;
                for (Tarea t : tareas) {
                    if (t.getId() > max) max = t.getId();
                }
                nextId = max + 1;
            }

            return true;
        } catch (Exception e) {
            // si no existe el archivo o hay error, dejamos la lista vacía y nextId = 1
            tareas.clear();
            nextId = 1;
            return false;
        }
    }

    // getter para pruebas/uso externo si necesitás
    public ArrayList<Tarea> getTareas() {
        return tareas;
    }
}
