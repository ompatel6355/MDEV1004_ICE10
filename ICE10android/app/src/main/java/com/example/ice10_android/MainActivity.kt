package com.example.ice10_android

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    private lateinit var paymentSheet: PaymentSheet
    private var clientSecret: String = ""
    private val publishableKey = "pk_test_51PhfMN2M3NXwxZNYNrS1cghvDhlBaA1XmKChBHUCRGAeRekXhyXVKjJWWwjXyWql9QF5cjyUq1YrbNqcXBnwGZCI00FEJUkvrt" // Replace with your actual publishable key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Stripe PaymentConfiguration
        PaymentConfiguration.init(applicationContext, publishableKey)

        // Initialize the PaymentSheet
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        // Fetch client secret from backend
        fetchClientSecret()

        // Set up the button click listener
        val payButton: Button = findViewById(R.id.pay_button)
        if (payButton != null) {
            Log.d("MainActivity", "Pay button found")
            payButton.setOnClickListener {
                Log.d("MainActivity", "Pay button clicked")
                presentPaymentSheet()
            }
        } else {
            Log.e("MainActivity", "Pay button not found")
        }
    }

    private fun fetchClientSecret() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:4242/") // Use 10.0.2.2 to access localhost from Android Emulator
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.createPaymentIntent().enqueue(object : retrofit2.Callback<Map<String, String>> {
            override fun onResponse(
                call: retrofit2.Call<Map<String, String>>,
                response: retrofit2.Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    clientSecret = response.body()?.get("clientSecret") ?: ""
                    Log.d("MainActivity", "Client Secret fetched: $clientSecret")
                } else {
                    Log.e("MainActivity", "Failed to fetch client secret: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<Map<String, String>>, t: Throwable) {
                Log.e("MainActivity", "Error fetching client secret: ${t.message}")
            }
        })
    }

    private fun presentPaymentSheet() {
        if (clientSecret.isNotEmpty()) {
            val configuration = PaymentSheet.Configuration(
                "Example, Inc.",
                customer = PaymentSheet.CustomerConfiguration(
                    id = "customer_id", // Replace with your customer ID from your server
                    ephemeralKeySecret = "ephemeral_key_secret" // Replace with your ephemeral key secret from your server
                )
            )

            paymentSheet.presentWithPaymentIntent(
                clientSecret,
                configuration
            )
        } else {
            Log.e("MainActivity", "clientSecret is not initialized.")
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Log.d("MainActivity", "Payment completed successfully.")
            }
            is PaymentSheetResult.Canceled -> {
                Log.d("MainActivity", "Payment was canceled.")
            }
            is PaymentSheetResult.Failed -> {
                Log.e("MainActivity", "Payment failed: ${paymentSheetResult.error.localizedMessage}")
            }
        }
    }
}

interface ApiService {
    @POST("create-payment-intent")
    fun createPaymentIntent(): retrofit2.Call<Map<String, String>>
}
