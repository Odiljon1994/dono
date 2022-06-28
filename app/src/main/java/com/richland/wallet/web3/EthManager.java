package com.richland.wallet.web3;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CenterPrime on 2020/09/19.
 */

public class EthManager {
    private static final EthManager ourInstance = new EthManager();


    /**
     * Web3j Client
     */
    private Web3j web3j;


    private HyperLedgerApi hyperLedgerApi;

    /**
     * Infura node url
     */
    private String mainnetInfuraUrl = "";

    public static EthManager getInstance() {
        return ourInstance;
    }

    public EthManager() {
    }

    /**
     * Initialize EthManager
     *
     * @param mainnetInfuraUrl : Infura Url
     */
    public void init(String mainnetInfuraUrl) {
        this.mainnetInfuraUrl = mainnetInfuraUrl;
        web3j = Web3j.build(new HttpService(mainnetInfuraUrl, false));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.231.96.72:8081") //http://34.231.96.72/
           //     .baseUrl("http://52.7.239.97:8081")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        hyperLedgerApi = retrofit.create(HyperLedgerApi.class);
    }

    /**
     * Get Current Gas Price
     */
    public BigInteger getGasPrice() {
        try {
            EthGasPrice price = web3j.ethGasPrice()
                    .send();
            return price.getGasPrice();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new BigInteger(Const.DEFAULT_GAS_PRICE);
    }

    public BigInteger getGasLimit(String senderAddress,
                                  BigInteger nonce,
                                  BigInteger gasPrice,
                                  BigInteger gasLimit,
                                  String contractAddress,
                                  BigInteger value,
                                  String data) {
        try {
            Transaction transaction = new Transaction(senderAddress, nonce, gasPrice, gasLimit, contractAddress, value, data);
            EthEstimateGas estimateGas = web3j.ethEstimateGas(transaction).send();
            return  estimateGas.getAmountUsed();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new BigInteger(Const.DEFAULT_GAS_LIMIT);
    }


    /**
     * Create Wallet by password
     */
    public Single<Wallet> createWallet(String password, Context context) {
        return Single.fromCallable(() -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
            try {

                String walletAddress = CenterPrimeUtils.generateNewWalletFile(password, new File(context.getFilesDir(), ""), false);
                String walletPath = context.getFilesDir() + "/" + walletAddress.toLowerCase();
                File keystoreFile = new File(walletPath);
                String keystore = read_file(context, keystoreFile.getName());

                body.put("action_type", "WALLET_CREATE");
                body.put("wallet_address", walletAddress);
                body.put("status", "SUCCESS");
                sendEventToLedger(body, context);
                return new Wallet(walletAddress, keystore);
            } catch (CipherException | IOException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
                e.printStackTrace();
                body.put("status", "FAILURE");
            }
            sendEventToLedger(body, context);
            return null;
        });
    }

    public MnemonicWallet createWalletWithMnemonic() {
        String privateKey = "";
        String walletAddress = "";
        Random random = new Random();

        byte[] initialEntropy = new byte[16];
        random.nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        System.out.println("Mnemonics: " + mnemonic);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, null));

