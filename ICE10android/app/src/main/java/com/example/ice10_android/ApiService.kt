import retrofit2.Call
import retrofit2.http.POST

interface ApiService {
    @POST("/create-payment-intent")
    fun createPaymentIntent(): Call<PaymentIntentResponse>
}
