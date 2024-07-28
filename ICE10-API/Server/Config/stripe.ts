export default{
    secretKey: process.env.STRIPE_SECRET_KEY as string,
    publishableKey: process.env.STRIPE_PUBLISHABLE_KEY as string
}