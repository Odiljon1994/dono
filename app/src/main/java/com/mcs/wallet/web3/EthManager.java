package com.mcs.wallet.web3;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;
import com.mcs.wallet.api.ApiUtils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeEncoder;
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
import org.web3j.crypto.Hash;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
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
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    public Single<BigInteger> getGasPriceAsSingle() {
        return Single.fromCallable(() -> {
            EthGasPrice price = web3j.ethGasPrice()
                    .send();
            return price.getGasPrice();
        });
    }

    private BigInteger requestEstimatedGasLimit(String from, String to, BigInteger gasPrice, BigInteger nonce, String data) {
        try {
            BigInteger gasLimit = BigInteger.valueOf(200000);
            Transaction transaction = new Transaction(from, null, gasPrice, gasLimit, to, BigInteger.ZERO, data);
            EthEstimateGas gas = web3j.ethEstimateGas(transaction).send();
            if (!gas.hasError()) {
                System.out.println("Gas Estimate : " + gas.getAmountUsed().toString() + " Nonce : " + nonce.toString());
                return gas.getAmountUsed();
            } else {
                Response.Error error = gas.getError();
                //            if (error.getMessage().equals("execution reverted: ERC721: token already minted")) {
                //                throw new NftException(NftException.NFT_ERROR.ALREADY_CREATED_TOKEN_ID, "token already minted : " + tokenID);
                //            } else if (error.getMessage().equals("execution reverted: ERC721: transfer caller is not owner nor approved")) {
                //                throw new NftException(NftException.NFT_ERROR.NOT_OWNER_OF_TOKEN, "not owner of tokenId : " + tokenID);
                //            }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return BigInteger.ZERO;
        }
        return BigInteger.ZERO;
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
            return estimateGas.getAmountUsed();
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

                return new Wallet(walletAddress, keystore);
            } catch (CipherException | IOException | NoSuchAlgorithmException |
                     InvalidAlgorithmParameterException | NoSuchProviderException e) {
                e.printStackTrace();
            }
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

        int[] path = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0};
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

        int[] path = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0};
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
            if (keystoreFile.exists()) {

                return read_file(context, keystoreFile.getName());
            } else {
                throw new Exception("Keystore is NULL");
            }
        });
    }

    /**
     * Import Wallet by Keystore
     */
    public Single<String> importFromKeystore(String keystore, String password, Context context) {
        return Single.fromCallable(() -> {
            try {
                Credentials credentials = CenterPrimeUtils.loadCredentials(password, keystore);
                String walletAddress = CenterPrimeUtils.generateWalletFile(password, credentials.getEcKeyPair(), new File(context.getFilesDir(), ""), false);

                return walletAddress;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Import Wallet with Private Key
     */
    public Single<String> importFromPrivateKey(String privateKey, Context context) {
        return Single.fromCallable(() -> {
            String password = "";
            // Decode private key
            ECKeyPair keys = ECKeyPair.create(Numeric.hexStringToByteArray(privateKey));

            try {
                Credentials credentials = Credentials.create(keys);
                String walletAddress = CenterPrimeUtils.generateWalletFile(password, credentials.getEcKeyPair(), new File(context.getFilesDir(), ""), false);

                return walletAddress;
            } catch (CipherException | IOException e) {
                e.printStackTrace();
            }
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

    public Single<Credentials> loadCredentialsByPrivateKey(String privateKey) {
        return Single.fromCallable(() -> Credentials.create(privateKey));
    }

    /**
     * Add Custom Token
     */
    public Single<Token> searchTokenByContractAddress(String contractAddress, String walletAddress, String password, Context context) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {
                    Integer maticMainnet = new Integer(137);
                    byte maticMainnetByte = maticMainnet.byteValue();

                    Integer maticTestnet = new Integer(80001);
                    byte maticTestnetByte = maticTestnet.byteValue();

                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            web3j, credentials, isMainNet() ? maticMainnetByte : maticTestnetByte, transactionReceiptProcessor);
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
                    Integer maticMainnet = new Integer(137);
                    byte maticMainnetByte = maticMainnet.byteValue();

                    Integer maticTestnet = new Integer(80001);
                    byte maticTestnetByte = maticTestnet.byteValue();

                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            //web3j, credentials, isMainNet() ? ChainId.MAINNET : ChainId.ROPSTEN, transactionReceiptProcessor);
                            web3j, credentials, isMainNet() ? maticMainnetByte : maticTestnetByte, transactionReceiptProcessor);
                    Erc20TokenWrapper contract = Erc20TokenWrapper.load(tokenContractAddress, web3j,
                            transactionManager, BigInteger.ZERO, BigInteger.ZERO);
                    Address address = new Address(walletAddress);
                    BigInteger tokenBalance = contract.balanceOf(address).getValue();
                    String tokenName = contract.name().getValue();
                    String tokenSymbol = contract.symbol().getValue();
                    BigInteger decimalCount = contract.decimals().getValue();

                    BigDecimal tokenValueByDecimals = BalanceUtils.balanceByDecimal(tokenBalance, decimalCount);


                    return Single.just(tokenValueByDecimals);
                });
    }

    public Single<BigDecimal> getERC1155TokenBalance(String walletAddress, String password, String tokenContractAddress, int tokenId, Context context) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {
                    Integer maticMainnet = new Integer(137);
                    byte maticMainnetByte = maticMainnet.byteValue();
                    Integer maticTestnet = new Integer(80001);
                    byte maticTestnetByte = maticTestnet.byteValue();
                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            web3j, credentials, isMainNet() ? maticMainnetByte : maticTestnetByte, transactionReceiptProcessor);
                    Erc1155TokenWrapper contract = Erc1155TokenWrapper.load(tokenContractAddress, web3j,
                            transactionManager, BigInteger.ZERO, BigInteger.ZERO);
                    BigInteger tokenBalance = contract.balanceOf(walletAddress, tokenId).getValue();
                    return Single.just(new BigDecimal(tokenBalance));
                });
    }

    public Single<String> sendERC1155Token(String walletAddress, String password,  Context context, String nftContractAddress, String toAddress,
                                           int tokenId, int tokenAmount) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {
                    Integer maticMainnet = new Integer(137);
                    byte maticMainnetByte = maticMainnet.byteValue();
                    Integer maticTestnet = new Integer(80001);
                    byte maticTestnetByte = maticTestnet.byteValue();
                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            web3j, credentials, isMainNet() ? maticMainnetByte : maticTestnetByte, transactionReceiptProcessor);

                    Erc1155TokenWrapper contract = Erc1155TokenWrapper.load(nftContractAddress, web3j,
                            transactionManager, BigInteger.ZERO, BigInteger.ZERO);

                    String transferWithData = contract.generateTransferFuncData(
                            walletAddress, toAddress, tokenId, tokenAmount
                    );
                    BigInteger currentGasPrice = getGasPrice();
                    BigInteger nonce = getNonce(walletAddress);
                    BigInteger currentGasLimit = getGasLimit(walletAddress, nonce, currentGasPrice, BigInteger.valueOf(21000), nftContractAddress, BigInteger.ZERO, transferWithData);
                    EthSendTransaction response = transactionManager.sendTransaction(currentGasPrice, currentGasLimit, nftContractAddress, transferWithData, BigInteger.ZERO);
                    return Single.just(response.getTransactionHash());
                });
    }

    public Single<String> sendERC1155TokenBySignature(HashMap<String,Object> signatureMap) {
        return loadCredentialsByPrivateKey(ApiUtils.getOperatorPrivateKeyAddress())
                .flatMap(credentials -> {
                    Integer maticMainnet = new Integer(137);
                    byte maticMainnetByte = maticMainnet.byteValue();
                    Integer maticTestnet = new Integer(80001);
                    byte maticTestnetByte = maticTestnet.byteValue();
                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            web3j, credentials, isMainNet() ? maticMainnetByte : maticTestnetByte, transactionReceiptProcessor);

                    String nftContractAddress = (String) signatureMap.get("contract_address");
                    String toAddress = (String) signatureMap.get("to_address");
                    String fromAddress = (String) signatureMap.get("from_address");
                    int tokenId = (int) signatureMap.get("token_id");
                    int tokenAmount = (int) signatureMap.get("token_amount");
                    int nonceSignature = (int) signatureMap.get("nonce");
                    String signature = (String) signatureMap.get("signature");

                    Erc1155TokenWrapper contract = Erc1155TokenWrapper.load(nftContractAddress, web3j,
                            transactionManager, BigInteger.ZERO, BigInteger.ZERO);
                    byte[] bytes = Numeric.hexStringToByteArray(signature);

                    String transferWithData = contract.generateTransferWithSignatureFuncData(
                            fromAddress, toAddress, tokenId, tokenAmount, nonceSignature, bytes
                    );
                    BigInteger currentGasPrice = getGasPrice();
                    BigInteger nonce = getNonce(credentials.getAddress());
                    BigInteger gasLimit = requestEstimatedGasLimit(credentials.getAddress(), nftContractAddress, currentGasPrice, nonce, transferWithData);
//                    BigInteger currentGasLimit = getGasLimit(credentials.getAddress(), nonce, currentGasPrice, BigInteger.valueOf(21000), nftContractAddress, BigInteger.ZERO, transferWithData);
                    RawTransaction rawTransaction = RawTransaction.createTransaction(
                            nonce, currentGasPrice, gasLimit, nftContractAddress, transferWithData);
                    byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, isMainNet() ? maticMainnet : maticTestnet, credentials);
                    String hexValue = Numeric.toHexString(signedMessage);
                    EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
                    return Single.just(transactionResponse.getTransactionHash());
                });
    }


    public static int generateEightDigitNumber() {
        SecureRandom random = new SecureRandom();
        // Generate a random number between 10000000 and 99999999 inclusive
        return 10000000 + random.nextInt(90000000);
    }

    public Single<HashMap<String, Object>> createSignature(String walletAddress, String password, Context context, String toAddress, int tokenId, int tokenAmount, String nftContractAddress) {
        return loadCredentials(walletAddress, password, context)
                .flatMap(credentials -> {
                    int nonce = generateEightDigitNumber();
                    String signature = signGiftCarbonNFT(walletAddress, toAddress, tokenId, tokenAmount, nonce, nftContractAddress, credentials);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("from_address", walletAddress);
                    map.put("to_address", toAddress);
                    map.put("contract_address", nftContractAddress);
                    map.put("token_id", tokenId);
                    map.put("token_amount", tokenAmount);
                    map.put("nonce", nonce);
                    map.put("signature", signature);
                    return Single.just(map);
                });
    }

    public String signGiftCarbonNFT(String fromAddress, String toAddress, int tokenId, int tokenAmount, int nonce, String nftContractAddress, Credentials credentials) {
        String encodedParams = transferBySignatureSoliditySha3(fromAddress, toAddress, tokenId, tokenAmount, nonce, nftContractAddress);
        String encodedHash = Hash.sha3(encodedParams);
        byte[] hashBytes = Numeric.hexStringToByteArray(encodedHash);
        Sign.SignatureData signatureData = Sign.signPrefixedMessage(hashBytes, credentials.getEcKeyPair());
        String r = Numeric.toHexString(signatureData.getR());
        String s = Numeric.toHexString(signatureData.getS());
        String v = Numeric.toHexString(signatureData.getV());
        return r + s.substring(2) + v.substring(2);
    }

    private String transferBySignatureSoliditySha3(String from, String to, int tokenId, int tokenAmount, int nonce, String contractAddress) {
        Address fromAddressValue = new Address(from);
        Address toAddressValue = new Address(to);
        Address contractAddressValue = new Address(contractAddress);
        Uint256 tokenIdValue = new Uint256(tokenId);
        Uint256 tokenAmountValue = new Uint256(tokenAmount);
        Uint256 nonceValue = new Uint256(nonce);
        String encodedFromValue = TypeEncoder.encode(fromAddressValue);
        encodedFromValue = encodedFromValue.substring(64 - (fromAddressValue.toUint().getBitSize() / 4), 64);
        String encodedToValue = TypeEncoder.encode(toAddressValue);
        encodedToValue = encodedToValue.substring(64 - (toAddressValue.toUint().getBitSize() / 4), 64);
        String encodedTokenIdValue = TypeEncoder.encode(tokenIdValue);
        String encodedTokenAmountValue = TypeEncoder.encode(tokenAmountValue);
        String encodedNonceValue = TypeEncoder.encode(nonceValue);
        String encodedContractValue = TypeEncoder.encode(contractAddressValue);
        encodedContractValue = encodedContractValue.substring(64 - (contractAddressValue.toUint().getBitSize() / 4), 64);
        return encodedFromValue + encodedToValue + encodedTokenIdValue + encodedTokenAmountValue + encodedNonceValue + encodedContractValue;
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
                    String transactionHash;
                    BigInteger nonce = getNonce(walletAddress);
                    BigDecimal weiValue = Convert.toWei(etherAmount, Convert.Unit.ETHER);
                    BigInteger currentGasPrice = getGasPrice();
                    System.out.println("** GasPrice Matic: " + currentGasPrice);
                    Integer maticMainnet = new Integer(137);
                    Integer maticTestnet = new Integer(80001);
                    RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                            nonce, currentGasPrice, gasLimit, to_Address, weiValue.toBigInteger());
                    byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, isMainNet() ? maticMainnet : maticTestnet, credentials);
                    String hexValue = Numeric.toHexString(signedMessage);
                    EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
                    transactionHash = transactionResponse.getTransactionHash();
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
                    Integer maticMainnet = new Integer(137);
                    Integer maticTestnet = new Integer(80001);
                    TransactionReceiptProcessor transactionReceiptProcessor = new NoOpProcessor(web3j);
                    TransactionManager transactionManager = new RawTransactionManager(
                            web3j, credentials, isMainNet() ? maticMainnet : maticTestnet, transactionReceiptProcessor);

                    BigInteger nonce = getNonce(walletAddress);
                    BigInteger currentGasPrice = getGasPrice();

                    BigDecimal formattedAmount1 = BalanceUtils.amountByDecimal(tokenAmount, new BigDecimal(18));

                    String data = generateDataForTokenTransfer(to_Address, formattedAmount1.toBigInteger());

                    BigInteger currentGasLimit = getGasLimit(walletAddress, nonce, currentGasPrice, gasLimit, tokenContractAddress, BigInteger.ZERO, data);

                    System.out.println("** GasPrice Token: " + currentGasPrice);
                    System.out.println("** GasLimit Token: " + currentGasLimit);

                    Erc20TokenWrapper contract = Erc20TokenWrapper.load(tokenContractAddress, web3j, transactionManager, currentGasPrice, currentGasLimit);

                    BigInteger decimalCount = contract.decimals().getValue();

                    BigDecimal formattedAmount = BalanceUtils.amountByDecimal(tokenAmount, new BigDecimal(decimalCount));
                    TransactionReceipt mReceipt = contract.transfer(new Address(to_Address), new Uint256(formattedAmount.toBigInteger()));

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
