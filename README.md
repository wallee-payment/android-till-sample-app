# Wallee Android Till SDK Sample

This project contains Android usage sample code for [Wallee's Android Till Interface SDK](https://github.com/wallee-payment/android-till-sdk) library.

## Who is this for?
Merchants that want to build their own Checkout/Till App to run on Android devices like
e.g. the [A920pro](https://terminal-shop.wallee.com/de/product/pax-a920pro-wallee/)

## What it has?
It has two samples example of how to use the **Android Till Interface SDK**

## Till Sample ## 

A simple example of a **Android Till Interface SDK** implementation that makes use of the 'API' client provided by the SDK:

#### About

For each **Android Till Interface SDK** request we provide an activity where we have an example of how to implement the sdk requests.

### Activities
- MainActivity: Provides example of how to send logs, open settings in wallee app and request required Android 10 permissions.
- CheckApiServiceCompatibilityActivity: Verify Wallee Android Till SDK compatibility.
- AuthorizeTransactionActivity: Provides example of how to perform purchase, credit, reservation transactions, generate pan token and choose transactions language.
- CompleteTransactionActivity: Provides example how to complete reservation.
- VoidTransactionActivity: Provides example how to cancel reservations.
- CancelLastTransactionOperationActivity: Provides example how to cancel last transaction.
- ExecuteSubmissionActivity: Provides example how to execute submission.
- ExecuteTransmissionActivity: Provides example how to execute transmission.
- ExecuteFinalBalanceActivity: Provides example how to perform final balance.
- GeneratePanTokenActivity: Provides example how to generate card PAN token.
- PinpadInformationActivity: Provides example how to get pinpad information (terminal ID, device serial number, space ID, merchant ID, and name).
- ExecuteConfigurationActivity: Provides example how to perform a configuration.
- ExecuteInitialisationActivity: Provides example how to perform a initialisation.

### Handle Response
- MockResponseHandler: Provide a example of how to handle response for all Wallee Android Till SDK requests.
- TransactionResponseActivity: Display Transaction Result.
- TransactionCompletionResponseActivity: Display Complete Reservation Result.
- TransactionVoidResponseActivity: Display Cancel Reservation Result.
- CancellationResultActivity: Display Cancel last Transaction Result.
- SubmissionResultActivity: Display Submission Result.
- TransmissionResultActivity: Display Transmission Result.
- FinalBalanceResultActivity: Display Final Balance Result.
- GeneratePanTokenResponseActivity: Display Generate Pan Token Result.
- PinpadInformationResponseActivity: Display Pinpad Information Result.
- ConfigurationResultActivity: Display Configuration Result.
- InitialisationResultActivity: Display Initialisation Result.

### How to use

- Open project in Android Studio.
- Select the `till` project.
- Run


## Interception App Sample

The Interception App uses two main activities to interact with transaction processes:

### Activities

1. **BeforeActivity:**
    - This activity is triggered before a transaction starts.
    - It contains an intent action defined as `<action android:name="com.wallee.android.AUTHORIZE_TRANSACTION_BEFORE"/>`.
    - Use this activity to perform any preliminary actions before the transaction begins.

2. **AfterActivity:**
    - This activity is triggered after a transaction finishes.
    - It contains an intent action defined as `<action android:name="com.wallee.android.AUTHORIZE_TRANSACTION_AFTER"/>`.
    - Use this activity to perform any final actions after the transaction completes.

By using these activities, you can effectively intercept and manage transactions within your application, ensuring that necessary actions are taken at both the start and end of the transaction process.


### How to use

- Open project in Android Studio.
- Select the `interception` project.
- Run