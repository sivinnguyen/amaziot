package me.nguyentran.amazfit.iot.firebase.restful

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import org.json.JSONException
import org.json.JSONObject
import okhttp3.RequestBody


class MainActivity : AppCompatActivity() {
    private val _TAG = "AMZIoT"
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getLightStatus()

        spark_button.setEventListener(object: SparkEventListener {
            override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {
                Log.d(_TAG, "Start")
            }

            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {
                Log.d(_TAG, "End")
            }

            override fun onEvent(button: ImageView?, buttonState: Boolean) {
                if (buttonState) {
                    // Button is active
                    // Turn on the light
                    setLightStatus(1)

                } else {
                    // Button is inactive
                    // Turn off the light
                    setLightStatus(0)
                }
            }
        })

    }

    private fun getLightStatus() {
        Log.d(_TAG, "Attempting to fetch JSON")

        val url = "https://<YOUR_ID>.firebaseio.com/light/on.json"
        val request = Request.Builder().url(url).build()
        // val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                Log.d(_TAG, "Light status: $body")

                if (body == "1") runOnUiThread {
                    spark_button.isChecked = true
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(_TAG, "Failed to get light status")
            }

        })
    }

    private fun setLightStatus(status: Int) {
        val url = "https://<YOUR_ID>.firebaseio.com/light.json"

        // create json here
        val jsonObject = JSONObject()
        try {
            jsonObject.put("on", status)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val _MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8")

        val body = RequestBody.create(_MEDIA_TYPE_JSON, jsonObject.toString())
        val request = Request.Builder()
            .url(url).patch(body)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(_TAG, "Request failed" + e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(_TAG, "Success :" + response.body()!!.string())
            }

        })

    }
}
