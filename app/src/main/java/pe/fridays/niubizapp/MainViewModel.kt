package pe.fridays.niubizapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.StringBuilder
import pe.fridays.niubizapp.Constants.EXTCALLER
import pe.fridays.niubizapp.Constants.EXTOP.CLOSING
import pe.fridays.niubizapp.Constants.EXTOP.COPY_VOUCHER
import pe.fridays.niubizapp.Constants.EXTOP.DUPE
import pe.fridays.niubizapp.Constants.EXTOP.INIT
import pe.fridays.niubizapp.Constants.EXTOP.PRINT
import pe.fridays.niubizapp.Constants.EXTOP.SALE
import pe.fridays.niubizapp.Constants.EXTOP.VOID
import pe.fridays.niubizapp.Constants.RESPONSE_CODE.CANCEL_OPERATION
import pe.fridays.niubizapp.Constants.RESPONSE_CODE.FAIL_IN
import pe.fridays.niubizapp.Constants.RESPONSE_CODE.NOT_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_CODE.SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_CODE.SUCCESS_IN
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.CANCEL_BY_USER
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.CLOSING_NOT_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.CLOSING_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.COPY_VOUCHER_NOT_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.COPY_VOUCHER_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.DUPE_NOT_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.DUPE_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.EMPTY_LOTE
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.INIT_FAIL
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.INIT_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.SALE_NOT_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.SALE_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.UNKNOWN
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.VOID_NOT_SUCCESS
import pe.fridays.niubizapp.Constants.RESPONSE_MESSAGE.VOID_SUCCESS
import pe.fridays.niubizapp.Constants.VNP_REQUEST_CODE

const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {
    /**
     * NO SE HA IMPLEMENTADO EL LLAMADO A IMPRESORA
     */

    var message = MutableLiveData<String?>(null)
    var response = MutableLiveData<String?>(null)
    var currentCode = 0

    var merchantSelected = ""

    init {
        message.postValue(null)
        response.postValue(null)
    }

    private fun checkAmount(amount: Double): Boolean {
        return if (amount <= 0.0) {
            message.postValue("El monto debe ser mayor a 0")
            false
        } else true
    }

    fun sendTestPrint(activity: Activity) {
        currentCode = PRINT
        val route = "posweb://transact/?EXTCALLER=${EXTCALLER}&EXTOP=${PRINT}&EXTMONTO=0&EXTHEAD=&EXTTAIL=&EXTBODY=${getStringToPrintTest()}"
        Log.e(TAG, route)
        callAppNiubiz(activity, route)
    }

    fun sendNotSale(activity: Activity, code: Int, merchantCode: String? = null) {
        currentCode = code
        val route = "posweb://transact/?EXTCALLER=${EXTCALLER}&EXTOP=${code}&EXTMONTO=0${merchantCode.toExtCom()}&EXTHEAD=&EXTBODY=&EXTTAIL="
        callAppNiubiz(activity, route)
    }

    fun sendSale(activity: Activity, amount: Double, merchantCode: String? = null) {
        if (checkAmount(amount)) {
            currentCode = SALE
            val route =
                "posweb://transact/?EXTCALLER=${EXTCALLER}&EXTOP=${SALE}&EXTMONTO=${amount.toAmountString()}${merchantCode.toExtCom()}&EXTHEAD=&EXTBODY=&EXTTAIL="
            callAppNiubiz(activity, route)
        }
    }

    private fun callAppNiubiz(activity: Activity, route: String) {
        Log.e("route", route)
        activity.startActivityForResult(
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(route)
            }, VNP_REQUEST_CODE
        )
    }

    fun checkActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VNP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var mPWRIPARAMS = ""
            val props = data?.extras
            Log.e("props", "=$props")
            if (data?.action != null && (data.action.equals(Intent.ACTION_VIEW)
                        || data.action.equals(Intent.ACTION_MAIN))) {
                data.data?.let {
                    mPWRIPARAMS = it.encodedQuery ?: ""
                }
            } else if (props != null) {
                mPWRIPARAMS = props.getString("PWRIPARAMS") ?: ""
            }
            var res = ""
            if (mPWRIPARAMS.isNotEmpty()) {
                Log.e(TAG, "mPWRIPARAMS = $mPWRIPARAMS")
                res = mPWRIPARAMS.substring(24, 26)
                if (res.isNotEmpty()) {
                    val resMessage = validateResponse(res)
                    response.postValue(
                        StringBuilder()
                            .append(resMessage)
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"))
                            .append(mPWRIPARAMS)
                            .toString()
                    )
                }
            }
            Log.e(TAG, "checkActivityResult = $res")
        }
    }

    private fun validateResponse(res: String): String {
        val messageResponse = when (currentCode) {
            SALE -> {
                when (res) {
                    SUCCESS -> SALE_SUCCESS
                    NOT_SUCCESS -> SALE_NOT_SUCCESS
                    CANCEL_OPERATION -> CANCEL_BY_USER
                    else -> UNKNOWN
                }
            }
            VOID -> {
                when (res) {
                    SUCCESS -> VOID_SUCCESS
                    NOT_SUCCESS -> VOID_NOT_SUCCESS
                    CANCEL_OPERATION -> CANCEL_BY_USER
                    else -> UNKNOWN
                }
            }
            CLOSING -> {
                when (res) {
                    SUCCESS -> CLOSING_SUCCESS
                    NOT_SUCCESS -> CLOSING_NOT_SUCCESS
                    "08" -> EMPTY_LOTE
                    else -> UNKNOWN
                }
            }
            COPY_VOUCHER -> {
                when (res) {
                    SUCCESS -> COPY_VOUCHER_SUCCESS
                    NOT_SUCCESS -> COPY_VOUCHER_NOT_SUCCESS
                    else -> UNKNOWN
                }
            }
            DUPE -> {
                when (res) {
                    SUCCESS -> DUPE_SUCCESS
                    NOT_SUCCESS -> DUPE_NOT_SUCCESS
                    else -> UNKNOWN
                }
            }
            INIT -> {
                when (res) {
                    SUCCESS_IN -> INIT_SUCCESS
                    FAIL_IN -> INIT_FAIL
                    else -> UNKNOWN
                }
            }
            else -> UNKNOWN
        }
        return messageResponse
    }

}