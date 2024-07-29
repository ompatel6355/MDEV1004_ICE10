const express = require('express');
const Stripe = require('stripe');
const stripe = Stripe('sk_test_51PhfMN2M3NXwxZNYlCYeUlszLhBTJ8lvA0LpRBqJUsyIldZ2AWVJXj1hWqswYVK8RNIPxqzS0PjYLeLeEXs7bK7Q00oKXSEEFE'); // Replace with your Stripe secret key

const app = express();
app.use(express.json());

app.post('/create-payment-intent', async (req, res) => {
    try {
        const paymentIntent = await stripe.paymentIntents.create({
            amount: 1099, // Amount in cents
            currency: 'usd',
            // Add more options here if needed
        });

        res.json({ clientSecret: paymentIntent.client_secret });
    } catch (error) {
        res.status(500).send({ error: error.message });
    }
});

app.listen(4242, () => console.log('Server is running on port 4242'));
