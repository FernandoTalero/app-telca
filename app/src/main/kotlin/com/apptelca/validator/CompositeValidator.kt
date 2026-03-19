package com.apptelca.validator

/**
 * Permite aplicar varios validadores a una misma cadena de texto.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class CompositeValidator(private val validators: List<Validator>) : Validator {
    /**
     * Valida una cadena de texto madiante una lista de validadores.
     *
     * @param text La cadena de texto a validar.
     * @return null si no hay errores o una cadena de texto con el primer error encontrado.
     */
    override fun validate(text: String): String? {
        // Obtenemos cada uno de los validadores...
        for(validator in validators) {
            // ... y aplicamos su función validate()
            val errorMessage = validator.validate(text)
            // Obtenemos el mensaje del primer error que se produzca
            if(errorMessage != null) {
                return errorMessage
            }
        }

        return null
    }
}