package org.tron.common.logsfilter;

import static org.tron.core.config.Parameter.ChainConstant.CYP_PRECISION;
import static org.tron.protos.contract.Common.ResourceCode.BANDWIDTH;

import com.google.protobuf.ByteString;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tron.common.logsfilter.capsule.TransactionLogTriggerCapsule;
import org.tron.common.utils.Sha256Hash;
import org.tron.core.capsule.BlockCapsule;
import org.tron.core.capsule.TransactionCapsule;
import org.tron.p2p.utils.ByteArray;
import org.tron.protos.Protocol;
import org.tron.protos.contract.BalanceContract;
import org.tron.protos.contract.Common;

public class TransactionLogTriggerCapsuleTest {

  private static final String OWNER_ADDRESS = "41548794500882809695a8a687866e76d4271a1abc";
  private static final String RECEIVER_ADDRESS = "41abd4b9367799eaa3197fecb144eb71de1e049150";

  public TransactionCapsule transactionCapsule;
  public BlockCapsule blockCapsule;

  @Before
  public void setup() {
    blockCapsule = new BlockCapsule(1, Sha256Hash.ZERO_HASH,
        System.currentTimeMillis(), Sha256Hash.ZERO_HASH.getByteString());
  }

  @Test
  public void testConstructorWithUnfreezeBalanceCypCapsule() {
    BalanceContract.UnfreezeBalanceContract.Builder builder2 =
        BalanceContract.UnfreezeBalanceContract.newBuilder()
        .setOwnerAddress(ByteString.copyFrom(ByteArray.fromHexString(OWNER_ADDRESS)))
        .setReceiverAddress(ByteString.copyFrom(ByteArray.fromHexString(RECEIVER_ADDRESS)));
    transactionCapsule = new TransactionCapsule(builder2.build(),
        Protocol.Transaction.Contract.ContractType.UnfreezeBalanceContract);
    Protocol.TransactionInfo.Builder builder = Protocol.TransactionInfo.newBuilder();
    builder.setUnfreezeAmount(CYP_PRECISION + 1000);


    TransactionLogTriggerCapsule triggerCapsule = new TransactionLogTriggerCapsule(
        transactionCapsule, blockCapsule,0,0,0,
        builder.build(),0);

    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getFromAddress());
    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getToAddress());
    Assert.assertEquals(CYP_PRECISION + 1000,
        triggerCapsule.getTransactionLogTrigger().getAssetAmount());
  }


  @Test
  public void testConstructorWithFreezeBalanceV2CypCapsule() {
    BalanceContract.FreezeBalanceV2Contract.Builder builder2 =
        BalanceContract.FreezeBalanceV2Contract.newBuilder()
        .setOwnerAddress(ByteString.copyFrom(ByteArray.fromHexString(OWNER_ADDRESS)))
        .setFrozenBalance(CYP_PRECISION + 100000)
        .setResource(Common.ResourceCode.BANDWIDTH);
    transactionCapsule = new TransactionCapsule(builder2.build(),
        Protocol.Transaction.Contract.ContractType.FreezeBalanceV2Contract);

    TransactionLogTriggerCapsule triggerCapsule =
        new TransactionLogTriggerCapsule(transactionCapsule, blockCapsule);

    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getFromAddress());
    Assert.assertEquals("cyp", triggerCapsule.getTransactionLogTrigger().getAssetName());
    Assert.assertEquals(CYP_PRECISION + 100000,
        triggerCapsule.getTransactionLogTrigger().getAssetAmount());
  }

  @Test
  public void testConstructorWithUnfreezeBalanceV2CypCapsule() {
    BalanceContract.UnfreezeBalanceV2Contract.Builder builder2 =
        BalanceContract.UnfreezeBalanceV2Contract.newBuilder()
        .setOwnerAddress(ByteString.copyFrom(ByteArray.fromHexString(OWNER_ADDRESS)))
        .setUnfreezeBalance(CYP_PRECISION + 4000)
        .setResource(Common.ResourceCode.BANDWIDTH);
    transactionCapsule = new TransactionCapsule(builder2.build(),
        Protocol.Transaction.Contract.ContractType.UnfreezeBalanceV2Contract);

    TransactionLogTriggerCapsule triggerCapsule =
        new TransactionLogTriggerCapsule(transactionCapsule, blockCapsule);

    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getFromAddress());
    Assert.assertEquals("cyp", triggerCapsule.getTransactionLogTrigger().getAssetName());
    Assert.assertEquals(CYP_PRECISION + 4000,
        triggerCapsule.getTransactionLogTrigger().getAssetAmount());
  }


  @Test
  public void testConstructorWithWithdrawExpireCypCapsule() {
    BalanceContract.WithdrawExpireUnfreezeContract.Builder builder2 =
        BalanceContract.WithdrawExpireUnfreezeContract.newBuilder()
        .setOwnerAddress(ByteString.copyFrom(ByteArray.fromHexString(OWNER_ADDRESS)));
    transactionCapsule = new TransactionCapsule(builder2.build(),
        Protocol.Transaction.Contract.ContractType.WithdrawExpireUnfreezeContract);

    Protocol.TransactionInfo.Builder builder = Protocol.TransactionInfo.newBuilder();
    builder.setWithdrawExpireAmount(CYP_PRECISION + 1000);

    TransactionLogTriggerCapsule triggerCapsule = new TransactionLogTriggerCapsule(
        transactionCapsule, blockCapsule,0,0,0,
        builder.build(),0);

    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getFromAddress());
    Assert.assertEquals("cyp", triggerCapsule.getTransactionLogTrigger().getAssetName());
    Assert.assertEquals(CYP_PRECISION + 1000,
        triggerCapsule.getTransactionLogTrigger().getAssetAmount());
  }


  @Test
  public void testConstructorWithDelegateResourceCypCapsule() {
    BalanceContract.DelegateResourceContract.Builder builder2 =
        BalanceContract.DelegateResourceContract.newBuilder()
        .setOwnerAddress(ByteString.copyFrom(ByteArray.fromHexString(OWNER_ADDRESS)))
        .setReceiverAddress(ByteString.copyFrom(ByteArray.fromHexString(RECEIVER_ADDRESS)))
        .setBalance(CYP_PRECISION + 2000);
    transactionCapsule = new TransactionCapsule(builder2.build(),
        Protocol.Transaction.Contract.ContractType.DelegateResourceContract);

    TransactionLogTriggerCapsule triggerCapsule =
        new TransactionLogTriggerCapsule(transactionCapsule, blockCapsule);

    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getFromAddress());
    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getToAddress());
    Assert.assertEquals("cyp", triggerCapsule.getTransactionLogTrigger().getAssetName());
    Assert.assertEquals(CYP_PRECISION + 2000,
        triggerCapsule.getTransactionLogTrigger().getAssetAmount());
  }

  @Test
  public void testConstructorWithUnDelegateResourceCypCapsule() {
    BalanceContract.UnDelegateResourceContract.Builder builder2 =
        BalanceContract.UnDelegateResourceContract.newBuilder()
        .setOwnerAddress(ByteString.copyFrom(ByteArray.fromHexString(OWNER_ADDRESS)))
        .setReceiverAddress(ByteString.copyFrom(ByteArray.fromHexString(RECEIVER_ADDRESS)))
        .setBalance(CYP_PRECISION + 10000);
    transactionCapsule = new TransactionCapsule(builder2.build(),
        Protocol.Transaction.Contract.ContractType.UnDelegateResourceContract);

    TransactionLogTriggerCapsule triggerCapsule =
        new TransactionLogTriggerCapsule(transactionCapsule, blockCapsule);

    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getFromAddress());
    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getToAddress());
    Assert.assertEquals("cyp", triggerCapsule.getTransactionLogTrigger().getAssetName());
    Assert.assertEquals(CYP_PRECISION + 10000,
        triggerCapsule.getTransactionLogTrigger().getAssetAmount());
  }

  @Test
  public void testConstructorWithCancelAllUnfreezeCypCapsule() {
    BalanceContract.CancelAllUnfreezeV2Contract.Builder builder2 =
        BalanceContract.CancelAllUnfreezeV2Contract.newBuilder()
        .setOwnerAddress(ByteString.copyFrom(ByteArray.fromHexString(OWNER_ADDRESS)));
    transactionCapsule = new TransactionCapsule(builder2.build(),
        Protocol.Transaction.Contract.ContractType.CancelAllUnfreezeV2Contract);

    Protocol.TransactionInfo.Builder builder = Protocol.TransactionInfo.newBuilder();
    builder.clearCancelUnfreezeV2Amount().putCancelUnfreezeV2Amount(
        BANDWIDTH.name(), CYP_PRECISION + 2000);

    TransactionLogTriggerCapsule triggerCapsule = new TransactionLogTriggerCapsule(
        transactionCapsule, blockCapsule,0,0,0,
        builder.build(),0);

    Assert.assertNotNull(triggerCapsule.getTransactionLogTrigger().getFromAddress());
    Assert.assertEquals(CYP_PRECISION + 2000,
        triggerCapsule.getTransactionLogTrigger().getExtMap().get(BANDWIDTH.name()).longValue());
  }

}