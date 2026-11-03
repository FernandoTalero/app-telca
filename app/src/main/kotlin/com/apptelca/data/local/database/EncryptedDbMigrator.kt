package com.apptelca.data.local.database

import android.content.Context
import android.util.Log
import com.apptelca.core.extension.secureDelete
import com.apptelca.core.extension.toSQLCipherHex
import net.zetetic.database.sqlcipher.SQLiteDatabase
import java.io.File

/**
 * Migra una base de datos sin cifrar a una cifrada con SQLCipher.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
object EncryptedDbMigrator {
    /**
     * Lleva a cabo el proceso de migración a la base de datos cifrada.
     *
     * @param context El Context de la aplicación
     * @param dbName El nombre de la base de datos sin cifrar
     * @param passphrase La clave de la nueva base de datos encriptada
     */
    fun migrate(context: Context, dbName: String, passphrase: ByteArray) {
        // Cargamos la librería de SQLCipher
        try {
            System.loadLibrary("sqlcipher")
        } catch(e: UnsatisfiedLinkError) {
            Log.e(
                "EncryptedDbMigrator::migrate",
                "La librería ya estaba cargada", e
            )
        }

        // Archivo principal de la base de datos
        val dbFile = context.getDatabasePath(dbName)
        /* Si existe el archivo de la base de datos y está sin cifrar (texto plano),
         llevamos a cabo el proceso de migración */
        if(dbFile.exists() && isPlainText(dbFile)) {
            // Archivo -wal de la base de datos
            val dbWalFile = File("${dbFile.absolutePath}-wal")
            // Archivo -shm de la base de datos
            val dbShmFile = File("${dbFile.absolutePath}-shm")

            // Archivo temporal para la nueva base de datos encriptada
            val tempDbFile = context.getDatabasePath("${dbName}_temp")
            /* Eliminamos el archivo en caso de que ya exista por un intento de migración
             anterior fallido */
            tempDbFile.secureDelete()

            try {
                // Nombre de la base de datos encriptada
                val encryptedDbName = "encrypted_db"

                // Abrimos la base de datos sin cifrar
                val plainTextDb = SQLiteDatabase
                    .openOrCreateDatabase(dbFile, "", null, null)

                // Forzamos un WAL checkpoint para volcar los datos del archivo -wal
                var sql = "PRAGMA wal_checkpoint(FULL);"
                plainTextDb.rawExecSQL(sql)

                // Adjuntamos la nueva base de datos encriptada
                sql = "ATTACH DATABASE '${tempDbFile.absolutePath}' AS $encryptedDbName" +
                        " KEY ${passphrase.toSQLCipherHex()};"
                plainTextDb.rawExecSQL(sql)

                // Volcamos el contenido de la base de datos actual
                sql = "SELECT sqlcipher_export('$encryptedDbName');"
                plainTextDb.rawExecSQL(sql)

                // Desvinculamos la base de datos cifrada y cerramos la conexión
                sql = "DETACH DATABASE $encryptedDbName;"
                plainTextDb.close()

                // Eliminamos los tres archivos antiguos de la base de datos
                if(dbFile.exists()) dbFile.delete()
                if(dbWalFile.exists()) dbWalFile.delete()
                if(dbShmFile.exists()) dbShmFile.delete()

                /* Renombramos el archivo temporal con la base de datos cifrada. En
                * caso de error, lo eliminamos para no dejar residuos del intento
                * de migración  */
                if(!tempDbFile.renameTo(dbFile)) {
                    Log.e(
                        "EncryptedDbMigrator::migrate",
                        "Error al renombrar la base de datos encriptada"
                    )
                    tempDbFile.secureDelete()
                }
            } catch(e: Exception) {
                Log.e(
                    "EncryptedDbMigrator::migrate",
                    "Error al migrar la base de datos", e
                )

                /* Eliminamos el archivo temporal para no dejar residuos del
                 intento de migración */
                tempDbFile.secureDelete()
            }
        }
    }

    /**
     * Comprueba si la base de datos está en texto plano.
     *
     * @param dbFile El archivo de la base de datos.
     * @return True si la base de datos está en texto plano; false
     * en caso contrario.
     */
    private fun isPlainText(dbFile: File): Boolean =
        try {
            val db = android.database.sqlite.SQLiteDatabase.openDatabase(
                dbFile.absolutePath,
                null,
                android.database.sqlite.SQLiteDatabase.OPEN_READONLY
            )

            db.close()
            true // La base de datos estaba sin cifrar
        } catch(_: Exception) {
            false // La base de datos estaba cifrada, o el archivo, corrupto
        }
}