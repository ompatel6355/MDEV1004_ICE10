"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.CreatePaymentIntent = void 0;
const stripe_1 = __importDefault(require("../Config/stripe"));
const stripe_2 = __importDefault(require("stripe"));
const stripe = new stripe_2.default(stripe_1.default.secretKey, {});
async function CreatePaymentIntent(req, res, next) {
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
        publishableKey: stripe_1.default.publishableKey
    });
}
exports.CreatePaymentIntent = CreatePaymentIntent;
//# sourceMappingURL=stripe.js.map