package easv.familiytracker.repository

import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import easv.familiytracker.MainActivity
import easv.familiytracker.models.BEFMember
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FamilyMembersDB {

    private val url = "https://familytracker.azurewebsites.net/api/FamilyMembers"

    private val httpClient : AsyncHttpClient = AsyncHttpClient()

    fun getAll(callback: ICallback) {
        httpClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val familyMembers = getMembersFromString(String(responseBody!!))
                callback.familyMembers(familyMembers);
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(MainActivity.TAG, "failure in getAll statusCode = $statusCode")
            }

        })
    }

    private fun getMembersFromString(jsonString: String?): List<BEFMember> {
        val result = ArrayList<BEFMember>()

        if (jsonString!!.startsWith("error")) {
            Log.d(MainActivity.TAG, "Error: $jsonString")
            return result
        }
        if (jsonString == null) {
            Log.d(MainActivity.TAG, "Error: NO RESULT")
            return result
        }
        var array: JSONArray?
        try {
            array = JSONObject(jsonString).getJSONArray("list")
            for (i in 0 until array.length()) {
                result.add(BEFMember(array.getJSONObject(i)))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

}