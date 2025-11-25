import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ToDoApp app = new ToDoApp();
        Scanner sc = new Scanner(System.in);

        // Cargar tareas guardadas
        app.cargarDesdeJSON();

        int opcion;

        do {
            System.out.println("\n --- ToDo App ---");
            System.out.println("1. Agregar tarea");
            System.out.println("2. Listar tareas");
            System.out.println("3. Completar tarea");
            System.out.println("4. Eliminar tarea");
            System.out.println("5. Guardar");
            System.out.println("0. Salir");
            System.out.print("Elegir opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> {
                    System.out.print("Descripción: ");
                    String desc = sc.nextLine();
                    app.agregarTarea(desc);
                    System.out.println("Tarea agregada!");
                }
                case 2 -> {
                    System.out.println("\nTus tareas:");
                    System.out.println(app.listarTareas());
                }
                case 3 -> {
                    System.out.print("ID de la tarea a completar: ");
                    int id = Integer.parseInt(sc.nextLine());
                    if (app.completarTareas(id))
                        System.out.println("Tarea completada!");
                    else
                        System.out.println("No se encontró la tarea.");
                }
                case 4 -> {
                    System.out.print("ID de la tarea a eliminar: ");
                    int id = Integer.parseInt(sc.nextLine());
                    if (app.eliminarTareas(id))
                        System.out.println("Tarea eliminada!");
                    else
                        System.out.println("No existe esa tarea.");
                }
                case 5 -> {
                    if(app.guardarEnJSON())
                        System.out.println("Guardado con éxito!");
                    else
                        System.out.println("Error al guardar.");
                }
                case 0 -> {
                    app.guardarEnJSON();
                    System.out.println("Guardado y salida exitosa.");
                }
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        sc.close();
    }
}
