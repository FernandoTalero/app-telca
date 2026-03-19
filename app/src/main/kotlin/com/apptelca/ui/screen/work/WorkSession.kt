package com.apptelca.ui.screen.work

/**
 * Define un singleton para controlar la validez del workId alamacenado
 * en el estado de la UI, la cual se basa en la existencia del objeto
 * Work en la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
object WorkSession {
    /**
     * Indica el estado del id del trabajo actual.
     * Valor null: no hay trabajo cargado.
     * Valor mayor de cero: hay un trabajo cargado.
     * Valor cero: el trabajo cargado se ha eliminado.
     */
    var activeWorkId: Long? = null
}