        int[] path = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0,0};
        Bip32ECKeyPair x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);

        Credentials credentials = Credentials.create(x);
        System.out.println("Wallet address: " + credentials.getAddress());
        System.out.println("Private key: " + credentials.getEcKeyPair().getPrivateKey());
        System.out.println("Private key: " + Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));

        privateKey = Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey());
        walletAddress = credentials.getAddress();

        return new MnemonicWallet(mnemonic, privateKey, walletAddress);
    }

    public MnemonicWallet importWalletByMnemonic(String mnemonic) {
        String privateKey;
        String walletAddress;

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, null));

        int[] path = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0,0};
        Bip32ECKeyPair x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);

        Credentials credentials = Credentials.create(x);
        System.out.println("Wallet address: " + credentials.getAddress());
        System.out.println("Private key: " + credentials.getEcKeyPair().getPrivateKey());
        System.out.println("Private key: " + Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));

        privateKey = Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey());
        walletAddress = credentials.getAddress();

        return new MnemonicWallet(mnemonic, privateKey, walletAddress);
    }

    /**
     * Get Keystore by wallet address
     */
    public Single<String> getKeyStore(String walletAddress, Context context) {
        return Single.fromCallable(() -> {
            String wallet = walletAddress;
            if (wallet.startsWith("0x")) {
                wallet = wallet.substring(2);
            }
            String walletPath = context.getFilesDir() + "/" + wallet.toLowerCase();
            File keystoreFile = new File(walletPath);
            if (keystoreFile.exists()) {
                return read_file(context, keystoreFile.getName());
            } else {
                throw new Exception("Keystore is NULL");
            }
        });
    }

    /**
     * Export Keystore by wallet address
     */
    public Single<String> exportKeyStore(String walletAddress, Context context) {
        return Single.fromCallable(() -> {
            String wallet = walletAddress;
            if (wallet.startsWith("0x")) {
                wallet = wallet.substring(2);
            }
            String walletPath = context.getFilesDir() + "/" + wallet.toLowerCase();
            File keystoreFile = new File(walletPath);
            HashMap<String, Object> body = new HashMap<>();
            body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
            if (keystoreFile.exists()) {
                body.put("action_type", "WALLET_EXPORT_KEYSTORE");
                body.put("wallet_address", walletAddress);
                body.put("status", "SUCCESS");
                sendEventToLedger(body, context);
                return read_file(context, keystoreFile.getName());
            } else {
                body.put("action_type", "WALLET_EXPORT_KEYSTORE");
                body.put("wallet_address", walletAddress);
                body.put("status", "FAILURE");
                sendEventToLedger(body, context);
                throw new Exception("Keystore is NULL");
            }
        });
    }

    /**
     * Import Wallet by Keystore
     */
    public Single<String> importFromKeystore(String keystore, String password, Context context) {
        return Single.fromCallable(() -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
            try {
                Credentials credentials = CenterPrimeUtils.loadCredentials(password, keystore);
                String walletAddress = CenterPrimeUtils.generateWalletFile(password, credentials.getEcKeyPair(), new File(context.getFilesDir(), ""), false);

                body.put("action_type", "WALLET_IMPORT_KEYSTORE");
                body.put("wallet_address", walletAddress);
                body.put("status", "SUCCESS");
                sendEventToLedger(body, context);
                return walletAddress;
            } catch (IOException e) {
                body.put("status", "FAILURE");
                e.printStackTrace();
            }
            sendEventToLedger(body, context);
            return null;
        });
    }

    /**
     * Import Wallet with Private Key
     */
    public Single<String> importFromPrivateKey(String privateKey, Context context) {
        return Single.fromCallable(() -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
            String password = "";
            // Decode private key
            ECKeyPair keys = ECKeyPair.create(Numeric.hexStringToByteArray(privateKey));

            try {
                Credentials credentials = Credentials.create(keys);
                String walletAddress = CenterPrimeUtils.generateWalletFile(password, credentials.getEcKeyPair(), new File(context.getFilesDir(), ""), false);

                body.put("action_type", "WALLET_IMPORT_PRIVATE_KEY");
                body.put("wallet_address", walletAddress);
                body.put("status", "SUCCESS");
                sendEventToLedger(body, context);
                return walletAddress;
            } catch (CipherException | IOException e) {
                e.printStackTrace();
                body.put("status", "FAILURE");
            }
            sendEventToLedger(body, context);
            return null;
        });
    }

    /**
     * Export Private Key
     */
    public Single<String> exportPrivateKey(String walletAddress, String password, Context context) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {
                    String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
                    body.put("action_type", "WALLET_EXPORT_PRIVATE_KEY");
                    body.put("wallet_address", walletAddress);
                    body.put("status", "SUCCESS");
                    sendEventToLedger(body, context);
                    return Single.just(privateKey);
                });
    }

    /**
     * Get Eth Balance of Wallet
     */
    public Single<BigDecimal> balanceInEth(String address, Context context) {
        return Single.fromCallable(() -> {
            BigInteger valueInWei = web3j
                    .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                    .send()
                    .getBalance();

            HashMap<String, Object> body = new HashMap<>();
            body.put("action_type", "COIN_BALANCE");
            body.put("wallet_address", address);
            body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
            body.put("balance", BalanceUtils.weiToEth(valueInWei));
            body.put("status", "SUCCESS");
            sendEventToLedger(body, context);


            return BalanceUtils.weiToEth(valueInWei);
        });
    }


    /**
     * Load Credentials
     */
    public Single<Credentials> loadCredentials(String walletAddress, String password, Context context) {
        return getKeyStore(walletAddress, context)
                .flatMap(keystore -> {
                    try {
                        Credentials credentials = CenterPrimeUtils.loadCredentials(password, keystore);
                        return Single.just(credentials);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Single.error(e);
                    } catch (CipherException e) {
                        e.printStackTrace();
                        return Single.error(e);
                    }
                });
    }
    /**
     * Add Custom Token
     */
    public Single<Token> searchTokenByContractAddress(String contractAddress, String walletAddress, String password, Context context) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {
                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            web3j, credentials, isMainNet() ? ChainId.MAINNET : ChainId.ROPSTEN, transactionReceiptProcessor);
                    Erc20TokenWrapper contract = Erc20TokenWrapper.load(contractAddress, web3j,
                            transactionManager, BigInteger.ZERO, BigInteger.ZERO);
                    String tokenName = contract.name().getValue();
                    String tokenSymbol = contract.symbol().getValue();
                    BigInteger decimalCount = contract.decimals().getValue();

                    return Single.just(new Token(contractAddress, tokenSymbol, tokenName, decimalCount.toString()));
                });

    }
    /**
     * Get ERC20 Token Balance of Wallet
     */
    public Single<BigDecimal> getTokenBalance(String walletAddress, String password, String tokenContractAddress, Context context) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {
                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            web3j, credentials, isMainNet() ? ChainId.MAINNET : ChainId.ROPSTEN, transactionReceiptProcessor);
                    Erc20TokenWrapper contract = Erc20TokenWrapper.load(tokenContractAddress, web3j,
                            transactionManager, BigInteger.ZERO, BigInteger.ZERO);
                    Address address = new Address(walletAddress);
                    BigInteger tokenBalance = contract.balanceOf(address).getValue();
                    String tokenName = contract.name().getValue();
                    String tokenSymbol = contract.symbol().getValue();
                    BigInteger decimalCount = contract.decimals().getValue();

                    BigDecimal tokenValueByDecimals = BalanceUtils.balanceByDecimal(tokenBalance, decimalCount);

                    HashMap<String, Object> body = new HashMap<>();
                    body.put("action_type", "TOKEN_BALANCE");
                    body.put("wallet_address", walletAddress);
                    body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
                    body.put("token_smart_contract", tokenContractAddress);
                    body.put("token_name", tokenName);
                    body.put("token_symbol", tokenSymbol);
                    body.put("balance", tokenValueByDecimals.doubleValue());
                    body.put("status", "SUCCESS");
                    sendEventToLedger(body, context);


                    return Single.just(tokenValueByDecimals);
                });
    }

    /**
     * Send Ether
     */
    public Single<String> sendEther(String walletAddress, String password,
                                    BigInteger gasPrice,
                                    BigInteger gasLimit,
                                    BigDecimal etherAmount,
                                    String to_Address,
                                    Context context) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {

                    String transactionHash = null;
                    BigInteger nonce = getNonce(walletAddress);
                    BigDecimal weiValue = Convert.toWei(etherAmount, Convert.Unit.ETHER);

                    BigInteger currentGasPrice = getGasPrice().add(new BigInteger("4000000000"));

                    System.out.println("** GasPrice Eth: " + currentGasPrice);

                    RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                            nonce, currentGasPrice, gasLimit, to_Address, weiValue.toBigIntegerExact());
                    byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                    String hexValue = Numeric.toHexString(signedMessage);

                    EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();

                    transactionHash = ethSendTransaction.getTransactionHash();

                    HashMap<String, Object> body = new HashMap<>();
                    body.put("action_type", "SEND_ETHER");
                    body.put("from_wallet_address", walletAddress);
                    body.put("to_wallet_address", to_Address);
                    body.put("amount", etherAmount.toPlainString());
                    body.put("tx_hash", transactionHash);
                    body.put("gasLimit", gasLimit.toString());
                    body.put("gasPrice", gasPrice.toString());
                    body.put("fee", gasLimit.multiply(gasPrice).toString());
                    body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
                    body.put("status", "SUCCESS");
                    sendEventToLedger(body, context);

                    return Single.just(transactionHash);
                });
    }
    private static String generateDataForTokenTransfer(String _to, BigInteger _amount) {
        List<Type> params = Arrays.asList(new Address(_to), new Uint256(_amount));
        List<TypeReference<?>> returnTypes = Arrays.asList(new TypeReference<Bool>() {
        });
        Function function = new Function("transfer", params, returnTypes);
        return FunctionEncoder.encode(function);
    }



    /**
     * Send Token
     */
    public Single<String> sendToken(String walletAddress, String password,
                                    BigInteger gasPrice,
                                    BigInteger gasLimit,
                                    BigDecimal tokenAmount,
                                    String to_Address,
                                    String tokenContractAddress,
                                    Context context) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {
                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            web3j, credentials, isMainNet() ? ChainId.MAINNET : ChainId.ROPSTEN, transactionReceiptProcessor);

                    BigInteger nonce = getNonce(walletAddress);
                    BigInteger currentGasPrice = getGasPrice().add(new BigInteger("8000000000"));

                 //   BigInteger decimalCount = contract.decimals().getValue();

                    BigDecimal formattedAmount1 = BalanceUtils.amountByDecimal(tokenAmount, new BigDecimal(18));

                    String data = generateDataForTokenTransfer(to_Address, formattedAmount1.toBigInteger());

                    BigInteger currentGasLimit = getGasLimit(walletAddress, nonce, currentGasPrice, gasLimit, tokenContractAddress, BigInteger.ZERO, data);


                    System.out.println("** GasPrice Token: " + currentGasPrice);
                    System.out.println("** GasLimit Token: " + currentGasLimit);


                    Erc20TokenWrapper contract = Erc20TokenWrapper.load(tokenContractAddress, web3j, transactionManager, currentGasPrice, currentGasLimit);

                    BigInteger decimalCount = contract.decimals().getValue();

                    BigDecimal formattedAmount = BalanceUtils.amountByDecimal(tokenAmount, new BigDecimal(decimalCount));
                    TransactionReceipt mReceipt = contract.transfer(new Address(to_Address), new Uint256(formattedAmount.toBigInteger()));

                    String tokenName = contract.name().getValue();
                    String tokenSymbol = contract.symbol().getValue();

                    HashMap<String, Object> body = new HashMap<>();
                    body.put("action_type", "SEND_TOKEN");
                    body.put("from_wallet_address", walletAddress);
                    body.put("to_wallet_address", to_Address);
                    body.put("amount", tokenAmount.toPlainString());
                    body.put("tx_hash", mReceipt.getTransactionHash());
                    body.put("gasLimit", gasLimit.toString());
                    body.put("gasPrice", gasPrice.toString());
                    body.put("fee", gasLimit.multiply(gasPrice).toString());
                    body.put("token_smart_contract", tokenContractAddress);
                    body.put("network", isMainNet() ? "MAINNET" : "TESTNET");
                    body.put("status", "SUCCESS");
                    body.put("token_name", tokenName);
                    body.put("token_symbol", tokenSymbol);

                    sendEventToLedger(body, context);

                    return Single.just(mReceipt.getTransactionHash());
                });
    }


    /**
     * Get Nonce for Current Wallet Address
     */
    protected BigInteger getNonce(String walletAddress) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                walletAddress, DefaultBlockParameterName.PENDING).send();

        return ethGetTransactionCount.getTransactionCount();
    }

    public String read_file(Context context, String filename) throws IOException {
        FileInputStream fis = context.openFileInput(filename);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private void sendEventToLedger(HashMap<String, Object> map, Context context) {
        try {
            SubmitTransactionModel submitTransactionModel = new SubmitTransactionModel();
            submitTransactionModel.setTx_type("ETHEREUM");
            submitTransactionModel.setUsername("user1");
            submitTransactionModel.setOrgname("org1");

            HashMap<String, Object> deviceInfo = deviceInfo(context);
            if (deviceInfo != null) {
                map.put("DEVICE_INFO", new Gson().toJson(deviceInfo));
            }

            submitTransactionModel.setBody(map);
            hyperLedgerApi.submitTransaction(submitTransactionModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((objectBaseResponse, throwable) -> {
                        System.out.println(objectBaseResponse);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HashMap<String, Object> deviceInfo(Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            String osName = "ANDROID";
            String serialNumber = Build.SERIAL;
            String model = Build.MODEL;
            String manufacturer = Build.MANUFACTURER;
            HashMap<String, Object> deviceInfo = new HashMap<>();
            deviceInfo.put("ID", androidId);
            deviceInfo.put("OS", osName);
            deviceInfo.put("MODEL", model);
            deviceInfo.put("SERIAL", serialNumber);
            deviceInfo.put("MANUFACTURER", manufacturer);
            return deviceInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isMainNet() {
        return mainnetInfuraUrl.contains("mainnet");
    }
}
