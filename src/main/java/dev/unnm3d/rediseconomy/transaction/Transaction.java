package dev.unnm3d.rediseconomy.transaction;

import dev.unnm3d.rediseconomy.redis.RedisKeys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Transaction {
    /**
     * The identifier of the account this transaction belongs to
     */
    private AccountID accountIdentifier = new AccountID(RedisKeys.getServerUUID());
    /**
     * The timestamp of the transaction
     */
    private long timestamp = 0;
    /**
     * The identifier of the actor that performed this transaction
     */
    private AccountID actor = new AccountID(RedisKeys.getServerUUID());
    @Setter
    private double amount = 0;
    private String currencyName = "";
    /**
     * The reason for this transaction
     * <p>
     * This is used to track the purpose of the transaction
     */
    @Setter
    private String reason = "Unknown";
    /**
     * The identifier of the transaction that reverted this transaction, if any
     * <p>
     * This is used to track reversions of transactions, such as refunds or corrections.
     */
    @Setter
    private String revertedWith = null;

    /**
     * Creates a new transaction from a string
     *
     * @param serializedTransaction The serialized transaction
     * @return The transaction created from the string
     */
    public static Transaction fromString(String serializedTransaction) {
        String[] parts = serializedTransaction.split(";");
        return new Transaction(
                parts[0].length() == 36 ? new AccountID(UUID.fromString(parts[0])) : new AccountID(parts[0]),
                Long.parseLong(parts[1]),
                parts[2].length() == 36 ? new AccountID(UUID.fromString(parts[2])) : new AccountID(parts[2]),
                Double.parseDouble(parts[3]),
                parts[4],
                parts[5],
                parts.length == 7 ? parts[6] : null);
    }

    @Override
    public String toString() {
        return accountIdentifier + ";" + timestamp + ";" + actor + ";" + amount + ";" + currencyName + ";" + reason.replace(";","") + (revertedWith == null ? "" : ";" + revertedWith);
    }
}
