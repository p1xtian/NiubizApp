package pe.fridays.niubizapp

object Constants {

    const val EXTCALLER = "testNiubiz"
    const val VNP_REQUEST_CODE = 221

    object EXTOP {
        const val SALE = 1
        const val VOID = 2
        const val MULTI_MERCHANT = 3//4
        const val CLOSING = 11
        const val COPY_VOUCHER = 13
        const val DUPE = 9
        const val INIT = 0
        const val SCAN = 40
        const val PRINT = 30
    }

    object RESPONSE_CODE {
        const val SUCCESS = "00"
        const val NOT_SUCCESS = "01"
        const val SUCCESS_IN = "07"
        const val FAIL_IN = "08"
        const val CANCEL_OPERATION = "13"
    }

    object RESPONSE_MESSAGE {
        const val SALE_SUCCESS = "Transacción exitosa"
        const val SALE_NOT_SUCCESS = "Transacción no exitosa"
        const val VOID_SUCCESS = "Anulación exitosa"
        const val VOID_NOT_SUCCESS = "Anulación no exitosa"
        const val CLOSING_SUCCESS = "Cierre exitoso"
        const val CLOSING_NOT_SUCCESS = "Cierre no exitoso"
        const val COPY_VOUCHER_SUCCESS = "Rerefencia encontrada en el lote"
        const val COPY_VOUCHER_NOT_SUCCESS = "Transacción no encontrada en el lote"
        const val DUPE_SUCCESS = "Lote no está vacío"
        const val DUPE_NOT_SUCCESS = "Lote vacío"
        const val INIT_SUCCESS = "Inicialización previamente realizada"
        const val INIT_FAIL = "Aplicación no inicializada"
        const val UNKNOWN = "Desconocido"
        const val CANCEL_BY_USER = "Cancelado por usuario"
        const val EMPTY_LOTE = "Lote vacío"
    }
}