import { Request, Response, NextFunction } from 'express';
import config from '../Config/stripe';
import Stripe from 'stripe';
const stripe = new Stripe(config.secretKey, {
    //apiVersion: '2022-11-15',
});
export async function CreatePaymentIntent(req: Request, res: Response, next: NextFunction) {
    const customer = await stripe.customers.create();
    const paymentIntent = await stripe.paymentIntents.create({
        amount: 999,
        currency: 'cad',
        customer: customer.id,
        automatic_payment_methods: { enabled: true }
    });
    return res.status(200).json({
        success: true,
        msg: "Payment Intent Created",
        clientSecret: paymentIntent.client_secret,
        customer: customer.id,
        publishableKey: config.publishableKey
    });
}