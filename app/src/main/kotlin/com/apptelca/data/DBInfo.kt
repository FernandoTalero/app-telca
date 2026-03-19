package com.apptelca.data

/**
 * Clase con la información de la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class DBInfo {
    companion object {
        const val NAME = "telca.db"
        const val VERSION = 1
    }

    /**
     * Clase interna Work con la información de la entidad Work.
     */
    class Work {
        companion object {
            const val TABLE_NAME = "works"
            const val C_ID = "id"
            const val C_TYPE = "type"
            const val C_SUBTYPE = "subtype"
            const val C_NAME = "name"
            const val C_DATE_TIME = "date_time"
        }
    }

    /**
     * Clase interna Measurement con la información de la entidad Measurement.
     */
    class Measurement {
        companion object {
            const val TABLE_NAME = "measurements"
            const val C_ID = "id"
            const val C_WORK_ID = "work_id"
            const val C_MEASUREMENT_TYPE = "measurement_type"
            const val C_VALUE = "value"
        }
    }

    /**
     * Clase interna Note con la información de la entidad Note.
     */
    class Note {
        companion object {
            const val TABLE_NAME = "notes"
            const val C_ID = "id"
            const val C_WORK_ID = "work_id"
            const val C_TEXT = "text"
            const val C_DATE_TIME = "date_time"
        }
    }
}