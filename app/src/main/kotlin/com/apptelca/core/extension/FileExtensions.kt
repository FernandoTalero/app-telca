package com.apptelca.core.extension

import java.io.File

/**
 * Elimina un archivo de manera segura.
 *
 * @return True si el archivo se ha eliminado correctamente; false
 * en caso contraro.
 */
fun File.secureDelete(): Boolean = this.exists() && this.delete()