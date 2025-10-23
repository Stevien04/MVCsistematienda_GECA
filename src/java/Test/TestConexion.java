package Test;

import Config.clsConexion;

public class TestConexion {
    public static void main(String[] args) {
        if (clsConexion.probarConexion()) {
            System.out.println("✅ Conexión exitosa con la base de datos.");
        } else {
            System.out.println("❌ No se pudo conectar a la base de datos.");
        }
    }
}
